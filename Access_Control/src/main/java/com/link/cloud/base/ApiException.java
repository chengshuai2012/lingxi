package com.link.cloud.base;

/**
 * Created by Shaozy on 2016/8/14.
 */
public class ApiException extends Exception {

    private int code;
    private String displayMessage;

    public static final int UNKNOWN = 1000;
    public static final int PARSE_ERROR = 1001;
    public static final int SOCKET_ERROR=2000;  //socket连接异常
    public static final int REGISTER_TEMPLATE_ERROR = 1002;//注册模板错误
    public static final int MATCH_TEMPLATE_ERROR = 1003;//验证模板错误
    public static final int MISSING_FINGER_ERROR = 1004;//未检测到手指
    public static final int MOVING_FINGER_ERROR = 1005;//手指未移开
    public static final int SAVE_TEMPLATE_ERROR = 1006;//保存模板错误
    public static final int CONNECTION_ERROR = 1007;//服务器连接异常

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(String displayMessage, int code) {
        super(new Throwable(displayMessage));
        this.displayMessage = displayMessage;
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String msg) {
        this.displayMessage = msg;
    }
}
