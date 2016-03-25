package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.BILILIFilmApi;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Single on 2016/3/25.
 */
public interface BililiService {
    public static String BASE_URL = "http://www.bilibili.com/index/rank/all-7-1.json";

    @GET("http://www.bilibili.com/index/rank/all-7-1.json")
    Observable<BILILIFilmApi> mBILILIFilmApi();
}
