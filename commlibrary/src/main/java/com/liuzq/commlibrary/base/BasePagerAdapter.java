package com.liuzq.commlibrary.base;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuzq on 2017/7/28.
 *
 * @ClassName: BasePagerAdapter
 * @Description: TODO(ViewPage+Fragment 切换适配器)
 */

public class BasePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<? extends Fragment> fragments; // 每个Fragment对应一个Page
    private FragmentManager fragmentManager;
    private ViewPager viewPager; // viewPager对象
    private int currentPageIndex = 0; // 当前page索引（切换之前）
    private List<String> mTabTitles;

    private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager切换页面时的额外功能添加接口

    public BasePagerAdapter(FragmentManager fragmentManager, ViewPager viewPager, Fragment[] fragments) {
        this.fragments = new ArrayList<>(Arrays.asList(fragments));
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.viewPager.addOnPageChangeListener(this);
    }

    public BasePagerAdapter(FragmentManager fragmentManager, ViewPager viewPager, List<? extends Fragment> fragments) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.viewPager.addOnPageChangeListener(this);
    }


    public void addTabTitles(String[]  tabTitles) {
        this.mTabTitles = new ArrayList<>(Arrays.asList(tabTitles));
    }

    public void addTabTitles(List<String> tabTitles) {
        this.mTabTitles = tabTitles;
    }

    /**
     * 获取页面切换额外功能监听器
     *
     * @return onExtraPageChangeListener
     */
    public OnExtraPageChangeListener getOnExtraPageChangeListenerImpl() {
        return onExtraPageChangeListener;
    }

    /**
     * 设置页面切换额外功能监听器
     *
     * @param onExtraPageChangeListener
     */
    public void setOnExtraPageChangeListener(OnExtraPageChangeListener onExtraPageChangeListener) {
        this.onExtraPageChangeListener = onExtraPageChangeListener;
    }

    /**
     * 当前page索引（切换之前）
     *
     * @return
     */
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        if (!fragment.isAdded()) { // 如果fragment还没有added
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            /**
             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
             * 会在进程的主线程中，用异步的方式来执行。
             * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
             */
            fragmentManager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            container.addView(fragment.getView()); // 为viewpager增加布局
        }
        return fragment.getView();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        fragments.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
        if (fragments.get(position).isAdded()) {
            fragments.get(position).onResume(); // 调用切换后Fargment的onResume()
        }
        currentPageIndex = position;

        if (onExtraPageChangeListener != null) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onExtraPageChangeListener != null) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageScrollStateChanged(state);
        }
    }
}
