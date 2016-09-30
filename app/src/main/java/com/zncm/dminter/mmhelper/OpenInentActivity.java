package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;

/**
 * Created by dminter on 2016/7/26.
 */

public class OpenInentActivity extends Activity {
    private String pkName = "";
    private String className = "";
    private int cardId = -1;
    Intent startIntent;
    String packageName = "com.zncm.dminter.mmhelper";
    String name = "com.zncm.dminter.mmhelper.OpenInentActivity";
    public final static String INSTALL_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ft_open);
        startIntent = getIntent();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pkName = bundle.getString("pkName");
            className = bundle.getString("className");
            cardId = bundle.getInt("cardId", -1);
            if (cardId != -1) {
                CardInfo cardInfo = DbUtils.getCardInfoById(cardId);
                if (cardInfo != null) {
                    MyFt.clickCard(this, cardInfo);
                }
            } else {
                Xutils.exec(Constant.common_pm_e_p + pkName);
                int ret = Xutils.cmdExe(Constant.common_am_pre + pkName + Constant.common_am_div + className);
                if (ret == -1) {
                    Xutils.tShort("打开失败~~");
                }
            }
            MyApplication.getInstance().isOpenInent = true;
            finish();
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
