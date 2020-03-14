package com.zx.app.ztalker.fragments.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.app.common.app.PresenterFragment;
import com.zx.app.common.widget.EmptyView;
import com.zx.app.common.widget.PortraitView;
import com.zx.app.common.widget.recycler.RecyclerAdapter;
import com.zx.app.factory.model.card.UserCard;
import com.zx.app.factory.presenter.contact.FollowContract;
import com.zx.app.factory.presenter.contact.FollowPresenter;
import com.zx.app.factory.presenter.search.SearchContract;
import com.zx.app.factory.presenter.search.SearchUserPresenter;
import com.zx.app.ztalker.R;
import com.zx.app.ztalker.activities.MessageActivity;
import com.zx.app.ztalker.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * create an instance of this fragment.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchContract.UserView, SearchActivity.SearchFragment {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard data) {
                return R.layout.cell_search_user_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        /*首次搜索*/
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        mEmptyView.triggerOkOrEmpty(userCards.size() > 0);
    }

    /**
     * 每个Cell的布局
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View {

        @BindView(R.id.pv_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.iv_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard data) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this), data.getPortrait());
            mName.setText(data.getName());
            mFollow.setEnabled(!data.isFollowing());
        }

        @OnClick(R.id.iv_follow)
        public void followClick() {
            mPresenter.follow(mData.getId());
        }

        @OnClick(R.id.pv_portrait)
        public void portraitClick() {
            MessageActivity.show(getContext(), mData);
        }

        @Override
        public void onFollowDone(UserCard card) {
            /*停止动画*/
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.stop();
                /*设为默认值*/
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            /*更新数据*/
            updateData(card);
        }

        @Override
        public void showError(int str) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                /*失败就停止动画，并且显示一个圆圈*/
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            /*初始化圆形的动画Drawable*/
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            /*设置颜色*/
            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            /*设置给ImageView*/
            mFollow.setImageDrawable(drawable);
            /*启动*/
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}
