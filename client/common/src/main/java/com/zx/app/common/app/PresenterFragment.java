package com.zx.app.common.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zx.app.factory.presenter.BaseContract;

/**
 * author Afton
 * date 2020/2/22
 * View的实现基类
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends BaseFragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*attach到Activity时*/
        initPresenter();
        Log.e("PresenterFragment", "onAttach");
    }

    /*初始化*/
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        Application.showToast(str);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
