package com.aya.bicycle006.component;

import com.aya.bicycle006.component.api.NewsApi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Single on 2016/3/23.
 */
public class NewsRetrofit {

//    public static String Base_Url = "http://c.3g.163.com/nc/article/list/T1348649580692/0-20.html";
    public static String Base_Url = "http://c.3g.163.com/nc/article/list/";

    private static Retrofit mRetrofit;
    private static NewsApi mNewsApi;

    public static void init() {
        Executor executor = Executors.newCachedThreadPool();
        mRetrofit = new Retrofit.Builder()
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Base_Url)
                .build();
        mNewsApi = mRetrofit.create(NewsApi.class);
    }

    public static NewsApi getApiService() {
        if (mNewsApi != null) return mNewsApi;
        init();
        return getApiService();
    }
}
