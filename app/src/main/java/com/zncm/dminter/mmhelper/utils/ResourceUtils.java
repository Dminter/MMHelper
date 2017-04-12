package com.zncm.dminter.mmhelper.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ResourceUtils {
    public static Drawable getPackageImage(PackageManager packageManager, String packageName, int paramInt, ApplicationInfo paramApplicationInfo) {
        if (paramInt == 0) {
            return null;
        }
        return packageManager.getDrawable(packageName, paramInt, paramApplicationInfo);
    }

    public static CharSequence getPackageString(PackageManager packageManager, String packageName, int paramInt, ApplicationInfo paramApplicationInfo) {
        if (paramInt == 0) {
            return null;
        }
        return packageManager.getText(packageName, paramInt, paramApplicationInfo);
    }
}
