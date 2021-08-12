package com.liuzq.fingerprintidentify.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * @author liuzq
 * @description 指纹校验 base
 * Created by 2020/11/23
 */
public abstract class BaseFingerprint {

    protected Context mContext;

    private Handler mHandler;
    private IdentifyListener mIdentifyListener;
    private ExceptionListener mExceptionListener;

    private int mNumberOfFailures = 0;                      // number of failures
    private int mMaxAvailableTimes = 3;                     // the most available times

    private boolean mIsHardwareEnable = false;              // if the phone equipped fingerprint hardware
    private boolean mIsRegisteredFingerprint = false;       // if the phone has any fingerprints

    private boolean mIsCalledStartIdentify = false;         // if started identify
    private boolean mIsCanceledIdentify = false;            // if canceled identify

    public BaseFingerprint(Context context, ExceptionListener exceptionListener) {
        mContext = context;
        mExceptionListener = exceptionListener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void startIdentify(int maxAvailableTimes, IdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCalledStartIdentify = true;
        mIdentifyListener = listener;
        mIsCanceledIdentify = false;
        mNumberOfFailures = 0;

        doIdentify();
    }


    /**
     * Continue to call fingerprint identify, keep the number of failures.
     */
    public void resumeIdentify() {
        if (mIsCalledStartIdentify && mIdentifyListener != null && mNumberOfFailures < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    /**
     * stop identify and release hardware
     */
    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    /**
     * do identify actually
     */
    protected abstract void doIdentify();

    /**
     * do cancel identify actually
     */
    protected abstract void doCancelIdentify();

    /**
     * verification passed
     */
    protected void onSucceed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onSucceed();
                }
            });
        }

        cancelIdentify();
    }

    /**
     * fingerprint not match
     */
    protected void onNotMatch() {
        if (mIsCanceledIdentify) {
            return;
        }

        if (++mNumberOfFailures < mMaxAvailableTimes) {
            if (mIdentifyListener != null) {
                final int chancesLeft = mMaxAvailableTimes - mNumberOfFailures;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIdentifyListener.onNotMatch(chancesLeft);
                    }
                });
            }

            if (needToCallDoIdentifyAgainAfterNotMatch()) {
                doIdentify();
            }

            return;
        }

        onFailed(false);
    }

    /**
     * verification failed
     * device locked
     *
     * @param isDeviceLocked
     */
    protected void onFailed(final boolean isDeviceLocked) {
        if (mIsCanceledIdentify) {
            return;
        }

        final boolean isStartFailedByDeviceLocked = isDeviceLocked && mNumberOfFailures == 0;

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isStartFailedByDeviceLocked) {
                        mIdentifyListener.onStartFailedByDeviceLocked();
                    } else {
                        mIdentifyListener.onFailed(isDeviceLocked);
                    }
                }
            });
        }

        cancelIdentify();
    }

    /**
     * OTHER ui thread
     * @param runnable
     */
    protected void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * is that hardware detected and has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFingerprint;
    }

    /**
     * is that hardware detected
     *
     * @return yes
     */
    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    /**
     * save the value of hardware detected
     *
     * @param hardwareEnable detected
     */
    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    /**
     * is that has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isRegisteredFingerprint() {
        return mIsRegisteredFingerprint;
    }

    /**
     * save the value of enrolled fingerprints
     *
     * @param registeredFingerprint enrolled
     */
    protected void setRegisteredFingerprint(boolean registeredFingerprint) {
        mIsRegisteredFingerprint = registeredFingerprint;
    }

    /**
     * is that need to recall doIdentify again when not match
     *
     * @return needed
     */
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    /**
     * catch the all exceptions
     *
     * @param exception exception
     */
    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    /**
     * identify callback
     */
    public interface IdentifyListener {
        void onSucceed(); // 成功

        void onNotMatch(int availableTimes); // 未匹配

        void onFailed(boolean isDeviceLocked); // 失败

        void onStartFailedByDeviceLocked(); // 设备锁定
    }

    /**
     * exception callback
     */
    public interface ExceptionListener {
        void onCatchException(Throwable exception);
    }
}
