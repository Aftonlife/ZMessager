package com.zx.app.ztalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;

/**
 * author Afton
 * date 2020/2/29
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG=MessageReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent) {
            return;
        }
        Bundle bundle = intent.getExtras();
        /*判断当前消息的意图*/
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID: {
                /*id初始化*/
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA: {
                /*常规消息*/
                byte[] payload = bundle.getByteArray("payload");
                if (null != payload) {
                    String message = new String(payload);
                    onMessageArrived(message);
                    break;
                }
            }
        }
    }

    /**
     * 设备id
     * 初始化
     */
    private void onClientInit(String cid) {

    }

    /**
     * 消息送达
     *
     * @param message
     */
    private void onMessageArrived(String message) {

    }
}
