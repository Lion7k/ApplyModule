package com.liuzq.basemodule.ui.fragment.flow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommFragment;
import com.liuzq.flowlayout.FlowLayout;
import com.liuzq.flowlayout.TagAdapter;
import com.liuzq.flowlayout.TagFlowLayout;

import butterknife.BindView;
/**
 * author：liuzq
 * create date：2019/12/15
 * description：自定义flowlayout 流式布局 最大选中数量3
 */
public class LimitSelectedFragment extends CommFragment {

    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView","Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView"};

    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_evnt_test;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }

        });
    }
}
