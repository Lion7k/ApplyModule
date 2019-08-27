package com.liuzq.basemodule.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.ui.activity.base.RefreshLayoutBaseActivity;
import com.liuzq.basemodule.ui.adapter.RefreshAdapter;
import com.liuzq.commlibrary.recycler.RVHelper;
import com.liuzq.commlibrary.recycler.RecycleViewDivider;
import com.liuzq.commlibrary.utils.DisplayUtils;
import com.liuzq.commlibrary.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefreshActivity extends RefreshLayoutBaseActivity implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener{

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RefreshAdapter mAdapter;
    private List<BannerBean> beans;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RefreshActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_refresh;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        initLayout();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        initAdapter();

        if (refreshLayout != null)
            refreshLayout.autoRefresh();

        setDatas();
    }

    private void initAdapter() {
        RVHelper.initRecyclerViewStyle(this, mRecyclerView, LinearLayout.VERTICAL);
        mAdapter = new RefreshAdapter(R.layout.adapter_refresh);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL,
                DisplayUtils.dip2px(this, 1), StringUtils.resColor(this, R.color.white_f5)));
    }

    private void setDatas(){
        beans = new ArrayList<>();

        BannerBean bean = new BannerBean();
        bean.setId(0);
        bean.setDesc("123");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(1);
        bean.setDesc("234");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(2);
        bean.setDesc("345");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(3);
        bean.setDesc("456");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(4);
        bean.setDesc("567");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(5);
        bean.setDesc("678");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(6);
        bean.setDesc("789");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(7);
        bean.setDesc("890");
        beans.add(bean);

        bean = new BannerBean();
        bean.setId(8);
        bean.setDesc("901");
        beans.add(bean);
    }

    @Override
    protected void loadData() {
        handleSuccess(mAdapter, beans);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BannerBean bean = (BannerBean) adapter.getItem(position);
        showToast(String.format("我是第%s個，id:%s desc:%s", position, bean.getId(), bean.getDesc()));
    }

    @Override
    public void showError(String msg) {

    }
}
