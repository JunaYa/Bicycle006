package com.aya.bicycle006.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Single on 2016/3/3.
 */
public class BILILIFilm  {

    @SerializedName("aid") private int aid;
    @SerializedName("play") private int play;
    @SerializedName("video_review") private int video_review;
    @SerializedName("coins") private int coins;
    @SerializedName("title") private String title;
    @SerializedName("author") private String author;
    @SerializedName("mid") private String mid;
    @SerializedName("pic") private String pic;
    @SerializedName("create") private String create;
    @SerializedName("description") private String description;
    @SerializedName("badgepay") private boolean badgepay;
    @SerializedName("pts") private int pts;

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public void setVideo_review(int video_review) {
        this.video_review = video_review;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBadgepay(boolean badgepay) {
        this.badgepay = badgepay;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getAid() {
        return aid;
    }

    public int getPlay() {
        return play;
    }

    public int getVideo_review() {
        return video_review;
    }

    public int getCoins() {
        return coins;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getMid() {
        return mid;
    }

    public String getPic() {
        return pic;
    }

    public String getCreate() {
        return create;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBadgepay() {
        return badgepay;
    }

    public int getPts() {
        return pts;
    }
}
