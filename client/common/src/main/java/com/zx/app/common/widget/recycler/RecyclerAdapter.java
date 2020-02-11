package com.zx.app.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zx.app.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author Afton
 * date 2020/2/8
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>>
        implements AdapterCallback<T>, View.OnClickListener, View.OnLongClickListener {
    private final List<T> mDataList;
    private AdapterListener<T> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<T>(), listener);
    }

    public RecyclerAdapter(List<T> dataList, AdapterListener<T> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * viewType处理为XML布局的id所以要重写一下
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型 子类复写
     *
     * @param position
     * @param data
     * @return XML文件的ID，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, T data);


    /**
     * 创建ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 这里处理为XMl布局的id (R.layout.xxx),这样不同的viewType 只的就是不同的Layout
     * @return
     */
    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*得到LayoutInflater来加载ViewType的Layout为View或者ViewGroup*/
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        /*子类来实现创建holder*/
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);
        /*配置holder回调*/
        holder.callback = this;

        /*设置View的Tag为ViewHolder，进行双向绑定*/
        root.setTag(R.id.tag_recycler_holder, holder);

        /*点击事件*/
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        /*注解绑定*/
        holder.unbinder = ButterKnife.bind(holder, root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        T data = mDataList.get(position);
        holder.bind(data);
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局类型，就是XML的ID
     * @return
     */
    protected abstract ViewHolder<T> onCreateViewHolder(View root, int viewType);

    /**
     * 得到集合数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 返回整个集合
     *
     * @return
     */
    public List<T> getItems() {
        return mDataList;
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(T data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入多条数据并通知插入更新
     */
    public void add(T... dataList) {
        if (null != dataList && dataList.length > 0) {
            int startPosition = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPosition, dataList.length);
        }
    }

    /**
     * 插入多条数据并通知插入更新
     */
    public void add(Collection<T> dataList) {
        if (null != dataList && dataList.size() > 0) {
            int startPosition = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPosition, dataList.size());
        }
    }

    /**
     * 清空
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 先清空再替换为新的集合
     *
     * @param dataList
     */
    public void replace(Collection<T> dataList) {
        mDataList.clear();
        if (null != dataList && dataList.size() > 0) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     *
     * @param data
     * @param viewHolder
     */
    @Override
    public void update(T data, ViewHolder<T> viewHolder) {
        /*得到当前ViewHolder坐标*/
        int pos = viewHolder.getAdapterPosition();
        if (pos >= 0) {
            /*先移除再替换*/
            mDataList.remove(pos);
            mDataList.add(pos, data);
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        /*View 通过Tag得到ViewHolder*/
        ViewHolder<T> viewHolder = (ViewHolder<T>) v.getTag(R.id.tag_recycler_holder);
        if (null != mListener) {
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder<T> viewHolder = (ViewHolder<T>) v.getTag(R.id.tag_recycler_holder);
        if (null != mListener) {
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器的监听
     *
     * @param listener
     */
    public void setListener(AdapterListener<T> listener) {
        this.mListener = listener;
    }


    /**
     * 自定义监听，主要是点击监听
     *
     * @param <T>
     */
    public interface AdapterListener<T> {
        void onItemClick(ViewHolder<T> holder, T data);

        void onItemLongClick(ViewHolder<T> holder, T data);
    }


    /**
     * 自定义ViewHolder
     *
     * @param <T>泛型
     */
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        protected T mData;
        private AdapterCallback<T> callback;
        private Unbinder unbinder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(T data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 触发绑定时的回调，必须复写
         */
        protected abstract void onBind(T data);

        /**
         * Holder自己对应的Data更新数据
         *
         * @param data
         */
        public void updateData(T data) {
            if (null != this.callback) {
                this.callback.update(data, this);
            }
        }
    }

    /**
     * 对回调接口的一次实现，后续设置监听可以只实现用到的回调
     *
     * @param <T>
     */
    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {
        @Override
        public void onItemClick(ViewHolder<T> holder, T data) {

        }

        @Override
        public void onItemLongClick(ViewHolder<T> holder, T data) {

        }
    }
}
