package com.zx.app.factory.net;

import android.text.TextUtils;

import com.zx.app.Common;
import com.zx.app.factory.Factory;
import com.zx.app.factory.persistent.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author Afton
 * date 2020/2/25
 * 网络请求封装
 */
public class NetWork {
    /*单例*/
    public static NetWork instance;
    /*全局缓存*/
    private OkHttpClient client;
    private Retrofit retrofit;

    public NetWork() {

    }

    static {
        instance = new NetWork();
    }

    /*构建OkHttpClient*/
    public static OkHttpClient getClient() {
        if (null != instance.client) {
            return instance.client;
        }
        instance.client = new OkHttpClient.Builder()
                /*添加拦截器*/
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        /*拿到我们的请求*/
                        Request request = chain.request();
                        /*重新build*/
                        Request.Builder builder = request.newBuilder();
                        /*有Token头部添加Token*/
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        /*请求类型为json*/
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        return instance.client;
    }

    /*构建Retrofit*/
    public static Retrofit getRetrofit() {
        if (null != instance.retrofit) {
            return instance.retrofit;
        }
        OkHttpClient client = getClient();
        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder
                /*连接地址*/
                .baseUrl(Common.Constance.API_URL)
                .client(client)
                /*设置解析器*/
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     *
     * @return
     */
    public static RemoteService remote() {
        return NetWork.getRetrofit().create(RemoteService.class);
    }
}