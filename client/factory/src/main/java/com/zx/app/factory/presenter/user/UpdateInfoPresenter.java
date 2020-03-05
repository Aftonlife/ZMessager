package com.zx.app.factory.presenter.user;

import android.text.TextUtils;

import com.zx.app.common.app.Application;
import com.zx.app.factory.Factory;
import com.zx.app.factory.R;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.UserHelper;
import com.zx.app.factory.model.api.user.UserUpdateModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.net.UploadHelper;
import com.zx.app.factory.presenter.BasePresenter;
import com.zx.app.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * author Afton
 * date 2020/3/4
 */
public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String portraitPath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();
        if (TextUtils.isEmpty(portraitPath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            /*上传头像到OSS*/
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String netPath = UploadHelper.uploadPortrait(portraitPath);
                    if (TextUtils.isEmpty(netPath)) {
                        /*上传失败，切换回主线程提示报错*/
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                view.showError(R.string.data_upload_error);
                            }
                        });
                    } else {
                        UserUpdateModel model = new UserUpdateModel("", netPath, desc,
                                isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        UserHelper.updateInfo(model, UpdateInfoPresenter.this);
                    }
                }
            });


        }
    }


    @Override
    public void onDataNotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (null == view) {
            return;
        }
        /*切换到主线程执行*/
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });

    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        final UpdateInfoContract.View view = getView();
        if (null == view) {
            return;
        }
        /*切换到主线程执行*/
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSuccess();
            }
        });

    }
}
