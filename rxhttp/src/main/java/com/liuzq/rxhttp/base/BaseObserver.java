package com.liuzq.rxhttp.base;

import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.rxhttp.exception.ApiException;
import com.liuzq.rxhttp.interfaces.ISubscriber;
import com.liuzq.rxhttp.manager.RxHttpManager;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by liuzq on 2017/5/3.
 *
 * @author liuzq
 *         <p>
 *         基类BaseObserver
 */

public abstract class BaseObserver<T> implements Observer<T>, ISubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return false;
    }

    /**
     * 标记网络请求的tag
     * tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
     * 设置一个tag就行就可以取消当前页面所有请求或者某个请求了
     *
     * @return string
     */
    protected String setTag() {
        return null;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        LogUtils.d("BaseObserver onSubscribe", setTag());

        RxHttpManager.get().add(setTag(), d);
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        int code = ApiException.handleException(e).getCode();
        setError(code, error);
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }

    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }

    /**
     * 新增
     */
    private void setError(int errorCode, String errorMsg) {
        doOnError(errorCode,errorMsg);
    }
}
