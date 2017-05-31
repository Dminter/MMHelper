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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.MyPackageInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.floatball.FloatBallService;
import com.zncm.dminter.mmhelper.floatball.FloatWindowManager;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.DataInitHelper;
import com.zncm.dminter.mmhelper.utils.MyPath;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ezy.assist.compat.SettingsCompat;

/**
 * Created by jiaomx on 2017/4/10.
 */
public class SettingNew extends MaterialSettings {

    private static SettingNew instance;
    Activity ctx;
    private String fzInfo;
    private boolean isNeedUpdate = false;


    CheckboxItem checkboxItemBall;
    CheckboxItem checkboxItemFloat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        Xutils.verifyStoragePermissions(this);
        fzInfo = SPHelper.getFzInfo(this);


        addItem(new TextItem(ctx, "").setTitle("建议活动").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                startActivity(new Intent(ctx, SuggestAc.class));
            }
        }));

        addItem(new HeaderItem(this).setTitle("创建快捷方式"));
        addItem(new TextItem(ctx, "").setTitle("全部冷冻").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_BATSTOP, "全部冷冻");
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("T9搜索").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_T9, "T9搜索");
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("采集活动").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_GET_ACTIVITY, "采集活动");

            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("收藏的活动").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.OPENINENT_LIKE, "收藏的活动", Constant.app_shortcut_openinentactivity);
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("锁屏").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                shortCutAdd(ctx, Constant.SA_LOCK_SCREEN, "锁屏");
            }
        }));

        addItem(new HeaderItem(this).setTitle("通用"));
        addItem(new TextItem(ctx, "").setTitle("分组").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                final EditText editText = new EditText(ctx);
                editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (Xutils.isNotEmptyOrNull(fzInfo)) {
                    editText.setText(fzInfo);
                }
                Xutils.themeMaterialDialog(ctx).title("分组-英文逗号分隔").customView(editText, false).positiveText("好").negativeText("不").onAny(new MaterialDialog.SingleButtonCallback() {
                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            String fz = editText.getText().toString();
                            if (!fzInfo.equals(fz)) {
                                SPHelper.setFzInfo(ctx, fz);
                                Xutils.tShort("分组修改成功！");
                                fzInfo = fz;
                                isNeedUpdate = true;
                            }
                        }
                    }
                }).show();
            }
        }));

        addItem(new TextItem(ctx, "").setTitle("添加应用")
                .setSubtitle("解决安装的应用未自动添加到应用列表的情形").setOnclick(new TextItem.OnClickListener() {
                                                                    public void onClick(TextItem textItem) {
                                                                        try {
                                                                            final ArrayList<String> appNames = new ArrayList<String>();
                                                                            final ArrayList<String> pkNames = new ArrayList<String>();
                                                                            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                                                                            List<PackageInfo> packages = pm.getInstalledPackages(0);
                                                                            for (PackageInfo packageInfo : packages) {
                                                                                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                                                                    String name = packageInfo.applicationInfo.loadLabel(pm)
                                                                                            .toString();
                                                                                    if (Xutils.isNotEmptyOrNull(name) && Xutils.isNotEmptyOrNull(packageInfo.packageName)) {
                                                                                        PkInfo pkInfo = DbUtils.getPkOne(packageInfo.packageName);
                                                                                        if (pkInfo == null) {
                                                                                            appNames.add(name);
                                                                                            pkNames.add(packageInfo.packageName);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            Xutils.themeMaterialDialog(ctx).title("添加应用").items(appNames).itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                                                                @Override
                                                                                public boolean onSelection(MaterialDialog dialog, final Integer[] which, CharSequence[] text) {
                                                                                    final StringBuffer names = new StringBuffer();
                                                                                    for (int i = 0; i < which.length; i++) {
                                                                                        String name = appNames.get(which[i]);
                                                                                        if (Xutils.isNotEmptyOrNull(name)) {
                                                                                            names.append(name).append("，");
                                                                                        }
                                                                                    }

                                                                                    Xutils.themeMaterialDialog(ctx).title("确定添加应用").content(names.toString()).positiveText("确定").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                                                                                        public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which2) {
                                                                                            if (which2 == DialogAction.POSITIVE) {
                                                                                                for (int i = 0; i < which.length; i++) {
                                                                                                    String pkName = pkNames.get(which[i]);
                                                                                                    if (Xutils.isNotEmptyOrNull(pkName)) {
                                                                                                        PkInfo pkInfo = DbUtils.getPkOne(pkName);
                                                                                                        if (pkInfo != null) {
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
                                                                                                            Bitmap bitmap = null;
                                                                                                            if (drawable != null) {
                                                                                                                bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                                                                            }
                                                                                                            byte[] bitInfo = null;
                                                                                                            if (bitmap != null) {
                                                                                                                bitInfo = Xutils.bitmap2Bytes(bitmap);
                                                                                                            }
                                                                                                            PkInfo tmp = new PkInfo(pkName, description, bitInfo, EnumInfo.appStatus.ENABLE.getValue(), EnumInfo.appType.THREE.getValue());
                                                                                                            DbUtils.insertPkInfo(tmp);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                Xutils.tShort("已全部添加~");

                                                                                            }
                                                                                        }
                                                                                    }).show();


                                                                                    return true;
                                                                                }
                                                                            }).positiveText("确定").negativeText("取消").show();
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }

                ));

        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("系统应用->冷冻室【慎重】").setOnclick(new TextItem.OnClickListener() {
                                                                               public void onClick(TextItem textItem) {
                                                                                   Xutils.themeMaterialDialog(ctx).title("系统应用->冷冻室【慎重】").content("注意：冻结系统应用可能会导致系统崩溃，无法开机，切忌盲目冻结，以免造成严重后果！！~")
                                                                                           .positiveText("确定").neutralText("取消").onAny(new MaterialDialog.SingleButtonCallback() {


                                                                                                                                           @Override
                                                                                                                                           public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                                                                                               if (which == DialogAction.POSITIVE) {

                                                                                                                                                   try {

                                                                                                                                                       final ArrayList<String> appNames = new ArrayList<String>();
                                                                                                                                                       final ArrayList<String> pkNames = new ArrayList<String>();
                                                                                                                                                       PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                                                                                                                                                       List<PackageInfo> packages = pm.getInstalledPackages(0);
                                                                                                                                                       for (PackageInfo packageInfo : packages) {
                                                                                                                                                           if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                                                                                                                                                           } else {
                                                                                                                                                               String name = packageInfo.applicationInfo.loadLabel(pm)
                                                                                                                                                                       .toString();
                                                                                                                                                               if (Xutils.isNotEmptyOrNull(name) && Xutils.isNotEmptyOrNull(packageInfo.packageName)) {
                                                                                                                                                                   appNames.add(name);
                                                                                                                                                                   pkNames.add(packageInfo.packageName);
                                                                                                                                                               }
                                                                                                                                                           }
                                                                                                                                                       }


                                                                                                                                                       Xutils.themeMaterialDialog(ctx).title("系统应用->冷冻室").items(appNames).itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                                                                                                                                           @Override
                                                                                                                                                           public boolean onSelection(MaterialDialog dialog, final Integer[] which, CharSequence[] text) {
                                                                                                                                                               final StringBuffer names = new StringBuffer();
                                                                                                                                                               for (int i = 0; i < which.length; i++) {
                                                                                                                                                                   String name = appNames.get(which[i]);
                                                                                                                                                                   if (Xutils.isNotEmptyOrNull(name)) {
                                                                                                                                                                       names.append(name).append("，");
                                                                                                                                                                   }
                                                                                                                                                               }

                                                                                                                                                               Xutils.themeMaterialDialog(ctx).title("确定添加到冷冻室").content(names.toString()).positiveText("确定").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                                                                                                                                                                   public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which2) {
                                                                                                                                                                       if (which2 == DialogAction.POSITIVE) {
                                                                                                                                                                           for (int i = 0; i < which.length; i++) {
                                                                                                                                                                               String pkName = pkNames.get(which[i]);
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
                                                                                                                                                                                       Bitmap bitmap = null;
                                                                                                                                                                                       if (drawable != null) {
                                                                                                                                                                                           bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                                                                                                                                                       }
                                                                                                                                                                                       byte[] bitInfo = null;
                                                                                                                                                                                       if (bitmap != null) {
                                                                                                                                                                                           bitInfo = Xutils.bitmap2Bytes(bitmap);
                                                                                                                                                                                       }
                                                                                                                                                                                       PkInfo tmp = new PkInfo(pkName, description, bitInfo, EnumInfo.appStatus.ENABLE.getValue(), EnumInfo.appType.THREE.getValue());
                                                                                                                                                                                       tmp.setExb2(1);
                                                                                                                                                                                       DbUtils.insertPkInfo(tmp);
                                                                                                                                                                                   }
                                                                                                                                                                               }
                                                                                                                                                                           }
                                                                                                                                                                           Xutils.tShort("已添加到冷冻室，但暂未冻结~");

                                                                                                                                                                       }
                                                                                                                                                                   }
                                                                                                                                                               }).show();


                                                                                                                                                               return true;
                                                                                                                                                           }
                                                                                                                                                       }).positiveText("确定").negativeText("取消").show();
                                                                                                                                                   } catch (Exception e) {
                                                                                                                                                       e.printStackTrace();
                                                                                                                                                   }


                                                                                                                                               }

                                                                                                                                           }
                                                                                                                                       }

                                                                                   ).show();
                                                                               }
                                                                           }

        ));

        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("抽屉网格大小-列数").setSubtitle(SPHelper.getGridColumns(ctx) + "").setOnclick(new TextItem.OnClickListener() {
            public void onClick(final TextItem textItem) {
                ArrayList<String> items = new ArrayList();
                int i = 0;
                for (int j = 2; j <= 12; j++) {
                    if (SPHelper.getGridColumns(ctx) == j) {
                        i = j - 2;
                    }
                    items.add(j + "");
                }
                Xutils.themeMaterialDialog(ctx).title("抽屉网格大小-列数").items(items).itemsCallbackSingleChoice(i, new MaterialDialog.ListCallbackSingleChoice() {

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








        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("全部解冻").setSubtitle("卸载本软件前请先解冻，若无法直接卸载，请先到设备管理器取消激活").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                Xutils.tShort("正在解冻...");
                new MyFt.BatStopTask().execute(EnumInfo.typeBatStop.ENABLE_ALL.getValue());
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("全部应用添加到桌面").setSubtitle("为全部应用创建桌面快捷方式，便于冷冻后打开").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                Xutils.tShort("正在创建桌面快捷方式...");
                new MyTaskBatSendToDesk().execute();
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("初始化应用列表").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {

                Xutils.themeMaterialDialog(ctx).title("初始化应用列表").content("应用列表和排序将会重建~")
                        .positiveText("确定").neutralText("取消").onAny(new MaterialDialog.SingleButtonCallback() {


                                                                        @Override
                                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                            if (which == DialogAction.POSITIVE) {
                                                                                DbUtils.deletePkAll();
                                                                                DataInitHelper.MyTask task = new DataInitHelper.MyTask();
                                                                                task.execute();
                                                                                Xutils.tShort("正在初始化应用列表...");
                                                                            }
                                                                        }
                                                                    }
                ).show();

            }
        }));

        final String str = SPHelper.getFcLog(ctx);
        if (Xutils.isNotEmptyOrNull(str)) {
            addItem(new DividerItem(ctx));
            addItem(new TextItem(ctx, "").setTitle("FC LOG").setSubtitle("若你愿意帮助开发者，完善和改进此程序，请点击反馈给开发者").setOnclick(new TextItem.OnClickListener() {
                public void onClick(TextItem paramAnonymousTextItem) {
                    Xutils.tShort("log已粘贴至剪切板！");
                    Xutils.copyText(ctx, str);
                    Xutils.cmdWxUserExe(Constant.author_wx);
                    SPHelper.setFcLog(ctx, "");
                }
            }));
        }



        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("导入配置").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                showFileChooser();
            }
        }));
        addItem(new TextItem(ctx, "").setTitle("导出配置").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                csvOutput();
            }


        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("关于应用").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                startActivity(new Intent(ctx, AboutAc.class));
            }
        }));



        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("应用图标").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsAppIcon(ctx, b);
                isNeedUpdate = true;
            }
        }).setDefaultValue(SPHelper.isAppIcon(ctx)));



        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("自动开启夜间模式").setSubtitle("开始时间18:00，结束时间6:00").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsAutoNight(ctx, b);
                isNeedUpdate = true;
            }
        }).setDefaultValue(SPHelper.isAutoNight(ctx)));


        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("主界面半屏风格").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setIsHS(ctx, b);
                isNeedUpdate = true;
            }
        }).setDefaultValue(SPHelper.isHS(ctx)));


        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("锁屏自动冷冻").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                isNeedUpdate = true;
                SPHelper.setIsAutoStop(ctx, b);
            }
        }).setDefaultValue(SPHelper.isAutoStop(ctx)));


        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("采集活动悬浮窗【左上角】").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                checkboxItemFloat = checkboxItem;
                SPHelper.setIsAcFloat(ctx, b);
                if (b) {
                    if (!SettingsCompat.canDrawOverlays(ctx)) {
                        try {
                            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                        }catch (Exception e){
                            Xutils.tShort("请先开启悬浮窗~");
                            e.printStackTrace();
                        }

                    }
                    MyFt.getActivityDlg(ctx);
                } else {
                    TasksWindow.dismiss(ctx);
                }

            }
        }).setDefaultValue(SPHelper.isAcFloat(ctx)));


        
        
        
        
        
        

        addItem(new HeaderItem(this).setTitle("T9搜索"));
        addItem(new TextItem(ctx, "").setTitle("搜索范围").setSubtitle(EnumInfo.typeT9.getTypeT9(SPHelper.getTypeT9(ctx)).getStrName()).setOnclick(new TextItem.OnClickListener() {
            public void onClick(final TextItem textItem) {
                try {
                    int typeT9 = SPHelper.getTypeT9(ctx);
                    String[] items = new String[]{EnumInfo.typeT9.APP.getStrName(), EnumInfo.typeT9.ACTIVITY.getStrName(), EnumInfo.typeT9.APP_ACTIVITY.getStrName()};
                    if (typeT9 < items.length + 1) {
                        items[typeT9 - 1] = items[typeT9 - 1] + "   ✔";
                    }
                    Xutils.themeMaterialDialog(ctx).items(items).itemsCallback(new MaterialDialog.ListCallback() {

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


        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("匹配结果仅一项自动打开").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setT9Auto(ctx, b);
            }
        }).setDefaultValue(SPHelper.isT9Auto(ctx)));
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("每次打开重置搜索条件").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                SPHelper.setT9Clear(ctx, b);
            }
        }).setDefaultValue(SPHelper.isT9Clear(ctx)));










        addItem(new HeaderItem(this).setTitle("Pro"));

        addItem(new TextItem(this, "").setTitle("排序").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                if (checkNotPro()) {
                    return;
                }
                startActivity(new Intent(ctx, SortActivity.class));
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("批量添加活动").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                if (checkNotPro()) {
                    return;
                }
                startActivity(new Intent(ctx, BatActivitys.class));
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("批量添加Shell").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                if (checkNotPro()) {
                    return;
                }
                startActivity(new Intent(ctx, ShellBatActivity.class));


            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("悬浮球").setSubtitle("点击【返回】，上【最近任务】，左【收藏的活动】，下【桌面】，右【采集活动】，长按【拖动位置】，长下拉【隐藏到通知栏】，长上拉【截屏】").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                if (checkNotPro()) {
                    return;
                }
                checkboxItemBall = checkboxItem;
                SPHelper.setIsFloatBall(ctx, b);
//                Intent localIntent = new Intent(ctx, FloatBallService.class);
                if (b) {
                    if (!SettingsCompat.canDrawOverlays(ctx)) {
                        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                    }
                    ctx.startService(new Intent(ctx, FloatBallService.class));
                } else {
                    ctx.stopService(new Intent(ctx, FloatBallService.class));
                    FloatWindowManager.removeBallView(ctx);
                }
            }
        }).setDefaultValue(SPHelper.isFloatBall(ctx)));



    }

    private void csvOutput() {

        try {
            ArrayList<CardInfo> cardInfos = DbUtils.getCardInfos(EnumInfo.homeTab.LIKE.getValue());
            if (Xutils.listNotNull(cardInfos)) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < cardInfos.size(); i++) {
                    CardInfo info = cardInfos.get(i);
                    if (info != null && info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
                        stringBuffer.append(info.getTitle());
                        stringBuffer.append("|");
                        stringBuffer.append(info.getPackageName());
                        stringBuffer.append("|");
                        stringBuffer.append(info.getClassName());
                        stringBuffer.append("\n");
                    }

                }
                String path = MyPath.getPathConfig() + "/" + Xutils.getTimeYMDHM_(new Date().getTime()) + ".txt";
                Xutils.writeTxtFile(path, stringBuffer.toString());
                Xutils.tLong("已导出到" + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyTaskBatSendToDesk extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                ArrayList<PkInfo> pkInfos = DbUtils.getPkInfos(null);
                if (Xutils.listNotNull(pkInfos)) {
                    for (PkInfo tmp : pkInfos
                            ) {
                        CardInfo info = new CardInfo();
                        info.setTitle(tmp.getName());
                        info.setPackageName(tmp.getPackageName());
                        info.setImg(tmp.getIcon());
                        info.setType(EnumInfo.cType.START_APP.getValue());
                        info.setDisabled(tmp.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
                        Xutils.sendToDesktop(SettingNew.this, info, false, false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Xutils.tShort("全部桌面快捷方式已创建完成~");
        }
    }

    public static void shortCutAdd(Context context, String action, String name) {
        shortCutAdd(context, action, name, "");
    }

    public static void shortCutAdd(Context context, String action, String name, String pkName) {
        Intent intent = new Intent(context, ShortcutActionActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.putExtra("android.intent.extra.UID", 0);
        if (Xutils.isNotEmptyOrNull(pkName)) {
            intent.putExtra("pkName", pkName);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent send = new Intent();
        intent.putExtra("action", action);
        send.putExtra("android.intent.extra.shortcut.INTENT", intent);
        send.putExtra("android.intent.extra.shortcut.NAME", name);
        send.putExtra("random", new Random().nextLong());
        try {
            intent.putExtra("duplicate", false);
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
        if (isNeedUpdate) {
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
        Xutils.themeMaterialDialog(ctx).title("解锁Pro版，仅需5元！").content("Pro版更多彰显的是情怀~").positiveText("支付宝购买").neutralText("取消").onAny(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {

                    Xutils.themeMaterialDialog(ctx).title("支付宝购买，仅需5.0元！").content("购买后请获取订单号【点击支付宝订单详情-》创建时间-》长按复制订单号-》输入订单号-》完成购买】注：重装或换机需重新购买，价格为0.01元").positiveText("购买").negativeText("取消").neutralText("输入订单号").onAny(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            if (which == DialogAction.NEUTRAL) {

                                final EditText editText = new EditText(ctx);
                                editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                Xutils.themeMaterialDialog(ctx).title("输入订单号").customView(editText, false).positiveText("好").negativeText("不").neutralText("打开支付宝").onAny(new MaterialDialog.SingleButtonCallback() {
                                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.NEUTRAL) {
                                            Xutils.startAppByPackageName(ctx, MyPackageInfo.pk_zfb, 3);
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
                                String payOrderCheck = SPHelper.getPayOrderCheck(ctx);
                                if (!payOrderCheck.contains(Xutils.getTimeTodayYMD())) {
                                    String payCheck = payOrderCheck + "," + Xutils.getTimeTodayYMD();
                                    SPHelper.setPayOrderCheck(ctx, payCheck);
                                }
                                MainActivity.pay(ctx);
                            }

                        }
                    }).show();


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
            backDo();
        }

        return true;
    }
    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }


    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("txt/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件导入"), 103);
        } catch (android.content.ActivityNotFoundException ex) {
            Xutils.tShort("没有找到文件管理器");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    String path = Xutils.getPathFromUri(ctx, uri);
                    importData(path);
                }
                break;
        }
    }


    private void importData(String path) {
        boolean canImport = false;
        File file = new File(path);
        String fileName = "";
        if (file.exists()) {
            fileName = file.getName();
            if (fileName.endsWith(".txt")) {
                canImport = true;
            }
        }
        if (canImport) {
            List<String> list = DbUtils.importTxt(new File(path));
            DbUtils.importCardFromTxt(list, false);
            Xutils.tShort(fileName + " 导入成功~");
        } else {
            Xutils.tShort("文件格式非法~");
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (SettingsCompat.canDrawOverlays(ctx)) {
//            SPHelper.setIsAcFloat(ctx, true);
//            SPHelper.setIsFloatBall(ctx, true);
//
//            if (checkboxItemBall != null) {
//                checkboxItemBall.setDefaultValue(true);
//            }
//            if (checkboxItemFloat != null) {
//                checkboxItemFloat.setDefaultValue(true);
//            }
//        } else {
//            SPHelper.setIsAcFloat(ctx, false);
//            SPHelper.setIsFloatBall(ctx, false);
//            if (checkboxItemBall != null) {
//                checkboxItemBall.setDefaultValue(false);
//            }
//            if (checkboxItemFloat != null) {
//                checkboxItemFloat.setDefaultValue(false);
//            }
//        }
//
//    }
}
