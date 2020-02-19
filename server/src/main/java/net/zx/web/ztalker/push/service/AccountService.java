package net.zx.web.ztalker.push.service;

import com.google.common.base.Strings;
import net.zx.web.ztalker.push.bean.api.account.AccountRspModel;
import net.zx.web.ztalker.push.bean.api.account.LoginModel;
import net.zx.web.ztalker.push.bean.api.account.RegisterModel;
import net.zx.web.ztalker.push.bean.api.base.ResponseModel;
import net.zx.web.ztalker.push.bean.card.UserCard;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.bean.db.UserFollow;
import net.zx.web.ztalker.push.factory.UserFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;

/**
 * @author Administrator
 * @date 2020/2/7
 * @time 18:46
 */
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {

    @POST
    @Path("/register")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (null != user) {
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if (null != user) {
            return ResponseModel.buildHaveNameError();
        }
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());
        if (null != user) {
            /*如果有pushId,就绑定*/
            return havePushId(user, model.getPushId());
        } else {
            return ResponseModel.buildRegisterError();
        }
    }

    @POST
    @Path("/login")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount(), model.getPassword());

        if (null != user) {
            /*如果有pushId,就绑定*/
            return havePushId(user, model.getPushId());
        } else {
            return ResponseModel.buildLoginError();
        }
    }

    /*绑定设置*/
    @POST
    @Path("/bind/{pushId}")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /*从请求头中获取Token,pushId从地址中获取*/
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {
        if (Strings.isNullOrEmpty(token) ||
                Strings.isNullOrEmpty(pushId)) {
            return ResponseModel.buildParameterError();
        }
        User self = UserFactory.findByToken(token);
        return bind(self, pushId);
    }

    /*有pushId*/
    private ResponseModel<AccountRspModel> havePushId(User user, String pushId) {
        /*如果有pushId,就绑定*/
        if (!Strings.isNullOrEmpty(pushId)) {
            return bind(user, pushId);
        }
        /*返回当前用户*/
        AccountRspModel rspModel = new AccountRspModel(user);
        return ResponseModel.buildOk(rspModel);
    }

    /*绑定的操作*/
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        User user = UserFactory.bindPushId(self, pushId);
        if (null == user) {
            return ResponseModel.buildServiceError();
        }
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}
