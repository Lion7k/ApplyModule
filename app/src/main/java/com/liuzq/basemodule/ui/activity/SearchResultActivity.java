package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;

import butterknife.BindView;

/**
 * @author liuzq
 * dec: 搜索结果
 */
public class SearchResultActivity extends CommActivity {

    @BindView(R.id.tv_show)
    TextView tv_show;
    Bundle bundle;

    @Override
    public void initData(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        if (bundle != null){
            tv_show.setText(bundle.getString("data"));
        }
    }
}
