package com.liuzq.basemodule.api;


import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.bean.HotBean;
import com.liuzq.httplibrary.bean.BaseData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * <pre>
 *      @author : liuzq
 *      desc    :
 * </pre>
 */
public interface WanAndroidApi {

    /**
     * 获取banner数据
     *
     * @return
     */
    @GET("banner/json")
    Observable<BaseData<List<BannerBean>>> getBanner();

    /**
     * 热搜
     *
     * @return
     */
    @GET("hotkey/json")
    Observable<BaseData<List<HotBean>>> getHotSearchData();

    /**
     * 热搜
     *
     * @return
     */
    @GET("hotkey/json")
    Observable<String> getHotSearchStringData();


}
