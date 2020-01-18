package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.commlibrary.utils.ActivityUtils;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.searchbox.cache.HistoryCache;
import com.liuzq.searchbox.callback.OnSearchCallBackListener;
import com.liuzq.searchbox.data.BaseData;
import com.liuzq.searchbox.widget.SearchLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends CommActivity {

    @BindView(R.id.searchlayout)
    SearchLayout<String> searchLayout;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        List<BaseData<String>> skills = HistoryCache.toArray(getApplicationContext(), "record", "record_key");
        List<BaseData<String>> skillHots = new ArrayList<>();
        BaseData<String> shareHotData = new BaseData<>();
        shareHotData.setSearch("C++");
        skillHots.add(shareHotData);

        shareHotData = new BaseData<>();
        shareHotData.setSearch("C");
        skillHots.add(shareHotData);

        shareHotData = new BaseData<>();
        shareHotData.setSearch("PHP");
        skillHots.add(shareHotData);

        shareHotData = new BaseData<>();
        shareHotData.setSearch("React");
        skillHots.add(shareHotData);

        searchLayout.initData(skills, skillHots, new OnSearchCallBackListener<String>() {
            @Override
            public void search(String str) {
                //进行或联网搜索
                LogUtils.e("点击", str);
                Bundle bundle = new Bundle();
                bundle.putString("data", str);
                ActivityUtils.skipActivity(SearchActivity.this, SearchResultActivity.class, bundle);
            }

            @Override
            public void back() {
                finish();
            }

            @Override
            public void clearOldData() {
                //清除历史搜索记录  更新记录原始数据
                HistoryCache.clear(getApplicationContext(), "record");
                LogUtils.e("点击","清除数据");
            }

            @Override
            public void saveOldData(List<BaseData<String>> allOldDataList) {
                //保存所有的搜索记录
                HistoryCache.saveHistory(getApplicationContext(),
                        "record", "record_key",
                        HistoryCache.toJsonArray(allOldDataList));
                LogUtils.e("点击","保存数据");
            }
        }, 1);

    }
}
