package com.liuzq.commlibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.liuzq.commlibrary.R;


/**
 * Created by ${liuzq} on 2018/10/12.
 */

public class PageLayout extends FrameLayout {

    enum State {
        EMPTY_TYPE,
        LOADING_TYPE,
        ERROR_TYPE,
        CONTENT_TYPE,
        CUSTOM_TYPE
    }

    private View mLoading;
    private View mEmpty;
    private View mError;
    private View mContent;
    private View mCustom;
    private Context mContext;
    private BlinkLayout mBlinkLayout;
    private State mCurrentState;

    public PageLayout(@NonNull Context context) {
        super(context);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void showView(final State state) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            changeView(state);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    changeView(state);
                }
            });
        }
    }

    private void changeView(State state) {
        mCurrentState = state;
        setViewGone();
        switch (state) {
            case LOADING_TYPE:
                mLoading.setVisibility(VISIBLE);
                break;
            case EMPTY_TYPE:
                mEmpty.setVisibility(VISIBLE);
                break;
            case ERROR_TYPE:
                mError.setVisibility(VISIBLE);
                break;
            case CUSTOM_TYPE:
                mCustom.setVisibility(VISIBLE);
                break;
            case CONTENT_TYPE:
                mContent.setVisibility(VISIBLE);
                break;
        }
    }

    private void setViewGone() {
        mLoading.setVisibility(GONE);
        mEmpty.setVisibility(GONE);
        mError.setVisibility(GONE);
        mContent.setVisibility(GONE);
        if (mCustom != null) {
            mCustom.setVisibility(GONE);
        }
    }

    public void showLoading() {
        showView(State.LOADING_TYPE);
        if (mBlinkLayout != null) {
            mBlinkLayout.startShimmerAnimation();
        }
    }

    public void showError() {
        showView(State.ERROR_TYPE);
    }

    public void showEmpty() {
        showView(State.EMPTY_TYPE);
    }

    public void hide() {
        showView(State.CONTENT_TYPE);
        if (mBlinkLayout != null) {
            mBlinkLayout.stopShimmerAnimation();
        }
    }

    public void showCustom() {
        showView(State.CUSTOM_TYPE);
    }

    public static class Builder {
        private PageLayout mPageLayout;
        private LayoutInflater mInflater;
        private Context mContext;
        private TextView mTvEmpty;
        private TextView mTvError;
        private TextView mTvErrorRetry;
        private TextView mTvLoading;
        private TextView mTvLoadingBlink;
        private BlinkLayout mBlinkLayout;
        private OnRetryClickListener mOnRetryClickListener;

        public Builder(Context context) {
            mContext = context;
            this.mPageLayout = new PageLayout(mContext);
            this.mInflater = LayoutInflater.from(mContext);
        }

        private void initDefault() {
            if (mPageLayout.mEmpty == null) {
                setDefaultEmpty();
            }

            if (mPageLayout.mError == null) {
                setDefaultError();
            }
            if (mPageLayout.mLoading == null) {
                setDefaultLoading();
            }
        }

        private void setDefaultEmpty() {
            mPageLayout.mEmpty = mInflater.inflate(R.layout.layout_empty, mPageLayout, false);
            mTvEmpty = mPageLayout.mEmpty.findViewById(R.id.tv_page_empty);
            mPageLayout.mEmpty.setVisibility(GONE);
            mPageLayout.addView(mPageLayout.mEmpty);
        }

        private void setDefaultError() {
            mPageLayout.mError = mInflater.inflate(R.layout.layout_error, mPageLayout, false);
            mTvError = mPageLayout.mError.findViewById(R.id.tv_page_error);
            mTvErrorRetry = mPageLayout.mError.findViewById(R.id.tv_page_error_retry);
            mTvErrorRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRetryClickListener != null)
                        mOnRetryClickListener.onRetry();
                }
            });
            mPageLayout.mError.setVisibility(GONE);
            mPageLayout.addView(mPageLayout.mError);
        }

        private void setDefaultLoading() {
            mPageLayout.mLoading = mInflater.inflate(R.layout.layout_loading, mPageLayout, false);
            mBlinkLayout = mPageLayout.mLoading.findViewById(R.id.blinklayout);
            mPageLayout.mBlinkLayout = mBlinkLayout;
            mTvLoadingBlink = mPageLayout.mLoading.findViewById(R.id.tv_page_loading_blink);
            mPageLayout.mLoading.setVisibility(GONE);
            mPageLayout.addView(mPageLayout.mLoading);
        }

        /**
         * 设置loading布局
         */
        public Builder setLoading(@LayoutRes int loadingId, @IdRes int loadingTvId) {
            mPageLayout.mLoading = mInflater.inflate(loadingId, mPageLayout, false);
            mTvLoading = mPageLayout.mLoading.findViewById(loadingTvId);
            mPageLayout.addView(mPageLayout.mLoading);
            return this;
        }

        /**
         * 自定义错误布局
         * 默认样式，传入错误文案ID，及点击回调
         */
        public Builder setError(@LayoutRes int errorId, @IdRes int errorClickId, final OnRetryClickListener onRetryClickListener) {
            mPageLayout.mError = mInflater.inflate(errorId, mPageLayout, false);
            mPageLayout.addView(mPageLayout.mError);
            mTvError = mPageLayout.mError.findViewById(errorClickId);
            mTvError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRetryClickListener != null)
                        onRetryClickListener.onRetry();
                }
            });
            return this;
        }


        /**
         * 自定义错误布局
         * 设置前需手动初始化好View中各个事件
         */
        public Builder setError(View errorView) {
            mPageLayout.mError = errorView;
            mPageLayout.addView(errorView);
            return this;
        }

        /**
         * 自定义空布局
         */
        public Builder setEmpty(@LayoutRes int empty, @IdRes int emptyTvId) {
            mPageLayout.mEmpty = mInflater.inflate(empty, mPageLayout, false);
            mTvEmpty = mPageLayout.mEmpty.findViewById(emptyTvId);
            mPageLayout.addView(mPageLayout.mEmpty);
            return this;
        }

        /**
         * 自定义布局
         */
        public Builder setCustomView(View view) {
            mPageLayout.mCustom = view;
            mPageLayout.addView(view);
            return this;
        }

        /**
         * 设置加载文案
         */
        public Builder setLoadingText(String text) {
            mTvLoading.setText(text);
            return this;
        }

        /**
         * 设置默认闪烁加载文案
         */
        public Builder setDefaultLoadingBlinkText(String text) {
            mTvLoadingBlink.setText(text);
            return this;
        }

        /**
         * 设置加载文字颜色
         */
        public Builder setLoadingTextColor(@ColorRes int color) {
            mTvLoading.setTextColor(mContext.getResources().getColor(color));
            return this;
        }

        /**
         * 设置默认加载闪烁颜色
         */
        public Builder setDefaultLoadingBlinkColor(@ColorRes int color) {
            mBlinkLayout.setShimmerColor(mContext.getResources().getColor(color));
            return this;
        }

        /**
         * 设置默认空布局文案
         */
        public Builder setDefaultEmptyText(String text) {
            mTvEmpty.setText(text);
            return this;
        }

        /**
         * 设置默认空布局文案颜色
         */
        public Builder setDefaultEmptyTextColor(@ColorRes int color) {
            mTvEmpty.setTextColor(mContext.getResources().getColor(color));
            return this;
        }

        /**
         * 设置默认错误布局文案
         */
        public Builder setDefaultErrorText(String text) {
            mTvError.setText(text);
            return this;
        }

        /**
         * 设置默认错误布局文案颜色
         */
        public Builder setDefaultErrorTextColor(@ColorRes int color) {
            mTvError.setTextColor(mContext.getResources().getColor(color));
            return this;
        }

        /**
         * 设置默认错误布局重试文案
         */
        public Builder setDefaultErrorRetryText(String text) {
            mTvErrorRetry.setText(text);
            return this;
        }

        /**
         * 设置默认错误布局重试文案颜色
         */
        public Builder setDefaultErrorRetryTextColor(@ColorRes int color) {
            mTvErrorRetry.setTextColor(mContext.getResources().getColor(color));
            return this;
        }

        /**
         * 设置空布局提醒图片
         */
        public Builder setEmptyDrawable(@DrawableRes int resId) {
            setTopDrawables(mTvError, resId);
            return this;
        }

        /**
         * 设置错误布局提醒图片
         */
        public Builder setErrorDrawable(int resId) {
            setTopDrawables(mTvError, resId);
            return this;
        }

        /**
         * 设置布局top drawable
         */
        private void setTopDrawables(TextView textView, @DrawableRes int resId) {
            if (resId == 0) {
                textView.setCompoundDrawables(null, null, null, null);
            }
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            textView.setCompoundDrawables(null, drawable, null, null);
            textView.setCompoundDrawablePadding(20);
        }

        /**
         * set target view for root
         */
        public Builder initPage(Object targetView) {
            ViewGroup content = null;
            if (targetView instanceof Activity) {  //如果是Activity，获取到android.R.content
                mContext = (Context) targetView;
                content = ((Activity) targetView).findViewById(android.R.id.content);
            } else if (targetView instanceof Fragment) {   //如果是Fragment获取到parent
                mContext = ((Fragment) targetView).getActivity();
                content = (ViewGroup) ((Fragment) targetView).getView().getParent();
            } else if (targetView instanceof View) {  //如果是View，也取到parent
                mContext = ((View) targetView).getContext();
                content = (ViewGroup) ((View) targetView).getParent();
            }
            if (content != null) {
                int childCount = content.getChildCount();
                int index = 0;
                View oldContent;
                if (targetView instanceof View) { //如果是某个线性布局或者相对布局时，遍历它的孩子，找到对应的索引，记录下来
                    oldContent = (View) targetView;
                    for (int i = 0; i < childCount; i++) {
                        if (content.getChildAt(i) == oldContent) {
                            index = i;
                            break;
                        }
                    }
                } else {    //如果是Activity或者Fragment时，取到索引为第一个的View
                    oldContent = content.getChildAt(0);
                }

                mPageLayout.mContent = oldContent;   //给PageLayout设置contentView
                mPageLayout.removeAllViews();
                content.removeView(oldContent);    //将本身content移除，并且把PageLayout添加到DecorView中去
                ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
                content.addView(mPageLayout, index, lp);
                mPageLayout.addView(oldContent);
                initDefault();  //设置默认状态布局
            }
            return this;
        }

        public Builder setOnRetryListener(OnRetryClickListener onRetryListener) {
            this.mOnRetryClickListener = onRetryListener;
            return this;
        }

        public PageLayout create() {
            return mPageLayout;
        }
    }

    public interface OnRetryClickListener {
        void onRetry();
    }
}
