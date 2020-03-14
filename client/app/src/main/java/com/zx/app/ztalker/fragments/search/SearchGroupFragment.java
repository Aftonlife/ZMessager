package com.zx.app.ztalker.fragments.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.app.common.app.PresenterFragment;
import com.zx.app.factory.model.card.GroupCard;
import com.zx.app.factory.presenter.search.SearchContract;
import com.zx.app.factory.presenter.search.SearchGroupPresenter;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.activities.SearchActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchContract.GroupView, SearchActivity.SearchFragment {

    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }
}
