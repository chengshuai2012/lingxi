package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class SyncUserFeature extends BasePresenter<SyncUserFeature.syncUser> {

    public interface syncUser extends MvpView {
        void syncUserSuccess(DownLoadData resultResponse);
        void syncSignUserSuccess(Sign_data downLoadData);
    }
    public ReservoirUtils reservoirUtils;

    public SyncUserFeature() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void syncUser(String deviceId){
        this.mCompositeSubscription.add(this.mDataManager.syncUserFeature(deviceId)
                .subscribe(new AbsAPICallback<DownLoadData>() {
                    @Override
                    public void onCompleted() {
                        if (SyncUserFeature.this.mCompositeSubscription != null)
                            SyncUserFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(DownLoadData resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        SyncUserFeature.this.getMvpView().syncUserSuccess(resultResponse);
                    }
                }));
    }
    public void syncSign(String deviceId){
        this.mCompositeSubscription.add(this.mDataManager.syncSignUserFeature(deviceId)
                .subscribe(new AbsAPICallback<Sign_data>() {
                    @Override
                    public void onCompleted() {
                        if (SyncUserFeature.this.mCompositeSubscription != null)
                            SyncUserFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        SyncUserFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(Sign_data resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        SyncUserFeature.this.getMvpView().syncSignUserSuccess(resultResponse);
                    }
                }));
    }

}
