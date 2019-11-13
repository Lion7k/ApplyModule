package com.liuzq.basemodule.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.bean.NetData;


public class NetAdapter extends BaseQuickAdapter<NetData, BaseViewHolder> {

    public NetAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NetData item) {
        TextView tv_main = helper.getView(R.id.tv_net);
        tv_main.setText(item.getName());
    }
}
