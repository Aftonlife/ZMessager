package com.zx.app.factory.presenter.contact;

import com.zx.app.common.widget.recycler.RecyclerAdapter;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.presenter.BaseContract;

import java.util.List;

/**
 * author Afton
 * date 2020/3/11
 */
public interface ContactContract {
    interface Presenter extends BaseContract.Presenter {
    }

    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }
}
