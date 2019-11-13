package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.basemodule.ui.activity.bean.NetData;
import com.liuzq.basemodule.ui.adapter.NetAdapter;
import com.liuzq.commlibrary.recycler.GridSpaceItemDivider;
import com.liuzq.commlibrary.recycler.RVHelper;
import com.liuzq.commlibrary.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NetActivity extends CommActivity implements BaseQuickAdapter.OnItemClickListener{
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_net;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void doBusiness() {
        initAdapter();
    }

    private void initAdapter(){
        RVHelper.initRecyclerViewStyle(this, mRecyclerView, 3);
        NetAdapter mAdapter = new NetAdapter(R.layout.adapter_net);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridSpaceItemDivider(3, 10 , true));
        mAdapter.replaceData(getDatas());
    }

    private List<NetData> getDatas(){
        List<NetData> datas = new ArrayList<>();
        NetData data = new NetData();
        data.setName("统一请求---使用全局配置的相关参数");
        datas.add(data);

        data = new NetData();
        data.setName("统一请求---接受string");
        datas.add(data);

        data = new NetData();
        data.setName("链式发送多个请求");
        datas.add(data);

        data = new NetData();
        data.setName("使用豆瓣baseUrl请求数据");
        datas.add(data);

        return datas;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        ActivityUtils.skipActivity(this, NetReqActivity.class, bundle);
    }

}
