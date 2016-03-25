package com.aya.bicycle006.component;

import com.aya.bicycle006.component.api.BililiService;
import com.aya.bicycle006.component.api.NewsService;
import com.aya.bicycle006.model.BILILIFilmApi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Single on 2016/3/25.
 */
public class BilibiliRetrofit {
    private static Retrofit mRetrofit;
    private static BililiService bililiService;

    public static void init() {
        Executor executor = Executors.newCachedThreadPool();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BililiService.BASE_URL)
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        bililiService = mRetrofit.create(BililiService.class);
    }

    public static BililiService getBililiService() {
        if (bililiService != null) return bililiService;
        init();
        return getBililiService();
    }
}
