package com.liuzq.commlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * toast管理器, 确保只有一个Toast对象
 * Created by liuzq on 2018/10/17.
 */
@SuppressLint("ShowToast")
public class ToastUtils {
    private static Toast it;
    private static Context context;

    private ToastUtils() {
    }

    /**
     * 在程序初始化的时候调用, 只需调用一次
     *
     * @param _context
     */
    public static void init(Context _context) {
        context = _context;
        View v = Toast.makeText(_context, "", Toast.LENGTH_SHORT).getView();
        init(_context, v);
    }

    /**
     * 在程序初始化的时候调用, 只需调用一次
     *
     * @param _context
     * @param view
     */
    public static void init(Context _context, View view) {
        it = new Toast(_context);
        it.setView(view);
    }

    /**
     * 设置Toast背景
     *
     * @param view
     */
    public static void setBackgroundView(View view) {
        if (it != null)
            it.setView(view);
    }

    public static void show(CharSequence text, int duration) {
        if (it == null) {
            LogUtils.e("ToastUtils", "ToastUtils is not initialized, please call init once before you call this method");
            return;
        }
        it.setText(text);
        it.setDuration(duration);
        it.show();
    }

    public static void show(int resid, int duration) {
        if (it == null) {
            LogUtils.e("ToastUtils", "ToastUtils is not initialized, please call init once before you call this method");
            return;
        }
        it.setText(resid);
        it.setDuration(duration);
        it.show();
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(int resid) {
        show(resid, Toast.LENGTH_SHORT);
    }
}
