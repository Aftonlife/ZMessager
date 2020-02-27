package com.zx.app.factory.presenter.account;

import android.text.TextUtils;

import com.zx.app.Common;
import com.zx.app.common.app.Application;
import com.zx.app.factory.R;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.AccountHelper;
import com.zx.app.factory.model.api.account.RegisterModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * author Afton
 * date 2020/2/22
 * 注册Presenter的实现类
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        /*调用开始方法，在start中默认启动了Loading*/
        start();

        RegisterContract.View view = getView();

        /*校验*/
        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else {
            /*请求网络*/
            RegisterModel model = new RegisterModel(phone, password, name);
            AccountHelper.register(model, this);
        }


    }

    @Override
    public boolean checkMobile(String phone) {
        /*手机号不为空且满足格式*/
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User user) {
        /*网络请求成功，回送一个用户信息，通知界面注册成功*/
        final RegisterContract.View view = getView();
        if (null == view) {
            return;
        }
        /*切换主线程执行*/
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
        if (null == view) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
