package com.zx.app.factory.presenter;

/**
 * author Afton
 * date 2020/2/22
 * Presenter的实现基类
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /*设置View 可以复写*/
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /*获取View 不允许复写*/
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        /*开始loading*/
        if (null != mView) {
            mView.showLoading();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        /*销毁时全置空*/
        T view = mView;
        mView = null;
        if (null != view) {
            view.setPresenter(null);
        }
    }
}
