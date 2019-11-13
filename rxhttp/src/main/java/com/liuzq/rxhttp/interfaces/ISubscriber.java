package com.liuzq.rxhttp.interfaces;

import io.reactivex.disposables.Disposable;

/**
 * Created by liuzq on 2018/10/18.
 * 定义请求结果处理接口
 */

public interface ISubscriber<T> {
    /**
     * doOnSubscribe 回调
     *
     * @param d Disposable
     */
    void doOnSubscribe(Disposable d);

    /**
     * 错误回调
     *
     * @param errorMsg 错误信息
     */
    void doOnError(String errorMsg);

    /**
     * 新增
     *
     * 错误回调
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    void doOnError(int errorCode ,String errorMsg);

    /**
     * 成功回调
     *
     * @param t 泛型
     */
    void doOnNext(T t);

    /**
     * 请求完成回调
     */
    void doOnCompleted();
}
