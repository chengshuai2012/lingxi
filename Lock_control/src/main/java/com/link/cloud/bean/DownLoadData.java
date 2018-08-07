package com.link.cloud.bean;

import java.util.List;

/**
 * Created by choan on 2018/7/20.
 */

public class DownLoadData {

    /**
     * data : [{"uid":"ncmchbv9n58","shopId":"0xj13ti_0000zd","userName":"123456","appid":"0xj13ti","feature":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","fingerId":6,"deviceId":"pmljt8z"},{"uid":"9e58mq89n74","shopId":"0xj13ti_0000zd","userName":"君君呀","appid":"0xj13ti","feature":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","fingerId":16,"deviceId":"pmljt8z"}]
     * msg : 请求成功
     * status : 0
     */

    private String msg;
    private int status;
    private List<Person> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }


}
