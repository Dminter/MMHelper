package com.zncm.dminter.mmhelper.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dminter on 2016/9/28.
 */

public class DataInitHelper {


    public static class MyTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                initPkInfo(EnumInfo.appStatus.ENABLE.getValue());
                initPkInfo(EnumInfo.appStatus.DISABLED.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPSINIT.getValue()));
        }
    }

    private static void initPkInfo(int status) {
        String cmd;
        if (status == EnumInfo.appStatus.ENABLE.getValue()) {
            cmd = Constant.common_pm_e;
        } else {
            cmd = Constant.common_pm_d;
        }
        cmd += Constant.common_pm_3;
        String retSTR = Xutils.exec(cmd);
        retSTR = retSTR.replaceAll("package:", "");
        String pkgStr[] = retSTR.split("\\n");
        for (int i = 0; i < pkgStr.length; i++) {
            String pkgName = pkgStr[i];
            PkInfo pkInfo = DbUtils.getPkOne(pkgName);
            if (pkInfo != null) {
                DbUtils.insertPkInfo(pkInfo);
            } else {
                String description = "";
                Drawable drawable = null;
                if (Xutils.isNotEmptyOrNull(pkgName)) {
                    ApplicationInfo app = Xutils.getAppInfo(pkgName);
                    if (app != null) {
                        PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                        drawable = app.loadIcon(pm);
                        description = app.loadLabel(pm).toString();
                    }
                }
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                PkInfo tmp = new PkInfo(pkgName, description, Xutils.bitmap2Bytes(bitmap), status, EnumInfo.appType.THREE.getValue());
                DbUtils.insertPkInfo(tmp);
            }
        }
    }
}
