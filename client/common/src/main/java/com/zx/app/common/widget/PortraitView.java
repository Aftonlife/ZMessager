package com.zx.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import com.bumptech.glide.RequestManager;
import com.zx.app.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author Afton
 * date 2020/2/8
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, String url) {
        setup(manager, url, R.drawable.default_portrait);
    }

    public void setup(RequestManager manager, String url, @DrawableRes int resourceId) {
        if (null == url) {
            url = "";
        }
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()//CircleImageView 控件中不能使用渐变动画，会导致显示延迟
                .into(this);
    }
}
