package com.liuzq.basemodule.api;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * <pre>
 *      @author : liuzq
 *      desc    :
 * </pre>
 */
public interface OtherOpenApi {

    @GET("jokes/list/random")
    Observable<String> getJokesRandom();
}
