package com.liuzq.searchbox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuzq.searchbox.data.BaseData;

public class SearchHistoryAdapter<T> extends BaseQuickAdapter<BaseData<T>, BaseViewHolder> {
    public SearchHistoryAdapter() {
        super(R.layout.adapter_search_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseData<T> item) {
        helper.setText(R.id.text, item.getSearch());
    }
}
