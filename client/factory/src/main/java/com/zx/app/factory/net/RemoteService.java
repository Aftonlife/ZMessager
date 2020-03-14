package com.zx.app.factory.net;

import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.api.account.LoginModel;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.api.user.UserUpdateModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * author Afton
 * date 2020/2/25
 * 网络请求的所有接口
 */
public interface RemoteService {

    /**
     * 注册
     *
     * @param model 注册model
     * @return 用户
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录
     *
     * @param model 登录model
     * @return 用户
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     *
     * @param pushId 设备Id
     * @return 用户
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户信息更新
     *
     * @param model 更新的model
     * @return 更新后的用户卡片
     */
    @PUT("user")
    Call<RspModel<UserCard>> updateInfo(@Body UserUpdateModel model);

    /**
     * 根据名字搜索用户
     *
     * @param name 用户名（模糊匹配）
     * @return 用户列表
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path(encoded = true, value = "name") String name);

    /**
     * 关注用户
     *
     * @param followId 用户Id
     * @return 用户Card
     */
    @PUT("user/follow/{followId}")
    Call<RspModel<UserCard>> follow(@Path(encoded = true, value = "followId") String followId);

    /**
     * 我的联系人
     *
     * @return 用户列表
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> contact();

    /**
     * 获取用户信息
     *
     * @return 用户Card
     */
    @GET("user/{id}")
    Call<RspModel<UserCard>> getPersonal(@Path(encoded = true, value = "id") String id);
}
