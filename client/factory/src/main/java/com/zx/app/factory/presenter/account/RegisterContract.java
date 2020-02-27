package com.zx.app.factory.presenter.account;

import androidx.annotation.StringRes;

import com.zx.app.factory.presenter.BaseContract;

/**
 * author Afton
 * date 2020/2/22
 * 注册的契约类
 */
public interface RegisterContract extends BaseContract {

    interface View extends BaseContract.View<Presenter> {
        /*注册成功*/
        void registerSuccess();

    }

    interface Presenter extends BaseContract.Presenter {
        /*发起注册*/
        void register(String phone, String password, String name);

        /*检查手机号是否正确*/
        boolean checkMobile(String phone);

    }
}
