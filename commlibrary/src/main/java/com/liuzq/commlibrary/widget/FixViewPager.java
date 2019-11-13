package com.liuzq.commlibrary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liuzq on 2018/3/5.
 * viewpager 不可滑动
 */

public class FixViewPager extends ViewPager {

    private boolean mTouchPager;

    public FixViewPager(Context context) {
        super(context);
    }

    public FixViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**去除页面切换时的滑动翻页效果*/
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**去除页面切换动画效果*/
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    /**是否可以手势滑动切换页面*/
    public void setmTouchPager(boolean mTouchPager) {
        this.mTouchPager = mTouchPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mTouchPager)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mTouchPager)
            return super.onTouchEvent(arg0);
        else
            return false;
    }
}
