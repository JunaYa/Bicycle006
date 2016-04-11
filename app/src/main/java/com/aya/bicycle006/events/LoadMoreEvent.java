package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/23.
 */
public class LoadMoreEvent {
    private boolean isLoadMore = false;

    public LoadMoreEvent(boolean isLoadMore) {

        this.isLoadMore = isLoadMore;
    }

    public boolean getIsGetMore(){
        return isLoadMore;
    }
}