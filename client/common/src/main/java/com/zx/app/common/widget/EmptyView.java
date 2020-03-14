package com.zx.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zx.app.common.R;
import com.zx.app.common.app.Application;
import com.zx.app.common.widget.convention.PlaceHolderView;
import com.zx.app.factory.presenter.BaseContract;

import net.qiujuer.genius.ui.widget.Loading;

/**
 * author Afton
 * date 2020/3/7
 * 占位控件，显示一个空的图片
 * 可以和MVP配合显示没有数据，正在加载等状态
 */
@SuppressWarnings("unused")
public class EmptyView extends LinearLayout implements PlaceHolderView {

    private ImageView mEmptyImg;
    private Loading mLoading;
    private TextView mStatusText;

    private int[] mDrawableIds = new int[]{0, 0};
    private int[] mTextIds = new int[]{0, 0, 0};

    private View[] mBindViews;

    public EmptyView(Context context) {
        super(context);
        init(null, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.lay_empty, this);

        mEmptyImg = (ImageView) findViewById(R.id.iv_empty);
        mLoading = (Loading) findViewById(R.id.loading);
        mStatusText = (TextView) findViewById(R.id.tv_empty);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EmptyView, defStyleAttr, 0);

        mDrawableIds[0] = a.getInt(R.styleable.EmptyView_comEmptyDrawable, R.drawable.status_empty);
        mDrawableIds[1] = a.getInt(R.styleable.EmptyView_comErrorDrawable, R.drawable.status_empty);
        mTextIds[0] = a.getInt(R.styleable.EmptyView_comEmptyText, R.string.prompt_empty);
        mTextIds[1] = a.getInt(R.styleable.EmptyView_comErrorText, R.string.prompt_error);
        mTextIds[2] = a.getInt(R.styleable.EmptyView_comErrorText, R.string.prompt_loading);

        a.recycle();
    }


    /**
     * 绑定一系列数据显示的布局
     * 当前布局隐藏时（有数据时）自动显示绑定的数据布局
     * 而当数据加载时，自动显示Loading，并隐藏数据布局
     *
     * @param views 数据显示的布局
     */
    public void bind(View... views) {
        this.mBindViews = views;
    }

    /**
     * 更改绑定布局的显示状态
     *
     * @param visible 显示的状态
     */
    private void changeBindViewVisibility(int visible) {
        final View[] views = mBindViews;
        if (null == views || views.length == 0) {
            return;
        }
        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void triggerEmpty() {
        mLoading.setVisibility(GONE);
        mLoading.stop();
        mEmptyImg.setImageResource(mDrawableIds[0]);
        mStatusText.setText(mTextIds[0]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerNetError() {
        mLoading.setVisibility(GONE);
        mLoading.stop();
        mEmptyImg.setImageResource(mDrawableIds[1]);
        mStatusText.setText(mTextIds[1]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerError(int strRes) {
        Application.showToast(strRes);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerLoading() {
        mLoading.setVisibility(VISIBLE);
        mLoading.start();
        mStatusText.setText(mTextIds[2]);
        mEmptyImg.setVisibility(GONE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerOK() {
        setVisibility(GONE);
        changeBindViewVisibility(VISIBLE);
    }

    @Override
    public void triggerOkOrEmpty(boolean isOk) {
        if(isOk){
            triggerOK();
        }else{
            triggerEmpty();
        }
    }
}
