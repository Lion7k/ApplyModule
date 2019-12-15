package com.liuzq.commlibrary.base;

/**
 * Created by liuzq on 2017/7/28.
 * page切换额外功能接口
 */

public abstract class OnExtraPageChangeListener {

    public abstract void onExtraPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    public abstract void onExtraPageSelected(int position);

    public abstract void onExtraPageScrollStateChanged(int state);
}
