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


    private static final Object monitor = new Object();

    private volatile static BilibiliRetrofit bilibiliRetrofit;
    private volatile static GankRetrofit gankRetrofit;
    private volatile static NewsRetrofit newsRetrofit;

    public static BilibiliRetrofit getBRetrofitSingleton() {
        if (bilibiliRetrofit == null) {
            synchronized (BilibiliRetrofit.class) {
                if (bilibiliRetrofit == null) {
                    bilibiliRetrofit = new BilibiliRetrofit();
                }
            }
        }
        return bilibiliRetrofit;
    }

    public static GankRetrofit getGRetrofitSingleton() {
        if (gankRetrofit == null) {
            synchronized (GankRetrofit.class) {
                if (gankRetrofit == null) {
                    gankRetrofit = new GankRetrofit();
                }
            }
        }
        return gankRetrofit;
    }

    public static NewsRetrofit getNRetrofitSingleton() {
        if (newsRetrofit == null) {
            synchronized (NewsRetrofit.class) {
                if (newsRetrofit == null) {
                    newsRetrofit = new NewsRetrofit();
                }
            }
        }
        return newsRetrofit;
    }


    public static void disposeFailureInfo(Throwable t, Context context, View view) {
        if (t.toString().contains("GaiException") || t.toString()
                                                      .contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
            Snackbar.make(view, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
