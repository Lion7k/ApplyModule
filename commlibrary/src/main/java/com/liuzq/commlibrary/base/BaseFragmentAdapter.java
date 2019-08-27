package com.liuzq.commlibrary.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> fragments;

    public BaseFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = Arrays.asList(fragments);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
