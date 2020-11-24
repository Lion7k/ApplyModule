package com.liuzq.basemodule.ui.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.commlibrary.utils.PreferencesUtils;
import com.liuzq.commlibrary.widget.CommDialog;
import com.liuzq.fingerprintidentify.FingerprintIdentify;
import com.liuzq.fingerprintidentify.base.BaseFingerprint;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author liuzq
 * dec: 指纹 适配大部分市面机型（安卓原生、三星、魅族）
 */
public class FingerprintActivity extends CommActivity {
    @BindView(R.id.tv_switch_fingerprint)
    TextView mTvSwithFingerprint;

    public static final String SAFE_SETTING_FINGERPRINT = "fingerprint"; // 指纹解锁密码校验

    private FingerprintIdentify mFingerprintIdentify;
    // 从SP中获取指纹密码是否开启,默认关闭
    boolean fingerprint;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_fingerprint;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {

    }

    @OnClick({R.id.tv_switch_fingerprint})
    public void swithFingerPrint(View v){
        switch (v.getId()) {
            case R.id.tv_switch_fingerprint:
                if (fingerprint) {
                    PreferencesUtils.putBoolean(SAFE_SETTING_FINGERPRINT, false);
                    setFingerPrintToggle();
                } else {
                    initFingerPrint();
                }
                break;
        }
    }

    /**
     * 初始化指纹识别
     */
    private void initFingerPrint() {
        final CommDialog dialog = new CommDialog(mActivity);
        final View[] rootContent = {LayoutInflater.from(mActivity).inflate(R.layout.layout_finger_info, null)};
        dialog.setCustom(rootContent[0]);
        final TextView textRetry = rootContent[0].findViewById(R.id.text_retry);
        final TextView textContent = rootContent[0].findViewById(R.id.text_title);
        final ImageView iconFinger = rootContent[0].findViewById(R.id.icon_finger);
        final TextView textOk = rootContent[0].findViewById(R.id.btn_cancle);
        textOk.setTextColor(getResources().getColor(R.color.gray_99));

        textRetry.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textRetry.setVisibility(View.GONE);
        rootContent[0].findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    mFingerprintIdentify.cancelIdentify();
                }
            }
        });
        dialog.show();// 成功开启指纹硬件情况下显示
        mFingerprintIdentify = new FingerprintIdentify(this);
        mFingerprintIdentify.init();
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {

//                mVerifyResult.setText("指纹验证初始化异常：" + exception.getMessage());

                // 没有指纹
                dialog.dismiss();
                iconFinger.setVisibility(View.GONE);
                textRetry.setVisibility(View.VISIBLE);
                textRetry.setText(R.string.finger_info_verify_failed);
                textContent.setVisibility(View.VISIBLE);
                textContent.setText(exception.getMessage());
                textOk.setText(R.string.ok);
                textOk.setTextColor(getResources().getColor(R.color.color_light_blue));
                dialog.show();
                PreferencesUtils.putBoolean(SAFE_SETTING_FINGERPRINT, false);
            }
        });

        if (!mFingerprintIdentify.isHardwareEnable()) {
//            mVerifyResult.setText("指纹验证初始化失败：设备硬件不支持指纹识别");
            // 硬件不支持
            dialog.dismiss();
            iconFinger.setVisibility(View.GONE);
            textRetry.setVisibility(View.VISIBLE);
            textRetry.setText(R.string.finger_info_verify_failed);
            textContent.setVisibility(View.VISIBLE);
            textContent.setText(R.string.finger_info_error_un_support);
            textOk.setText(R.string.ok);
            textOk.setTextColor(getResources().getColor(R.color.color_light_blue));
            dialog.show();
            PreferencesUtils.putBoolean(SAFE_SETTING_FINGERPRINT, false);
            return;
        }

        if (!mFingerprintIdentify.isRegisteredFingerprint()) {
//            mVerifyResult.setText("指纹验证初始化失败：未注册指纹");
            // 没有指纹 未注册指纹
            dialog.dismiss();
            iconFinger.setVisibility(View.GONE);
            textRetry.setVisibility(View.VISIBLE);
            textRetry.setText(R.string.finger_info_verify_failed);
            textContent.setVisibility(View.VISIBLE);
            textContent.setText(R.string.finger_info_error_un_get_fingerprint);
            textOk.setText(R.string.ok);
            textOk.setTextColor(getResources().getColor(R.color.color_light_blue));
            dialog.show();
            PreferencesUtils.putBoolean(SAFE_SETTING_FINGERPRINT, false);
            return;
        }

        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                dialog.dismiss();
                textContent.setVisibility(View.VISIBLE);
                textContent.setText(R.string.auth_ok);
                textRetry.setVisibility(View.GONE);
                iconFinger.setVisibility(View.GONE);
                textOk.setText(R.string.ok);
                mFingerprintIdentify.cancelIdentify();
                dialog.show();
                PreferencesUtils.putBoolean(SAFE_SETTING_FINGERPRINT, true);
                setFingerPrintToggle();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                textContent.setVisibility(View.VISIBLE);
                textContent.setText(R.string.toast_text_retry_to);
                textRetry.setVisibility(View.GONE);
                iconFinger.setVisibility(View.VISIBLE);
                iconFinger.clearAnimation();
                Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.anim_shake);
                iconFinger.startAnimation(anim);

            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                dialog.dismiss();
                iconFinger.setVisibility(View.GONE);
                textRetry.setVisibility(View.VISIBLE);
                textRetry.setText(R.string.finger_info_verify_failed);
                textContent.setVisibility(View.VISIBLE);
                textContent.setText(R.string.toast_text_retry_to_much);
                textOk.setText(R.string.ok);
                textOk.setTextColor(getResources().getColor(R.color.color_light_blue));
                dialog.show();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                dialog.dismiss();
                iconFinger.setVisibility(View.GONE);
                textRetry.setVisibility(View.VISIBLE);
                textRetry.setText(R.string.finger_info_verify_failed);
                textContent.setVisibility(View.VISIBLE);
                textContent.setText(R.string.toast_text_retry_to_much_lock);
                textOk.setText(R.string.ok);
                textOk.setTextColor(getResources().getColor(R.color.color_light_blue));
                dialog.show();
            }
        });


    }

    // 设置指纹开关状态
    private void setFingerPrintToggle() {
        // 从SP中获取手势密码是否开启,默认关闭
        fingerprint = PreferencesUtils.getBoolean(SAFE_SETTING_FINGERPRINT, false);
        Drawable toggle_on = getResources().getDrawable(R.mipmap.user_status_on);
        Drawable toggle_off = getResources().getDrawable(R.mipmap.user_status_off);
        toggle_on.setBounds(0, 0, toggle_on.getMinimumWidth(), toggle_on.getMinimumHeight()); // 设置边界
        toggle_off.setBounds(0, 0, toggle_off.getMinimumWidth(), toggle_off.getMinimumHeight()); // 设置边界

        // 设置是否开启手势密码小图标
        if (fingerprint) {
            mTvSwithFingerprint.setCompoundDrawables(null, null, toggle_on, null);
        } else {
            mTvSwithFingerprint.setCompoundDrawables(null, null, toggle_off, null);
        }

    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mFingerprintIdentify != null) {
            mFingerprintIdentify.cancelIdentify();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFingerPrintToggle();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mFingerprintIdentify != null) {
            mFingerprintIdentify.resumeIdentify();
        }
    }
}
