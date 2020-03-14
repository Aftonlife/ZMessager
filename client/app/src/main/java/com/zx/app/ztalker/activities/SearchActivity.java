package com.zx.app.ztalker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.raizlabs.android.dbflow.sql.language.Operator;
import com.zx.app.common.app.ToolbarActivity;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.search.SearchGroupFragment;
import com.zx.app.ztalker.fragments.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1;
    public static final int TYPE_GROUP = 2;

    private int type;
    private SearchFragment mSearchFragment;

    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected Boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type == TYPE_USER || type == TYPE_GROUP;
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        Fragment fragment;
        if (type == TYPE_USER) {
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        } else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*初始化菜单*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        /*找到搜索菜单*/
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (null != searchView) {
            /*拿到一个搜索管理器*/
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            /*添加搜索监听*/
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    /*点击了搜索按钮*/
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    /*文字改变时不会及时搜索，只在为null时搜索*/
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索
     *
     * @param query 搜索的文字
     */
    private void search(String query) {
        if (null == mSearchFragment) {
            return;
        }
        mSearchFragment.search(query);
    }

    /**
     * 搜索的Fragment必须继承的接口
     */
    public interface SearchFragment {
        void search(String content);
    }
}
