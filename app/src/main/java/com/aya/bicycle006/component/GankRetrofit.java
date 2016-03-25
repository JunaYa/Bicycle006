package com.aya.bicycle006.component;

import com.aya.bicycle006.component.api.BililiService;
import com.aya.bicycle006.component.api.GankService;
import com.aya.bicycle006.component.api.NewsService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Single on 2016/3/25.
 */
public class GankRetrofit {
    private static Retrofit mRetrofit;
    private static GankService gankService;

    public static void init() {
        Executor executor = Executors.newCachedThreadPool();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(GankService.BASE_URL)
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gankService = mRetrofit.create(GankService.class);
    }

    public static GankService getGankService() {
        if (gankService != null) return gankService;
        init();
        return getGankService();
    }
}
