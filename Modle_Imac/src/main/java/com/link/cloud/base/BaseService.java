package com.link.cloud.base;

import com.google.gson.JsonObject;
import com.link.cloud.bean.CodeInfo;
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
import com.link.cloud.bean.UserResponse;
import com.link.cloud.bean.Voucher;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Shaozy on 2016/8/10.
 */
public interface BaseService {

    /**
     * 1.根据设备ID和手机号查询会员信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<Member>
     */
//    @POST("getMemInfoByVeinDeviceIDPhone")
//    Observable<Member> getMemInfo( @Body JsonObject params);
    @POST("validationUser")
    Observable<Member> getMemInfo( @Body JsonObject params);
    @POST("validationUser")
    Observable<FaceBindBean> getMemFace( @Body JsonObject params);

    @POST("getNotReveiceFeature")
    Observable<DownLoadData>downloadNotReceiver(@Body JsonObject params);

    @POST("appUpdateInfo")
    Observable<UpDateBean>appUpdateInfo(@Body JsonObject params);
    /**
     * 2.指静脉设备绑定会员接口
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("bindUserFinger")
    Observable<Member> bindVeinMemeber(@Body JsonObject params);

    /**
     * 3.通过指静脉查询会员信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<Member>
     */
    @POST("queryMemInfoByVein")
    Observable<Member> getMemberInfoByVein(@Body JsonObject params);

    /**
     * 4通过会员ID查询会员信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<Member>
     */
    @POST("queryMemInfoByMemID")
    Observable<Member> getMemInfoByMemID(@Body JsonObject params);

    /**
     * 5.查询会员卡信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<CardInfo>
     */
    @POST("queryMemCardByMemID")
    Observable<ReturnBean> getCardInfo(@Body JsonObject params);
    /**
     * 6.会员的上次签到时间
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("getLastSignedTime")
    Observable<ReturnBean> getLastSignedTime(@Body JsonObject params);
    /**
     * 7.发送日志信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("validateLogs")
    Observable<RestResponse> sendLogMessage(@Body JsonObject params);

    /**
     * 8.指静脉消费接口
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("consumeRecord")
    Observable<Voucher> consumeRecord(@Body JsonObject params);
    /**
     * 9.新增签到接口 员工&会员签到
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("checkIn")
    Observable<Code_Message> signMember(@Body JsonObject params);
    /**
     * 10.用户消课指静脉验证接口
     */
    @POST("verifyUserEliminateLesson")
    Observable<UserResponse>verifyUserEliminateLesson(@Body JsonObject params);
    /**
     *  消课接口
     */
    @POST("getLessonInfo")
    Observable<RetrunLessons>eliminateLesson(@Body JsonObject params);
    /**
     * 选择消课接口
     */
    @POST("selectLesson")
    Observable<RetrunLessons>selectLesson(@Body JsonObject params);
    /**
     * 11.获取签到二维码接口
     */
    @POST("signedCodeInfo")
    Observable<CodeInfo>singnedCodeInfo(@Body JsonObject params);
    /**
     * 12.检测软件版本
     */
    @POST("deviceUpgrade")
    Observable<UpdateMessage>deviceUpgrade(@Body JsonObject params);
    /**
     * 2.心跳包
     */
    @POST("deviceHeartBeat")
    Observable<ResultHeartBeat>deviceHeartBeat(@Body JsonObject params);
    //2018
    @POST("deviceRegister")
    //13.获取设备id
    Observable<DeviceData>getdeviceId(@Body JsonObject params);
    /**
     * 同步接口
     * @param params
     * @return
     */
    @POST("syncUserFeature")
    Observable<DownLoadData>syncUserFeature(@Body JsonObject params);
    /**
     * 下载指静脉数据
     * @param params
     * @return
     */
    @POST("downloadFeature")
    Observable<DownLoadData>downloadFeature(@Body JsonObject params);

    @POST("syncUserFeatureCount")
    Observable<PagesInfoBean>getPagesInfo(@Body JsonObject params);

    @POST("syncUserFeaturePages")
    Observable<SyncFeaturesPage>syncUserFeaturePages(@Body JsonObject params);

    @POST("checkInByQrCode")
    Observable<Code_Message>checkInByQrCode(@Body JsonObject paras);
    @Multipart
    @POST("bindUserFace")
    Observable<FaceBindBean>bindFace(@Part("deviceId")RequestBody deviceId,
    @Part("userType")RequestBody userType,
                                     @Part("numberValue")RequestBody numberValue,
                                     @Part("numberType")RequestBody numberType,
                                     @Part("code")RequestBody code,
                                     @Part("key")RequestBody key,
                                     @Part("datetime")RequestBody datetime,
                                     @Part("sign")RequestBody sign,
                                     @Part() MultipartBody.Part faceImage,
                                     @Part() MultipartBody.Part faceData

    );

    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
    @POST("syncUserFacePages")
    Observable<SyncUserFace>syncUserFacePages(@Body JsonObject params);

}
