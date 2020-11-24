package com.liuzq.commlibrary.utils.keyboard;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Time:2019/12/24
 * Author:liuzq
 * Description: 实时监听软键盘显示或者隐藏
 * 不依赖于windowSoftInputMode属性与Activity是否全屏
 * 兼容activity/fragment
 */
public class SoftKeyBoardManager implements ViewTreeObserver.OnGlobalLayoutListener{

    private final List<OnSoftKeyBoardChangeListener> listeners = new LinkedList<OnSoftKeyBoardChangeListener>();

    private View activityRootView; // activity的根视图
    private int rootViewVisibleHeight; // 记录根视图显示的高度
    private boolean isSoftKeyboardOpened; // 键盘是否打开

    public SoftKeyBoardManager(Activity activity) {
        this(activity, false);
    }

    private SoftKeyBoardManager(Activity activity, boolean isSoftKeyboardOpened) {
        this(activity.getWindow().getDecorView(), isSoftKeyboardOpened);
    }

    public SoftKeyBoardManager(View activityRootView) {
        this(activityRootView, false);
    }

    /**
     *
     * @param activityRootView 第一个参数View即是你的根View，activity可以直接用getWindow().getDecorView();获取，Fragment传入顶层View即可
     * @param isSoftKeyboardOpened
     */
    private SoftKeyBoardManager(View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        this.activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (activityRootView == null) return;
        // 获取当前根视图在屏幕上显示的大小
        Rect rect = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(rect);
        int visibleHeight = rect.height();
        if(rootViewVisibleHeight == 0){
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        // 根视图显示高度没有变化，可以看做软键盘显示/隐藏状态没有变化
        if(rootViewVisibleHeight == visibleHeight){
            return;
        }

        // 根视图显示高度变小超过200，可以看做软键盘显示了
        if(!isSoftKeyboardOpened && rootViewVisibleHeight - visibleHeight > 200){
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(rootViewVisibleHeight - visibleHeight);
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        // 根视图显示高度变大超过了200，可以看做软键盘隐藏了
        if(isSoftKeyboardOpened && visibleHeight - rootViewVisibleHeight > 200){
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed(visibleHeight - rootViewVisibleHeight);
            rootViewVisibleHeight = visibleHeight;
            return;
        }
    }

    public interface OnSoftKeyBoardChangeListener{
        void onSoftKeyboardOpened(int height);
        void onSoftKeyboardClosed(int height);
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    public void addSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.rootViewVisibleHeight = keyboardHeightInPx;

        for (OnSoftKeyBoardChangeListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed(int keyboardHeightInPx) {
        this.rootViewVisibleHeight = keyboardHeightInPx;

        for (OnSoftKeyBoardChangeListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed(keyboardHeightInPx);
            }
        }
    }

    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        setListener(activity.getWindow().getDecorView(), onSoftKeyBoardChangeListener);
    }

    public static void setListener(View view, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        SoftKeyBoardManager softKeyBoardListener = new SoftKeyBoardManager(view);
        softKeyBoardListener.addSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }
}
