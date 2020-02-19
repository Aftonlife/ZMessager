package net.zx.web.ztalker.push.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * @author Administrator
 * @date 2020/2/18
 * @time 20:36
 */
public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String pushId;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public static boolean check(LoginModel model){
        return null!=model
                && !Strings.isNullOrEmpty(model.getAccount())
                && !Strings.isNullOrEmpty(model.getPassword());
    }
}
