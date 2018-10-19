package com.liuzq.httplibrary.download;

import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.httplibrary.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;

/**
 * Created by liuzq on 2018/10/19.
 */

public abstract class BaseDownloadObserver implements Observer<ResponseBody> {

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void doOnError(String errorMsg);

    @Override
    public void onError(@NonNull Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        setError(error);
    }

    private void setError(String errorMsg) {
        ToastUtils.show(errorMsg);
        doOnError(errorMsg);
    }
}
