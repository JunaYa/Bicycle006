package com.aya.bicycle006.listeners;

import com.aya.bicycle006.model.Gank;
import android.view.View;

import rx.Observer;

/**
 * Created by Single on 2016/3/28.
 */
public interface OnBicycleItemLongClickListener {
    void onLongClick(View view, Object gank);
}
