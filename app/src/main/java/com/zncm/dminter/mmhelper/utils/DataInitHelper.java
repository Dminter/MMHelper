package com.zncm.dminter.mmhelper.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by dminter on 2016/9/28.
 */

public class DataInitHelper {
    public static class MyTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                initPkInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
        }
    }
    private static void initPkInfo() {
        PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                String pkgName = packageInfo.packageName;
                PkInfo pkInfo = DbUtils.getPkOne(pkgName);
                if (pkInfo != null) {
                    DbUtils.insertPkInfo(pkInfo);
                } else {
                    String description = packageInfo.applicationInfo.loadLabel(pm)
                            .toString();
                    Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    PkInfo tmp = new PkInfo(pkgName, description, Xutils.bitmap2Bytes(bitmap), EnumInfo.appStatus.ENABLE.getValue(), EnumInfo.appType.THREE.getValue());
                    DbUtils.insertPkInfo(tmp);
                }
            }

        }
    }
}
