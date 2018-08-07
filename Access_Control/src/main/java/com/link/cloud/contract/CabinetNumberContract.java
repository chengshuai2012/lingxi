package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.CabinetNumberData;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class CabinetNumberContract extends BasePresenter<CabinetNumberContract.cabinetNumber> {

    public interface cabinetNumber extends MvpView {
        void cabinetNumberSuccess(CabinetNumberData cabinetNumberData);
    }
    public ReservoirUtils reservoirUtils;

    public CabinetNumberContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void cabinetNumber(String deviceId){
        this.mCompositeSubscription.add(this.mDataManager.cabinetNumberList(deviceId)
                .subscribe(new AbsAPICallback<CabinetNumberData>() {
                    @Override
                    public void onCompleted() {
                        if (CabinetNumberContract.this.mCompositeSubscription != null)
                            CabinetNumberContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        CabinetNumberContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        CabinetNumberContract.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        CabinetNumberContract.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(CabinetNumberData cabinetNumberData) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        CabinetNumberContract.this.getMvpView().cabinetNumberSuccess(cabinetNumberData);
                    }
                }));
    }

}
