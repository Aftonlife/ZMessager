package com.zx.app.factory.net;

import com.zx.app.Common;
import com.zx.app.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author Afton
 * date 2020/2/25
 * 网络请求封装
 */
public class NetWork {
    /*构建Retrofit*/
    public static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder
                /*连接地址*/
                .baseUrl(Common.Constance.API_URL)
                .client(client)
                /*设置解析器*/
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
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