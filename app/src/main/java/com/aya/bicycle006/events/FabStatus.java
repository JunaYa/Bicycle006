package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/23.
 */
public class FabStatus {
    private boolean mIsShow = true;

    public FabStatus(boolean isShow) {
        mIsShow = isShow;
    }

    public boolean getIsShow() {
        return mIsShow;
    }
}
