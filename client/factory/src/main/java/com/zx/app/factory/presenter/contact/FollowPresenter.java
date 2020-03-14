package com.zx.app.factory.presenter.contact;

import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.UserHelper;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * author Afton
 * date 2020/3/11
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, DataSource.Callback<UserCard> {
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String followId) {
        start();
        UserHelper.follow(followId, this);
    }

    @Override
    public void onDataLoaded(final UserCard card) {
        final FollowContract.View view = getView();
        if (null == view) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onFollowDone(card);
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
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
