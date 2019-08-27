package com.liuzq.commlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;

import com.liuzq.commlibrary.base.BaseApplication;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by susan on 2017/2/20.
 */

public class StringUtils {

    /**
     * 非空
     *
     * @param boo
     * @return
     */
    public static boolean isNotNull(Boolean boo) {
        return !isNull(boo);
    }

    /**
     * 非空
     *
     * @param boo
     * @return
     */
    public static boolean isNull(Boolean boo) {
        return boo == null;
    }

    /**
     * 非空
     *
     * @param num
     * @return
     */
    public static boolean isNotNull(Double num) {
        return !isNull(num);
    }

    /**
     * 非空
     *
     * @param num
     * @return
     */
    public static boolean isNull(Double num) {
        return num == null;
    }


    /**
     * 非空
     *
     * @param integer
     * @return
     */
    public static boolean isNotNull(Integer integer) {
        return !isNull(integer);
    }

    /**
     * 非空
     *
     * @param integer
     * @return
     */
    public static boolean isNull(Integer integer) {
        return integer == null;
    }

    /**
     * 非空
     *
     * @param integer
     * @return
     */
    public static boolean isNotNull(Long integer) {
        return !isNull(integer);
    }

    /**
     * 非空
     *
     * @param integer
     * @return
     */
    public static boolean isNull(Long integer) {
        return integer == null;
    }


    /**
     * 字符串非空
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * null或者""或者"null", 字符串判断
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (TextUtils.isEmpty(s)) return true;
        if ("null".equals(s)) return true;
        return false;
    }

    /**
     * 两个字符串不相同
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isNotEquals(CharSequence str1, CharSequence str2) {
        return !isEquals(str1, str2);
    }

    /**
     * 两个字符串相同
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isEquals(CharSequence str1, CharSequence str2) {
        return TextUtils.equals(str1, str2);
    }

    /**
     * @param content
     * @return
     */
    public static int getCharacterNum(String content) {
        if (content.equals("") || null == content) {
            return 0;
        } else {
            return content.length() + getChineseNum(content);
        }
    }

    /**
     * 计算字符串的中文长度
     *
     * @param s
     * @return
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取资源字符串
     *
     * @param resId
     * @return
     */
    public static String resStr(Context context, int resId) {
        return context.getString(resId);
    }

    /**
     * 获取资源颜色值
     *
     * @param resId
     * @return
     */
    public static int resColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * 获取资源图片
     *
     * @param resId
     * @return
     */
    public static Drawable resDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 字符串格式化
     *
     * @param resId
     * @param args
     * @return
     */
    public static String strFormat(Context context, int resId, Object... args) {
        return String.format(resStr(context, resId), args);
    }

    /**
     * 剔除空格回车换行tab等字符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 往图片添加数字
     */
    public static Bitmap setNumToIcon(Context context, int drawableId, int num) {
        BitmapDrawable bd = (BitmapDrawable) context
                .getResources().getDrawable(drawableId);
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        int widthX;
        int heightY = 0;
        if (num < 10) {
            paint.setTextSize(25);
            widthX = 8;
            heightY = 6;
        } else {
            paint.setTextSize(20);
            widthX = 11;
        }

        canvas.drawText(String.valueOf(num),
                ((bitmap.getWidth() / 2) - widthX),
                ((bitmap.getHeight() / 2) + heightY), paint);
        return bitmap;
    }

