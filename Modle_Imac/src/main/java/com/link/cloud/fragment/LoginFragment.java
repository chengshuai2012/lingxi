package com.link.cloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.link.cloud.R;
import com.link.cloud.core.BaseFragment;


import com.link.cloud.base.ApiException;
import com.link.cloud.contract.LoginTaskContract;
import com.link.cloud.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginTaskContract.LoginView {
//
//    @Bind(R.id.deviceID)
//    EditText etDeviceID;
//    @Bind(R.id.deviceCode)
//    EditText etDeviceCode;
//    @Bind(R.id.login)
//    Button login;
//    MediaPlayer mediaPlayer;
    private LoginTaskContract presenter;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mediaPlayer=MediaPlayer.create(getActivity(),R.raw.error_password);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }
    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initData() {
        this.presenter = new LoginTaskContract();
        this.presenter.attachView(this);
//        etDeviceID.setText(Utils.getCurrentDeviceID());
    }
    @Override
    protected void onVisible() {
    }
    @Override
    protected void onInvisible() {
    }
    @Override
    public void onLoginSuccess() {
        this.showProgress(false);
//        etDeviceID.setText(Utils.getCurrentDeviceID());
//        etDeviceCode.setText("");
    }
    @Override
    public void onError(ApiException e) {
        super.onError(e);
        this.showProgress(false);
        this.showToast(e.getDisplayMessage());
    }
    @Override
    public void onPermissionError(ApiException e) {
        this.showProgress(false);
        this.showToast(e.getDisplayMessage());
    }
    @Override
    public void onResultError(ApiException e) {
        this.showProgress(false, false, e.getDisplayMessage());
    }
    @Override
    public void onDestroy() {
        this.presenter.detachView();
        super.onDestroy();
    }
//    @OnClick({R.id.login})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.login:
//                String deviceId = etDeviceID.getText().toString().trim();
//                String devicecode = etDeviceCode.getText().toString().trim();
//                etDeviceID.setError(null);
//                etDeviceCode.setError(null);
//                View focusView = null;
//                boolean cancel = false;
//                if (TextUtils.isEmpty(deviceId)) {
//                    etDeviceID.setError(getResources().getString(R.string.error_invalid_deviceid_empty));
//                    focusView = etDeviceID;
//                    cancel = true;
//                } else if (TextUtils.isEmpty(devicecode)) {
//                    etDeviceCode.setError(getResources().getString(R.string.error_invalid_devicecode_empty));
//                    focusView = etDeviceCode;
//                    cancel = true;
//                } else if (!devicecode.equals("888888")) {
//                    etDeviceCode.setError(getResources().getString(R.string.error_invalid_devicecode));
////                    mediaPlayer.start();
//                    focusView = etDeviceCode;
//                    cancel = true;
//                }
//                if (cancel) {
//                    focusView.requestFocus();
//                } else {
//                    this.showProgress(true);
//                    this.presenter.login(deviceId, devicecode);
//                }
//                this.etDeviceCode.setText("");
//                break;
//        }
//    }
}
