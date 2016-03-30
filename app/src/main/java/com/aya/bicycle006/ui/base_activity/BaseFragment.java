package com.aya.bicycle006.ui.base_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aya.bicycle006.App;
import com.aya.bicycle006.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/19.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected App mApp;
    protected View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = App.getApp();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        rootView = super.onCreateView(inflater, container, savedInstanceState);
//        if (rootView == null) {
//            rootView = inflater.inflate(getLayoutView(), container, false);
//        }
//        ButterKnife.bind(this, rootView);
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    protected abstract
    @LayoutRes
    int getLayoutView();

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
    }
}
