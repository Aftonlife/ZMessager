package com.zx.app.ztalker.fragments.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zx.app.common.widget.GalleryView;
import com.zx.app.ztalker.R;

import net.qiujuer.genius.ui.Ui;

/**
 * author Afton
 * date 2020/2/12
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {
    private GalleryView mGallery;
    private OnSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransBottomSheetDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedChange(int count) {
        if (count > 0) {
            dismiss();
            if (null != listener) {
                /*得到选中的图片地址*/
                String[] paths = mGallery.getAllSelectedPath();
                listener.onSelectedImage(paths[0]);
                /*取消和唤起者的联系，以便更快的内存回收*/
                listener = null;
            }
        }
    }

    /**
     * 设置事件监听
     *
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 选中图片监听
     */
    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    private static class TransBottomSheetDialog extends BottomSheetDialog {

        public TransBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Window window = getWindow();
            if (null == window) {
                return;
            }
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
            int statusHeight = (int) Ui.dipToPx(getContext().getResources(), 25);
            int dialogHeight = screenHeight - statusHeight;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
