package net.zx.web.ztalker.push.bean.api.base;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020/2/14
 * @time 17:26
 */
public class ResponseModel<M> implements Serializable {
    /*成功*/
    public static final int SUCCEED = 1;
    /*未知错误*/
    public static final int ERROR_UNKNOW = 1;

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
    /*创建群成员失败*/
    public static final int ERROR_CREATE_GROUP_MEMBER = 3003;

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

    @Expose
    private int code;
    @Expose
    private String msg;
    @Expose
    private LocalDateTime time = LocalDateTime.now();
    @Expose
    private M result;


    public ResponseModel() {
        code = 1;
        msg = "ok";
    }

    public ResponseModel(M result) {
        this();
        this.result = result;
    }

    public ResponseModel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseModel(int code, String msg, M result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

    public static <M> ResponseModel<M> buildOk() {
        return new ResponseModel<>();
    }

    public static <M> ResponseModel<M> buildOk(M result) {
        return new ResponseModel<>(result);
    }

    public static <M> ResponseModel<M> buildParameterError() {
        return new ResponseModel<>(ERROR_PARAMETERS, "Parameters Error.");
    }

    public static <M> ResponseModel<M> buildHaveAccountError() {
        return new ResponseModel<>(ERROR_PARAMETERS_EXIST_ACCOUNT, "Already have this account.");
    }

    public static <M> ResponseModel<M> buildHaveNameError() {
        return new ResponseModel<>(ERROR_PARAMETERS_EXIST_NAME, "Already have this name.");
    }

    public static <M> ResponseModel<M> buildServiceError() {
        return new ResponseModel<>(ERROR_SERVICE, "Service error.");
    }

    public static <M> ResponseModel<M> buildNotFoundUserError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_USER, str != null ? str : "Not Found User.");
    }

    public static <M> ResponseModel<M> buildNotFoundGroupError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_GROUP, str != null ? str : "Not Found Group.");
    }

    public static <M> ResponseModel<M> buildNotFoundGroupMemberError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_GROUP_MEMBER, str != null ? str : "Not Found GroupMember.");
    }

    public static <M> ResponseModel<M> buildAccountError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_TOKEN, "Account Error; you need login.");
    }

    public static <M> ResponseModel<M> buildLoginError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_LOGIN, "Account or password error.");
    }

    public static <M> ResponseModel<M> buildRegisterError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_REGISTER, "Have this account.");
    }

    public static <M> ResponseModel<M> buildNoPermissionError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_NO_PERMISSION, "You do not have permission to operate.");
    }

    public static <M> ResponseModel<M> buildCreateError(int type) {
        return new ResponseModel<M>(type, "Create failed.");
    }
}
