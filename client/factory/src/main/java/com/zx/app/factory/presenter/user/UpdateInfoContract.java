package com.zx.app.factory.presenter.user;

import com.zx.app.factory.presenter.BaseContract;
import com.zx.app.factory.presenter.account.LoginContract;

/**
 * author Afton
 * date 2020/3/4
 * 用户信息更新
 */
public interface UpdateInfoContract {
    interface View extends BaseContract.View<UpdateInfoContract.Presenter> {
        void updateSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        void update(String portraitPath, String desc, boolean isMan);
    }
}
