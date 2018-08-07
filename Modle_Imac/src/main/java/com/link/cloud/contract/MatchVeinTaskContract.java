package com.link.cloud.contract;

//import com.baidu.tts.client.SpeechSynthesizer;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.SignUserdata;
import com.link.cloud.bean.SignedResponse;
import com.link.cloud.bean.Voucher;
        import com.link.cloud.core.BasePresenter;
import com.link.cloud.core.MvpView;
import com.link.cloud.utils.ReservoirUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Shaozy on 2016/8/11.
 */
public class MatchVeinTaskContract extends BasePresenter<MatchVeinTaskContract.MatchVeinView> {
//    SpeechSynthesizer mSpeechSynthesizer=SpeechSynthesizer.getInstance();
//    ActMainActivity mainActivity;
    public interface MatchVeinView extends MvpView {
        void signSuccess(Code_Message signedResponse);
        void checkInSuccess(Code_Message code_message);
    }
    public ReservoirUtils reservoirUtils;

    public MatchVeinTaskContract() {
        this.reservoirUtils = new ReservoirUtils();
    }

    //2014/4/3新品台签到接口
    public void signedMember(String deviceId, String uid, String fromType) {
        this.mCompositeSubscription.add(this.mDataManager.signedMember(deviceId, uid, fromType)
                .subscribe(new AbsAPICallback<Code_Message>() {
                    @Override
                    public void onCompleted() {
                        if (MatchVeinTaskContract.this.mCompositeSubscription != null)
                            MatchVeinTaskContract.this.mCompositeSubscription.remove(this);
                    }

                    @Override
                    protected void onError(ApiException e) {
                        MatchVeinTaskContract.this.getMvpView().onError(e);
                    }

                    @Override
                    protected void onPermissionError(ApiException e) {
                        onError(e);
                    }

                    @Override
                    protected void onResultError(ApiException e) {
                        onError(e);
                    }

                    @Override
                    public void onNext(Code_Message signedResponse) {
                        MatchVeinTaskContract.this.getMvpView().signSuccess(signedResponse);
                    }
                }));
    }
    public void checkInByQrCode(String deviceId,String qrcode){
        this.mCompositeSubscription.add(this.mDataManager.checkInByQrCode(deviceId,qrcode)
                .subscribe(new AbsAPICallback<Code_Message>() {
                    @Override
                    public void onCompleted() {
                        if (MatchVeinTaskContract.this.mCompositeSubscription != null)
                            MatchVeinTaskContract.this.mCompositeSubscription.remove(this);
                    }
                    @Override
                    protected void onError(ApiException e) {
//                        Logger.e("VersoinUpdateContract onError"+e.getMessage());
                        MatchVeinTaskContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        Logger.e("VersoinUpdateContract onPermissionError"+e.getMessage());
                        MatchVeinTaskContract.this.getMvpView().onPermissionError(e);
                    }
                    @Override
                    protected void onResultError(ApiException e) {
                        Logger.e("VersoinUpdateContract onResultError"+e.getMessage());
                        MatchVeinTaskContract.this.getMvpView().onResultError(e);
                    }
                    @Override
                    public void onNext(Code_Message resultResponse) {
//                        Logger.e("VersoinUpdateContract"+deviceData.toString());
                        MatchVeinTaskContract.this.getMvpView().checkInSuccess(resultResponse);
                    }
                }));
    }

//    //消费
//    public void consumeRecord(String phoneNum, String deviceID, String veinFingerID, String price, String method, String mark) {
//        this.mCompositeSubscription.add(this.mDataManager.consumeRecord(phoneNum, deviceID, veinFingerID, price, method, mark)
//                .subscribe(new AbsAPICallback<Voucher>() {
//                    @Override
//                    public void onCompleted() {
//                        if (MatchVeinTaskContract.this.mCompositeSubscription != null)
//                            MatchVeinTaskContract.this.mCompositeSubscription.remove(this);
//                    }
//                    @Override
//                    protected void onError(ApiException e) {
//                        MatchVeinTaskContract.this.getMvpView().onError(e);
//                    }
//                    @Override
//                    protected void onPermissionError(ApiException e) {
//                        MatchVeinTaskContract.this.getMvpView().onPermissionError(e);
//                    }
//                    @Override
//                    protected void onResultError(ApiException e) {
//                        MatchVeinTaskContract.this.getMvpView().onResultError(e);
//                    }
//                    @Override
//                    public void onNext(Voucher result) {
//                        //result.setCost(Utils.isEmpty(price) ? 0 : Double.valueOf(price));
//                        MatchVeinTaskContract.this.getMvpView().costSuccess(result);
//                    }
//                }));
//    }

}
