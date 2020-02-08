package com.zx.app.common.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * author Administrator
 * date 2020/2/8
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*界面未初始化之前的初始化窗口*/
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            /*界面Id设置到Activity界面*/
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true, 错误返回false
     */
    protected Boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 获取布局Id
     * 子类实现
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
    }

    /**
     * 初始化控件
     */
    protected void initData() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        /*当点击导航返回时，finish当前界面*/
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 系统返回键
     */
    @Override
    public void onBackPressed() {
        /*得到当前Activity的所有Fragment*/
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        /*判断是否为空*/
        if (null != fragments && fragments.size() > 0) {
            /*判断是否是自己可以处理的fragment类型*/
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseFragment) {
                    /*Fragment是否要处理返回键*/
                    if (((BaseFragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
