package com.zx.app.ztalker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.factory.persistent.Account;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.main.ActiveFragment;
import com.zx.app.ztalker.fragments.main.ContactFragment;
import com.zx.app.ztalker.fragments.main.GroupFragment;
import com.zx.app.ztalker.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.onTabChangeListener<Integer> {
    @BindView(R.id.appbar)
    View mAppBarLayout;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.pv_portrait)
    PortraitView mPortrait;
    @BindView(R.id.bnv_navigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.fab_action)
    FloatActionButton mAction;


    private NavHelper<Integer> mNavHelper;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected Boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {
            /*信息完整正常走流程*/
            return super.initArgs(bundle);
        } else {
            /*信息不完整，跳转UserActivity*/
            UserActivity.show(this);
            return false;
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        /*Glide裁剪appbar背景防止拉伸*/
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new CustomViewTarget<View, Drawable>(mAppBarLayout) {

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        this.view.setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        /*加载头像*/
        Glide.with(this)
                .load(Account.getUser().getPortrait())
                .centerCrop()
                .into(mPortrait);

        mNavHelper = new NavHelper(this, getSupportFragmentManager(), R.id.fl_container, this);
        /*添加Fragment,加类*/
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
    }

    @Override
    protected void initData() {
        super.initData();
        mNavHelper.performClickMenu(R.id.action_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

    @Override
    public void onTabChange(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        mAction.animate()
                .translationY(transY)
                .rotation(rotation)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(500)
                .start();
    }

    @OnClick(R.id.fab_action)
    public void actionClick(View view) {
        AccountActivity.show(this);
    }

    @OnClick(R.id.iv_search)
    public void searchClick(View view) {
        /*群的界面点击进入搜索群的界面，其他进入人的搜索界面*/
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group) ?
                SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);

    }
}
