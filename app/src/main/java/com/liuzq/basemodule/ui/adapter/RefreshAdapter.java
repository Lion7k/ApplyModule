package com.liuzq.basemodule.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.bean.BannerBean;

public class RefreshAdapter extends BaseQuickAdapter<BannerBean, BaseViewHolder> {

    public RefreshAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerBean item) {
       TextView txt_id =  helper.getView(R.id.txt_id);
       TextView txt_desc =  helper.getView(R.id.txt_desc);
       txt_id.setText(String.valueOf(item.getId()));
       txt_desc.setText(item.getDesc());
    }
}
