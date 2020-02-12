package com.zx.app.ztalker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.common.app.BaseFragment;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.ztalker.activities.AccountActivity;
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
}
