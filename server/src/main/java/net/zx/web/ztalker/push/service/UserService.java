package net.zx.web.ztalker.push.service;

import com.google.common.base.Strings;
import net.zx.web.ztalker.push.bean.api.base.ResponseModel;
import net.zx.web.ztalker.push.bean.api.user.UploadInfoModel;
import net.zx.web.ztalker.push.bean.card.UserCard;
import net.zx.web.ztalker.push.bean.db.User;
import net.zx.web.ztalker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2020/2/19
 * @time 11:30
 * 127.0.0/api/user/
 */
@Path("/user")
public class UserService extends BaseService {

    /**
     * 更新用户信息
     *
     * @param model 信息
     * @return UserCard
     */
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

    /**
     * 我的联系人列表
     *
     * @return 联系人列表
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();
        /*拿到我的联系人*/
        List<User> users = UserFactory.contacts(self);
        /*转换为UserCard*/
        List<UserCard> cards = users.stream()
                /*map操作，相当于转置操作，User->UserCard*/
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());
        return ResponseModel.buildOk(cards);
    }

    /**
     * 关注人操作,双向操作
     *
     * @param followId 被关注人的Id
     * @return 被关注人的信息
     */
    @PUT //修改类的方法用PUT
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();
        if (Strings.isNullOrEmpty(followId)
                || followId.equalsIgnoreCase(self.getId())) {
            return ResponseModel.buildParameterError();
        }

        User target = UserFactory.findById(followId);
        if (null == target) {
            /*未找到人*/
            return ResponseModel.buildNotFoundUserError(null);
        }
        /*发起关注*/
        target = UserFactory.follow(self, target, null);

        if (null == target) {
            /*关注失败，返回服务器异常*/
            return ResponseModel.buildServiceError();
        }

        return ResponseModel.buildOk(new UserCard(target, true));
    }

    @GET  //查询类的方法用PUT
    @Path("{id}")
    //http://127.0.0.1/api/user/xxxxxxxxxxxxxx
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        if (id.equalsIgnoreCase(self.getId())) {
            /*查看自己的信息*/
            return ResponseModel.buildOk(new UserCard(self, true));
        }
        User user = UserFactory.findById(id);
        if (null == user) {
            /*没找到*/
            return ResponseModel.buildNotFoundUserError(null);
        }

        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }

    // 搜索人的接口实现
    @GET  //查询类的方法用PUT
    @Path("/search/{name:(.*)?}")//名字为任意字符，可为空
    //http://127.0.0.1/api/user/xxxxxxxxxxxxxx
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();

        /*查询数据*/
        List<User> searchUsers = UserFactory.search(name);
        // 把查询的人封装为UserCard
        // 判断这些人是否有我已经关注的人，
        // 如果有，则返回的关注状态中应该已经设置好状态

        /*查询我的联系人*/
        final List<User> contacts = UserFactory.contacts(self);

        /*User->UserCard*/
        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    /*判断是否是自己或者我的联系人*/
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            /*进行联系人的任意匹配，匹配其中的Id*/
                            || contacts.stream().anyMatch(
                            contactUser -> contactUser.getId().equalsIgnoreCase(user.getId()));
                    return new UserCard(user, isFollow);
                }).collect(Collectors.toList());


        return ResponseModel.buildOk(userCards);
    }
}

