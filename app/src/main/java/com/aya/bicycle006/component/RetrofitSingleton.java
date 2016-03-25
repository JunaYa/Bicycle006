package com.aya.bicycle006.component;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aya.bicycle006.component.api.BililiService;
import com.aya.bicycle006.component.api.DouBanMovieService;
import com.aya.bicycle006.component.api.GankService;
import com.aya.bicycle006.component.api.NewsService;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Single on 2016/3/3.
 */
public class RetrofitSingleton {


    private static NewsService newsApi = null;
    private static BililiService bililiApi = null;
    private static DouBanMovieService douBanMovieApi = null;
    private static GankService gankService = null;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
    private static RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static Executor executor = Executors.newCachedThreadPool();

    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    public static Context context;


    public static BililiService getBililiApi() {
        if (bililiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .callbackExecutor(executor)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            bililiApi = retrofit.create(BililiService.class);
        }
        return getBililiApi();
    }


    public static DouBanMovieService getDouBanMovieApi() {
        if (douBanMovieApi != null) return douBanMovieApi;
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(DouBanMovieService.BASE_URL)
                .callbackExecutor(executor)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
        douBanMovieApi = retrofit.create(DouBanMovieService.class);
        return douBanMovieApi;
    }

    public static NewsService getNewsApi() {
        if (newsApi != null) return newsApi;
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NewsService.Base_Url)
                .callbackExecutor(executor)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
        newsApi = retrofit.create(NewsService.class);
        return getNewsApi();
    }

    public static GankService getGankApi() {
        if (gankService != null) return gankService;
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(GankService.BASE_URL)
                .callbackExecutor(executor)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
        gankService = retrofit.create(GankService.class);
        return getGankApi();
    }

    public static void disposeFailureInfo(Throwable t, Context context, View view) {
        if (t.toString().contains("GaiException") || t.toString()
                                                      .contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
            Snackbar.make(view, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
        Log.w(TAG, t.toString());
    }

}
