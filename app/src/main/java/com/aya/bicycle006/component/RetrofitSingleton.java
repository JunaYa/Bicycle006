package com.aya.bicycle006.component;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aya.bicycle006.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private static ApiInterface apiService = null;

    private static Retrofit retrofit = null;

    private static OkHttpClient okHttpClient = null;

    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    public static Context context;

    /**
     * 初始化
     */

    public static void init() {

        Executor executor = Executors.newCachedThreadPool();

        Gson gson = new GsonBuilder().create();

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiInterface.HEHEHE)
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        apiService = retrofit.create(ApiInterface.class);
    }


    public static ApiInterface getApiService() {
        if (apiService != null) return apiService;
        init();
        return getApiService();
    }


    public static Retrofit getRetrofit() {
        if (retrofit != null) return retrofit;
        init();
        return getRetrofit();
    }


    public static OkHttpClient getOkHttpClient( ) {
        if (okHttpClient != null) return okHttpClient;
        init();
        return getOkHttpClient();
    }


    public static void disposeFailureInfo(Throwable t, Context context, View view) {
        if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
            Snackbar.make(view, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
        Log.w(TAG, t.toString());
    }

    public static void loadMore(View view){
       Snackbar.make(view, "加载更多,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();

    }
}
