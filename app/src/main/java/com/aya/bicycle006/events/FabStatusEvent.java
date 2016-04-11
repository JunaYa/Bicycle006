package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/23.
 */
public class FabStatusEvent {
    private static String N;
    private static String M;
    private static String G;

    private boolean mIsShow = true;

    public static enum Which {
        M, G, N;

        private Which() {
        }
        }

    public FabStatusEvent(boolean isShow) {
        mIsShow = isShow;
    }

    public boolean getIsShow() {
        return mIsShow;
    }
}
