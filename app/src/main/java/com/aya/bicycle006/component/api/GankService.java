package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.Gank;
import com.aya.bicycle006.model.GankApi;

import java.util.List;

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
}
