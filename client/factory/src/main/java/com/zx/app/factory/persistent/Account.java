package com.zx.app.factory.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.zx.app.Common;
import com.zx.app.factory.Factory;
import com.zx.app.factory.model.api.account.AccountRspModel;
import com.zx.app.factory.model.db.User;
import com.zx.app.factory.model.db.User_Table;

/**
 * author Afton
 * date 2020/2/28
 * 持久化数据
 */
public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    /*设备推送Id*/
    private static String pushId = "test";
    /*是否绑定服务器*/
    private static boolean isBind;
    /*token用于接口请求*/
    private static String token;
    /*登录用户的Id*/
    private static String userId;
    /*登录的账户*/
    private static String account;

    /**
     * 存储数据
     *
     * @param context
     */
    private static void save(Context context) {
        /*获取数据持久化的SP*/
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        /*存储数据*/
        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 进行数据加载
     *
     * @param context
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
    }

    /*设置推送Id*/
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    public static String getPushId() {
        return Account.pushId;
    }

    /*判断是否登录*/
    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);
    }

    /*是否绑定*/
    public static boolean isBind() {
        return Account.isBind;
    }

    /*设置绑定状态*/
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }

    /*是否完善用户信息*/
    public static boolean isComplete() {
        /*首先确保登录成功*/
        if (isLogin()) {
        }
        return false;
    }

    /*获取当前登录的用户信息*/
    public static User getUser() {
        /*如果userId为null返回一个new User，其次从数据库查询*/
        return TextUtils.isEmpty(userId) ? new User() : SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    /*获取当前登录的Token*/
    public static String getToken() {
        return token;
    }

    /**
     * 保持登录的信息
     */
    public static void login(AccountRspModel model) {
        /*存储用户account,Id，token,*/
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        Account.token = model.getToken();
        save(Factory.app());
    }
}
