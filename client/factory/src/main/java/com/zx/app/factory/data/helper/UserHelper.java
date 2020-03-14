package com.zx.app.factory.data.helper;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.zx.app.factory.Factory;
import com.zx.app.factory.R;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.user.UserUpdateModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.model.db.User_Table;
import com.zx.app.factory.net.NetWork;
import com.zx.app.factory.net.RemoteService;
import com.zx.app.factory.persistent.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author Afton
 * date 2020/3/5
 */
public class UserHelper {
    /*更新用户信息*/
    public static void updateInfo(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.updateInfo(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard card = rspModel.getResult();
                    User user = card.build();
                    user.save();
                    callback.onDataLoaded(card);
                } else {
                    Factory.decodeRspModel(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /*搜索人*/
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (null != rspModel && rspModel.success()) {
                    List<UserCard> cards = rspModel.getResult();
                    callback.onDataLoaded(cards);
                } else {
                    Factory.decodeRspModel(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        /*返回当前调度*/
        return call;
    }

    /*关注用户*/
    public static void follow(String followId, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.follow(followId);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard card = rspModel.getResult();
                    User user = card.build();
                    /*保持到数据库*/
                    user.save();
                    callback.onDataLoaded(card);
                } else {
                    Factory.decodeRspModel(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    // 刷新联系人的操作，不需要Callback，直接存储到数据库，
    // 并通过数据库观察者进行通知界面更新，
    // 界面更新的时候进行对比，然后差异更新
    public static void contact(final DataSource.SucceedCallback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        service.contact().enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (null != rspModel && rspModel.success()) {
                    List<UserCard> cards = rspModel.getResult();
                    if (null == cards || cards.size() < 1) {
                        return;
                    }
//                    UserCard[] cards1 = cards.toArray(new UserCard[0]);
                    callback.onDataLoaded(cards);
                }else{
                    Factory.decodeRspModel(rspModel,null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
            }
        });
    }

    /*获取用户*/
    public static void getPersonal(String userId, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.getPersonal(userId);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard card = rspModel.getResult();
                    User user = card.build();
                    /*保持到数据库*/
                    user.save();
                    callback.onDataLoaded(card);
                } else {
                    Factory.decodeRspModel(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
