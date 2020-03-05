package com.zx.app.factory.data.helper;

import com.zx.app.factory.R;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.model.api.RspModel;
import com.zx.app.factory.model.api.user.UserUpdateModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.net.NetWork;
import com.zx.app.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author Afton
 * date 2020/3/5
 */
public class UserHelper {
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
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
