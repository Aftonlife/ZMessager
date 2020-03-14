package com.zx.app.common.app;

import com.zx.app.factory.presenter.BaseContract;
import com.zx.app.factory.presenter.BasePresenter;

/**
 * author Afton
 * date 2020/3/14
 */
public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;


    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    protected abstract Presenter initPresenter();



    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
