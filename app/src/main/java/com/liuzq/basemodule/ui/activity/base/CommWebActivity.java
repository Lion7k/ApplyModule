package com.liuzq.basemodule.ui.activity.base;

import android.os.Bundle;
import android.view.View;

import com.liuzq.basemodule.R;
import com.liuzq.commlibrary.base.BasePresenter;
import com.liuzq.commlibrary.base.BaseWebActivity;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class CommWebActivity<T extends BasePresenter>  extends BaseWebActivity<T> {

    @BindView(R.id.title_bar)
    protected CommonTitleBar title_bar;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }
}
