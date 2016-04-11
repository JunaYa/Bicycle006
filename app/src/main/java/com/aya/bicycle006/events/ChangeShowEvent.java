package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/24.
 */
public class ChangeShowEvent {
    private boolean isList = true;

    public ChangeShowEvent(boolean isList) {
        this.isList = isList;
    }

    public boolean isList() {
        return isList;
    }
}
