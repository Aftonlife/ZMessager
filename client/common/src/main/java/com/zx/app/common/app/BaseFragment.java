package com.zx.app.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zx.app.common.widget.convention.PlaceHolderView;
import com.zx.app.factory.presenter.BaseContract;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author Administrator
 * date 2020/2/8
 */
public abstract class BaseFragment extends Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    protected PlaceHolderView mPlaceHolderView;

    /*标示是否第一次初始化数据*/
    protected boolean mIsFirstInitData = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRoot) {
            int layId = getContentLayoutId();
            /*初始化当前的跟布局，但是不在创建时就添加到container里边*/
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (null != mRoot.getParent()) {
                /*把当前Root从其父控件中移除*/
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            /*触发一次以后就不会触发*/
            mIsFirstInitData = false;
            /*触发*/
            onFirstInit();
        }
        // 当View创建完成后初始化数据
        initData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*初始化参数*/
        initArgs(getArguments());
    }

    /**
     * 初始化参数
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     * 获取布局Id
     * 子类实现
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
    }


    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected void onFirstInit() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

}
