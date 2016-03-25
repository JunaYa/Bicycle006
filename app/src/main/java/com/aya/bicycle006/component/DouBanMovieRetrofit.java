package com.aya.bicycle006.component;

import com.aya.bicycle006.component.api.DouBanMovieService;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Single on 2016/3/22.
 */
public class DouBanMovieRetrofit {
    private static Retrofit mRetrofit;
    private static DouBanMovieService mDouBanMovie;
    private static OkHttpClient okHttpClient ;


    public static void init() {
        Executor executor = Executors.newCachedThreadPool();
        mRetrofit = new Retrofit.Builder()
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(DouBanMovieService.BASE_URL)
                .build();
        mDouBanMovie = mRetrofit.create(DouBanMovieService.class);
    }

    public static DouBanMovieService getApiService() {
        if (mDouBanMovie != null) return mDouBanMovie;
        init();
        return getApiService();
    }

}
