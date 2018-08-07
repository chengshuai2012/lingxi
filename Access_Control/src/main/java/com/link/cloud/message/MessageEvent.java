package com.link.cloud.message;

import com.link.cloud.bean.Lockdata;

/**
 * Created by 30541 on 2018/6/26.
 */

public class MessageEvent {

        public int type;
        public String userId;
        public String deviceId;
        public Lockdata lockdata;
       public String errorStr;
        public MessageEvent(int type, String deviceId, String userId) {
            this.type = type;
            this.userId = userId;
            this.deviceId=deviceId;
        }
        public MessageEvent(int type, String string){
            this.type=type;
            this.errorStr=string;
        }
}
