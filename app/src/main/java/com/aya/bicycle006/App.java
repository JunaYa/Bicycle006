package com.aya.bicycle006;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;


/**
 * Created by Single on 2016/3/18.
 */
public class App extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
