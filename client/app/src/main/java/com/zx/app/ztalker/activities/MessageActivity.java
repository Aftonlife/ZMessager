package com.zx.app.ztalker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Operator;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.factory.model.Author;
import com.zx.app.factory.model.db.User;
import com.zx.app.ztalker.R;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView mName;

    public static void show(Context context, Author author) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("name",author.getName());
        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Intent intent=getIntent();
        mName.setText(intent.getStringExtra("name"));
    }
}
