package com.link.cloud.contract;

import android.content.Context;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.link.cloud.R;
import com.link.cloud.bean.FaceBindBean;
import com.orhanobut.logger.Logger;

import com.link.cloud.base.AbsAPICallback;
import com.link.cloud.core.BasePresenter;
import com.link.cloud.utils.ReservoirUtils;
import com.link.cloud.BaseApplication;

import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.core.MvpView;

import java.util.HashMap;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Shaozy on 2016/8/11.
 */
public class RegisterTaskContract extends BasePresenter<RegisterTaskContract.RegisterView> {

    public RegisterTaskContract registerTaskContract;
    public interface RegisterView extends MvpView {
        void onSuccess(Member memberInfo);
        void onBindFaceSuccess(FaceBindBean faceBindBean);
        void onFaceSuccess(FaceBindBean faceBindBean);
    }
    public ReservoirUtils reservoirUtils;

    public RegisterTaskContract() {
        this.reservoirUtils = new ReservoirUtils();
    }



    public void getMemInfo(String deviceID,int numberType,String numberValue) {
        this.mCompositeSubscription.add(this.mDataManager.getMemInfo(deviceID,numberType,numberValue)
                .subscribe(new AbsAPICallback<Member>() {
                    @Override
                    public void onCompleted() {
                        if (RegisterTaskContract.this.mCompositeSubscription != null) {
                            RegisterTaskContract.this.mCompositeSubscription.remove(this);
                        }
                    }
                    @Override
                    protected void onError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onPermissionError(e);
                    }

                    @Override
                    protected void onResultError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onResultError(e);
                    }

                    @Override
                    public void onNext(Member member) {
                        //准备指静脉设备
                        /*Member member = new Member();
                        member.setMemID("123");
                        member.setName("老王");
                        member.setPhone("13007436471");
                        member.setSex("男");
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setName("白金卡");
                        cardInfo.setCardID("123");
                        cardInfo.setCardBalance("99.99元");
                        cardInfo.setEndTime("至2016年12月20日");
                        //member.setCardInfo(cardInfo);*/
                        RegisterTaskContract.this.getMvpView().onSuccess(member);
                    }
                }));
    }
    public void getMemFace(String deviceID,int numberType,String numberValue) {
        this.mCompositeSubscription.add(this.mDataManager.getMemFace(deviceID,numberType,numberValue)
                .subscribe(new AbsAPICallback<FaceBindBean>() {
                    @Override
                    public void onCompleted() {
                        if (RegisterTaskContract.this.mCompositeSubscription != null) {
                            RegisterTaskContract.this.mCompositeSubscription.remove(this);
                        }
                    }
                    @Override
                    protected void onError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onPermissionError(e);
                    }

                    @Override
                    protected void onResultError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onResultError(e);
                    }

                    @Override
                    public void onNext(FaceBindBean member) {
                        //准备指静脉设备
                        /*Member member = new Member();
                        member.setMemID("123");
                        member.setName("老王");
                        member.setPhone("13007436471");
                        member.setSex("男");
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setName("白金卡");
                        cardInfo.setCardID("123");
                        cardInfo.setCardBalance("99.99元");
                        cardInfo.setEndTime("至2016年12月20日");
                        //member.setCardInfo(cardInfo);*/
                        RegisterTaskContract.this.getMvpView().onFaceSuccess(member);
                    }
                }));
    }

    public void bindFace(String deviceID, int numberType, String numberValue, int userType, String path, String faceFile) {
        this.mCompositeSubscription.add(this.mDataManager.bindFace(deviceID,numberType,numberValue,userType,path,faceFile)
                .subscribe(new AbsAPICallback<FaceBindBean>() {
                    @Override
                    public void onCompleted() {
                        if (RegisterTaskContract.this.mCompositeSubscription != null) {
                            RegisterTaskContract.this.mCompositeSubscription.remove(this);
                        }
                    }
                    @Override
                    protected void onError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onError(e);
                    }
                    @Override
                    protected void onPermissionError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onPermissionError(e);
                    }

                    @Override
                    protected void onResultError(ApiException e) {
                        RegisterTaskContract.this.getMvpView().onResultError(e);
                    }

                    @Override
                    public void onNext(FaceBindBean faceBindBean) {
                        //准备指静脉设备
                        /*Member member = new Member();
                        member.setMemID("123");
                        member.setName("老王");
                        member.setPhone("13007436471");
                        member.setSex("男");
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setName("白金卡");
                        cardInfo.setCardID("123");
                        cardInfo.setCardBalance("99.99元");
                        cardInfo.setEndTime("至2016年12月20日");
                        //member.setCardInfo(cardInfo);*/
                        RegisterTaskContract.this.getMvpView().onBindFaceSuccess(faceBindBean);
                    }
                }));
    }
}
