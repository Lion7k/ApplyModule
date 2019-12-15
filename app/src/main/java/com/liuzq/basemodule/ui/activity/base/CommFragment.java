package com.liuzq.basemodule.ui.activity.base;

import android.os.Bundle;
import android.view.View;

import com.liuzq.commlibrary.base.BaseFragment;
import com.liuzq.commlibrary.base.BasePresenter;

import butterknife.ButterKnife;

public abstract class CommFragment<T extends BasePresenter> extends BaseFragment<T> {

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }
}
