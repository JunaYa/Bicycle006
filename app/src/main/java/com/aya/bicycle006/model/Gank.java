package com.aya.bicycle006.model;

import android.graphics.Point;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Single on 2016/3/25.
 */
public class Gank {

    /**
     * _id : 56f36e8b67765933d8be9133
     * _ns : ganhuo
     * createdAt : 2016-03-24T12:35:23.841Z
     * desc : 3.24
     * publishedAt : 2016-03-25T11:23:49.570Z
     * source : chrome
     * type : 福利
     * url : http://ww1.sinaimg.cn/large/7a8aed7bjw1f27uhoko12j20ez0miq4p.jpg
     * used : true
     * who : 张涵宇
     */

    private String _id;
    private String _ns;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_ns() {
        return _ns;
    }

    public void set_ns(String _ns) {
        this._ns = _ns;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
