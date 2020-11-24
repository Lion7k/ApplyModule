package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.basemodule.ui.fragment.flow.EventTestFragment;
import com.liuzq.basemodule.ui.fragment.flow.GravityFragment;
import com.liuzq.basemodule.ui.fragment.flow.LimitSelectedFragment;
import com.liuzq.basemodule.ui.fragment.flow.ListViewTestFragment;
import com.liuzq.basemodule.ui.fragment.flow.ScrollViewTestFragment;
import com.liuzq.basemodule.ui.fragment.flow.SimpleFragment;
import com.liuzq.basemodule.ui.fragment.flow.SingleChooseFragment;
import com.liuzq.commlibrary.base.BasePagerAdapter;

import butterknife.BindView;

/**
 * @author liuzq
 * dec: 流式布局单选、多选
 */
public class FlowLayoutActivity extends CommActivity {
    @BindView(R.id.id_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;

    private String[] mTabTitles = new String[]
            {"Muli Selected", "Limit 3",
                    "Event Test", "ScrollView Test", "Single Choose", "Gravity", "ListView Sample"};

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_flow_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(), mViewPager, new Fragment[
                ]{new SimpleFragment(), new LimitSelectedFragment(), new EventTestFragment(),
                new ScrollViewTestFragment(), new SingleChooseFragment(),new GravityFragment(),
                new ListViewTestFragment()});
        adapter.addTabTitles(mTabTitles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
