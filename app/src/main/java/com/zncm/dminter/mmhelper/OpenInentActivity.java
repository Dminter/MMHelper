package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.BottomSheetDlg;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dminter on 2016/7/26.
 */

public class OpenInentActivity extends AppCompatActivity {
    private String pkName = "";
    private String className = "";
    private int cardId = -1;
    Intent startIntent;
    String packageName = "com.zncm.dminter.mmhelper";
    String name = "com.zncm.dminter.mmhelper.OpenInentActivity";
    public final static String INSTALL_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";
    Activity ctx;
    boolean isShotcut = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        startIntent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pkName = bundle.getString("pkName");
            className = bundle.getString("className");
            cardId = bundle.getInt("cardId", 0);
            isShotcut = bundle.getBoolean("isShotcut", false);


            if ((Xutils.isNotEmptyOrNull(pkName)) && (pkName.equals(Constant.OPENINENT_LIKE))) {
                initLikes();
            } else if ((Xutils.isNotEmptyOrNull(pkName)) && (pkName.equals(Constant.OPENINENT_BALL))) {
                MainActivity.initBallService(ctx);
                finish();
            } else {
                if (cardId != 0) {
                    CardInfo cardInfo = DbUtils.getCardInfoById(cardId);
                    if (cardInfo != null) {
                        MyFt.clickCard(this, cardInfo);
                        finish();
                    }
                } else if (isShotcut) {
                    initAppBS(new CardInfo(pkName));
                } else {

                    if (Xutils.isNotEmptyOrNull(className)) {
                        Xutils.exec(Constant.common_pm_e_p + pkName);
                        int ret = Xutils.cmdExe(Constant.common_am_pre + pkName + Constant.common_am_div + className);
                        if (ret == -1) {
                            Xutils.tShort("打开失败~~");
                        }
                        finish();
                    } else {
                        MyFt.appNewStatus(new CardInfo(pkName));
                        Xutils.startAppByPackageName(ctx, pkName, Constant.attempt);
                        finish();
                    }

                }
            }


            MyApplication.getInstance().isOpenInent = true;

        } else {
            final ArrayList<CardInfo> tmps = DbUtils.getCardInfos(null);
            if (!Xutils.listNotNull(tmps)) {
                return;
            }
            ArrayList<String> items = new ArrayList<>();
            for (CardInfo info : tmps
                    ) {
                items.add(info.getTitle());
            }
            new MaterialDialog.Builder(this)
                    .title("添加活动到桌面")
                    .items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            CardInfo info = tmps.get(which);
                            if (info == null) {
                                return;
                            }
                            dialog.dismiss();
                            onDone(info);
                        }
                    })
                    .show();
        }

    }


    private void initLikes() {

        final ArrayList<Map<String, Object>> list = new ArrayList<>();
        //应用相关的活动
        final ArrayList<CardInfo> like = DbUtils.getCardInfos(EnumInfo.homeTab.LIKE.getValue());
        if (Xutils.listNotNull(like)) {
            for (CardInfo tmp : like
                    ) {
                Map<String, Object> map = new HashMap<>();
                map.put("text", tmp.getTitle());
                map.put("key", tmp.getId());
                list.add(map);
            }
        }
        new BottomSheetDlg(ctx, list, false) {
            @Override
            public void onGridItemClickListener(int position) {
                if (Xutils.listNotNull(like) && position < like.size()) {
                    CardInfo cardInfo = like.get(position);
                    if (cardInfo != null) {
                        MyFt.clickCard(ctx, cardInfo);
                    }

                }
            }

            @Override
            public void onGridItemLongClickListener(int position) {

            }

            @Override
            public void onOutClickListener() {
                finish();
            }
        };

    }


    public void initAppBS(final CardInfo info) {

        final ArrayList<Map<String, Object>> list = new ArrayList<>();
        //应用相关的活动
        final ArrayList<CardInfo> activitys = DbUtils.getCardInfosByPackageName(info.getPackageName());
        if (Xutils.listNotNull(activitys)) {
            for (CardInfo tmp : activitys
                    ) {
                Map<String, Object> map = new HashMap<>();
                map.put("text", tmp.getTitle());
                map.put("key", tmp.getId());
                list.add(map);
            }
        }


        final PkInfo pkInfo = DbUtils.getPkOne(info.getPackageName());
        final boolean isDisabled = pkInfo.getStatus() == EnumInfo.appStatus.DISABLED.getValue();
        //冻结，解冻
        Map<String, Object> map = new HashMap<>();
        map.put("text", isDisabled ? "解冻" : "冻结");
        map.put("key", "-1");
        list.add(map);

        //应用信息
        Map<String, Object> map3 = new HashMap<>();
        map3.put("text", "应用信息");
        map3.put("key", "-2");
        list.add(map3);
        //应用信息
        Map<String, Object> map4 = new HashMap<>();
        map4.put("text", "打开");
        map4.put("key", "-3");
        list.add(map4);


        new BottomSheetDlg(ctx, list, true) {
            @Override
            public void onGridItemClickListener(int position) {

                if (Xutils.listNotNull(activitys) && position < activitys.size()) {

                    CardInfo cardInfo = activitys.get(position);
                    if (cardInfo != null) {
                        MyFt.clickCard(ctx, cardInfo);
                    }

                }

                switch (position - activitys.size()) {

                    case 0:
                        if (isDisabled) {
                            Xutils.exec("pm enable " + info.getPackageName());
                        } else {
                            Xutils.exec("pm disable " + info.getPackageName());
                        }
                        info.setDisabled(isDisabled);
                        pkInfo.setStatus(info.isDisabled() ? EnumInfo.appStatus.ENABLE.getValue() : EnumInfo.appStatus.DISABLED.getValue());
                        DbUtils.updatePkInfo(pkInfo);
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                        break;
                    case 1:
                        MyFt.appInfo(ctx, info.getPackageName());
                        break;
                    case 2:
                        MyFt.appNewStatus(info);
                        Xutils.startAppByPackageName(ctx, info.getPackageName(), Constant.attempt);
                        break;

                }


            }

            @Override
            public void onGridItemLongClickListener(int position) {

            }

            @Override
            public void onOutClickListener() {
                finish();
            }
        };


    }







    private void onDone(CardInfo info) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(packageName, name);
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final Intent intent = new Intent();
        shortcutIntent.putExtra("cardId", info.getId());
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, info.getTitle());
        try {
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(createPackageContext(info.getPackageName(), 0),
                            Xutils.getAppIconId(info.getPackageName())));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (startIntent != null
                && startIntent.getAction() == Intent.ACTION_CREATE_SHORTCUT) {
            setResult(RESULT_OK, intent);
        } else {
            intent.setAction(INSTALL_SHORTCUT_ACTION);
            sendBroadcast(intent);
        }
        finish();
    }
}
