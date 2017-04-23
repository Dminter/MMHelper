package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.floatball.ScreenOffAdminReceiver;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

import ezy.assist.compat.SettingsCompat;

/**
 * Created by dminter on 2016/7/26.
 */

public class ShortcutActionActivity extends Activity {
    String action = Constant.SA_T9;
    Activity ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString("action");
            if (Xutils.isNotEmptyOrNull(action)) {
                if (action.equals(Constant.SA_BATSTOP)) {
                    new MyFt.BatStopTask().execute(EnumInfo.typeBatStop.DISABLE_LESS.getValue());
                    finish();
                } else if (action.equals(Constant.SA_T9)) {
                    startActivity(new Intent(ctx, T9SearchActivity.class));
                    finish();
                } else if (action.equals(Constant.SA_GET_ACTIVITY)) {
                    SPHelper.setIsAcFloat(ctx, true);
                    if (!SettingsCompat.canDrawOverlays(ctx)) {
                        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                    }
                    MyFt.getActivityDlg(ctx);
                    finish();
                } else if (action.equals(Constant.SA_LOCK_SCREEN)) {
                    lockScreen(ctx);
                    finish();
                } else if (action.equals(Constant.OPENINENT_LIKE)) {
                    OpenInentActivity.initLikes(ctx);
                }
            }
//            MyApplication.getInstance().isOpenInent = true;

        } else {
            SettingNew.shortCutAdd(ctx, Constant.SA_BATSTOP, "全部冷冻");
            SettingNew.shortCutAdd(ctx, Constant.SA_T9, "T9搜索");
            SettingNew.shortCutAdd(ctx, Constant.SA_LOCK_SCREEN, "锁屏");
        }

    }


    public static void lockScreen(Context context) {
        try {
            DevicePolicyManager devicePolicy = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (devicePolicy.isAdminActive(new ComponentName(context, ScreenOffAdminReceiver.class))) {
            devicePolicy.lockNow();
            return;
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(context, ScreenOffAdminReceiver.class));
                context.startActivity(intent);
                Xutils.tLong("请在设备管理器授权~");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
