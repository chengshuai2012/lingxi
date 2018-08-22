package com.link.cloud.model.impl;
import com.google.gson.JsonObject;
import com.link.cloud.base.BaseApi;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.FaceBindBean;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.bean.RetrunLessons;
import com.link.cloud.bean.ReturnBean;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.bean.UpdateMessage;
import com.link.cloud.model.IHttpClientHelper;
import com.orhanobut.logger.Logger;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public Observable<Member> bindVeinMemeber( String deviceId,int userType,int numberType,String numberValue,String img1,String img2,String img3, String feature) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("userType",userType);
            params.addProperty("numberType",numberType);
            params.addProperty("numberValue",numberValue);
            params.addProperty("feature1",img1);
            params.addProperty("feature2",img2);
            params.addProperty("feature3",img3);
            params.addProperty("feature", feature);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().bindVeinMemeber(params);
    }
    @Override
    public Observable<Member> getMemInfo(String deviceID,int numberType,String numberValue) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceID);
            params.addProperty("numberType",numberType);
            params.addProperty("numberValue", numberValue);
        } catch (Exception e) {
                Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getMemInfo(params);
    }

    @Override
    public Observable<FaceBindBean> getMemFace(String deviceID, int numberType, String numberValue) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceID);
            params.addProperty("numberType",numberType);
            params.addProperty("numberValue", numberValue);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getMemFace(params);
    }

    @Override
    public Observable<ReturnBean> getCardInfo(String memID) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("memId", memID);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getCardInfo(params);
    }
    @Override
    public Observable<Code_Message> signedMember(String deviceId, String uid, String fromType) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("uid", uid);
            params.addProperty("fromType", fromType);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().signMember(params);
    }
    @Override
    public Observable<RetrunLessons> selectLesson(String deviceID, int type, String lessonId, String memberid, String coachid, String clerkid,String CardNo,int count) {
        JsonObject params=new JsonObject();
        try {
            params.addProperty("deviceId",deviceID);
            params.addProperty("type",type);
            params.addProperty("lessonId",lessonId);
            params.addProperty("memberid",memberid);
            params.addProperty("coachid",coachid);
            params.addProperty("cardNo",CardNo);
            params.addProperty("number",count);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().selectLesson(params);
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
    public Observable<RetrunLessons> eliminateLesson(String deviceID,int type, String memberid, String coachid, String clerkid) {
        JsonObject params=new JsonObject();
        try {
            params.addProperty("deviceId",deviceID);
            params.addProperty("type",type);
            params.addProperty("memberid",memberid);
            params.addProperty("coachid",coachid);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().eliminateLesson(params);
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
    public Observable<DeviceData> getdeviceID(String deviceTargetValue,int devicetype) {
        JsonObject pareams=new JsonObject();
        try {
            pareams.addProperty("deviceTargetValue",deviceTargetValue);
            pareams.addProperty("deviceType",devicetype);
        }catch (Exception e){
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().getdeviceId(pareams);
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
    public Observable<Code_Message> checkInByQrCode(String deviceId, String qrcode) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceId);
            params.addProperty("qrCodeStr", qrcode);

        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        return BaseApi.getInstance().getBaseService().checkInByQrCode(params);
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

    @Override
    public Observable<FaceBindBean> bindFace(String deviceId, int numberType, String numberValue, int userType, String path,String faceFile) {

           RequestBody requestdeviceId = RequestBody.create(MediaType.parse("multipart/form-data"), deviceId);
            RequestBody requestnumberType = RequestBody.create(MediaType.parse("multipart/form-data"), numberType+"");
            RequestBody requestnumberValue = RequestBody.create(MediaType.parse("multipart/form-data"), numberValue);
            RequestBody requestuserType = RequestBody.create(MediaType.parse("multipart/form-data"), userType+"");
            RequestBody link = RequestBody.create(MediaType.parse("multipart/form-data"), "link");
            RequestBody key = RequestBody.create(MediaType.parse("multipart/form-data"), "848ec6fa44ac6bae");
            RequestBody dateTme = RequestBody.create(MediaType.parse("multipart/form-data"), "1512028642184");
            RequestBody sign = RequestBody.create(MediaType.parse("multipart/form-data"), "cc5224ee3e9f2e2089624f676d840524");
            File file =new File(path);
            RequestBody requestImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
           MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("imgFile", file.getName(), requestImgFile);
            File file2 =new File(faceFile);
            RequestBody requestfacedata = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
           MultipartBody.Part facedataPart = MultipartBody.Part.createFormData("file", file2.getName(), requestfacedata);

        return BaseApi.getInstance().getBaseService().bindFace(requestdeviceId,requestuserType,requestnumberValue,requestnumberType,link,key,dateTme,sign,requestImgPart,facedataPart);
    }


}
