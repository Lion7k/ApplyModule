package com.liuzq.basemodule.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T>  extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public T getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(this.mContext, convertView, parent, this.layoutId, position);
        this.convert(holder, this.getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder var1, T var2);
}
