package com.zx.app.common.app;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.zx.app.common.R;

/**
 * author Afton
 * date 2020/3/9
 * Toolbar视图基类
 */
public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化toolbar
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (null != toolbar) {
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    /*设置左上角的返回按钮为实际的返回效果*/
    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }

}
