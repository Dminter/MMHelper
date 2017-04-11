package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zncm.dminter.mmhelper.data.EnumInfo;

public class SPHelper {

    public static boolean isShowWindow(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_show_window", true);
    }

    public static void setIsShowWindow(Context context, boolean isShow) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_show_window", isShow).commit();
    }

    public static boolean isHS(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_hs", true);
    }

    public static void setIsHS(Context context, boolean is_hs) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_hs", is_hs).commit();
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


    public static void setIsT9(Context context, boolean is_t9) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("is_t9", is_t9).commit();
    }

    public static boolean isT9(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_t9", false);
    }

    public static void setT9Auto(Context context, boolean t9_auto) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("t9_auto", true).commit();
    }

    public static boolean isT9Auto(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("t9_auto", false);
    }

    public static void setT9Clear(Context context, boolean t9_clear) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("t9_clear", true).commit();
    }

    public static boolean isT9Clear(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("t9_clear", false);
    }

    //主题配色

    public static int getThemeColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("theme_color", -11751600);
    }


    public static void setThemeColor(Context context, int theme_color) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("theme_color", theme_color).commit();
    }

    public static int getTypeT9(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("type_t9", EnumInfo.typeT9.APP.getValue());
    }


    public static void setTypeT9(Context context, int type_t9) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("type_t9", type_t9).commit();
    }


    public static int getGridColumns(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("grid_columns", 5);
    }

    public static void setGridColumns(Context context, int grid_columns) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("grid_columns", grid_columns).commit();
    }


    public static int getBallUp(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("ball_up", 0);
    }

    public static void setBallUp(Context context, int ball_up) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("ball_up", ball_up).commit();
    }

    public static int getBallLeft(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("ball_left", 0);
    }

    public static void setBallLeft(Context context, int ball_left) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("ball_left", ball_left).commit();
    }

    public static int getBallDown(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("ball_down", 0);
    }

    public static void setBallDown(Context context, int ball_down) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("ball_down", ball_down).commit();
    }

    public static int getBallRight(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("ball_right", 0);
    }

    public static void setBallRight(Context context, int ball_right) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("ball_right", ball_right).commit();
    }


    public static String getFcLog(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("fc_log", "");
    }

    public static void setFcLog(Context context, String fc_log) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("ball_right", fc_log).commit();
    }

    public static String getPayOrderCheck(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("pay_order_check", "");
    }

    public static void setPayOrderCheck(Context context, String pay_order_check) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("pay_order_check", pay_order_check).commit();
    }

    public static String getPayOrder(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("pay_order", "");
    }

    public static void setPayOrder(Context context, String pay_order) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("pay_order", pay_order).commit();
    }






}
