package com.zx.app.factory.presenter.search;

import com.zx.app.factory.presenter.BasePresenter;

/**
 * author Afton
 * date 2020/3/9
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter {


    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
