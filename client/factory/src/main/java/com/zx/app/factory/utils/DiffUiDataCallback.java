package com.zx.app.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * author Afton
 * date 2020/3/14
 * RecyclerView 数据不同的判断
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T>mOldList,List<T>mNewList){
        this.mOldList=mOldList;
        this.mNewList=mNewList;
    }

    @Override
    public int getOldListSize() {
        /*旧数据大小*/
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        /*新数据大小*/
        return mNewList.size();
    }

    /*两个类是否时同一个东西，例如Id相等的User*/
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    // 在经过相等判断后，进一步判断是否有数据更改
    // 比如，同一个用户的两个不同实例，其中的name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    // 进行比较的数据类型
    // 范型的目的，就是你是和一个你这个类型的数据进行比较
    public interface UiDataDiffer<T> {
        /*传递一个旧数据，是否和标识的是同一个数据*/
        boolean isSame(T old);

        /*和旧数据相比是否内容相同*/
        boolean isUiContentSame(T old);
    }
}
