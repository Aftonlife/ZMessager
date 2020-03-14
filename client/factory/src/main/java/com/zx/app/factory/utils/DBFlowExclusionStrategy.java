package com.zx.app.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * author Afton
 * date 2020/3/2
 * DbFlow 数据库过滤字段
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        /*需要跳过的字段，只要是属于DBFlow数据的*/
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        /*需要跳过的类*/
        return false;
    }
}
