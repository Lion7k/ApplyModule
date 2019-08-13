package com.liuzq.httplibrary.interceptor;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.liuzq.httplibrary.utils.NetUtils.isNetworkConnected;

/**
 * Created by liuzq on 2018/10/18.
 * desc    : 网络缓存
 */

public class NoNetCacheInterceptor implements Interceptor {
    /**
     * 无网络缓存时间3600秒
     */
    private int noNetCacheTime;

    public NoNetCacheInterceptor(int noNetCacheTime) {
        this.noNetCacheTime = noNetCacheTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        boolean connected = isNetworkConnected();
        //如果没有网络，则启用 FORCE_CACHE
        if (!connected) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            Response response = chain.proceed(request);

            //没网的时候如果也没缓存的话就走网络
            if (response.code() == 504) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                return chain.proceed(request);
            }

            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale="+noNetCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }
        //有网络的时候，这个拦截器不做处理，直接返回
        return chain.proceed(request);
    }
}
