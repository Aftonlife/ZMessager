package com.zx.app.factory.data.helper;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.zx.app.factory.Factory;
import com.zx.app.factory.R;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.api.account.LoginModel;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.db.AppDatabase;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.net.NetWork;
import com.zx.app.factory.net.RemoteService;
import com.zx.app.factory.persistent.Account;

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

    /**
     * 登录
     *
     * @param model
     * @param callback
     */
    public static void login(LoginModel model, DataSource.Callback<User> callback) {
        /*调用Retrofit对我们的网络请求接口做代理*/
        RemoteService service = NetWork.remote();

        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);

        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 绑定消息推送id
     */
    public static void bindPushId(final DataSource.Callback<User> callback) {
        /*检查是否为空*/
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
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
                /*保存到数据库*/
                //第一种，直接保持
//                user.save();
                //第二种通过ModelAdapter
                /*FlowManager.getModelAdapter(User.class)
                        .save(user);
                //第三种事务中
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .save(user);
                    }
                }).build().execute();*/

                /*保存到持久化Xml中*/
                Account.login(accountRspModel);
                /*判断绑定状态，是否绑定设备*/
                if (accountRspModel.isBind()) {
                    /*设置绑定状态*/
                    Account.setBind(true);
                    if (null != callback) {
                        callback.onDataLoaded(user);
                    }
                } else {
                    /*进行绑定唤起*/
                    bindPushId(callback);
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
