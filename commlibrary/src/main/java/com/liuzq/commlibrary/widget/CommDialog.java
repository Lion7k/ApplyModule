package com.liuzq.commlibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.liuzq.commlibrary.R;

/**
 * 使用方法：
 * final CommDialog selfDialog = new CommDialog(MainActivity.this);
 * <p>
 * selfDialog.setCustom(你的布局);
 * <p>
 * CommDialog.show();
 * <p>
 * Created by liuzq on 2020/11/09.
 * <p>
 */
public class CommDialog extends Dialog {

    private View mContentView;

    private RelativeLayout contentRoot;

    public CommDialog(Context context) {
        super(context, R.style.dialog_finger);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_comm_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

        setCancelable(false);
    }


    private void initView() {
        contentRoot = findViewById(R.id.content_view_root);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

        if (mContentView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            contentRoot.addView(mContentView, params);

        }
    }

    /**
     * 初始化界面控件
     */

    private void initEvent() {

    }


    public View getContentView() {
        return mContentView;
    }

    public void setCustom(View mContentView) {
        this.mContentView = mContentView;
    }

}
