package com.zx.app.ztalker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zx.app.common.app.BaseActivity;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.account.AccountFragment;
import com.zx.app.ztalker.fragments.media.GalleryFragment;

public class AccountActivity extends BaseActivity {

    private Fragment mCurFragment;

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
        mCurFragment = new AccountFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mCurFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mCurFragment) {
            mCurFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
