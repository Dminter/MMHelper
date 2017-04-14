package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zncm.dminter.mmhelper.floatball.ScreenOffAdminReceiver;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

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
                    MyFt.BatStopTask batStopTask = new MyFt.BatStopTask();
                    batStopTask.execute(false);
                } else if (action.equals(Constant.SA_T9)) {
                    startActivity(new Intent(ctx, T9SearchActivity.class));
                } else if (action.equals(Constant.SA_GET_ACTIVITY)) {
                    SPHelper.setIsAcFloat(ctx, true);
                    MyFt.getActivityDlg(ctx);
                } else if (action.equals(Constant.SA_LOCK_SCREEN)) {
                    lockScreen(ctx);
                }
            }
            MyApplication.getInstance().isOpenInent = true;
            finish();
        } else {
            Xutils.tShort("已创建，全部冷冻、T9搜索、锁屏三个快捷方式~");
            SettingNew.shortCutAdd(ctx, Constant.SA_BATSTOP, "全部冷冻");
            SettingNew.shortCutAdd(ctx, Constant.SA_T9, "T9搜索");
            SettingNew.shortCutAdd(ctx, Constant.SA_LOCK_SCREEN, "锁屏");
        }

    }


    public static void lockScreen(Context paramContext) {
        DevicePolicyManager devicePolicy = (DevicePolicyManager) paramContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicy.isAdminActive(new ComponentName(paramContext, ScreenOffAdminReceiver.class))) {
            devicePolicy.lockNow();
            return;
        }
        Xutils.tLong("请在设备管理器授权~");
    }

}
