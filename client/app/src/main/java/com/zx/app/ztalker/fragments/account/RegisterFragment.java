package com.zx.app.ztalker.fragments.account;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.app.common.app.PresenterFragment;
import com.zx.app.factory.presenter.account.RegisterContract;
import com.zx.app.factory.presenter.account.RegisterPresenter;
import com.zx.app.ztalker.activities.MainActivity;
import com.zx.app.ztalker.R;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 注册的View实现类
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.et_password)
    EditText mPassword;
    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.iv_name)
    ImageView mIvName;
    @BindView(R.id.tv_launch)
    TextView mLaunch;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    private AccountTrigger mAccountTrigger;

    private boolean isLoading;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mName.setVisibility(View.VISIBLE);
        mIvName.setVisibility(View.VISIBLE);
        mLaunch.setText(R.string.label_go_login);
        mSubmit.setText(R.string.label_register);
        mToolbar.setTitle(R.string.label_register_welcome);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*拿到Activity的引用*/
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }


    @OnClick({R.id.tv_launch, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_launch:
                if (null != mAccountTrigger && !isLoading) {
                    mAccountTrigger.triggerView();
                }
                break;
            case R.id.btn_submit:
                if (null != mPresenter) {
                    String phone = mPhone.getText().toString();
                    String password = mPassword.getText().toString();
                    String name = mName.getText().toString();
                    mPresenter.register(phone, password, name);
                }
        }
    }

    @Override
    public void registerSuccess() {
        /*注册成功跳转主界面*/
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
        mName.setEnabled(!isLoading);
        mPassword.setEnabled(!isLoading);
        mPhone.setEnabled(!isLoading);
        mSubmit.setEnabled(!isLoading);
    }
}
