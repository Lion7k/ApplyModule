package com.liuzq.commlibrary.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class RVHelper {

    /***
     * 封装recycler基本属性
     * @param context
     * @param recyclerView
     * @param orientation listivew列表传1或者LinearLayoutManager.VERTICAL，grid列表传固定值
     */
    public static RecyclerView.LayoutManager initRecyclerViewStyle(Context context, RecyclerView recyclerView, int orientation) {
        RecyclerView.LayoutManager layoutManager;
        /**提高性能*/
        recyclerView.setHasFixedSize(true);

        if (LinearLayoutManager.HORIZONTAL == orientation || LinearLayoutManager.VERTICAL == orientation) {
            /**listview*/
            layoutManager = new LinearLayoutManager(context);
            ((LinearLayoutManager) layoutManager).setOrientation(orientation);
        } else {
            /**gridview**/
            layoutManager = new GridLayoutManager(context, orientation);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        return layoutManager;
    }
}
