package com.aya.bicycle006.events;

/**
 * Created by Single on 2016/3/24.
 */
public class ChangeShow {
    private boolean isList = true;

    public ChangeShow(boolean isList) {
        this.isList = isList;
    }

    public boolean isList() {
        return isList;
    }
}
