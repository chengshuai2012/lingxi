package com.link.cloud.contract;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.MessagetoJson;
import com.link.cloud.bean.UpdateMessage;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/9/11.
 */

public class GetDeviceIDContract extends BasePresenter<GetDeviceIDContract.VersoinUpdate> {

    public interface VersoinUpdate extends MvpView {
        void getDeviceSuccess(DeviceData deviceData);
    }
    public ReservoirUtils reservoirUtils;

    public GetDeviceIDContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    public void getDeviceID(String devicedata,int deviceType){
        this.mCompositeSubscription.add(this.mDataManager.getdeviceID(devicedata,deviceType)
                .subscribe(new AbsAPICallback<DeviceData>() {
                    @Override
                    public void onCompleted() {
                        if (GetDeviceIDContract.this.mCompositeSubscription != null)
                            GetDeviceIDContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        GetDeviceIDContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        GetDeviceIDContract.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        GetDeviceIDContract.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(DeviceData deviceData) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        GetDeviceIDContract.this.getMvpView().getDeviceSuccess(deviceData);
                    }
                }));
    }

}
