package com.aya.bicycle006.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aya.bicycle006.events.FabStatusEvent;
import com.aya.bicycle006.events.LoadMoreEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Single on 2016/3/23.
 */
public class HideScrollLoadMoreListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    private View mView;

    private int mBottomMargin;

    private int mLastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private int mCount;

    public HideScrollLoadMoreListener(int bottomMargin, LinearLayoutManager manager, int count) {
        this.mBottomMargin = bottomMargin;
        this.mLayoutManager = manager;
        this.mCount = count;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (RecyclerView.SCROLL_STATE_IDLE == newState && mLastVisibleItem + 1 == mCount) {
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }

        //加载更多
        mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
    }

    public void onHide() {
        EventBus.getDefault().post(new FabStatusEvent(false));
    }

    public void onShow() {
        EventBus.getDefault().post(new FabStatusEvent(true));
    }

    public void onLoadMore() {
        EventBus.getDefault().post(new LoadMoreEvent(true));
    }
}
