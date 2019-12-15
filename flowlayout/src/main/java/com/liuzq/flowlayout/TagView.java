package com.liuzq.flowlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * author：liuzq
 * create date：2019/12/15
 * description：标签 tag
 */
public class TagView extends FrameLayout implements Checkable {

    private boolean isChecked;
    private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

    public TagView(@NonNull Context context) {
        super(context);
    }

    public View getTagView ()
    {
        return getChildAt(0);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace)
    {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
        {
            mergeDrawableStates(states, CHECK_STATE);
        }
        return states;
    }

    /**
     * Change the checked state of the view
     *
     * @param checked The new checked state
     */
    @Override
    public void setChecked(boolean checked) {
        if (this.isChecked != checked)
        {
            this.isChecked = checked;
            refreshDrawableState();
        }
    }

    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked() {
        return this.isChecked;
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }
}
