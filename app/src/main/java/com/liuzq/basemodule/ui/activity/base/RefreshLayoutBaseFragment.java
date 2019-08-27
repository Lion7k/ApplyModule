package com.liuzq.basemodule.ui.activity.base;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuzq.basemodule.R;
import com.liuzq.commlibrary.base.BaseFragment;
import com.liuzq.commlibrary.base.BasePresenter;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.statusview.StatusView;
import com.liuzq.statusview.StatusViewBuilder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by liuzq on 2018/3/8.
 */

public abstract class RefreshLayoutBaseFragment<T,V extends BasePresenter> extends BaseFragment<V> implements OnRefreshLoadMoreListener {

    protected final int REFRESH = 654654;
    protected final int LOADMORE = 685463541;
    @BindView(R.id.refresh_layout)
    protected RefreshLayout refreshLayout;

    protected StatusView statusView;

    protected int loadType = -1;
    protected int start = -1;
    protected int rows = 20;
    protected boolean loadMoreEnable = true;

    protected void initLayout(View view) {
        ButterKnife.bind(this, view);
        initRefreshLayout();
    }


    protected void initRefreshLayout() {
        if (refreshLayout != null) refreshLayout.setOnRefreshLoadMoreListener(this);

        statusView = StatusView.init(this, R.id.refresh_layout);

        statusView.config(new StatusViewBuilder.Builder()
                .setOnErrorRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        statusView.showLoadingView();
                        loadData();
                    }
                })
                .setOnEmptyRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusView.showLoadingView();
                        loadData();
                    }
                }).build());
        statusView.showLoadingView();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadType = REFRESH;
        start = 1;
        LogUtils.e("liuzq onLoadMore", "start = " + start);
        loadData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (loadMoreEnable) {
            loadType = LOADMORE;
            start++;
            LogUtils.e("liuzq onRefresh", "start = " + start);
            loadData();
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * @param adapter
     * @param datas 不可以传空的集合
     */
    protected void handleSuccess(BaseQuickAdapter<T, ? extends BaseViewHolder> adapter, List<T> datas){
        if (refreshLayout == null) {
            return;
        }

        // 刷新
        if (loadType == REFRESH) {
            loadMoreEnable = true;
            refreshLayout.finishRefresh();
            if (datas.size() > 0) {
                statusView.showContentView();
                adapter.setNewData(datas);
            } else {
                //空数据
                if (adapter != null) {
                    adapter.setNewData(datas);
                }
                statusView.showEmptyView();
            }
            // 加载
        } else if (loadType == LOADMORE){
            refreshLayout.finishLoadMore();
            if (datas.size() > 0) {
                adapter.addData(datas);
            }
        }

        /**
         * 这里必须注意一点，后台必须传过来一个大小为0的集合
         */
        if (datas.size() < rows) {
            //没有更多数据了
            loadMoreEnable = false;
        }
    }

    protected void handleFailure(){
        if (refreshLayout == null) {
            return;
        }

        if (loadType == REFRESH) {
            refreshLayout.finishRefresh();
            statusView.showEmptyView();
        } else {
            refreshLayout.finishLoadMore();
            statusView.showEmptyView();
        }
    }

    //加载数据
    protected abstract void loadData();
}
