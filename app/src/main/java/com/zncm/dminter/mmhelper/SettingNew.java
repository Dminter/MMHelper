package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_BATSTOP, "全部冷冻");
            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("T9搜索").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_T9, "T9搜索");
            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("采集活动").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_GET_ACTIVITY, "采集活动");

            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("收藏的活动").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.OPENINENT_LIKE, "收藏的活动", Constant.app_shortcut_openinentactivity);
            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("锁屏").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_LOCK_SCREEN, "锁屏");
            }
        }));

        addItem(new HeaderItem(this).setTitle("通用"));
        addItem(new TextItem(this.ctx, "").setTitle("分组").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                final EditText editText = new EditText(ctx);
                editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                new MaterialDialog.Builder(ctx).title("分组-英文逗号分隔").customView(editText, false).positiveText("好").negativeText("不").onAny(new MaterialDialog.SingleButtonCallback() {
                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            String fz = editText.getText().toString();
                            if (Xutils.isEmptyOrNull(fz)) {
                                return;
                            }
                            if (!fzInfo.equals(fz)) {
                                SPHelper.setFzInfo(ctx, fz);
                                Xutils.tShort("分组修改成功！");
                                isNeedUpdate = true;
                            }
                        }
                    }
                }).show();
            }
        }));

        addItem(new TextItem(this.ctx, "").setTitle("系统应用->冷冻室【慎重】").setOnclick(new TextItem.OnClickListener() {
                                                                                    public void onClick(TextItem textItem) {
                                                                                        new MaterialDialog.Builder(ctx).title("系统应用->冷冻室【慎重】").content("注意：冻结系统应用可能会导致系统崩溃，无法开机，切忌盲目冻结，以免造成严重后果！！~")
                                                                                                .positiveText("确定").neutralText("取消").onAny(new MaterialDialog.SingleButtonCallback() {


                                                                                                                                                @Override
                                                                                                                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                                                                                                    if (which == DialogAction.POSITIVE) {

                                                                                                                                                        ArrayList<String> appNames = new ArrayList<String>();
                                                                                                                                                        final ArrayList<String> pkNames = new ArrayList<String>();
                                                                                                                                                        PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                                                                                                                                                        List<PackageInfo> packages = pm.getInstalledPackages(0);
                                                                                                                                                        for (PackageInfo packageInfo : packages) {
                                                                                                                                                            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                                                                                                                                                            } else {
                                                                                                                                                                appNames.add(packageInfo.applicationInfo.loadLabel(pm)
                                                                                                                                                                        .toString());
                                                                                                                                                                pkNames.add(packageInfo.packageName);
                                                                                                                                                            }
                                                                                                                                                        }


                                                                                                                                                        new MaterialDialog.Builder(ctx).title("系统应用->冷冻室").items(appNames).theme(Theme.LIGHT).itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                                                                                                                                            @Override
                                                                                                                                                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                                                                                                                                                                for (int i = 0; i < which.length; i++) {
                                                                                                                                                                    String pkName = pkNames.get(i);
                                                                                                                                                                    if (Xutils.isNotEmptyOrNull(pkName)) {
                                                                                                                                                                        PkInfo pkInfo = DbUtils.getPkOne(pkName);
                                                                                                                                                                        if (pkInfo != null) {
                                                                                                                                                                            pkInfo.setExb2(1);
                                                                                                                                                                            DbUtils.insertPkInfo(pkInfo);
                                                                                                                                                                        } else {
                                                                                                                                                                            String description = "";
                                                                                                                                                                            Drawable drawable = null;
                                                                                                                                                                            if (Xutils.isNotEmptyOrNull(pkName)) {
                                                                                                                                                                                ApplicationInfo app = Xutils.getAppInfo(pkName);
                                                                                                                                                                                if (app != null) {
                                                                                                                                                                                    PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                                                                                                                                                                                    drawable = app.loadIcon(pm);
                                                                                                                                                                                    description = app.loadLabel(pm).toString();
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                                                                                                                                            PkInfo tmp = new PkInfo(pkName, description, Xutils.bitmap2Bytes(bitmap), EnumInfo.appStatus.ENABLE.getValue(), EnumInfo.appType.THREE.getValue());
                                                                                                                                                                            tmp.setExb2(1);
                                                                                                                                                                            DbUtils.insertPkInfo(tmp);
                                                                                                                                                                        }
                                                                                                                                                                    }


                                                                                                                                                                }


                                                                                                                                                                return true;
                                                                                                                                                            }
                                                                                                                                                        }).positiveText("确定").negativeText("取消").show();
                                                                                                                                                    }

                                                                                                                                                }
                                                                                                                                            }

                                                                                        ).show();
                                                                                    }
                                                                                }

        ));


        addItem(new DividerItem(this.ctx));
        addItem(new CheckboxItem(this, "").setTitle("主界面半屏风格").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsHS(ctx, b);
            }
        }).setDefaultValue(SPHelper.isHS(this.ctx)));


        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this, "").setTitle("抽屉网格大小-列数").setSubtitle(SPHelper.getGridColumns(this.ctx) + "").setOnclick(new TextItem.OnClickListener() {
            public void onClick(final TextItem textItem) {
                ArrayList<String> items = new ArrayList();
                int i = 0;
                for (int j = 2; j <= 12; j++) {
                    if (SPHelper.getGridColumns(ctx) == j) {
                        i = j - 2;
                    }
                    items.add(j + "");
                }
                new MaterialDialog.Builder(ctx).title("抽屉网格大小-列数").items(items).theme(Theme.LIGHT).itemsCallbackSingleChoice(i, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        try {
                            SPHelper.setGridColumns(ctx, Integer.parseInt(text.toString()));
                            textItem.updateSubTitle(text.toString());
                            isNeedUpdate = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }).positiveText("确定").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();

            }
        }));
        
        
        
        
        
        
        
        
        
        
        

        addItem(new HeaderItem(this).setTitle("T9搜索"));
        addItem(new TextItem(this.ctx, "").setTitle("搜索范围").setSubtitle(EnumInfo.typeT9.getTypeT9(SPHelper.getTypeT9(ctx)).getStrName()).setOnclick(new TextItem.OnClickListener() {
            public void onClick(final TextItem textItem) {
                try {
                    int typeT9 = SPHelper.getTypeT9(ctx);
                    String[] items = new String[]{EnumInfo.typeT9.APP.getStrName(), EnumInfo.typeT9.ACTIVITY.getStrName(), EnumInfo.typeT9.APP_ACTIVITY.getStrName()};
                    if (typeT9 < items.length + 1) {
                        items[typeT9 - 1] = items[typeT9 - 1] + "   ✔";
                    }
                    new MaterialDialog.Builder(ctx).items(items).itemsCallback(new MaterialDialog.ListCallback() {

                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            SPHelper.setTypeT9(ctx, position + 1);
                            MyApplication.t9List = new ArrayList();
                            textItem.updateSubTitle(EnumInfo.typeT9.getTypeT9(position + 1).getStrName());
                        }
                    }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        addItem(new DividerItem(this.ctx));
        addItem(new CheckboxItem(this, "").setTitle("匹配结果仅一项自动打开").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setT9Auto(ctx, b);
            }
        }).setDefaultValue(SPHelper.isT9Auto(this.ctx)));
        addItem(new DividerItem(this.ctx));
        addItem(new CheckboxItem(this, "").setTitle("每次打开重置搜索条件").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setT9Clear(ctx, b);
            }
        }).setDefaultValue(SPHelper.isT9Clear(this.ctx)));
    }


    public static void shortCutAdd(Context context, String action, String name) {

        shortCutAdd(context, action, name, Constant.app_shortcut);
    }

    public static void shortCutAdd(Context context, String action, String name, String className) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName(Constant.app_pkg, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent send = new Intent();
        intent.putExtra("action", action);
        send.putExtra("android.intent.extra.shortcut.INTENT", intent);
        send.putExtra("android.intent.extra.shortcut.NAME", name);
        try {
            send.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context.createPackageContext(Constant.app_pkg, 0), Xutils.getAppIconId(Constant.app_pkg)));
            send.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(send);
            Xutils.tShort("已创建快捷方式" + name);
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

                    new MaterialDialog.Builder(ctx).title("支付宝购买，仅需5.0元！").content("购买后请获取订单号【点击支付宝订单详情-》创建时间-》长按复制订单号-》输入订单号-》完成购买】注：重装或换机需重新购买，价格为0.01元").positiveText("购买").negativeText("取消").neutralText("输入订单号").onAny(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            if (which == DialogAction.NEUTRAL) {

                                final EditText editText = new EditText(ctx);
                                editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                new MaterialDialog.Builder(ctx).title("输入订单号").customView(editText, false).positiveText("好").negativeText("不").neutralText("打开支付宝").onAny(new MaterialDialog.SingleButtonCallback() {
                                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.NEUTRAL) {
                                            Xutils.startAppByPackageName(ctx, "com.eg.android.AlipayGphone", 3);
                                        } else if (which == DialogAction.POSITIVE) {
                                            String code = editText.getText().toString();
                                            if (Xutils.isEmptyOrNull(code)) {
                                                Xutils.tShort("请输入订单号");
                                                return;
                                            }

                                            boolean flag = false;
                                            if (code.length() == 32) {
                                                String payOrder = code.substring(0, 8);
                                                SPHelper.setPayOrder(ctx, payOrder);
                                                String payOrderCheck = SPHelper.getPayOrderCheck(ctx);
                                                if (!payOrderCheck.contains(Xutils.getTimeTodayYMD())) {
                                                    String payCheck = payOrderCheck + "," + Xutils.getTimeTodayYMD();
                                                    SPHelper.setPayOrderCheck(ctx, payCheck);
                                                }
                                                flag = isPay(ctx);
                                            }
                                            if (!flag) {
                                                Xutils.tShort("订单号错误~");
                                                return;
                                            }
                                            MyApplication.isPay = flag;
                                            System.exit(0);
                                            Intent intent = new Intent(ctx, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }).show();


                            } else if (which == DialogAction.POSITIVE) {
                                Xutils.startAppByPackageName(ctx, "com.eg.android.AlipayGphone", 3);
                            }

                        }
                    });


                }


            }
        }).show();


    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        paramMenu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(paramMenu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.FZ.getValue()));
            finish();
        }

        return true;
    }
    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }
}
