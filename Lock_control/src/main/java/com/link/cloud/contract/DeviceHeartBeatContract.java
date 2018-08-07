package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/11.
 */

public class DeviceHeartBeatContract extends BasePresenter<DeviceHeartBeatContract.Devicehearbeat> {

    public interface Devicehearbeat extends MvpView {
        void deviceHearBeat(ResultHeartBeat deviceHeartBeat);
    }
    public ReservoirUtils reservoirUtils;

    public DeviceHeartBeatContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void deviceUpgrade(String deviceID){
        this.mCompositeSubscription.add(this.mDataManager.deviceHeartBeat(deviceID)
                .subscribe(new AbsAPICallback<ResultHeartBeat>() {
                    @Override
                    public void onCompleted() {
                        if (DeviceHeartBeatContract.this.mCompositeSubscription != null)
                            DeviceHeartBeatContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
                        Logger.e("VersoinUpdateContract onError"+e.getMessage());

                        DeviceHeartBeatContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());

                        DeviceHeartBeatContract.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        DeviceHeartBeatContract.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(ResultHeartBeat deviceHeartBeat) {
//                        Logger.e("VersoinUpdateContract"+updateMessage.toString());
                        DeviceHeartBeatContract.this.getMvpView().deviceHearBeat(deviceHeartBeat);
                    }
                }));
    }

}
