package com.zx.app.ztalker.activities;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.account.AccountTrigger;
import com.zx.app.ztalker.fragments.account.LoginFragment;
import com.zx.app.ztalker.fragments.account.RegisterFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements AccountTrigger {

    private Fragment mCurFragment;
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    @BindView(R.id.iv_bg)
    ImageView mBg;

    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mLoginFragment = new LoginFragment();
        mCurFragment = mLoginFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mCurFragment)
                .commit();
        /*初始化背景*/
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new DrawableImageViewTarget(mBg) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        if (null == resource) {
                            super.setResource(resource);
                            return;
                        }
                        /*使用适配类包装*/
                        Drawable drawable = DrawableCompat.wrap(resource);
                        /*设置着色效果和颜色，蒙蔽模式*/
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);
                        /*设置给View*/
                        super.setResource(drawable);
                    }
                });
    }


    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            fragment = mLoginFragment;
        }
        mCurFragment = fragment;
        /*替换*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, mCurFragment)
                .commit();
    }
}
