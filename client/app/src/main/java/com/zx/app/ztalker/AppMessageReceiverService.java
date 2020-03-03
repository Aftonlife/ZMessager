package com.zx.app.ztalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.zx.app.factory.Factory;
import com.zx.app.factory.data.helper.AccountHelper;
import com.zx.app.factory.persistent.Account;

/**
 * author Afton
 * date 2020/2/29
 * 个推接收消息的IntentService，用以接收具体的消息信息
 */
public class AppMessageReceiverService extends GTIntentService {
    private static final String TAG = AppMessageReceiverService.class.getName();

    @Override
    public void onReceiveServicePid(Context context, int i) {
        // 返回消息接收进程id，当前APP中就是AppPushService进程id
        Log.i(TAG, "onReceiveServicePid() called with: context = [" + context + "], i = [" + i + "]");
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        // 当设备成功在个推注册时返回个推唯一设备码
        Log.i(TAG, "onReceiveClientId() called with: context = [" + context + "], s = [" + s + "]");
        // 当Id初始化的时候
        // 获取设备Id
        onClientInit(s);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        // 当接收到透传消息时回调，跟之前广播接收消息一样
        Log.i(TAG, "onReceiveMessageData() called with: context = [" + context + "], gtTransmitMessage = [" + gtTransmitMessage + "]");
        /*拿到透传消息的实体信息转换为字符串*/
        byte[]payload=gtTransmitMessage.getPayload();
        if(null!=payload){
            String message = new String(payload);
            onMessageArrived(message);
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        // 当设备状态变化时回调，是否在线
        Log.i(TAG, "onReceiveOnlineState() called with: context = [" + context + "], b = [" + b + "]");
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        // 当个推Command命名返回时回调
        Log.i(TAG, "onReceiveCommandResult() called with: context = [" + context + "], gtCmdMessage = [" + gtCmdMessage + "]");
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        // 当通知栏消息达到时回调
        Log.i(TAG, "onNotificationMessageArrived() called with: context = [" + context + "], gtNotificationMessage = [" + gtNotificationMessage + "]");
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        // 当通知栏消息点击时回调
        Log.i(TAG, "onNotificationMessageClicked() called with: context = [" + context + "], gtNotificationMessage = [" + gtNotificationMessage + "]");
    }

    /**
     * 设备id
     * 初始化
     */
    private void onClientInit(String cid) {
        /*设备Id*/
        Account.setPushId(cid);
        if (Account.isLogin()) {
            /*账户登录了，进行一次绑定，没有登录不绑定*/
            AccountHelper.bindPushId(null);
        }
    }

    /**
     * 消息送达
     *
     * @param message
     */
    private void onMessageArrived(String message) {
        /*Factory处理*/
        Factory.dispatchPush(message);
    }

}
