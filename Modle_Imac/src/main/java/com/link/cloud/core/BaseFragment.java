/*
 * {EasyGank}  Copyright (C) {2015}  {CaMnter}
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `show c' for details.
 *
 * The hypothetical commands `show w' and `show c' should show the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <http://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <http://www.gnu.org/philosophy/why-not-lgpl.html>.
 */

package com.link.cloud.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.link.cloud.R;
import com.orhanobut.logger.Logger;
import com.link.cloud.BaseApplication;

import com.link.cloud.base.ApiException;
import com.link.cloud.utils.ToastUtils;
import com.link.cloud.view.ProgressHUD;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Description：BaseFragment
 */
public abstract class BaseFragment extends Fragment {
    protected boolean isVisble = false;
    protected boolean isCreated = false;

    protected View self;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Logger.e("BaseFragment-----setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }

        if (getUserVisibleHint()) {
            Logger.e("BaseFragment"+getUserVisibleHint());
            isVisble = true;
            onVisible();
        } else {
            Logger.e("BaseFragment"+getUserVisibleHint());
            isVisble = false;
            onInvisible();
        }
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.self == null) {
            this.self = inflater.inflate(this.getLayoutId(), container, false);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
        isCreated = true;
        ButterKnife.bind(this, this.self);
        this.initViews(this.self, savedInstanceState);
        this.initData();
        this.initListeners();
        return this.self;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getContext());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        isCreated = false;
    }
    /**
     * Fill in layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();
    /**
     * Initialize the view in the layout
     *
     * @param self               self
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initViews(View self, Bundle savedInstanceState);
    /**
     * Initialize the View of the listener
     */
    protected abstract void initListeners();

    /**
     * Initialize the Activity data
     */
    protected abstract void initData();

    /**
     * Find the view by id
     *
     * @param id  id
     * @param <V> V
     * @return V
     */
    @SuppressWarnings("unchecked")
    protected <V extends View> V findView(int id) {
        return (V) this.self.findViewById(id);
    }

    protected abstract void onVisible();

    protected abstract void onInvisible();

    /*********
     * Toast *
     *********/

    public void showToast(String msg) {

        this.showToast(msg, Toast.LENGTH_LONG);
    }


    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), msg, duration);
        } else {
            ToastUtils.show(this.getActivity(), msg, ToastUtils.LENGTH_SHORT);
        }
    }


    public void showToast(int resId) {
        this.showToast(resId, Toast.LENGTH_SHORT);
    }


    public void showToast(int resId, int duration) {
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), resId, duration);
        } else {
            ToastUtils.show(this.getActivity(), resId, ToastUtils.LENGTH_SHORT);
        }
    }

    /*********
     * Toast *
     *********/

    /**
     * ProgressHUD
     *
     * @param obj 不定参数，约定obj[0]为Boolean类型，obje[1]为String类型
     */
    public void showProgress(boolean show, Object... obj) {
        if (show) {
            boolean cancelable = false;
            String msg = BaseApplication.getInstance().getApplicationContext().getString(R.string.loading);
            if (obj != null && obj.length > 0) {
                cancelable = (boolean) obj[0];
                msg = (String) obj[1];
            }
            ProgressHUD.showProgress(this.getActivity(), msg, cancelable);
        } else {
            if (obj != null && obj.length > 0) {
                boolean success = (boolean) obj[0];
                String msg = (String) obj[1];
                ProgressHUD.dismissProgress(this.getActivity(), success, msg);
            } else {
                ProgressHUD.dismissProgress(this.getActivity());
            }
        }
    }

    //隐藏虚拟键盘
    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    //未知错误上报友盟
    protected void onError(ApiException error) {
        if (error != null) {
            switch (error.getCode()) {
                case ApiException.MATCH_TEMPLATE_ERROR:
                case ApiException.REGISTER_TEMPLATE_ERROR:
                case ApiException.MISSING_FINGER_ERROR:
                case ApiException.MOVING_FINGER_ERROR:
                    //采集指静脉错误不上报
                    break;
                case ApiException.UNKNOWN:
                case ApiException.PARSE_ERROR:
                    try {
                        MobclickAgent.reportError(BaseApplication.getInstance(), error.getMessage());
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                    break;
            }
        }
    }
}
