package com.zx.app.factory;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zx.app.common.app.Application;
import com.zx.app.factory.data.DataSource;
import com.zx.app.factory.model.api.RspModel;

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
    /*全局Gson*/
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        /*创建一个4个线程的线程池*/
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                /*设置时间格式*/
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
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

    /**
     * 得到Gson
     *
     * @return
     */
    public static Gson getGson() {
        return instance.gson;
    }

    /**
     * 解析错误Code
     * 网络返回的Code转换为一个String资源
     *
     * @param model
     * @param callback
     */
    public static void decodeRspModel(RspModel model, DataSource.FailedCallback callback) {
        if (null == model) {
            return;
        }
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId, final DataSource.FailedCallback callback) {
        if (null != callback) {
            callback.onDataNotAvailable(resId);
        }
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout(){

    }
}
