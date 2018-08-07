package com.link.cloud.model.impl;

import android.widget.EditText;

import com.google.gson.JsonObject;
import com.link.cloud.bean.CabinetNumberData;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.bean.MessagetoJson;
import com.link.cloud.bean.OpenByQrCode;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.orhanobut.logger.Logger;
import com.link.cloud.base.RestApi;
import com.link.cloud.base.BaseApi;
import com.link.cloud.bean.CodeInfo;
import com.link.cloud.bean.UpdateMessage;
import com.link.cloud.bean.UserResponse;
import com.link.cloud.bean.LessonResponse;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.ReturnBean;
import com.link.cloud.bean.SignedResponse;
import com.link.cloud.bean.Voucher;
import com.link.cloud.model.IHttpClientHelper;

import java.util.concurrent.ExecutionException;

import rx.Observable;

/**
 * Created by Shaozy on 2016/8/10.
 */
public class HttpClientHelper implements IHttpClientHelper {
    public Boolean flag=true;
    private static final HttpClientHelper ourInstance = new HttpClientHelper();

    public static HttpClientHelper getInstance() {
        return ourInstance;
    }

    public HttpClientHelper() {
    }
    @Override
    public Observable<PagesInfoBean> getPagesInfo(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getPagesInfo(params);
    }
    @Override
    public Observable<SyncFeaturesPage> syncUserFeaturePages(String deviceId, int currentPage) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("currPage", currentPage);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().syncUserFeaturePages(params);
    }
    @Override
    public Observable<DownLoadData> downloadNotReceiver(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().downloadNotReceiver(params);
    }
    @Override
    public Observable<UpDateBean> appUpdateInfo(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("sign", "4924d3cea90f496b2d1d8f0996e19ef5");
            params.addProperty("code","link");
            params.addProperty("datetime",  "1529474071070");
            params.addProperty("deviceId", deviceId);
            params.addProperty("key", "848ec6fa44ac6bae");

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().appUpdateInfo(params);
    }
    @Override
    public Observable<RestResponse> sendLogMessage(String deviceId, String uid,String uids,String feature,String time,String scope, String result) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("uid", uid);
            params.addProperty("uids", uids);
            params.addProperty("feature",feature);
            params.addProperty("time",time);
            params.addProperty("scope",scope);
            params.addProperty("result",result);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().sendLogMessage(params);
    }
    //心跳接口
    @Override
    public Observable<ResultHeartBeat> deviceHeartBeat(String deviceId) {
        JsonObject params=new JsonObject();
        try {
            params.addProperty("deviceId",deviceId);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().deviceHeartBeat(params);
    }
    @Override
    public Observable<Sign_data> syncSignUserFeature(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().syncSignUserFeature(params);
    }
    @Override
    public Observable<UpdateMessage> deviceUpgrade(String deviceID) {
        JsonObject params=new JsonObject();
        try {
            params.addProperty("deviceId",deviceID);

        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().deviceUpgrade(params);
    }


    @Override
    public Observable<Code_Message> validationQrCode(String deviceId, String qrCodeStr) {
        JsonObject params=new JsonObject();
        try {
            params.addProperty("deviceId",deviceId);
            params.addProperty("qrCodeStr",qrCodeStr);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().validationQrCode(params);
    }

    @Override
    public Observable<DeviceData> getdeviceID(String deviceTargetValue,int deviceType) {
        JsonObject pareams=new JsonObject();
        try {
            pareams.addProperty("deviceType",deviceType);
            pareams.addProperty("deviceTargetValue",deviceTargetValue);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getdeviceId(pareams);
    }

    @Override
    public Observable<Lockdata> isOpenCabinet(String deviceId,String uid, String fromType) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("uid", uid);
            params.addProperty("fromType",fromType);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().isOpenCabinet(params);
    }
    @Override
    public Observable<ResultResponse> clearCabinet(String deviceId, String cabinetNumber) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("cabinetNumber", cabinetNumber);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().clearCabinet(params);
    }
    @Override
    public Observable<ResultResponse> adminiOpenCabinet(String deviceId, String cabinetNumber) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("cabinetNumber", cabinetNumber);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().adminiOpenCabinet(params);
    }
    @Override
    public Observable<DownLoadData> downloadFeature(String messageId, String appid, String shopId, String deviceId, String uid) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("messageId", messageId);
            params.addProperty("appid", appid);
            params.addProperty("shopId", shopId);
            params.addProperty("deviceId", deviceId);
            params.addProperty("uid", uid);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().downloadFeature(params);
    }
    @Override
    public Observable<DownLoadData> syncUserFeature(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().syncUserFeature(params);
    }

    @Override
    public Observable<CabinetNumberData> cabinetNumberList(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().cabinetNumberList(params);
    }
    @Override
    public Observable<SyncUserFace> syncUserFacePages(String deviceId) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);


        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().syncUserFacePages(params);
    }
}
