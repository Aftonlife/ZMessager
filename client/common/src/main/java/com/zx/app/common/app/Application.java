package com.zx.app.common.app;

import android.os.SystemClock;

import java.io.File;

/**
 * author Afton
 * date 2020/2/12
 */
public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 外部获取单例
     *
     * @return
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * 获取当前app缓存文件夹
     *
     * @return
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * 获取头像临时存储文件地址
     *
     * @return
     */
    public static File getPortraitTmpFile() {
        File dir = new File(getCacheDirFile(), "portrait");
        dir.mkdir();
        /*删除久缓存*/
        File[] files = dir.listFiles();
        if (null != files && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        /*返回一个当前时间戳的目录文件地址*/
        File path = new File(dir,SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }
}
