package com.zx.app.factory.presenter.account;

import androidx.annotation.StringRes;

import com.zx.app.factory.presenter.BaseContract;

/**
 * author Afton
 * date 2020/2/22
 */
public interface LoginContract extends BaseContract {

    interface View extends BaseContract.View<Presenter> {
        /*注册成功*/
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        /*发起登录*/
        void login(String phone, String password);
    }
}
