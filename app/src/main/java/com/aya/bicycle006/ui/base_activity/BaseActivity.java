package com.aya.bicycle006.ui.base_activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.aya.bicycle006.App;

import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/19.
 */
public abstract  class BaseActivity extends AppCompatActivity {
    protected  App mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = App.getApp();
        setContentView(getLayoutView());
        ButterKnife.bind(this);
    }

    protected abstract
    @LayoutRes
    int getLayoutView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
