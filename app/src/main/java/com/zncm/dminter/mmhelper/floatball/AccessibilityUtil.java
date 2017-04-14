package com.zncm.dminter.mmhelper.floatball;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.zncm.dminter.mmhelper.WatchingAccessibilityService;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

public class AccessibilityUtil {
    /**
     * 单击返回功能
     *
     * @param service
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doBack(AccessibilityService service) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 下拉打开通知栏
     *
     * @param service
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doGetActivity(AccessibilityService service) {
//        if (WatchingAccessibilityService.getInstance() != null) {
            final String pName = WatchingAccessibilityService.getInstance().packageName;
            final String cName = WatchingAccessibilityService.getInstance().className;
            if (Xutils.isEmptyOrNull(pName) || Xutils.isEmptyOrNull(cName)) {
                return;
            }
            String title = cName;
            if (Xutils.isNotEmptyOrNull(cName) && cName.contains(".")) {
                title = cName.substring(cName.lastIndexOf("."));
            }
            CardInfo card = new CardInfo(pName, cName, title);
            DbUtils.insertCard(card);
            Xutils.tShort("已添加 " + title);
//        }

//        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
//        AccessibilityEvent accessibilityEvent = service.performGlobalAction(AccessibilityService.)
//        Xutils.debug("RootInActiveWindow：" + service.getWindows().get() + " " + service.getApplicationInfo().className + " " + service.getApplicationInfo().name);
//        TasksWindow.show(service, service.getPackageName() + "\n" + service.getClassName());

    }

    /**
     * 上拉返回桌面
     *
     * @param service
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doHome(AccessibilityService service) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /**
     * 左右滑动打开多任务
     *
     * @param service
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doRecent(AccessibilityService service) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }


}