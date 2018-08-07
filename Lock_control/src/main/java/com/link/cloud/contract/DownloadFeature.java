package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class DownloadFeature extends BasePresenter<DownloadFeature.download> {

    public interface download extends MvpView {
        void syncUserFeaturePagesSuccess(SyncFeaturesPage syncFeaturesPage);
        void getPagesInfo(PagesInfoBean pagesInfoBean);
        void downloadSuccess(DownLoadData resultResponse);
        void downloadNotReceiver(DownLoadData resultResponse);
        void downloadApK(UpDateBean resultResponse);
    }
    public ReservoirUtils reservoirUtils;

    public DownloadFeature() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void download(String messageId,String appid,String shopId,String deviceId,String uid){
        this.mCompositeSubscription.add(this.mDataManager.downloadFeature(messageId,appid,shopId,deviceId,uid)
                .subscribe(new AbsAPICallback<DownLoadData>() {
                    @Override
                    public void onCompleted() {
                        if (DownloadFeature.this.mCompositeSubscription != null)
                            DownloadFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(DownLoadData resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        DownloadFeature.this.getMvpView().downloadSuccess(resultResponse);
                    }
                }));
    }
    public void downloadNotReceiver(String deviceId,int vs){
        this.mCompositeSubscription.add(this.mDataManager.downloadNotReceiver(deviceId,vs)
                .subscribe(new AbsAPICallback<DownLoadData>() {
                    @Override
                    public void onCompleted() {
                        if (DownloadFeature.this.mCompositeSubscription != null)
                            DownloadFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(DownLoadData resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        DownloadFeature.this.getMvpView().downloadNotReceiver(resultResponse);
                    }
                }));
    }
    public void appUpdateInfo(String deviceId){
        this.mCompositeSubscription.add(this.mDataManager.appUpdateInfo(deviceId)
                .subscribe(new AbsAPICallback<UpDateBean>() {
                    @Override
                    public void onCompleted() {
                        if (DownloadFeature.this.mCompositeSubscription != null)
                            DownloadFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(UpDateBean resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        DownloadFeature.this.getMvpView().downloadApK(resultResponse);
                    }
                }));
    }
    public void syncUserFeaturePages(String deviceId, int currentPage){
        this.mCompositeSubscription.add(this.mDataManager.syncUserFeaturePages(deviceId,currentPage)
                .subscribe(new AbsAPICallback<SyncFeaturesPage>() {
                    @Override
                    public void onCompleted() {
                        if (DownloadFeature.this.mCompositeSubscription != null)
                            DownloadFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(SyncFeaturesPage resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        DownloadFeature.this.getMvpView().syncUserFeaturePagesSuccess(resultResponse);
                    }
                }));
    }
    public void getPagesInfo(String deviceId){
        this.mCompositeSubscription.add(this.mDataManager.getPagesInfo(deviceId)
                .subscribe(new AbsAPICallback<PagesInfoBean>() {
                    @Override
                    public void onCompleted() {
                        if (DownloadFeature.this.mCompositeSubscription != null)
                            DownloadFeature.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DownloadFeature.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(PagesInfoBean resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        DownloadFeature.this.getMvpView().getPagesInfo(resultResponse);
                    }
                }));
    }


}
