package com.zx.app.factory.model.api.account;

import com.zx.app.factory.model.db.User;

/**
 * author Afton
 * date 2020/2/25
 * 返回的数据
 */
public class AccountRspModel {
    /*用户基本信息*/
    private User user;
    /*账号*/
    private String account;
    /*登录成功获取Token,之后可以用Token获取用户信息*/
    private String token;
    /*是否绑定到设备PushId*/
    private boolean isBind;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
