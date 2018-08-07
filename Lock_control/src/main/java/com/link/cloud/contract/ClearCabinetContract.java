package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ClearCabinetContract extends BasePresenter<ClearCabinetContract.clearCabinet> {

    public interface clearCabinet extends MvpView {
        void ClearCabinetSuccess(ResultResponse resultResponse);
    }
    public ReservoirUtils reservoirUtils;

    public ClearCabinetContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void clearCabinet(String deviceId,String cabinetNumber){
        this.mCompositeSubscription.add(this.mDataManager.clearCabinet(deviceId,cabinetNumber)
                .subscribe(new AbsAPICallback<ResultResponse>() {
                    @Override
                    public void onCompleted() {
                        if (ClearCabinetContract.this.mCompositeSubscription != null)
                            ClearCabinetContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        ClearCabinetContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        ClearCabinetContract.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        ClearCabinetContract.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(ResultResponse resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        ClearCabinetContract.this.getMvpView().ClearCabinetSuccess(resultResponse);
                    }
                }));
    }

}
