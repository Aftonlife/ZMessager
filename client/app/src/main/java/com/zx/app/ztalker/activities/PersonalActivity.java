package com.zx.app.ztalker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.common.app.PresenterToolbarActivity;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.persistent.Account;
import com.zx.app.factory.presenter.contact.PersonalContract;
import com.zx.app.factory.presenter.contact.PersonalPresenter;
import com.zx.app.ztalker.R;

import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {

    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";

    @BindView(R.id.iv_header)
    ImageView mImageHeader;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.pv_portrait)
    PortraitView mPortrait;
    @BindView(R.id.tv_follows)
    TextView mFollows;
    @BindView(R.id.tv_following)
    TextView mFollowing;
    @BindView(R.id.tv_desc)
    TextView mDesc;
    @BindView(R.id.btn_say_hello)
    Button mBtn;


    private String mUserId;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra("BOUND_KEY_ID", userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected Boolean initArgs(Bundle bundle) {
        mUserId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(mUserId);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    public String getUserId() {
        return mUserId;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onLoadDone(User user) {
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        mPortrait.setup(Glide.with(PersonalActivity.this), user.getPortrait());
        /*自己按钮隐藏*/
        mBtn.setVisibility(user.getId().equalsIgnoreCase(Account.getUserId()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void allowSayHello(boolean isAllow) {

    }

    @Override
    public void setFollowStatus(boolean isFollow) {

    }

    @Override
    public void showError(int str) {

    }

    @Override
    public void showLoading() {

    }
}
