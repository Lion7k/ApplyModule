package com.liuzq.basemodule.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.bean.MainData;


public class MainAdapter extends BaseQuickAdapter<MainData, BaseViewHolder> {

    public MainAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainData item) {
        TextView tv_main = helper.getView(R.id.tv_main);
        tv_main.setText(item.getName());
    }
}
