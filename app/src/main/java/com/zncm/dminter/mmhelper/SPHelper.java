package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPHelper {

    public static boolean isShowWindow(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_show_window", true);
    }

    public static void setIsShowWindow(Context context, boolean isShow) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_show_window", isShow).commit();
    }


    public static String getFzInfo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("fz_info", "");
    }

    public static void setFzInfo(Context context, String fz_info) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("fz_info", fz_info).commit();
    }

    //夜间模式
    public static boolean isNightMode(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_night_mode", false);
    }

    //夜间模式-设置
    public static void setIsNightMode(Context context, boolean is_night_mode) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_show_window", is_night_mode).commit();


    }


    //是否已经支付
    public static void setIsPay(Context context, boolean is_pay) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("is_pay", is_pay).commit();
    }

    public static boolean isPay(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_pay", false);
    }

    //主题配色

    public static int getThemeColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("theme_color", -11751600);
    }


    public static void setThemeColor(Context context, int theme_color) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("theme_color", theme_color).commit();
    }

}
