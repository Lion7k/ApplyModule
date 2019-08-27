package com.liuzq.commlibrary.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by 59395 on 2018/3/3.
 */

public interface IBaseView {
    /**
     * 初始化数据
     * @param bundle 传递过来的 bundle
     */
    void initData(Bundle bundle);

    /**
     * 绑定布局

     * @return 布局 Id
     */
    int bindLayout();

    /**
     * 初始化 view
     */
    void initView(Bundle savedInstanceState, View view);

    /**
     * 初始化 Presenter
     */
    void initPresenter();

    /**
     * 初始化 Listener
     */
    void initListener();

    /**
     * 业务操作
     */
    void doBusiness();

    /**
     * Toast
     * @param msg
     */
    void showToast(String msg);
}
