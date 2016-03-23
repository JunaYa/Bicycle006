package com.aya.bicycle006.component.api;

import com.aya.bicycle006.model.N;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Single on 2016/3/21.
 */
public interface NewsApi {
    public static String[] road = {
            "T1348649580692", "T1348649776727", "T1351233117091",
            "T1421997195219", "T1401272877187", "T1348649654285"
    };
    public static String Base_Url = "http://c.3g.163.com/nc/article/list/";

    @GET("{which}/{page}-{no}.html")
    Observable<N> mNews(@Path("which") String which, @Path("page") int page, @Path("no") int no);
}
