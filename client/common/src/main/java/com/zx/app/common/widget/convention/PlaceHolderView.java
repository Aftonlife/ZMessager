package com.zx.app.common.widget.convention;

import androidx.annotation.StringRes;

/**
 * 基础占位布局接口定义
 * author Afton
 * date 2020/3/7
 */
public interface PlaceHolderView {
    /*没有数据，显示空布局，隐藏当前数据布局*/
    void triggerEmpty();

    /*网络错误，显示一个网络错误图标*/
    void triggerNetError();

    /*加载错误，显示错误信息*/
    void triggerError(@StringRes int strRes);

    /*显示正在加载状态*/
    void triggerLoading();

    /*数据加载成功，隐藏当前占位布局*/
    void triggerOK();

    /*是否加载成功数据isOK：true 隐藏布局 false 显示空布局*/
    void triggerOkOrEmpty(boolean isOk);
}
