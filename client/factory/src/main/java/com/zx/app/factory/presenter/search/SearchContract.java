package com.zx.app.factory.presenter.search;

import com.zx.app.factory.model.card.GroupCard;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.presenter.BaseContract;

import java.util.List;

/**
 * author Afton
 * date 2020/3/9
 */
public interface SearchContract {
    /*搜索人*/
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    /*搜索群*/
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }

    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }
}
