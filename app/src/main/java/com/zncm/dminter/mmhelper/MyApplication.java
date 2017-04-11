package com.zncm.dminter.mmhelper;

import android.app.Application;
import android.content.Context;

import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.FzInfo;

import java.util.ArrayList;

public class MyApplication extends Application {
    public Context ctx;
    public static boolean isOpenInent = false;
    public static MyApplication instance;
    public static ArrayList<FzInfo> fzInfos = new ArrayList<>();
    public static boolean isPay = false;

    public static ArrayList<CardInfo> t9List = new ArrayList();


    @Override
    public void onCreate() {
        super.onCreate();
        this.ctx = this.getApplicationContext();
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(new CustomException(this));
        isPay = SettingNew.isPay(this.ctx);
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
