package com.zx.app.ztalker.fragments.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.zx.app.common.app.Application;
import com.zx.app.common.app.BaseFragment;
import com.zx.app.common.app.PresenterFragment;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.factory.Factory;
import com.zx.app.factory.net.UploadHelper;
import com.zx.app.factory.presenter.account.LoginContract;
import com.zx.app.factory.presenter.account.LoginPresenter;
import com.zx.app.ztalker.MainActivity;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.assist.PermissionsFragment;
import com.zx.app.ztalker.fragments.media.GalleryFragment;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * author Afton
 * date 2020/2/10
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter> implements LoginContract.View {

    private static final String TAG = LoginFragment.class.getName();

    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.et_password)
    EditText mPassword;
    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.line_password)
    View mLine;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    private AccountTrigger mAccountTrigger;

    private boolean isLoading;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mLine.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*拿到Activity的引用*/
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }


    @OnClick({R.id.tv_launch, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_launch:
                if (null != mAccountTrigger) {
                    mAccountTrigger.triggerView();
                }
                break;
            case R.id.btn_submit:
                if (null != mPresenter) {
                    String phone = mPhone.getText().toString();
                    String password = mPassword.getText().toString();
                    mPresenter.login(phone, password);
                }
        }
    }

    @Override
    public void loginSuccess() {
        mLoading.stop();
        MainActivity.show(getContext());
        getActivity().finish();
    }
    @Override
    public void showError(int str) {
        super.showError(str);
        mLoading.stop();
        isLoading = false;
        isLoading();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        isLoading = true;
        isLoading();
    }

    /*控件的状态*/
    private void isLoading() {
        /*其他控件不可用*/
        mPassword.setEnabled(!isLoading);
        mPhone.setEnabled(!isLoading);
        mSubmit.setEnabled(!isLoading);
    }
}
