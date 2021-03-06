package com.zx.app.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zx.app.common.R;
import com.zx.app.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO: document your custom view class.
 */
public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;//最大选中数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024; // 最小的图片大小

    private Adapter mAdapter = new Adapter();
    private SelectedChangeListener mListener;
    private List<Image> mSelectedImages = new LinkedList<>();//LinkedList 增加删除Item时性能比Array好
    private LoaderCallback mLoaderCallback = new LoaderCallback();


    public GalleryView(Context context) {
        super(context);
        init(null, 0);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Image> holder, Image data) {
                if (onItemSelectClick(data)) {
                    holder.updateData(data);
                }
            }
        });
    }

    /**
     * 初始化方法
     *
     * @param loaderManager Loader管理器
     * @return 任何一个LOADER_ID，可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    public String[] getAllSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * Cell点击的具体逻辑
     *
     * @param image
     * @return True，代表我进行了数据更改，你需要刷新；反之不刷新
     */
    @SuppressLint("StringFormatMatches")
    private boolean onItemSelectClick(Image image) {
        boolean notifyChange = false;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyChange = true;
        } else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyChange = true;
            }
        }
        if (notifyChange) {
            notifySelectChanged();
        }
        return notifyChange;
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        // 得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener = mListener;
        if (null != listener) {
            listener.onSelectedChange(mSelectedImages.size());
        }
    }

    /**
     * 通知Adapter数据更改
     *
     * @param images
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 用于实际的数据加载Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,//Id
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_ADDED//创建时间
        };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            /*创建Loader*/
            if (id == LOADER_ID) {
                /*进行初始化*/
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");//倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            /*Loader加载完成时*/
            List<Image> images = new ArrayList<>();
            if (null != data) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    // 得到对应的列的Index坐标
                    int idIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int pathIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int dateIndex = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = data.getInt(idIndex);
                        String path = data.getString(pathIndex);
                        Long date = data.getLong(dateIndex);

                        /*判断文件是否存在和符合要求*/
                        File file = new File(path);
                        if (null == file || file.length() < MIN_IMAGE_FILE_SIZE) {
                            continue;
                        }
                        /*添加Image*/
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = date;
                        images.add(image);

                    } while (data.moveToNext());
                }
                updateSource(images);
            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            /*Loader重置或者销毁,进行清空界面*/
            updateSource(null);
        }
    }

    /**
     * 内部的数据结构
     * 静态内部类可以在外部直接创建
     */
    private static class Image {
        int id;//数据的ID
        String path;//图片的路径
        long date;//图片的创建日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || getClass() != obj.getClass()) {
                return false;
            }
            Image image = (Image) obj;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image data) {
            return R.layout.cell_galley_view;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    /**
     * cell 对应的Holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mMask;
        private CheckBox mSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.iv_galley);
            mMask = itemView.findViewById(R.id.v_mask);
            mSelected = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image data) {
            Glide.with(getContext())
                    .load(data.path)//加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不使用缓存，直接原图加载
                    .placeholder(R.color.green_200)
                    .centerCrop()
                    .into(mPic);
            mMask.setVisibility(data.isSelect ? VISIBLE : GONE);
            mSelected.setVisibility(VISIBLE);
            mSelected.setChecked(data.isSelect);
        }
    }

    /**
     * 图片选择变化监听
     */
    public interface SelectedChangeListener {
        void onSelectedChange(int count);
    }
}
