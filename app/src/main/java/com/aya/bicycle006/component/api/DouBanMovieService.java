package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.DApi;
import com.aya.bicycle006.model.DouBanMovie;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Single on 2016/3/22.
 */
public interface DouBanMovieService {
    String BASE_URL = "https://api.douban.com/v2/movie/";

    @GET("top250")
    Observable<DApi> mDouBanMovieSApi(@Query("start") int start, @Query("count") int count);
}
