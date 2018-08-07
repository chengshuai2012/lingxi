package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class AdminopenCabinet extends BasePresenter<AdminopenCabinet.adminopen> {

    public interface adminopen extends MvpView {
        void adminopenSuccess(ResultResponse resultResponse);
    }
    public ReservoirUtils reservoirUtils;

    public AdminopenCabinet() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void adminopen(String deviceId,String cabinetNumber){
        this.mCompositeSubscription.add(this.mDataManager.adminiOpenCabinet(deviceId,cabinetNumber)
                .subscribe(new AbsAPICallback<ResultResponse>() {
                    @Override
                    public void onCompleted() {
                        if (AdminopenCabinet.this.mCompositeSubscription != null)
                            AdminopenCabinet.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        AdminopenCabinet.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        AdminopenCabinet.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        AdminopenCabinet.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(ResultResponse resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        AdminopenCabinet.this.getMvpView().adminopenSuccess(resultResponse);
                    }
                }));
    }

}
