package com.zx.app.ztalker.fragments.main;


import com.zx.app.common.app.BaseFragment;
import com.zx.app.common.widget.GalleryView;
import com.zx.app.ztalker.R;

import butterknife.BindView;

/**
 *
 */
public class ActiveFragment extends BaseFragment {
    @BindView(R.id.galleryView)
    GalleryView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