    /**
     * @param src
     * @param startText
     * @param style     文本样式
     * @return
     */
    public static SpannableString spannKeyword(String src, String startText, int count, float[] style) {
        if (null == src
                || null == startText
                || src.length() < startText.length()
                || -1 == src.indexOf(startText)) {
            return new SpannableString(src);
        }
        SpannableString spanText = new SpannableString(src);
        int start = src.indexOf(startText) + startText.length();
        int end = start + count;
        spanText.setSpan(new ForegroundColorSpan((int) style[0]), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new RelativeSizeSpan((int) style[1]), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    /**
     * 设置指定位置以后文本字体大小
     *
     * @param str
     * @param key
     * @param size 相对大小,比原来字体大多少比例或小多少比例
     * @return
     */
    public static SpannableString spannKeyText(String str, String key, float[] size) {
        if (str == null || str.length() < key.length()) return null;
        SpannableString spanText = new SpannableString(str);
        int start = str.indexOf(key);
        int len = str.length();
        spanText.setSpan(new RelativeSizeSpan(size[0]), 0, start, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanText.setSpan(new RelativeSizeSpan(size[1]), start, len, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanText;
    }

    /**
     * 设置文字背景颜色，文字颜色，字体大小
     *
     * @return
     */
    public static SpannableString spannText(String str, float[] style) {
        if (str == null) return null;
        SpannableString spanText = new SpannableString(str);
        int len = str.length();
        if (len <= 0) return spanText;
        if (style.length >= 1) {
            spanText.setSpan(new BackgroundColorSpan((int) style[0]), 0, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (style.length >= 2) {
                spanText.setSpan(new ForegroundColorSpan((int) style[1]), 0, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (style.length >= 3) {
                    spanText.setSpan(new RelativeSizeSpan(style[2]), 0, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spanText;
    }

    /**
     * 设置文本样式 字体颜色，字体大小
     *
     * @param str1
     * @param str2
     * @param style
     * @return
     */
    public static SpannableString spannText(String str1, String str2, String regex, float... style) {
        if (str1 == null && str2 == null) return null;
        String str = (str1 == null ? "" : str1) + regex + (str2 == null ? "" : str2);
        SpannableString spanText = new SpannableString(str);
        int len1;
        int len;
        if (str1 == null) {
            spanText = new SpannableString(regex + str2);
            len1 = regex.length();
            len = str.length();
        } else if (str2 == null) {
            spanText = new SpannableString(str1 + regex);
            len1 = str1.length();
            len = str1.length();
        } else {
            len1 = str1.length();
            len = str.length();
        }
        if (len <= 0) return spanText;
        if (style.length >= 2) {
            spanText.setSpan(new ForegroundColorSpan((int) style[0]), 0, len1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new ForegroundColorSpan((int) style[1]), len1 + regex.length(), len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (style.length == 4) {
                spanText.setSpan(new RelativeSizeSpan(style[2]), 0, len1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spanText.setSpan(new RelativeSizeSpan(style[3]), len1 + regex.length(), len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spanText;
    }

    /**
     * 设置文本样式 字体颜色，字体大小
     *
     * @param str1
     * @param str2
     * @param style
     * @return
     */
    public static SpannableString spannText(String str1, String str2, float... style) {
        if (str1 == null && str2 == null) return null;
        String str = (str1 == null ? "" : str1) + (str2 == null ? "" : str2);
        SpannableString spanText = new SpannableString(str);
        int len1;
        int len;
        if (str1 == null) {
            spanText = new SpannableString(str2);
            len1 = 0;
            len = str2.length();
        } else if (str2 == null) {
            spanText = new SpannableString(str1);
            len1 = str1.length();
            len = str1.length();
        } else {
            len1 = str1.length();
            len = str.length();
        }
        if (len <= 0) return spanText;
        if (style.length >= 2) {
            spanText.setSpan(new ForegroundColorSpan((int) style[0]), 0, len1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new ForegroundColorSpan((int) style[1]), len1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (style.length == 4) {
                spanText.setSpan(new RelativeSizeSpan(style[2]), 0, len1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spanText.setSpan(new RelativeSizeSpan(style[3]), len1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spanText;
    }

    /**
     * 字符串BASE64加密
     */
    public static String encodeData(String s) {
        if (TextUtils.isEmpty(s)) return s;
        try {
            return new String(Base64.encode(s.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 字符串BASE64解密
     */
    public static String decodeData(String s) {
        if (TextUtils.isEmpty(s)) return s;
        try {
            return new String(Base64.decode(s.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
