package com.liuzq.basemodule.ui.fragment.flow;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommFragment;
import com.liuzq.basemodule.ui.adapter.CommonAdapter;
import com.liuzq.basemodule.ui.adapter.ViewHolder;
import com.liuzq.flowlayout.FlowLayout;
import com.liuzq.flowlayout.TagAdapter;
import com.liuzq.flowlayout.TagFlowLayout;
import com.liuzq.flowlayout.inter.OnSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

public class ListViewTestFragment extends CommFragment
{

    private List<List<String>> mDatas = new ArrayList<List<String>>();
    @BindView(R.id.id_listview)
    ListView mListView;

    @Override
    public void initData(Bundle bundle) {
        initDatas();
    }

    private void initDatas()
    {
        for (int i = 'A'; i < 'z'; i++)
        {
            List<String> itemData = new ArrayList<String>(3);
            for (int j = 0; j < 3; j++)
            {
                itemData.add((char) i + "");
            }
            mDatas.add(itemData);
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_listview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        mListView.setAdapter(new CommonAdapter<List<String>>(getActivity(), R.layout.item_for_listview, mDatas)
        {
            Map<Integer, Set<Integer>> selectedMap = new HashMap<Integer, Set<Integer>>();


            @Override
            public void convert(final ViewHolder viewHolder, List<String> strings)
            {
                TagFlowLayout tagFlowLayout = viewHolder.getView(R.id.id_tagflowlayout);

                TagAdapter<String> tagAdapter = new TagAdapter<String>(strings)
                {
                    @Override
                    public View getView(FlowLayout parent, int position, String o)
                    {
                        //can use viewholder
                        TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                parent, false);
                        tv.setText(o);
                        return tv;
                    }
                };
                tagFlowLayout.setAdapter(tagAdapter);
                //重置状态
                tagAdapter.setSelectedList(selectedMap.get(viewHolder.getItemPosition()));

                tagFlowLayout.setOnSelectListener(new OnSelectListener()
                {
                    @Override
                    public void onSelected(Set<Integer> selectPosSet)
                    {
                        selectedMap.put(viewHolder.getItemPosition(), selectPosSet);
                    }
                });
            }
        });

    }
}
