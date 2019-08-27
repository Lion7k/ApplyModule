package com.liuzq.commlibrary.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.liuzq.commlibrary.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


import io.reactivex.functions.Consumer;

/**
 * Created by 59395 on 2018/3/3.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IBaseView, BaseView {
    /**
     * 当前 Activity 渲染的视图 View
     */
    protected View mContentView;

    protected Activity mActivity;

    protected ImmersionBar immersionBar;

    protected T mPresenter;

    protected RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        initData(getIntent().getExtras());
        setBaseView(bindLayout());
        initImmersionBar();
        initView(savedInstanceState, mContentView);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.attachContext(this);
        }
        BaseApplication.getInstance().addActivity(this);
        initListener();
        doBusiness();
    }

    private void setBaseView(@LayoutRes int layoutId) {
        mContentView = LayoutInflater.from(this).inflate(layoutId, null);
        setContentView(mContentView);
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    public void initListener() {
    }

    /**
     * 权限申请
     *
     * @param permissions
     */
    @SuppressLint("CheckResult")
    protected void requestPermission(String... permissions) {
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // All requested permissions are granted
                            applyPermissionResult();
                        } else {
                            // At least one permission is denied
                            showToast("请授权");
                        }
                    }
                });
    }

    /**
     * 权限申请
     */
    protected void applyPermissionResult() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        BaseApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
