package com.liuzq.flowlayout.inter;

import android.view.View;

import com.liuzq.flowlayout.FlowLayout;

/**
 * author：liuzq
 * create date：2019/12/15
 * description：自定义flowlayout 流式布局 tag 点击 listener
 */
public interface OnTagClickListener {
    boolean onTagClick(View view, int position, FlowLayout parent);
}
