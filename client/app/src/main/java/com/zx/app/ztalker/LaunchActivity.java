package com.zx.app.ztalker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.zx.app.common.app.BaseActivity;
import com.zx.app.factory.persistent.Account;
import com.zx.app.ztalker.activities.AccountActivity;
import com.zx.app.ztalker.activities.MainActivity;
import com.zx.app.ztalker.fragments.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends BaseActivity {

    private ColorDrawable mBgDrawable;
    // 是否已经得到PushId
    private boolean mAlreadyGotPushReceiverId = false;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lanuch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        /*给背景设置颜色*/
        View root = findViewById(R.id.cl_launch);
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        root.setBackground(drawable);
        /*可以改变drawable做特效*/
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();

        startAnim(0.5f, this::waitReceivePushId);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        MainActivity.show(this);
        // 判断是否已经得到推送Id，如果已经得到则进行跳转操作，
        // 在操作中检测权限状态
        if (mAlreadyGotPushReceiverId) {
            reallySkip();
        }
    }

    /*等待个推框架通过广播给pushId赋值*/
    private void waitReceivePushId() {
        if (Account.isLogin()) {
            /*已登录判断是否绑定*/
            if (Account.isBind()) {
                waitPushReceiverIdDone();
                return;
            }
        } else {
            /*没有登录不能去绑定pushId*/
            if (!TextUtils.isEmpty(Account.getPushId())) {
                waitPushReceiverIdDone();
                return;
            }
        }
        /*循环等待*/
        getWindow().getDecorView().postDelayed(this::waitReceivePushId, 500);
    }

    /*跳转前完成剩下的50%*/
    private void waitPushReceiverIdDone() {
        /*已得到pushId*/
        mAlreadyGotPushReceiverId = true;
        startAnim(1f, this::reallySkip);
    }

    /**
     * 真实的跳转
     */
    private void reallySkip() {
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback) {
        /*最终颜色*/
        int finalColor = Resource.Color.WHITE;
        /*运算当前进度的颜色*/
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        /*构建属性动画*/
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        /*设置时间*/
        valueAnimator.setDuration(1000);
        /*开始&结束值*/
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                /*结束时触发*/
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    /*自定义可变颜色属性*/
    private final Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((int) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };
}
