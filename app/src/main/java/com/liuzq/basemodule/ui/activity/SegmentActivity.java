package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.tablayout.SegmentTabLayout;

import butterknife.BindView;

public class SegmentActivity extends CommActivity {

    @BindView(R.id.stl)
    SegmentTabLayout sTl;

    private String[] mTitles = {"首页", "消息"};
    private String[] mTitles_2 = {"首页", "消息", "联系人"};
    private String[] mTitles_3 = {"首页", "消息", "联系人", "更多"};

    @Override
    public void initData(Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_segment;
    }

    @Override
    public void initPresenter() {
        sTl.setTabData(mTitles);
    }

    @Override
    public void doBusiness() {

    }
}