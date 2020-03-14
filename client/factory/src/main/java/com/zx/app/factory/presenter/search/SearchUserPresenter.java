package com.zx.app.factory.presenter.search;

import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.data.helper.UserHelper;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * author Afton
 * date 2020/3/9
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {

    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        /*如果有上一次的请求没有取消，就调用取消*/
        if (null != searchCall && !searchCall.isCanceled()) {
            searchCall.cancel();
        }
        searchCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView view = getView();
        if (null == view) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onSearchDone(userCards);
            }
        });

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.UserView view = getView();
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
