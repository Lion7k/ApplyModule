package com.liuzq.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.flowlayout.inter.OnDataChangedListener;
import com.liuzq.flowlayout.inter.OnSelectListener;
import com.liuzq.flowlayout.inter.OnTagClickListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.liuzq.commlibrary.utils.DisplayUtils.dip2px;

/**
 * author：liuzq
 * create date：2019/12/15
 * description：自定义tagflowlayout
 */
public class TagFlowLayout extends FlowLayout implements OnDataChangedListener {
    private static final String TAG = "TagFlowLayout";

    private TagAdapter mTagAdapter;
    private int mSelectedMax = -1;//-1为不限制数量

    private Set<Integer> mSelectedView = new HashSet<Integer>();

    private OnSelectListener mOnSelectListener;
    private OnTagClickListener mOnTagClickListener;
    private boolean isCancelChecked = true;  // 是否可以取消

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
        ta.recycle();
    }

    public TagAdapter getAdapter() {
        return this.mTagAdapter;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.mOnTagClickListener = onTagClickListener;
    }

    public void isCancelChecked(boolean isCancelChecked) {
        this.isCancelChecked = isCancelChecked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == View.GONE) continue;
            if (tagView.getTagView().getVisibility() == View.GONE) {
                tagView.setVisibility(View.GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAdapter(TagAdapter adapter) {
        this.mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        mSelectedView.clear();
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = this.mTagAdapter;
        TagView tagViewContainer = null;
        HashSet preCheckedList = mTagAdapter.getPreCheckedList();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tagView = adapter.getView(this, i, adapter.getItem(i));

            tagViewContainer = new TagView(getContext());
            tagView.setDuplicateParentStateEnabled(true);
            if (tagView.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            } else {
                MarginLayoutParams lp = new MarginLayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5));
                tagViewContainer.setLayoutParams(lp);
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tagView.setLayoutParams(lp);
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
            // 自动匹配 预选中tag
            if (preCheckedList.contains(i)) {
                setChildChecked(i, tagViewContainer);
            }
            // 自动选中与之匹配的选项
            if (mTagAdapter.setSelected(i, adapter.getItem(i))) {
                setChildChecked(i, tagViewContainer);
            }
            tagView.setClickable(false);
            final TagView finalTagViewContainer = tagViewContainer;
            final int position = i;
            tagViewContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTagClickListener != null
                            && (isCancelChecked || !finalTagViewContainer.isChecked())) {
                        mOnTagClickListener.onTagClick(finalTagViewContainer, position, TagFlowLayout.this);
                    }
                    doSelect(finalTagViewContainer, position);
                }
            });
        }
        mSelectedView.addAll(preCheckedList);
    }

    private void doSelect(TagView child, int position) {
        // 假如没有选中
        if (!child.isChecked()) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size() == 1) {
                Iterator<Integer> iterator = mSelectedView.iterator();
                Integer preIndex = iterator.next();
                TagView pre = (TagView) getChildAt(preIndex);
                setChildUnChecked(preIndex, pre);
                setChildChecked(position, child);

                mSelectedView.remove(preIndex);
                mSelectedView.add(position);
            } else {
                if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax) {
                    return;
                }
                setChildChecked(position, child);
                mSelectedView.add(position);
            }
        } else {
            // 当不可取消时，不调用此方法
            if (isCancelChecked) {
                setChildUnChecked(position, child);
                mSelectedView.remove(position);
            }
        }
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(new HashSet<Integer>(mSelectedView));
        }
    }

    // 设置最大选中数量
    public void setMaxSelectCount(int count) {
        if (mSelectedView.size() > count) {
            LogUtils.w(TAG, "you has already select more than " + count + " views , so it will be clear .");
        }
        mSelectedMax = count;
    }

    private static final String KEY_CHOOSE_POS = "key_choose_pos";
    private static final String KEY_DEFAULT = "key_default";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

        String selectPos = "";
        if (mSelectedView.size() > 0) {
            for (int key : mSelectedView) {
                selectPos += key + "|";
            }
            selectPos = selectPos.substring(0, selectPos.length() - 1);
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtils.isEmpty(mSelectPos)) {
                String[] split = mSelectPos.split("\\|");
                for (String pos : split) {
                    int index = Integer.parseInt(pos);
                    mSelectedView.add(index);

                    TagView tagView = (TagView) getChildAt(index);
                    if (tagView != null) {
                        setChildChecked(index, tagView);
                    }
                }

            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void setChildChecked(int position, TagView view) {
        view.setChecked(true);
        mTagAdapter.onSelected(position, view.getTagView());
    }

    private void setChildUnChecked(int position, TagView view) {
        view.setChecked(false);
        mTagAdapter.unSelected(position, view.getTagView());
    }

    @Override
    public void onChanged() {
        mSelectedView.clear();
        changeAdapter();
    }
}
