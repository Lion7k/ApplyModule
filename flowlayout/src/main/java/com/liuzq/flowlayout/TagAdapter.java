package com.liuzq.flowlayout;

import android.view.View;

import com.liuzq.flowlayout.inter.OnDataChangedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author：liuzq
 * create date：2019/12/15
 * description：自定义flowlayout 流式布局 适配器
 */
public abstract class TagAdapter<T> {
    private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;

    @Deprecated
    private HashSet<Integer> mCheckedPosList = new HashSet<>();

    @Deprecated
    public TagAdapter(T[] datas) {
        this.mTagDatas = new ArrayList<>(Arrays.asList(datas));
    }

    public TagAdapter(List<T> datas) {
        this.mTagDatas = datas;
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.mOnDataChangedListener = listener;
    }

    @Deprecated
    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    @Deprecated
    public void setSelectedList(Set<Integer> set) {
        this.mCheckedPosList.clear();
        if (set != null) {
            this.mCheckedPosList.addAll(set);
        }
        notifyDataChanged();
    }

    @Deprecated
    HashSet<Integer> getPreCheckedList() {
        return this.mCheckedPosList;
    }

    public int getCount() {
        return this.mTagDatas == null ? 0 : this.mTagDatas.size();
    }

    public T getItem(int position) {
        return this.mTagDatas.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public void notifyDataChanged() {
        if (this.mOnDataChangedListener != null) {
            this.mOnDataChangedListener.onChanged();
        }
    }

    public void onSelected(int position, View view) {}

    public void unSelected(int position, View view) {}

    public boolean setSelected(int position, T t) {
        return false;
    }
}
