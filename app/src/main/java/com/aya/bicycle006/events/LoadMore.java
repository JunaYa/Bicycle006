package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/23.
 */
public class LoadMore {
    private boolean isLoadMore = false;

    public LoadMore(boolean isLoadMore) {

        this.isLoadMore = isLoadMore;
    }

    public boolean getIsGetMore(){
        return isLoadMore;
    }
}