package com.aya.bicycle006.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Single on 2016/3/22.
 */
public class DApi {

    /**
     * count : 10
     * start : 0
     * total : 250
     * subjects : []
     * title : 豆瓣电影Top250
     */

    @SerializedName("count") public int count;
    @SerializedName("start") public int start;
    @SerializedName("total") public int total;
    @SerializedName("subjects") public List<D> subjects;
    @SerializedName("title") public String title;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<D> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<D> subjects) {
        this.subjects = subjects;
    }
}
