package com.zx.app.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * author Afton
 * date 2020/3/2
 * DbFlow 数据库过滤字段
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        /*需要跳过的字段*/
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        /*需要跳过的类*/
        return false;
    }
}
