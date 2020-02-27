package com.zx.app.factory.data.helper;

import com.zx.app.factory.Factory;
import com.zx.app.factory.R;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.api.account.LoginModel;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.net.NetWork;
import com.zx.app.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author Afton
 * date 2020/2/25
 * 用户的请求处理类
 */
public class AccountHelper {
    /**
     * 注册的接口，异步的调用
     *
     * @param model    注册的model
     * @param callback 成功与失败的接口回调
     */
    public static void register(RegisterModel model, DataSource.Callback<User> callback) {
        /*调用Retrofit对我们的网络请求接口做代理*/
        RemoteService service = NetWork.remote();

        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

        call.enqueue(new AccountRspCallback(callback));
    }

    public static void login(LoginModel model, DataSource.Callback<User> callback) {
        /*调用Retrofit对我们的网络请求接口做代理*/
        RemoteService service = NetWork.remote();

        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);

        call.enqueue(new AccountRspCallback(callback));
    }
    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            /*返回中得到全局Model，内部用Gson解析*/
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                /*拿到实体*/
                AccountRspModel accountRspModel = rspModel.getResult();
                User user = accountRspModel.getUser();
                if (null != callback) {
                    callback.onDataLoaded(user);
                }
            } else {
                /*错误解析*/
                Factory.decodeRspModel(rspModel, callback);
            }

        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            callback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
