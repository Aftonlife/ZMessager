package com.zx.app.ztalker;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Property;

import com.zx.app.common.app.BaseActivity;
import com.zx.app.ztalker.activities.AccountActivity;
import com.zx.app.ztalker.fragments.assist.PermissionsFragment;

import pub.devrel.easypermissions.EasyPermissions;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lanuch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim(1f, this::reallySkip);
    }

    /**
     * 真实的跳转
     */
    private void reallySkip() {
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            AccountActivity.show(this);
            finish();
        }
    }

    private void startAnim(float endProgress, final Runnable endCallback) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofFloat(this, "color", Color.RED);
        animator.setDuration(1500)
                .addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        /*结束时触发*/
                        endCallback.run();
                    }
                });
        animator .start();
    }
}
