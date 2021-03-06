package net.zx.web.ztalker.push.bean.api.account;

import com.google.gson.annotations.Expose;
import net.zx.web.ztalker.push.bean.card.UserCard;
import net.zx.web.ztalker.push.bean.db.User;

/**
 * @author Administrator
 * @date 2020/2/18
 * @time 19:04
 */
public class AccountRspModel {
    /*用户基本信息*/
    @Expose
    private UserCard user;
    /*账号*/
    @Expose
    private String account;
    /*登录成功获取Token,之后可以用Token获取用户信息*/
    @Expose
    private String token;
    /*是否绑定到设备PushId*/
    @Expose
    private boolean isBind;

    /*默认无绑定*/
    public AccountRspModel(User user) {
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind) {
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
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
