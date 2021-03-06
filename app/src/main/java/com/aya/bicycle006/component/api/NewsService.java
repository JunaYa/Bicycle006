package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.N;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Single on 2016/3/21.
 */
public interface NewsService {
    public static String[] road = {
            "T1348649580692", "T1348649776727", "T1351233117091",
            "T1421997195219", "T1401272877187", "T1348649654285"
    };

    @GET("{which}/{page}-{no}.html")
    Observable<N> mNewsApi(@Path("which") String which, @Path("page") int page, @Path("no") int no);
}
