package com.link.cloud.contract;

import android.media.MediaPlayer;

import com.link.cloud.R;
import com.link.cloud.bean.Member;
import com.orhanobut.logger.Logger;
import com.link.cloud.BaseApplication;

import com.link.cloud.TestActivityManager;
import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.ReturnBean;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;

/**
 * Created by Shaozy on 2016/8/11.
 */
public class BindTaskContract extends BasePresenter<BindTaskContract.BindView> {

    final int REGISTER_TEMPLATE = 0x001;
    final int SAVE_TEMPLATE = 0x002;
//    final  static int MOVE_FINGER=0x004;//移开手指常量
    final static int PUT_FINGER=0x005;//放入手指常量
    long start=0,endtime=0;
    BaseApplication baseApplication;
//    private MediaPlayer mediaPlayer0,mediaPlayer1;
    public interface BindView extends MvpView {
        void bindSuccess(Member returnBean) throws InterruptedException;
    }

    public ReservoirUtils reservoirUtils;
    public BindTaskContract() {
        this.reservoirUtils = new ReservoirUtils();
//        mediaPlayer0=MediaPlayer.create(TestActivityManager.getInstance().getCurrentActivity(), R.raw.finger_move);
//        mediaPlayer1=MediaPlayer.create(TestActivityManager.getInstance().getCurrentActivity(), R.raw.putfinger_again);

    }
    public void bindVeinMemeber( String deviceId,int userType,int numberType, String numberValue,String img1,String img2,String img3, String feature) {
        this.mCompositeSubscription.add(this.mDataManager.bindVeinMemeber(deviceId,userType,numberType,numberValue,img1,img2,img3,feature)
                .subscribe(new AbsAPICallback<Member>() {
                    @Override
                    public void onCompleted() {
                        if (BindTaskContract.this.mCompositeSubscription != null)
                            BindTaskContract.this.mCompositeSubscription.remove(this);

                    }
                    @Override
                    public void onNext(Member returnBean) {
                        try {
                            BindTaskContract.this.getMvpView().bindSuccess(returnBean);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void onError(ApiException e) {
                        BindTaskContract.this.getMvpView().onError(e);
                        Logger.e("BindTaskContract---onError"+e.getMessage());
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        BindTaskContract.this.getMvpView().onPermissionError(e);
                        Logger.e("BindTaskContract---onPermissionError"+e.getMessage());
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        BindTaskContract.this.getMvpView().onResultError(e);
                        Logger.e("BindTaskContract---onResultError"+e.getMessage());
                    }
                }));

    }

    @Override
    public void detachView() {
        super.detachView();
//        if (mediaPlayer0!=null) {
//            mediaPlayer0.stop();
//            mediaPlayer0.release();
//            mediaPlayer0 = null;
//        }else if (mediaPlayer0!=null) {
//            mediaPlayer0.stop();
//            mediaPlayer0.release();
//            mediaPlayer0 = null;
//        }else if (mediaPlayer1!=null) {
//            mediaPlayer1.stop();
//            mediaPlayer1.release();
//        }
    }
}
