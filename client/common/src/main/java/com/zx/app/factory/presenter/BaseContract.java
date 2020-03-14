package com.zx.app.factory.presenter;

import androidx.annotation.StringRes;

import com.zx.app.common.widget.recycler.RecyclerAdapter;

/**
 * author Afton
 * date 2020/2/22
 * MVP模式中公共契约
 * 定义了View presenter 两个接口
 */
public interface BaseContract {
    interface View<T extends Presenter> {
        /*公共：显示字符串错误*/
        void showError(@StringRes int str);

        /*公共：显示进度条*/
        void showLoading();

        /*支持设置一个Presenter*/
        void setPresenter(T presenter);
    }

    interface Presenter {
        /*公共：启动*/
        void start();

        /*公共：销毁*/
        void destroy();
    }

    /*列表View*/
    interface RecyclerView<T extends Presenter, ViewModel> extends View<T> {
        /*拿到一个适配器，然后自己自主刷新*/
        RecyclerAdapter<ViewModel> getRecyclerAdapter();

        /*当适配器数据更改了触发*/
        void onAdapterDataChanged();
    }
}
