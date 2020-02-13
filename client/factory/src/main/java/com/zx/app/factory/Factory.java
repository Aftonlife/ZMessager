package com.zx.app.factory;

import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author Afton
 * date 2020/2/13
 */
public class Factory {
    private static final String TAG = Factory.class.getName();
    /*懒汉单例模式*/
    private static final Factory instance;
    /*全局线程池*/
    private final Executor executor;

    static {
        instance = new Factory();
    }

    private Factory() {
        /*创建一个4个线程的线程池*/
        executor = Executors.newFixedThreadPool(4);

    }

    /**
     * 异步运行 线程池
     *
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }
}
