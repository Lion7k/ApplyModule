package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.basemodule.ui.fragment.TabFragment;
import com.liuzq.bottombar.BottomBarItem;
import com.liuzq.bottombar.BottomBarLayout;
import com.liuzq.commlibrary.base.BaseFragmentAdapter;
import com.liuzq.commlibrary.utils.LogUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FragmentActivity extends CommActivity implements BottomBarLayout.OnItemSelectedListener, CommonTitleBar.OnTitleBarListener {

    @BindView(R.id.vp_content)
    ViewPager mVpContent;
    @BindView(R.id.bbl)
    BottomBarLayout mBottomBarLayout;

    private List<TabFragment> mFragmentList = new ArrayList<>();
    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_fragment;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initListener() {
        super.initListener();
        title_bar.setListener(this);
        mBottomBarLayout.setOnItemSelectedListener(this);
    }

    @Override
    public void doBusiness() {
        initData();
        initAdapter();
    }

    private void initData() {
        TabFragment homeFragment = new TabFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(TabFragment.CONTENT, "首页");
        homeFragment.setArguments(bundle1);
        mFragmentList.add(homeFragment);

        TabFragment videoFragment = new TabFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(TabFragment.CONTENT, "视频");
        videoFragment.setArguments(bundle2);
        mFragmentList.add(videoFragment);

        TabFragment microFragment = new TabFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(TabFragment.CONTENT, "微头条");
        microFragment.setArguments(bundle3);
        mFragmentList.add(microFragment);

        TabFragment meFragment = new TabFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString(TabFragment.CONTENT, "我的");
        meFragment.setArguments(bundle4);
        mFragmentList.add(meFragment);
    }

    private void initAdapter(){
        mVpContent.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mBottomBarLayout.setViewPager(mVpContent);

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字

        mBottomBarLayout.setCurrentItem(0);
    }

    @Override
    public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition,final int currentPosition) {
        LogUtils.e("FragmentActivity", "position: " + currentPosition);
        if (currentPosition == 0) {
            //如果是第一个，即首页
            if (previousPosition == currentPosition) {
                //如果是在原来位置上点击,更换首页图标并播放旋转动画
                if (mRotateAnimation != null && !mRotateAnimation.hasEnded()){
                    //如果当前动画正在执行
                    return;
                }

                bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标

                //播放旋转动画
                if (mRotateAnimation == null) {
                    mRotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnimation.setDuration(800);
                    mRotateAnimation.setRepeatCount(-1);
                }
                ImageView bottomImageView = bottomBarItem.getImageView();
                bottomImageView.setAnimation(mRotateAnimation);
                bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画

                //模拟数据刷新完毕
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean tabNotChanged = mBottomBarLayout.getCurrentItem() == currentPosition; //是否还停留在当前页签
                        bottomBarItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换成首页原来选中图标
                        cancelTabLoading(bottomBarItem);
                    }
                }, 3000);
                return;
            }
        }

        //如果点击了其他条目
        BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
        bottomItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换为原来的图标

        cancelTabLoading(bottomItem);//停止旋转动画
    }

    /**
     * 停止首页页签的旋转动画
     */
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null) {
            animation.cancel();
        }
    }

    @Override
    public void onClicked(View v, int action, String extra) {
        if (action == CommonTitleBar.ACTION_RIGHT_BUTTON || action == CommonTitleBar.ACTION_RIGHT_TEXT) {
            new XPopup.Builder(this)
                    .hasShadowBg(false)
//                        .popupAnimation(PopupAnimation.NoAnimation) //NoAnimation表示禁用动画
//                        .isCenterHorizontal(true) //是否与目标水平居中对齐
//                        .offsetY(-10)
//                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
                    .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .asAttachList(new String[]{"clearUnread", "clearNotify", "clearMsg"},
                            new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    switch (position) {
                                        case 0:
                                            mBottomBarLayout.setUnread(0, 0);
                                            mBottomBarLayout.setUnread(1, 0);
                                            break;
                                        case 1:
                                            mBottomBarLayout.hideNotify(2);
                                            break;
                                        case 2:
                                            mBottomBarLayout.hideMsg(3);
                                            break;
                                    }
                                }
                            })
                    .show();
        }
    }
}
