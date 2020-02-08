package com.zx.app.ztalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zx.app.common.app.BaseActivity;
import com.zx.app.ztalker.activities.AccountActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.tv_test)
    public void textClick(View view) {
        startActivity(new Intent(MainActivity.this, AccountActivity.class));
    }
}
