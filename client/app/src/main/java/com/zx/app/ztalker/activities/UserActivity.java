package com.zx.app.ztalker.activities;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.zx.app.common.app.Application;
import com.zx.app.common.app.BaseActivity;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.factory.Factory;
import com.zx.app.factory.net.UploadHelper;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.fragments.account.AccountTrigger;
import com.zx.app.ztalker.fragments.account.LoginFragment;
import com.zx.app.ztalker.fragments.account.RegisterFragment;
import com.zx.app.ztalker.fragments.assist.PermissionsFragment;
import com.zx.app.ztalker.fragments.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity {

    public static final String TAG = UserActivity.class.getName();

    @BindView(R.id.pv_portrait)
    PortraitView mPortrait;


    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mCurFragment)
                .commit();
    }

    @OnClick(R.id.pv_portrait)
    public void portraitClick() {
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
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
                                .start(this);
                    })
                    .show(getSupportFragmentManager(), LoginFragment.class.getName());
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
        Glide.with(this)
                .load(portrait)
                .centerCrop()
                .into(mPortrait);
        String localPath = portrait.getPath();
        Log.e(TAG, "localPath=" + localPath);
        Factory.runOnAsync(() -> {
            String netPath = UploadHelper.uploadPortrait(localPath);
            Log.e(TAG, "netPath=" + netPath);
        });
    }
}
