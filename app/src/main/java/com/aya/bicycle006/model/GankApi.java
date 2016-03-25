package com.aya.bicycle006.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Single on 2016/3/25.
 */
public class GankApi {
    private boolean error;
    private List<Gank> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Gank> getResults() {
        return results;
    }

    public void setResults(List<Gank> results) {
        this.results = results;
    }
}
