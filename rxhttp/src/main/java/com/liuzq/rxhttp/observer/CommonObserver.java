package com.liuzq.rxhttp.observer;


import android.text.TextUtils;

import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.rxhttp.base.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by liuzq on 2017/5/3.
 *
 * @author liuzq
 *         通用的Observer
 *         用户可以根据自己需求自定义自己的类继承BaseObserver<T>即可
 */

public abstract class CommonObserver<T> extends BaseObserver<T> {


    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 新增
     *
     * 失败回调
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    protected abstract void onError(int errorCode,String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void doOnSubscribe(Disposable d) {
    }

    @Override
    public void doOnError(String errorMsg) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtils.show(errorMsg);
        }
        onError(errorMsg);
    }

    @Override
    public void doOnError(int errorCode, String errorMsg) {
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtils.show(errorMsg);
        }
        onError(errorCode, errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
    }

}
