package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jiaomx on 2017/4/10.
 */
public class SettingNew extends MaterialSettings {

    private static SettingNew instance;
    Activity ctx;
    private String fzInfo;
    private boolean isNeedUpdate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        Xutils.verifyStoragePermissions(this);
        this.fzInfo = SPHelper.getFzInfo(this);
        addItem(new HeaderItem(this).setTitle("创建快捷方式"));
        addItem(new TextItem(this.ctx, "").setTitle("全部冷冻").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem paramAnonymousTextItem) {
                shortCutAdd(SettingNew.this.ctx, Constant.SA_BATSTOP, "全部冷冻");
            }
        }));
        addItem(new DividerItem(this.ctx));

    }


    public static void shortCutAdd(Context paramContext, String action, String name) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName(Constant.app_pkg, Constant.app_shortcut);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent send = new Intent();
        intent.putExtra("action", action);
        send.putExtra("android.intent.extra.shortcut.INTENT", intent);
        send.putExtra("android.intent.extra.shortcut.NAME", name);
        try {
            send.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(paramContext.createPackageContext(Constant.app_pkg, 0), Xutils.getAppIconId(Constant.app_pkg)));
            send.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            paramContext.sendBroadcast(send);
            Xutils.tShort("已创建快捷方式" + name);
            return;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void backDo() {
        finish();
        if (this.isNeedUpdate) {
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.FZ.getValue()));
        }
    }

    private boolean checkNotPro() {
        if (!isPay(ctx)) {
            goPro();
            return true;
        }
        return false;
    }


    public static boolean isPay(Context context) {
        String payOrderCheck = SPHelper.getPayOrderCheck(context);
        String payOrder = SPHelper.getPayOrder(context);
        if (Xutils.isNotEmptyOrNull(payOrderCheck) && Xutils.isNotEmptyOrNull(payOrder)) {
            return payOrderCheck.contains(payOrder);
        }
        return false;
    }


    private void goPro() {
        new MaterialDialog.Builder(this.ctx).title("解锁Pro版，仅需5元！").content("Pro版更多彰显的是情怀~").positiveText("支付宝购买").neutralText("取消").onAny(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {

                    new MaterialDialog.Builder(SettingNew.this.ctx).title("支付宝购买，仅需5.0元！").content("购买后请获取订单号【点击支付宝订单详情-》创建时间-》长按复制订单号-》输入订单号-》完成购买】注：重装或换机需重新购买，价格为0.01元").positiveText("购买").negativeText("取消").neutralText("输入订单号").onAny(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            if (which == DialogAction.NEUTRAL) {

                                final EditText editText = new EditText(SettingNew.this.ctx);
                                editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                new MaterialDialog.Builder(SettingNew.this.ctx).title("输入订单号").customView(editText, false).positiveText("好").negativeText("不").neutralText("打开支付宝").onAny(new MaterialDialog.SingleButtonCallback() {
                                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.NEUTRAL) {
                                            Xutils.startAppByPackageName(SettingNew.this.ctx, "com.eg.android.AlipayGphone", 3);
                                        } else if (which == DialogAction.POSITIVE) {
                                            String code = editText.getText().toString();
                                            if (Xutils.isEmptyOrNull(code)) {
                                                Xutils.tShort("请输入订单号");
                                                return;
                                            }

                                            boolean flag = false;
                                            if (code.length() == 32) {
                                                String payOrder = code.substring(0, 8);
                                                SPHelper.setPayOrder(SettingNew.this.ctx, payOrder);
                                                String payOrderCheck = SPHelper.getPayOrderCheck(SettingNew.this.ctx);
                                                if (!payOrderCheck.contains(Xutils.getTimeTodayYMD())) {
                                                    String payCheck = payOrderCheck + "," + Xutils.getTimeTodayYMD();
                                                    SPHelper.setPayOrderCheck(SettingNew.this.ctx, payCheck);
                                                }
                                                flag = isPay(ctx);
                                            }
                                            if (!flag) {
                                                Xutils.tShort("订单号错误~");
                                                return;
                                            }
                                            MyApplication.isPay = flag;
                                            System.exit(0);
                                            Intent intent = new Intent(SettingNew.this.ctx, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }).show();


                            } else if (which == DialogAction.POSITIVE) {
                                Xutils.startAppByPackageName(SettingNew.this.ctx, "com.eg.android.AlipayGphone", 3);
                            }

                        }
                    });


                }


            }
        }).show();


    }


    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }
}
