package com.liuzq.fingerprintidentify.impl;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.os.CancellationSignal;

import com.liuzq.fingerprintidentify.aosp.FingerprintManagerCompat;
import com.liuzq.fingerprintidentify.base.BaseFingerprint;



/**
 * @author liuzq
 * @description 适配android原生组件库
 * Created by 2020/11/23
 */
public class AndroidFingerprint extends BaseFingerprint {

    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat mFingerprintManagerCompat;

    public AndroidFingerprint(Context context, ExceptionListener exceptionListener, boolean iSupportAndroidL) {
        super(context, exceptionListener);

        if (!iSupportAndroidL) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return;
            }
        }

        try {
            mFingerprintManagerCompat = FingerprintManagerCompat.from(context);
            setHardwareEnable(mFingerprintManagerCompat.isHardwareDetected());
            setRegisteredFingerprint(mFingerprintManagerCompat.hasEnrolledFingerprints());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        try {
            mCancellationSignal = new CancellationSignal();
            mFingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    onSucceed();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    onNotMatch();
                }

                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);

                    if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_CANCELED ||
                            errMsgId == FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED) {
                        return;
                    }

                    onFailed(errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT ||
                            errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT);
                }
            }, null);

        } catch (Throwable e) {
            onCatchException(e);
            onFailed(false);
        }
    }

    @Override
    protected void doCancelIdentify() {
        try {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return false;
    }
}
