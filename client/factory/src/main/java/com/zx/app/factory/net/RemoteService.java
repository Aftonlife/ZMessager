package com.zx.app.factory.net;

import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.api.account.LoginModel;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.api.user.UserUpdateModel;

import retrofit2.Call;
import retrofit2.http.Body;
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
     * @param model
     * @return
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录
     *
     * @param model
     * @return
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     *
     * @param pushId
     * @return
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户信息更新
     *
     * @param model
     * @return
     */
    @PUT("user")
    Call<RspModel<UserCard>> updateInfo(@Body UserUpdateModel model);
}
