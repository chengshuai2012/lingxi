package com.link.cloud.model;

import com.link.cloud.bean.CabinetNumberData;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.bean.UpdateMessage;
import com.link.cloud.bean.RestResponse;

import rx.Observable;

/**
 * Created by Shaozy on 2016/8/10.
 */
public interface IHttpClientHelper {
    /**
     *
     * @param deviceID  设备ID
     * @return
     */
    Observable<UpdateMessage>deviceUpgrade(String deviceID);

    /**
     *
     * @param openType
     * @param deviceId
     * @param qrCodeStr
     * @return
     */
    Observable<Code_Message>openCabinetByQrCode(int openType, String deviceId, String qrCodeStr);
    /**
     * 心跳接口
     * @param deviceId
     * @return
     */
    Observable<ResultHeartBeat>deviceHeartBeat(String deviceId);
    /**
     * @param deviceTargetValue
     * @return
     */
    Observable<DeviceData>getdeviceID(String deviceTargetValue, int deviceType);
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
    Observable<RestResponse> sendLogMessage(String deviceId, String uid, String uids, String feature, String time, String scope, String result);

    /**
     * 储物柜操作
     * @param openType
     * @param deviceId
     * @param uid
     * @param fromType
     * @return
     */
    Observable<Lockdata>isOpenCabinet(int openType, String deviceId, String uid, String fromType);


    /**
     * 清除储物柜使用信息
     * @param deviceId
     * @param cabinetNumber
     * @return
     */
    Observable<ResultResponse>clearCabinet(String deviceId, String cabinetNumber);

    /**
     *管理员开柜
     * @param deviceId
     * @param cabinetNumber
     * @return
     */
    Observable<ResultResponse>adminiOpenCabinet(String deviceId, String cabinetNumber);

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
     * 数据同步
     * @param deviceId
     * @return
     */
    Observable<DownLoadData>syncUserFeature(String deviceId);

    /**
     * 签到数据同步
     * @param deviceId
     * @return
     */
    Observable<Sign_data>syncSignUserFeature(String deviceId);

    /**
     * 查询柜号列表
     * @param deviceId
     * @return
     */
    Observable<CabinetNumberData>cabinetNumberList(String deviceId);

    /**
     *
     * @param deviceId
     * @return
     */
    Observable<DownLoadData>downloadNotReceiver(String deviceId, int vs);

    /**
     *
     * @param deviceId
     * @return
     */
    Observable<UpDateBean>appUpdateInfo(String deviceId);

    Observable<PagesInfoBean> getPagesInfo(String deviceId);

    Observable<SyncFeaturesPage> syncUserFeaturePages(String deviceId, int currentPage);
}
