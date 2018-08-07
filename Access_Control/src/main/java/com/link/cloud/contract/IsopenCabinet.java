package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.Isopenmessage;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class IsopenCabinet extends BasePresenter<IsopenCabinet.isopen> {

    public interface isopen extends MvpView {
        void qrCodeSuccess(Code_Message code_message);
        void isopenSuccess(Lockdata resultResponse);
    }
    public ReservoirUtils reservoirUtils;

    public IsopenCabinet() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void isopen(String deviceId,String uid,String fromType){
        this.mCompositeSubscription.add(this.mDataManager.isOpenCabinet(deviceId,uid,fromType)
                .subscribe(new AbsAPICallback<Lockdata>() {
                    @Override
                    public void onCompleted() {
                        if (IsopenCabinet.this.mCompositeSubscription != null)
                            IsopenCabinet.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        IsopenCabinet.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        IsopenCabinet.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        IsopenCabinet.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(Lockdata resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        IsopenCabinet.this.getMvpView().isopenSuccess(resultResponse);
                    }
                }));
    }
    public void memberCode(String deviceId,String qrCodeStr){
        this.mCompositeSubscription.add(this.mDataManager.validationQrCode(deviceId,qrCodeStr)
                .subscribe(new AbsAPICallback<Code_Message>() {
                    @Override
                    public void onCompleted() {
                        if (IsopenCabinet.this.mCompositeSubscription != null)
                            IsopenCabinet.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
                        IsopenCabinet.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        IsopenCabinet.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        IsopenCabinet.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(Code_Message resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        IsopenCabinet.this.getMvpView().qrCodeSuccess(resultResponse);
                    }
                }));
    }
}
