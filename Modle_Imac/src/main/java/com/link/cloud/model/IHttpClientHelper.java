package com.link.cloud.model;

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

import rx.Observable;

/**
 * Created by Shaozy on 2016/8/10.
 */
public interface IHttpClientHelper {


    /**
     * 静脉设备绑定会员接口
     *
     * @param phone         会员手机号码
     * @param deviceID      指静脉设备ID
     * @param veinFingerID1 指静脉ID
     * @param veinFingerID2 指静脉ID
     * @param veinFingerID3 指静脉ID
     * @return Observable<ReturnBean>
     */
    /**
     *
     * @param deviceId  设备id
     * @param userType  用户类型
     * @param numberType  号码类型
     * @param numberValue 号码
     * @param feature    指静脉数据
     * @return
     */
    Observable<Member> bindVeinMemeber(String deviceId,int userType,int numberType,String numberValue,String img1,String img2,String img3,String feature);
    /**
     * 心跳接口
     * @param deviceId
     * @return
     */
    Observable<ResultHeartBeat>deviceHeartBeat(String deviceId);

    /**
     * 根据设备ID和手机号查询会员信息
     * @param numberType 号码类型
     * @param numberValue 会员手机号码
     * @param deviceID 指静脉设备ID
     * @return Observable<Member>
     */
    Observable<Member> getMemInfo( String deviceID,int numberType,String numberValue);

    Observable<FaceBindBean> getMemFace( String deviceID,int numberType,String numberValue);

    /**
     * 查询会员卡信息
     *
     * @param memID 会员ID
     * @return Observable<CardInfo>
     */
    Observable<ReturnBean> getCardInfo(String memID);

    /**
     * 会员签到
     *
     * @param deviceID  设备ID
     * @param veinFingerID 用户指静脉数据
     * @param phoneNum 用户手机号
     * @return Observable<ReturnBean>
     */
    Observable<Code_Message> signedMember(String deviceID, String veinFingerID, String phoneNum);

    /**
     * 发送日志信息
     * @param deviceId
     * @param uid
     * @param uids
     * @param feature
     * @param time
     * @param scope
     * @param result
     * @return
     */
    Observable<RestResponse> sendLogMessage(String deviceId, String uid,String uids,String feature,String time,String scope, String result);
    /**
     *
     * @param deviceID  设备ID
     * @param memberid  会员UID
     * @param coachid   教练UID
     * @param clerkid   前台UID
     * @return
     */
    Observable<RetrunLessons>eliminateLesson(String deviceID, int type, String memberid, String coachid, String clerkid);

    /**
     * 选择课程
     * @param deviceID
     * @param type
     * @param lessonId
     * @param memberid
     * @param coachid
     * @return
     */
    Observable<RetrunLessons>selectLesson(String deviceID, int type, String lessonId, String memberid, String coachid, String clerkid,String CardNo,int count);

    /**
     *
     * @param deviceID  设备ID
     * @return
     */
    Observable<UpdateMessage>deviceUpgrade(String deviceID);

    /**
     *
     * @param deviceTargetValue
     * @return
     */
    Observable<DeviceData>getdeviceID(String deviceTargetValue,int devicetype);

    /**
     * 数据同步
     * @param deviceId
     * @return
     */
    Observable<DownLoadData>syncUserFeature(String deviceId);
    /**
     *  下载指静脉数据
     * @param messageId
     * @param appid
     * @param shopId
     * @param deviceId
     * @param uid
     * @return
     */
    Observable<DownLoadData> downloadFeature(String messageId, String appid, String shopId, String deviceId, String uid);

    /**
     *
     * @param deviceId
     * @return
     */
    Observable<DownLoadData>downloadNotReceiver(String deviceId);

    /**
     *
     * @param deviceId
     * @return
     */
    Observable<UpDateBean>appUpdateInfo (String deviceId);

    Observable<PagesInfoBean> getPagesInfo(String deviceId);
    Observable<SyncFeaturesPage> syncUserFeaturePages(String deviceId, int currentPage);
    Observable<Code_Message> checkInByQrCode(String deviceId, String qrcode);
    Observable<FaceBindBean> bindFace(String deviceID, int numberType, String numberValue, int userType, String path,String faceFile);
    Observable<SyncUserFace> syncUserFacePages(String deviceID);
}
