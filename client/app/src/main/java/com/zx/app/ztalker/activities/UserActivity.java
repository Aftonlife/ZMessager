package com.zx.app.ztalker.activities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.zx.app.common.app.BaseActivity;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.user.UpdateInfoFragment;

/**
 * 用户信息界面用于修改信息
 */
public class UserActivity extends BaseActivity {

    public static final String TAG = UserActivity.class.getName();

//    @BindView(R.id.pv_portrait)
//    PortraitView mPortrait;


    private Fragment mCurFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mCurFragment)
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
