package com.liuzq.commlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzq.commlibrary.utils.ToastUtils;

/**
 * Created by 59395 on 2018/3/3.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IBaseView, BaseView{

    protected Context mContext;
    protected Activity activity;
    protected View mContentView;
    protected T mPresenter;

    private String  STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView != null) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            parent.removeView(mContentView);
        } else {
            mContentView = inflater.inflate(bindLayout(), container, false);
        }
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(getArguments());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        activity = (Activity) mContext;
        initView(savedInstanceState,mContentView);
        initPresenter();
        if (mPresenter != null){
            mPresenter.attachView(this);
            mPresenter.attachContext(this.getActivity());
        }

        initListener();
        doBusiness();
    }

    public void initListener() {
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void onDestroyView() {
        if (mContentView != null){
            ViewGroup group = (ViewGroup)mContentView.getParent();
            if (null != group){
                group.removeView(mContentView);
            }
        }
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
    }
}
