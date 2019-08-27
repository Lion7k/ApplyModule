package com.liuzq.commlibrary.base;

import android.content.Context;

/**
 * Created by 59395 on 2018/3/3.
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void attachContext(Context mContext);

    void detachView();
}
