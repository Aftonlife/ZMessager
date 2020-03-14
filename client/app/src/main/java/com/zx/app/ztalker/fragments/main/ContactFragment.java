package com.zx.app.ztalker.fragments.main;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.app.common.app.BaseFragment;
import com.zx.app.common.app.PresenterFragment;
import com.zx.app.common.widget.EmptyView;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.common.widget.recycler.RecyclerAdapter;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.presenter.contact.ContactContract;
import com.zx.app.factory.presenter.contact.ContactPresenter;
import com.zx.app.factory.presenter.contact.FollowContract;
import com.zx.app.factory.presenter.contact.FollowPresenter;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.activities.MessageActivity;
import com.zx.app.ztalker.activities.PersonalActivity;
import com.zx.app.ztalker.fragments.search.SearchUserFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User data) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        /*点击监听*/
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<User> holder, User data) {
                MessageActivity.show(getContext(), data);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        /*空布局*/
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 每个Cell的布局
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.pv_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_desc)
        TextView mDesc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User data) {
            mPortraitView.setup(Glide.with(ContactFragment.this), data.getPortrait());
            mName.setText(data.getName());
            mDesc.setText(data.getDesc());
        }

        @OnClick(R.id.pv_portrait)
        public void portraitClick() {
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}
