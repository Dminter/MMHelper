package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zncm.dminter.mmhelper.data.EnumInfo;

public class SPHelper {

    public static boolean isFloatBall(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("float_ball", false);
    }

    public static void setIsFloatBall(Context context, boolean float_ball) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("float_ball", float_ball).commit();
    }

    public static boolean isAcFloat(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("ac_float", true);
    }

    public static void setIsAcFloat(Context context, boolean ac_float) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("ac_float", ac_float).commit();
    }

    public static boolean isHS(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_hs", false);
    }

    public static void setIsHS(Context context, boolean is_hs) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_hs", is_hs).commit();
    }

    public static boolean isAutoStop(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_auto_stop", false);
    }

    public static void setIsAutoStop(Context context, boolean is_auto_stop) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("is_auto_stop", is_auto_stop).commit();
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
        sp.edit().putBoolean("is_night_mode", is_night_mode).commit();
    }


    public static void setIsT9(Context context, boolean is_t9) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("is_t9", is_t9).commit();
    }

    public static boolean isT9(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("is_t9", true);
    }

    public static void setT9Auto(Context context, boolean t9_auto) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("t9_auto", t9_auto).commit();
    }

    public static boolean isT9Auto(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("t9_auto", false);
    }

    public static void setT9Clear(Context context, boolean t9_clear) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("t9_clear", t9_clear).commit();
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


    /**
     * private float mBigBallX;
     * private float mBigBallY;
     */
    public static int getBigBallX(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("bigballx", -1);
    }

    public static void setBigBallY(Context context, int bigbally) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("bigbally", bigbally).commit();
    }

    public static int getBigBallY(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("bigbally", -1);
    }

    public static void setBigBallX(Context context, int bigballx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("bigballx", bigballx).commit();
    }



    public static int getCurTab(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("cur_tab", 1);
    }

    public static void setCurTab(Context context, int cur_tab) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("cur_tab", cur_tab).commit();
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
