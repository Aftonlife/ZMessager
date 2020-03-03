package com.zx.app.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * author Afton
 * date 2020/3/2
 * 数据库级别信息
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;

}
