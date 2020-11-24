package com.liuzq.basemodule;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuzq.basemodule.ui.activity.AddSubtractActivity;
import com.liuzq.basemodule.ui.activity.FingerprintActivity;
import com.liuzq.basemodule.ui.activity.FlowLayoutActivity;
import com.liuzq.basemodule.ui.activity.FragmentActivity;
import com.liuzq.basemodule.ui.activity.MultipleLayoutActivity;
import com.liuzq.basemodule.ui.activity.NetReqActivity;
import com.liuzq.basemodule.ui.activity.RefreshActivity;
import com.liuzq.basemodule.ui.activity.WebActivity;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.basemodule.ui.activity.bean.MainData;
import com.liuzq.basemodule.ui.adapter.MainAdapter;
import com.liuzq.commlibrary.recycler.GridSpaceItemDivider;
import com.liuzq.commlibrary.recycler.RVHelper;
import com.liuzq.commlibrary.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends CommActivity implements BaseQuickAdapter.OnItemClickListener {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
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
        MainAdapter mAdapter = new MainAdapter(R.layout.adapter_main);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridSpaceItemDivider(3, 10 , true));
        mAdapter.replaceData(getDatas());
    }

    private List<MainData> getDatas(){
        List<MainData> datas = new ArrayList<>();
        MainData data = new MainData();
        data.setName("rxhttp request");
        datas.add(data);

        data = new MainData();
        data.setName("error layout");
        datas.add(data);

        data = new MainData();
        data.setName("refresh layout");
        datas.add(data);

        data = new MainData();
        data.setName("bottom bar");
        datas.add(data);

        data = new MainData();
        data.setName("webview");
        datas.add(data);

        data = new MainData();
        data.setName("flowlayout");
        datas.add(data);

        data = new MainData();
        data.setName("addsubtractview");
        datas.add(data);

        data = new MainData();
        data.setName("fingerprint");
        datas.add(data);

        return datas;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 0:
                // rxhttp demo
                ActivityUtils.skipActivity(this, NetReqActivity.class);
                break;
            case 1:
                // statusview demo
                ActivityUtils.skipActivity(this, MultipleLayoutActivity.class);
                break;
            case 2:
                // refresh demo
                ActivityUtils.skipActivity(this, RefreshActivity.class);
                break;
            case 3:
                // bottombar demo
                ActivityUtils.skipActivity(this, FragmentActivity.class);
                break;
            case 4:
                // webview demo
                ActivityUtils.skipActivity(this, WebActivity.class);
                break;
            case 5:
                // flowlayout demo
                ActivityUtils.skipActivity(this, FlowLayoutActivity.class);
                break;
            case 6:
                ActivityUtils.skipActivity(this, AddSubtractActivity.class);
                break;
            case 7:
                ActivityUtils.skipActivity(this, FingerprintActivity.class);
                break;
        }
    }
}
