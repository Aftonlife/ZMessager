package com.zx.app.factory.net;

import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.api.account.LoginModel;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.db.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * author Afton
 * date 2020/2/25
 * 网络请求的所有接口
 */
public interface RemoteService {

    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);
}
