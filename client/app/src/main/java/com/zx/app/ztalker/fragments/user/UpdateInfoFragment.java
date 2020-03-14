package com.zx.app.ztalker.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.zx.app.common.app.Application;
import com.zx.app.common.app.BaseFragment;
import com.zx.app.common.app.PresenterFragment;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.factory.Factory;
import com.zx.app.factory.net.UploadHelper;
import com.zx.app.factory.presenter.BaseContract;
import com.zx.app.factory.presenter.account.LoginContract;
import com.zx.app.factory.presenter.user.UpdateInfoContract;
import com.zx.app.factory.presenter.user.UpdateInfoPresenter;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.activities.MainActivity;
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
 * date 2020/3/4
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {

    @BindView(R.id.portraitView)
    PortraitView mPortrait;
    @BindView(R.id.et_desc)
    EditText mDesc;
    @BindView(R.id.iv_sex)
    ImageView mSex;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    private String mPortraitPath;
    private boolean isMan = true, isLoading;

    public UpdateInfoFragment() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }


    @OnClick(R.id.btn_submit)
    public void submit() {
        String desc = mDesc.getText().toString();
        mPresenter.update(mPortraitPath, desc, isMan);
    }

    @OnClick(R.id.iv_sex)
    public void sexClick() {
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan ? 0 : 1);
    }

    @OnClick(R.id.portraitView)
    public void portraitClick() {
        if (PermissionsFragment.haveAll(getActivity(), getChildFragmentManager())) {
            new GalleryFragment()
                    .setListener(path -> {
                        UCrop.Options options = new UCrop.Options();
                        /*图片格式*/
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        /*图片精度*/
                        options.setCompressionQuality(96);

                        /*得到头像缓存临时地址*/
                        File dPath = Application.getPortraitTmpFile();
                        /*发起裁剪*/
                        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                                .withAspectRatio(1, 1)//宽高比
                                .withMaxResultSize(520, 520)//最大尺寸
                                .withOptions(options)//相关参数
                                .start(getActivity());
                    })
                    .show(getChildFragmentManager(), UpdateInfoFragment.class.getName());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (null != resultUri) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载头像
     *
     * @param portrait
     */
    private void loadPortrait(Uri portrait) {
        mPortraitPath = portrait.getPath();
        Glide.with(this)
                .load(portrait)
                .centerCrop()
                .into(mPortrait);
    }

    @Override
    public void updateSuccess() {
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
        mDesc.setEnabled(!isLoading);
        mSex.setEnabled(!isLoading);
        mSubmit.setEnabled(!isLoading);
    }

}
