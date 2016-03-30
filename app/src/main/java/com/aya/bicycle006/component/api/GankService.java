package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.GankApi;
import com.aya.bicycle006.model.GankData;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Single on 2016/3/25.
 */
public interface GankService {
    public static String BASE_URL = "http://gank.io/api/";

    @GET("data/福利/{number}/{page}")
    Observable<GankApi> mGankApi(@Path("number") int number, @Path("page") int page);

    @GET("day/{date}")
    Observable<GankData> mGankDataApi(@Path("date") String date);
}
