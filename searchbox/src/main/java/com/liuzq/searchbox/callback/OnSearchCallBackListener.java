package com.liuzq.searchbox.callback;

import com.liuzq.searchbox.data.BaseData;

import java.util.List;

public interface OnSearchCallBackListener<T> {

    /**
     * @param str  搜索关键字
     */
    void search(String str);
    /**
     * 后退
     */
    void back();
    /**
     * 清除历史搜索记录
     */
    void clearOldData();

    /**
     * 保存搜索记录
     */
    void saveOldData(List<BaseData<T>> allOldDataList);
}
