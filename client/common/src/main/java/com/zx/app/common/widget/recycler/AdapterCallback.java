package com.zx.app.common.widget.recycler;

/**
 * author Afton
 * date 2020/2/8
 */
public interface AdapterCallback<T> {
    void update(T data, RecyclerAdapter.ViewHolder<T> viewHolder);
}
