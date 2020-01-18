package com.liuzq.searchbox.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuzq.searchbox.data.BaseData;

import java.util.List;

/**
 * 历史缓存工具类
 */
public class HistoryCache<T> {

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * 保存历史记录到SharedPreferences中
     *
     * @param result
     */
    public static void saveHistory(Context context, String name, String key, String result) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, result);
        editor.commit();
    }

    /**
     * 获取保存在SharedPreferences中的历史记录
     *
     * @return
     */
    public static String getHistory(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String result = sp.getString(key, null);
        return result;
    }

    /**
     * 把json数据转换成list
     * @param context
     * @return
     */
    public static <T> List<BaseData<T>> toArray(Context context, String name, String key) {

        String history = getHistory(context, name, key);
        Gson gson = new Gson();
        List<BaseData<T>> retList = gson.fromJson(history, new TypeToken<List<BaseData<T>>>() {
        }.getType());
        return retList;
    }

    /**
     * ArrayList 转换成JsonArray
     * @param historyList
     * @return
     */
    public static <T> String toJsonArray(List<BaseData<T>> historyList) {

        Gson gson = new Gson();
        return gson.toJson(historyList); // 将JSONArray转换得到String
    }
}
