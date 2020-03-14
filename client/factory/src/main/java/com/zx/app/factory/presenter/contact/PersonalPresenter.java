package com.zx.app.factory.presenter.contact;

import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.UserHelper;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * author Afton
 * date 2020/3/14
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter, DataSource.Callback<UserCard> {
    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        PersonalContract.View view = getView();
        if (null == view) {
            return;
        }
        /*个人数据优先从网络拉取*/
        UserHelper.getPersonal(view.getUserId(), this);
    }

    @Override
    public User getUserPersonal() {
        return null;
    }

    @Override
    public void onDataLoaded(final UserCard card) {
        final PersonalContract.View view = getView();
        if (null == view) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                if (null != card) {
                    view.onLoadDone(card.build());
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(int strRes) {

    }
}
