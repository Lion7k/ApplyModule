package com.liuzq.fingerprintidentify;

import android.content.Context;

import com.liuzq.fingerprintidentify.base.BaseFingerprint;
import com.liuzq.fingerprintidentify.impl.AndroidFingerprint;
import com.liuzq.fingerprintidentify.impl.MeiZuFingerprint;
import com.liuzq.fingerprintidentify.impl.SamsungFingerprint;

/**
 * @author liuzq
 * @description 指纹鉴定
 * Created by 2020/11/23
 */
public class FingerprintIdentify {

    protected Context mContext;
    protected BaseFingerprint.ExceptionListener mExceptionListener;

    protected boolean mIsSupportAndroidL = false;

    protected BaseFingerprint mFingerprint;
    protected BaseFingerprint mSubFingerprint;

    public FingerprintIdentify(Context context) {
        mContext = context;
    }

    public void setSupportAndroidL(boolean supportAndroidL) {
        mIsSupportAndroidL = supportAndroidL;
    }

    public void setExceptionListener(BaseFingerprint.ExceptionListener exceptionListener) {
        mExceptionListener = exceptionListener;
    }

    /**
     * 指纹初始化操作
     */
    public void init() {
        AndroidFingerprint androidFingerprint = new AndroidFingerprint(mContext, mExceptionListener, mIsSupportAndroidL);
        if (androidFingerprint.isHardwareEnable()) {
            mSubFingerprint = androidFingerprint;
            if (androidFingerprint.isRegisteredFingerprint()) {
                mFingerprint = androidFingerprint;
                return;
            }
        }

        SamsungFingerprint samsungFingerprint = new SamsungFingerprint(mContext, mExceptionListener);
        if (samsungFingerprint.isHardwareEnable()) {
            mSubFingerprint = samsungFingerprint;
            if (samsungFingerprint.isRegisteredFingerprint()) {
                mFingerprint = samsungFingerprint;
                return;
            }
        }

        MeiZuFingerprint meiZuFingerprint = new MeiZuFingerprint(mContext, mExceptionListener);
        if (meiZuFingerprint.isHardwareEnable()) {
            mSubFingerprint = meiZuFingerprint;
            if (meiZuFingerprint.isRegisteredFingerprint()) {
                mFingerprint = meiZuFingerprint;
            }
        }
    }

    // 开始指纹认证
    public void startIdentify(int maxAvailableTimes, BaseFingerprint.IdentifyListener listener) {
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.startIdentify(maxAvailableTimes, listener);
    }

    public void cancelIdentify() { // 取消指纹认证
        if (mFingerprint != null) {
            mFingerprint.cancelIdentify();
        }
    }

    public void resumeIdentify() { // 重复指纹认证
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.resumeIdentify();
    }

    public boolean isFingerprintEnable() { // 指纹验证初始化失败：指纹识别不可用
        return mFingerprint != null && mFingerprint.isEnable();
    }

    public boolean isHardwareEnable() { // 指纹验证初始化失败：设备硬件不支持指纹识别
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isHardwareEnable());
    }

    public boolean isRegisteredFingerprint() { // 指纹验证初始化失败：未注册指纹
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isRegisteredFingerprint());
    }
}
