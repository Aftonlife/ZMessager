package net.zx.web.ztalker.push.service;

import net.zx.web.ztalker.push.bean.api.account.AccountRspModel;
import net.zx.web.ztalker.push.bean.api.base.ResponseModel;
import net.zx.web.ztalker.push.bean.api.user.UploadInfoModel;
import net.zx.web.ztalker.push.bean.card.UserCard;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.awt.*;

/**
 * @author Administrator
 * @date 2020/2/19
 * @time 11:30
 * 127.0.0/api/user/
 */
@Path("/user")
public class UserService extends BaseService {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> updateInfo(UploadInfoModel model) {
        if (!UploadInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = getSelf();
        user = model.updateToUser(user);
        user = UserFactory.update(user);
        UserCard userCard = new UserCard(user, true);
        return ResponseModel.buildOk(userCard);
    }
}
