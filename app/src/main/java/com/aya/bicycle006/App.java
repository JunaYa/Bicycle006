package com.aya.bicycle006;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by Single on 2016/3/18.
 */
public class App extends Application {
    private static App app;
    private RefWatcher mRefWatcher;


    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = (App) getApplicationContext();
        mRefWatcher = LeakCanary.install(this);

    }

    public static App getApp() {
        return app;
    }

    public RefWatcher getRefWatcher() {
        return App.getApp().mRefWatcher;
    }

    /**
     * 列表显示方式
     */
    public static boolean isList = true;

}
