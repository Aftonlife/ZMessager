package com.zx.app.factory.presenter.contact;

import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.presenter.BaseContract;

/**
 * author Afton
 * date 2020/3/11
 */
public interface FollowContract {
    interface Presenter extends BaseContract.Presenter {
        void follow(String followId);
    }

    interface View extends BaseContract.View<Presenter> {
        void onFollowDone(UserCard card);
    }
}
