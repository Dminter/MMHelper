package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

/**
 * Created by dminter on 2016/7/26.
 */

public class ShortcutActionActivity extends Activity {
    private String pkName = "";
    private String className = "";
    private int cardId = 0;
    String action = Constant.SA_T9;
    public final static String INSTALL_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";

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
//                    SPHelper.setIsAcFloat(ctx, true);
//                    MyFt.getActivityDlg(ctx);
                } else if (action.equals(Constant.SA_LOCK_SCREEN)) {
//                    FloatBallView.lockScreen(ctx);
                }
            }
            MyApplication.getInstance().isOpenInent = true;
            finish();
        } else {
//            Xutils.tShort("已创建，全部冷冻、T9搜索、采集活动三个快捷方式~");
            SettingNew.shortCutAdd(ctx, Constant.SA_BATSTOP, "全部冷冻");
            SettingNew.shortCutAdd(ctx, Constant.SA_T9, "T9搜索");
            SettingNew.shortCutAdd(ctx, Constant.SA_LOCK_SCREEN, "锁屏");
//            SettingNew.shortCutAdd(ctx, Constant.SA_GET_ACTIVITY, "采集活动");
        }

    }


}
