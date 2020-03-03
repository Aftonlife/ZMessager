package com.zx.app.ztalker;

import com.igexin.sdk.PushManager;
import com.zx.app.common.app.Application;
import com.zx.app.factory.Factory;

/**
 * author Afton
 * date 2020/2/12
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Factory初始化*/
        Factory.setUp();

        /*个推服务初始化*/
        PushManager.getInstance().initialize(this, AppPushService.class);
        /*个推消息接受服务注册*/
        PushManager.getInstance().registerPushIntentService(this, AppMessageReceiverService.class);
    }
}
