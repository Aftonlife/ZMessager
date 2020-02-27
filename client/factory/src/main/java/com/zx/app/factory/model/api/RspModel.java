package com.zx.app.factory.model.api;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * author Afton
 * date 2020/2/25
 */
public class RspModel<T> {
    /*成功*/
    public static final int SUCCEED = 1;
    /*未知错误*/
    public static final int ERROR_UNKNOWN = 0;

    /*找不到用户信息*/
    public static final int ERROR_NOT_FOUND_USER = 4041;
    /*找不到群信息*/
    public static final int ERROR_NOT_FOUND_GROUP = 4042;
    /*找不到群成员信息*/
    public static final int ERROR_NOT_FOUND_GROUP_MEMBER = 4043;

    /*创建用户失败*/
    public static final int ERROR_CREATE_USER = 3001;
    /*创建群失败*/
    public static final int ERROR_CREATE_GROUP = 3002;
    /*创建群消息失败*/
    public static final int ERROR_CREATE_MESSAGE = 3003;

    /*参数错误*/
    public static final int ERROR_PARAMETERS = 4001;
    /*参数错误-已存在账户*/
    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;
    /*参数错误-已存在名称*/
    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;

    /*服务器错误*/
    public static final int ERROR_SERVICE = 5001;

    /*账户Token错误，需要重新登录*/
    public static final int ERROR_ACCOUNT_TOKEN = 2001;
    /*账户登录失败*/
    public static final int ERROR_ACCOUNT_LOGIN = 2002;
    /*账户注册失败*/
    public static final int ERROR_ACCOUNT_REGISTER = 2003;
    /*账户没有权限*/
    public static final int ERROR_ACCOUNT_NO_PERMISSION = 2010;

    private int code;
    private String msg;
    private Date time;
    private T result;

    public boolean success() {
        return code == SUCCEED;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
