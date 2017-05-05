package com.zncm.dminter.mmhelper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;

import org.greenrobot.eventbus.EventBus;

public class MyInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String pkgName = intent.getDataString();

        if (Xutils.isNotEmptyOrNull(pkgName) && pkgName.contains("package:")) {
            pkgName = pkgName.replaceAll("package:", "");
        }
        if (!Xutils.isNotEmptyOrNull(pkgName)) {
            return;
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            PkInfo pk = DbUtils.getPkOne(pkgName,false);
            if (pk != null) {
                pk.setExi1(EnumInfo.pkStatus.NORMAL.getValue());
                DbUtils.updatePkInfo(pk);
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
                PkInfo tmp = new PkInfo(pkgName, description, Xutils.bitmap2Bytes(bitmap), EnumInfo.appStatus.ENABLE.getValue(), EnumInfo.appType.THREE.getValue());
                DbUtils.insertPkInfo(tmp);
                }
            Log.i("homer", "安装了 :" + pkgName);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
        }

        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            PkInfo pk = DbUtils.getPkOne(pkgName);
            if (pk != null) {
                pk.setExi1(EnumInfo.pkStatus.DELETE.getValue());
                DbUtils.updatePkInfo(pk);
            }
            Log.i("homer", "卸载了 :" + pkgName);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
        }

        if (intent.getAction().equals("android.intent.action.ACTION_PACKAGE_REPLACED")) {
            PkInfo pk = DbUtils.getPkOne(pkgName,false);
            if (pk != null) {
                pk.setExi1(EnumInfo.pkStatus.NORMAL.getValue());
                DbUtils.updatePkInfo(pk);
            }
            Log.i("homer", "重装 :" + pkgName);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
        }
    }
}  