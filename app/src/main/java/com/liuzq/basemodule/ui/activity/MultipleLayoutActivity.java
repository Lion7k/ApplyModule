package com.liuzq.basemodule.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.liuzq.basemodule.R;
import com.liuzq.commlibrary.base.BaseActivity;
import com.liuzq.statusview.StatusView;
import com.liuzq.statusview.StatusViewBuilder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleLayoutActivity extends BaseActivity implements CommonTitleBar.OnTitleBarListener {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.title_bar)
    CommonTitleBar title_bar;

    private StatusView statusView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MultipleLayoutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_multiple_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initListener() {
        super.initListener();
        title_bar.setListener(this);
    }

    @Override
    public void doBusiness() {
        statusView = StatusView.init(this, R.id.ll);

        statusView.config(new StatusViewBuilder.Builder()
                .setOnErrorRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        statusView.showLoadingView();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                statusView.showErrorView();
                            }
                        }, 1500);
                    }
                })
                .setOnEmptyRetryClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusView.showLoadingView();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                statusView.showEmptyView();
                            }
                        }, 1500);
                    }
                }).build());
        statusView.showLoadingView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statusView.showContentView();
            }
        }, 1500);
    }

    @Override
    public void showError(String msg) {

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
                    .asAttachList(new String[]{"Loading", "Error", "Empty", "Content"},
                            new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    switch (position) {
                                        case 0:
                                            statusView.showLoadingView();
                                            break;
                                        case 1:
                                            statusView.showErrorView();
                                            break;
                                        case 2:
                                            statusView.showEmptyView();
                                            break;
                                        case 3:
                                            statusView.showContentView();
                                            break;
                                    }
                                }
                            })
                    .show();
        }
    }
}
