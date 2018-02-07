package com.zncm.dminter.mmhelper.ft;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sofaking.iconpack.IconPack;
import com.sofaking.iconpack.IconPackManager;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MainActivity;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.OpenInentActivity;
import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.SPHelper;
import com.zncm.dminter.mmhelper.SettingNew;
import com.zncm.dminter.mmhelper.ShortcutActionActivity;
import com.zncm.dminter.mmhelper.SortActivity;
import com.zncm.dminter.mmhelper.WatchingAccessibilityService;
import com.zncm.dminter.mmhelper.WatchingService;
import com.zncm.dminter.mmhelper.adapter.CardAdapter;
import com.zncm.dminter.mmhelper.adapter.MxItemClickListener;
import com.zncm.dminter.mmhelper.autocommand.AndroidCommand;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.FzInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.dragrecyclerview.CallbackWrap;
import com.zncm.dminter.mmhelper.dragrecyclerview.OnTouchListener;
import com.zncm.dminter.mmhelper.utils.ApkInfoUtils;
import com.zncm.dminter.mmhelper.utils.BottomSheetDlg;
import com.zncm.dminter.mmhelper.utils.ColorGenerator;
import com.zncm.dminter.mmhelper.utils.DataInitHelper;
import com.zncm.dminter.mmhelper.utils.ShellUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ezy.assist.compat.SettingsCompat;

public class MyFt extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mListView;
    private Activity ctx;
    public ArrayList<CardInfo> cardInfos;
    private RecyclerView.LayoutManager layoutManager;
    private String packageName = "";
    private SwipeRefreshLayout swipeLayout;
    public ArrayList<FzInfo> pkInfos = new ArrayList<>();
    private GetDate getDate;
    public CardAdapter cardAdapter;
    public FloatingActionButton actionButton;
    private boolean isSort = false;
    private boolean isAppIcon = false;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ft_myft, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = getActivity();
        pkInfos = MyApplication.fzInfos;
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setProgressViewOffset(false, 100, 400);
        swipeLayout.setColorSchemeColors(
                SPHelper.getThemeColor(ctx)
        );
        mListView = (RecyclerView) view.findViewById(R.id.recyclerView);
        actionButton = (FloatingActionButton) view.findViewById(R.id.action_button);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            packageName = bundle.getString("packageName");
        }
        isAppIcon = SPHelper.isAppIcon(ctx);
        layoutManager = new GridLayoutManager(getActivity(), SPHelper.getGridColumns(this.ctx));
        mListView.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(ctx) {
            @Override
            public void setData(int position, final CardViewHolder holder) {
                if (!Xutils.listNotNull(cardInfos) || position >= cardInfos.size()) {
                    return;
                }
                final CardInfo info = cardInfos.get(position);
                String title = info.getTitle();
                int titleColor = getResources().getColor(R.color.material_light_black);
                String pkgName = info.getPackageName();
                Drawable drawable = null;
                if (info.getType() == EnumInfo.cType.URL.getValue()) {
                    drawable = getResources().getDrawable(R.mipmap.ic_url);
                } else if (info.getType() == EnumInfo.cType.CMD.getValue()) {
                    drawable = getResources().getDrawable(R.mipmap.ic_shell);
                } else if (info.getType() == EnumInfo.cType.SHORT_CUT_SYS.getValue()) {
                    drawable = getResources().getDrawable(R.mipmap.ic_shortcut);
                } else {
                    drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                }
                if (info.getType() == EnumInfo.cType.START_APP.getValue() && info.isDisabled()) {
                    titleColor = getResources().getColor(R.color.material_amber_accent_700);
                } else {
                    titleColor = getResources().getColor(R.color.material_light_black);
                }
                final int index = info.getExi2();
                if (index > 0) {
                    titleColor = getResources().getColor(R.color.material_amber_accent_700);
                }
                if (Xutils.isNotEmptyOrNull(title)) {
                    holder.title.setText(title);
                }
                if (info.getImg() == null) {
                    PkInfo pkInfo = DbUtils.getPkOne(info.getPackageName());
                    if (pkInfo != null) {
                        info.setImg(pkInfo.getIcon());
                        DbUtils.updateCard(info);
                    }
                }
                if (info.getImg() == null) {
                    if (drawable != null) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        info.setImg(Xutils.bitmap2Bytes(bitmap));
                        DbUtils.updateCard(info);
                    }
                    if (Xutils.isNotEmptyOrNull(pkgName)) {
                        ApplicationInfo app = Xutils.getAppInfo(pkgName);
                        if (app != null) {
                            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                            drawable = app.loadIcon(pm);
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            info.setImg(Xutils.bitmap2Bytes(bitmap));
                            DbUtils.updateCard(info);
                        }
                    }
                }
                if (isAppIcon) {
                    holder.image.setVisibility(View.VISIBLE);

                    Bitmap bitmap = bytes2Bimap(info.getImg());
                    if (bitmap != null) {
                        holder.image.setImageBitmap(bitmap);
                    }
                } else {
                    holder.image.setVisibility(View.GONE);
                    int bgColor = ColorGenerator.MATERIAL.getColor(!Xutils.isEmptyOrNull(info.getPackageName()) ? info.getPackageName() : "");
                    if (bgColor != 0) {
                        holder.llBg.setBackgroundColor(bgColor);
                        titleColor = getResources().getColor(R.color.material_light_white);
                    }
                }
                if (titleColor != 0) {
                    holder.title.setTextColor(titleColor);
                }
                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (!Xutils.listNotNull(cardInfos) || position >= cardInfos.size()) {
                            return;
                        }
                        CardInfo info = cardInfos.get(position);
                        if (isLongClick) {
                            if (isSort) {
                                return;
                            }
                            longClick(info, position);
                        } else {
                            if (info.getType() == EnumInfo.cType.START_APP.getValue() && info.isDisabled()) {
                                info.setDisabled(false);
                                cardInfos.set(position, info);
                                mListView.getAdapter().notifyDataSetChanged();
                            }
                            clickCard(ctx, info);
                        }
                    }
                });
            }
        };
        mListView.setAdapter(cardAdapter);
        fillArray();
        actionButton.setBackgroundTintList(ColorStateList.valueOf(SPHelper.getThemeColor(ctx)));
        if ((packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) || (packageName.equals(EnumInfo.homeTab.ALL.getValue()))) {
            actionButton.setVisibility(View.VISIBLE);
            if (packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                actionButton.setImageResource(R.mipmap.ic_dj);
            }
        } else {
            actionButton.setVisibility(View.GONE);
        }
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                    new MyFt.BatStopTask().execute(EnumInfo.typeBatStop.DISABLE_LESS.getValue());
                } else if (packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
                    String[] items = {"采集活动", "添加活动", "微信聊天", "QQ聊天", "书签", "Shell", "快捷方式"};
                    Xutils.themeMaterialDialog(ctx).items(items).itemsCallback(new MaterialDialog.ListCallback() {

                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            switch (position) {
                                case 0:
                                    if (ctx instanceof MainActivity) {
                                        openAcFloat(ctx);
//                                        MyFt.getActivityDlg(ctx);
                                    }
                                    break;
                                case 1:
                                    initActivity(null, -1);
                                    break;
                                case 2:
                                    talkUI(null, EnumInfo.cType.WX.getValue(), "微信-直接聊天", "朋友姓名", "朋友微信号");
                                    break;
                                case 3:
                                    talkUI(null, EnumInfo.cType.QQ.getValue(), "QQ-直接聊天", "好友姓名", "好友QQ号");
                                    break;
                                case 4:
                                    talkUI(null, EnumInfo.cType.URL.getValue(), "书签-直达网页or应用页面", "标题", "http://");
                                    break;
                                case 5:
                                    talkUI(null, EnumInfo.cType.CMD.getValue(), "Shell命令", "Shell名称", Constant.zfb_sys);
                                    break;
                                case 6:
                                    talkUI(null, EnumInfo.cType.SHORT_CUT_SYS.getValue(), "快捷方式", "快捷方式名称", "#Intent;action=com.tencent.mm.action.BIZSHORTCUT...;end");
                                    break;
                            }
                        }
                    }).show();
                }
            }
        });

        actionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                    ArrayList<String> items = new ArrayList();
                    final ArrayList<PkInfo> pkInfos = DbUtils.getPkInfosBatStop(0);
                    if (Xutils.listNotNull(pkInfos)) {
                        for (PkInfo tmp : pkInfos
                                ) {
                            items.add(tmp.getName());
                        }
                    }
                    Xutils.themeMaterialDialog(ctx).title("添加到冷冻室").items(items).itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            for (int i = 0; i < which.length; i++) {
                                PkInfo pkInfo = pkInfos.get(which[i]);
                                if (pkInfo != null) {
                                    pkInfo.setExb2(1);
                                    DbUtils.updatePkInfo(pkInfo);
                                }
                            }
                            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                            return true;
                        }
                    }).positiveText("确定").negativeText("取消").neutralText("彻底冻结").onAny(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.NEUTRAL) {
                                Xutils.tShort("正在彻底冻结...");
                                ArrayList<PkInfo> pkInfos = DbUtils.getPkInfosBatStop(1);
                                if (Xutils.listNotNull(pkInfos)) {
                                    for (PkInfo tmp : pkInfos
                                            ) {
                                        if (!tmp.getPackageName().equals(Constant.app_pkg)) {
                                            tmp.setStatus(EnumInfo.appStatus.ENABLE.getValue());
                                            DbUtils.updatePkInfo(tmp);
                                        }
                                    }
                                }
                                new MyFt.BatStopTask().execute(EnumInfo.typeBatStop.DISABLE_ALL.getValue());
                            }
                        }
                    }).show();

                }
                return true;
            }
        });


        if (SPHelper.isHS(ctx)) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setImageResource(R.mipmap.ic_setting);
            actionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    startActivity(new Intent(ctx, SettingNew.class));
                }
            });
            actionButton.setOnLongClickListener(null);
        }
        if (ctx instanceof SortActivity) {
            isSort = true;
            final ItemTouchHelper helper = new ItemTouchHelper(new CallbackWrap(cardAdapter));
            helper.attachToRecyclerView(mListView);
            mListView.addOnItemTouchListener(new OnTouchListener(mListView) {
                @Override
                public void onLongClick(RecyclerView.ViewHolder vh) {
                    if (vh.getLayoutPosition() != cardInfos.size()) {
                        helper.startDrag(vh);
                    }
                }
            });
            swipeLayout.setEnabled(false);
        }


    }





    public static void openAcFloat(Context ctx) {
        if (WatchingAccessibilityService.getInstance() == null) {
            MyFt.getActivityDlg(ctx);
            return;
        }
        SPHelper.setIsAcFloat(ctx, true);
        if (!SettingsCompat.canDrawOverlays(ctx)) {
            try {
                ctx.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
            } catch (Exception e) {
                Xutils.tShort("请先开启悬浮窗~");
                e.printStackTrace();
            }
        }
        ctx.startService(new Intent(ctx, WatchingService.class));
    }

    public void talkUI(final CardInfo info, final int type, String title, String hint1, String hint2) {
        View wxView = LayoutInflater.from(ctx).inflate(
                R.layout.view_card, null);
        wxView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        final EditText name = (EditText) wxView.findViewById(R.id.activityName);
        final EditText wx = (EditText) wxView.findViewById(R.id.className);
        ((EditText) wxView.findViewById(R.id.packageName)).setVisibility(View.GONE);
        name.setHint(hint1);
        wx.setHint(hint2);
        if (info != null) {
            if (Xutils.isNotEmptyOrNull(info.getTitle())) {
                name.setText(info.getTitle());
            }
            if (Xutils.isNotEmptyOrNull(info.getCmd())) {
                wx.setText(info.getCmd());
            }
        }
        Xutils.themeMaterialDialog(ctx)
                .title(title)
                .customView(wxView, false)
                .positiveText("好")
                .negativeText("不")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String wxName = name.getText().toString();
                        String cmd = wx.getText().toString();
                        if (info != null) {
                            info.setTitle(wxName);
                            info.setCmd(cmd);
                            DbUtils.updateCard(info);
                        } else {
                            CardInfo card = new CardInfo(type, cmd, wxName);
                            DbUtils.insertCard(card);
                        }
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void getActivityDlg(final Context ctx) {
        if (WatchingAccessibilityService.getInstance() == null) {
            if (ctx instanceof Activity) {
                final Activity mCtx = (Activity) ctx;

                if (mCtx instanceof ShortcutActionActivity || mCtx instanceof OpenInentActivity) {
                    openSetting(ctx);
                    return;
                }
                Xutils.themeMaterialDialog(ctx)
                        .title("开启无障碍")
                        .content(R.string.dialog_enable_accessibility_msg)
                        .positiveText("好")
                        .negativeText("不")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                openSetting(ctx);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                updateServiceStatus(mCtx);
                            }
                        })
                        .canceledOnTouchOutside(false)
                        .show();
            } else {
                openSetting(ctx);
            }
        }
    }

    public static void openSetting(Context ctx) {
        Intent intent = new Intent();
        intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


    public static void updateServiceStatus(Activity activity) {
        boolean serviceEnabled = false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) activity.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(activity.getPackageName() + "/.WatchingAccessibilityService")) {
                serviceEnabled = true;
                break;
            }
        }
        if (serviceEnabled) {
            activity.startService(new Intent(activity, WatchingService.class));
        }


    }

    public static Bitmap bytes2Bimap(byte[] b) {
        if (b == null) {
            return null;
        }
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    //app信息界面
    public static void appInfo(Activity activity, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivity(intent);
    }



    public static void clickCard(final Context activity, PkInfo pkInfo) {
        CardInfo info = new CardInfo();
        info.setTitle(pkInfo.getName());
        info.setPackageName(pkInfo.getPackageName());
        info.setImg(pkInfo.getIcon());
        info.setType(EnumInfo.cType.START_APP.getValue());
        info.setDisabled(pkInfo.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
        clickCard(activity,info);
    }

    public static void clickCard(final Context activity, CardInfo info) {
        if (info == null) {
            return;
        }
        int ret = 0;
        if (info.getType() == EnumInfo.cType.WX.getValue()) {
            ret = Xutils.cmdWxUserExe(info.getCmd());
        } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
            Xutils.openUrl(info.getCmd());
        } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + info.getCmd();
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Xutils.tShort("请检查是否安装QQ~");
                e.printStackTrace();
            }
        } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
            appNewStatus(info);
            RunOpenActivity runOpenActivity = new RunOpenActivity(info, Constant.open_ac_attempt);
            runOpenActivity.execute();
        } else if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
            appNewStatus(info);
            Xutils.startAppByPackageName(activity, info.getPackageName(), Constant.attempt);
        } else if (info.getType() == EnumInfo.cType.CMD.getValue()) {
            String cmd = info.getCmd();
            if (cmd.contains(";") || cmd.startsWith("am")) {
                try {
                    String arr[] = cmd.split(";");
                    if (arr != null && arr.length > 0) {
                        for (String tmp : arr
                                ) {
                            try {
                                ret = Xutils.cmdExe(tmp);
                                if (ret == AndroidCommand.appDisable) {
                                    if (Xutils.isNotEmptyOrNull(tmp) && tmp.contains("/")) {
                                        String arrs[] = cmd.split("/");
                                        if (arrs != null && arrs.length > 0 && arrs.length == 2) {
                                            String pkgName = arrs[0];
                                            pkgName = pkgName.substring(pkgName.lastIndexOf(" ") + 1, pkgName.length());
                                            if (Xutils.isNotEmptyOrNull(pkgName)) {
                                                info.setPackageName(pkgName);
                                                DbUtils.updateCard(info);
                                                Xutils.exec("pm enable " + pkgName);
                                                appNewStatus(info);
                                                Xutils.cmdExe(tmp);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            final ShellUtils.CommandResult result = ShellUtils.execCommand(cmd, true);
            if (result != null) {
                if (Xutils.isNotEmptyOrNull(result.successMsg)) {
                    MaterialDialog dialog = Xutils.themeMaterialDialog(activity).title(cmd).content(result.successMsg).positiveText("知").neutralText("复制").onAny(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.NEUTRAL) {
                                Xutils.copyText(activity, result.successMsg);
                                Xutils.tShort("已复制");
                            }
                        }
                    }).build();
                    BottomSheetDlg.checkSysDlg(activity, dialog);
                    dialog.show();
                } else {
                }

            } else {
                Xutils.tShort("找不到该命令~");
            }
        } else if (info.getType() == EnumInfo.cType.SHORT_CUT_SYS.getValue()) {
            String cmd = info.getCmd();
            try {
                activity.startActivity(Intent.parseUri(cmd, 0));
            } catch (Exception e) {
                try {
                    String pkgName = info.getPackageName();
                    if (Xutils.isNotEmptyOrNull(cmd)) {
                        if (cmd.contains("component")) {
                            pkgName = cmd.substring(1 + (cmd.indexOf("component") + "component".length()), cmd.length());
                            /**
                             *兼容支付宝
                             * alipays://platformapi/startapp?appId=10000007&sourceId=shortcut&source=shortcut#Intent;
                             * component=com.eg.android.AlipayGphone/com.alipay.mobile.quinox.LauncherActivity.alias;B.directly=true;B.fromDesktop=true;end
                             */
                            if (cmd.contains("/")) {
                                pkgName = pkgName.substring(0, pkgName.indexOf("/"));
                            }
                        } else if (cmd.contains("package")) {
                            pkgName = cmd.substring(1 + (cmd.indexOf("package") + "package".length()), cmd.length());
                            if (pkgName.contains(";")) {
                                pkgName = pkgName.substring(0, pkgName.indexOf(";"));
                            }
                        }
                        if (Xutils.isNotEmptyOrNull(pkgName)) {
                            info.setPackageName(pkgName);
                            DbUtils.updateCard(info);
                            Xutils.exec("pm enable " + pkgName);
                            appNewStatus(info);

                            activity.startActivity(Intent.parseUri(cmd, 0));

                        } else {
                            Xutils.tShort("快捷方式错误~");
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Xutils.tShort("快捷方式错误~");
                }
            }
        }
        if (ret == AndroidCommand.noRoot) {
            Xutils.tShort(Constant.no_root);
        }
    }

    public static void appNewStatus(CardInfo info) {
        PkInfo pk = DbUtils.getPkOne(info.getPackageName());
        if (pk != null && pk.getStatus() == EnumInfo.appStatus.DISABLED.getValue()) {
            pk.setStatus(EnumInfo.appStatus.ENABLE.getValue());
            DbUtils.updatePkInfo(pk);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
        }
    }


    public static class RunOpenActivity extends AsyncTask<Void, Integer, Integer> {
        CardInfo info;
        int attempt;

        public RunOpenActivity(CardInfo info, int attempt) {
            this.info = info;
            this.attempt = attempt;
        }

        protected Integer doInBackground(Void... params) {
            int ret = AndroidCommand.noRoot;
            try {
                if (info.getType() == EnumInfo.cType.START_APP.getValue() && info.isDisabled()) {
                    info.setDisabled(false);
                }

                ret = Xutils.cmdExe(Constant.common_am_pre + info.getPackageName() + Constant.common_am_div + info.getClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer ret) {
            super.onPostExecute(ret);
            if (ret == AndroidCommand.appDisable) {
                if (attempt > 0) {
                    Xutils.exec(Constant.common_pm_e_p + info.getPackageName());
                    RunOpenActivity runOpenActivity = new RunOpenActivity(info, --attempt);
                    runOpenActivity.execute();
                } else {
                    Xutils.tShort(Constant.no_open);
                }
            } else if (ret == AndroidCommand.noRoot) {
                Xutils.tShort(Constant.no_root);
            }

        }
    }


    class GetDate extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {
                if (Xutils.isNotEmptyOrNull(packageName)) {
                    if (packageName.equals(EnumInfo.homeTab.SUGGEST_ACTIVITY.getValue())) {

                    } else if (packageName.equals(EnumInfo.homeTab.APPS.getValue()) || packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                        cardInfos = new ArrayList<>();
                        ArrayList<PkInfo> tmps = new ArrayList<>();
                        if (packageName.equals(EnumInfo.homeTab.APPS.getValue())) {
                            tmps = DbUtils.getPkInfos(null, EnumInfo.pkStatus.NORMAL.getValue());
                        } else if (packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                            tmps = DbUtils.getPkInfosBatStop(1);
                        }
                        for (PkInfo tmp : tmps
                                ) {
                            CardInfo info = new CardInfo();
                            info.setTitle(tmp.getName());
                            info.setPackageName(tmp.getPackageName());
                            info.setImg(tmp.getIcon());
                            info.setType(EnumInfo.cType.START_APP.getValue());
                            info.setDisabled(tmp.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
                            cardInfos.add(info);
                        }
                    } else {
                        cardInfos = DbUtils.getCardInfos(packageName);
                        if (packageName.equals(EnumInfo.homeTab.LIKE.getValue())) {

                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cardAdapter.setItems(cardInfos);
        }
    }


    public void fillArray() {
        getDate = new GetDate();
        getDate.execute();
    }


    private void initActivity(final CardInfo info, final int position) {
        View view = LayoutInflater.from(ctx).inflate(
                R.layout.view_card, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        final EditText activityName = (EditText) view.findViewById(R.id.activityName);
        final EditText packageName = (EditText) view.findViewById(R.id.packageName);
        final EditText className = (EditText) view.findViewById(R.id.className);

        if (info != null) {
            if (Xutils.isNotEmptyOrNull(info.getTitle())) {
                activityName.setText(info.getTitle());
            }
            if (Xutils.isNotEmptyOrNull(info.getPackageName())) {
                packageName.setText(info.getPackageName());
            }
            if (Xutils.isNotEmptyOrNull(info.getClassName())) {
                className.setText(info.getClassName());
            }
        }
        dialog = Xutils.themeMaterialDialog(ctx)
                .title("配置活动")
                .customView(view, false)
                .positiveText("好")
                .negativeText("不")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String acName = activityName.getText().toString();
                        String pName = packageName.getText().toString();
                        String cName = className.getText().toString();
                        if (info != null) {
                            info.setTitle(acName);
                            info.setPackageName(pName);
                            info.setClassName(cName);
                            DbUtils.updateCard(info);
                            if (position != -1) {
                                cardInfos.set(position, info);
                                cardAdapter.notifyDataSetChanged();
                            }
                        } else {
                            CardInfo card = new CardInfo(pName, cName, acName);
                            DbUtils.insertCard(card);
                            fillArray();
                        }
                        dialog.dismiss();

                    }
                })
                .show();
    }


    private void longClick(final CardInfo info, final int position) {
        if (info == null) {
            return;
        }
        if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
            initAppBS(info, position);
        } else {

            initAppCard(info, position);
        }
    }

    private void initAppCard(final CardInfo info, final int position) {


        /**
         *建议的活动【收藏+发送到桌面】
         */
        final ArrayList<Map<String, Object>> list = new ArrayList<>();

        final boolean like = info.getExi1() == 1;
        final boolean top = info.getExi2() > 0;
        if (packageName.equals(EnumInfo.homeTab.SUGGEST_ACTIVITY.getValue())) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("text", like ? "取消收藏" : "收藏");
            mapa.put("key", "a");
            list.add(mapa);
            Map<String, Object> mapb = new HashMap<>();
            mapb.put("text", "添加到桌面");
            mapb.put("key", "-b");
            list.add(mapb);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("text", like ? "取消收藏" : "收藏");
            map.put("key", "-1");
            list.add(map);
            Map<String, Object> map2 = new HashMap<>();
            map2.put("text", "修改");
            map2.put("key", "-2");
            list.add(map2);
            Map<String, Object> map3 = new HashMap<>();
            map3.put("text", "删除");
            map3.put("key", "-3");
            list.add(map3);
            Map<String, Object> map4 = new HashMap<>();
            map4.put("text", top ? "取消置顶" : "置顶");
            map4.put("key", "-4");
            list.add(map4);
            Map<String, Object> map5 = new HashMap<>();
            map5.put("text", "添加到桌面");
            map5.put("key", "-5");
            list.add(map5);
            Map<String, Object> map6 = new HashMap<>();
            map6.put("text", "分组");
            map6.put("key", "-6");
            list.add(map6);
            Map<String, Object> map7 = new HashMap<>();
            map7.put("text", "批量删除");
            map7.put("key", "-7");
            list.add(map7);
        }


        new BottomSheetDlg(ctx, list, false) {
            @Override
            public void onGridItemClickListener(final int pos) {
                switch (pos) {

                    case 0:
                        if (packageName.equals(EnumInfo.homeTab.SUGGEST_ACTIVITY.getValue())) {
                            info.setExi4(Constant.sort_apps);
                            info.setExi1(1);
                            DbUtils.insertCard(info);
                            Xutils.tShort("已添加，并收藏~");
                        } else {
                            if (like) {
                                info.setExi1(0);
                            } else {
                                info.setExi1(1);
                            }
                            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                            DbUtils.updateCard(info);
                        }
                        break;
                    case 1:
                        if (packageName.equals(EnumInfo.homeTab.SUGGEST_ACTIVITY.getValue())) {
                            Xutils.sendToDesktop(ctx, info);
                        } else {
                            if (info.getType() == EnumInfo.cType.WX.getValue() || info.getType() == EnumInfo.cType.QQ.getValue() || info.getType() == EnumInfo.cType.URL.getValue() || info.getType() == EnumInfo.cType.CMD.getValue() || info.getType() == EnumInfo.cType.SHORT_CUT_SYS.getValue()) {
                                talkUI(info, 0, "修改", "", "");
                            } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
                                initActivity(info, position);
                            }
                            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                        }
                        break;
                    case 2:
                        info.setStatus(EnumInfo.cStatus.DELETE.getValue());
                        DbUtils.updateCard(info);
                        cardInfos.remove(position);
                        cardAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        if (top) {
                            info.setExi2(0);
                        } else {
                            info.setExi2(DbUtils.getMaxIndex() + 1);
                        }
                        DbUtils.updateCard(info);
                        cardInfos.set(position, info);
                        cardAdapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                        break;
                    case 4:
                        Xutils.sendToDesktop(ctx, info);
                        break;
                    case 5:
                        final ArrayList<String> fzStr = new ArrayList<String>();
                        if (!Xutils.listNotNull(pkInfos)) {
                            Xutils.tShort("请先添加分组~");
                            return;
                        }
                        for (FzInfo tmp : pkInfos
                                ) {
                            if (Xutils.isNotEmptyOrNull(tmp.getName())) {
                                fzStr.add(tmp.getName());
                            }

                        }
                        Xutils.themeMaterialDialog(ctx)
                                .items(fzStr)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        info.setExi3(which + 1);
                                        DbUtils.updateCard(info);
                                        Xutils.tShort("已添加到分组->" + (String) fzStr.get(which));
                                    }
                                })
                                .show();
                        break;
                    case 6:


                        ArrayList<String> items = new ArrayList();

                        final ArrayList<CardInfo> cardInfos = MyFt.this.cardInfos;

                        if (Xutils.listNotNull(cardInfos)) {

                            for (CardInfo tmp : cardInfos
                                    ) {
                                items.add(tmp.getTitle());
                            }
                        }
                        Xutils.themeMaterialDialog(ctx).title("批量删除活动").items(items).itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, final Integer[] which, CharSequence[] text) {
                                final StringBuffer names = new StringBuffer();
                                for (int i = 0; i < which.length; i++) {
                                    CardInfo cardInfo = (CardInfo) cardInfos.get(which[i]);
                                    if (cardInfo != null) {
                                        names.append(cardInfo.getTitle()).append("，");
                                    }
                                }

                                Xutils.themeMaterialDialog(ctx).title("确定删除").content(names.toString()).positiveText("确定").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                                    public void onClick(@NonNull MaterialDialog paramAnonymous3MaterialDialog, @NonNull DialogAction which2) {
                                        if (which2 == DialogAction.POSITIVE) {
                                            for (int i = 0; i < which.length; i++) {
                                                CardInfo cardInfo = cardInfos.get(which[i]);
                                                if (cardInfo != null) {
                                                    cardInfo.setStatus(EnumInfo.cStatus.DELETE.getValue());
                                                    DbUtils.updateCard(cardInfo);
                                                }
                                            }
                                            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                                        }
                                    }
                                }).show();

                                return true;
                            }
                        }).positiveText("确定").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();

                        break;

                }


            }

            @Override
            public void onGridItemLongClickListener(int position) {

            }

            @Override
            public void onOutClickListener() {

            }
        }

        ;


    }

    public void initAppBS(final CardInfo info, final int position) {

        final ArrayList<Map<String, Object>> list = new ArrayList<>();
        //应用相关的活动
        final ArrayList<CardInfo> like = DbUtils.getCardInfosByPackageName(info.getPackageName());
        if (Xutils.listNotNull(like)) {
            for (CardInfo tmp : like
                    ) {
                Map<String, Object> map = new HashMap<>();
                map.put("text", tmp.getTitle());
                map.put("key", tmp.getId());
                list.add(map);
            }
        }

        final PkInfo pkInfo = DbUtils.getPkOne(info.getPackageName());
        if (pkInfo == null) {
            return;
        }
        final boolean isDisabled = pkInfo.getStatus() == EnumInfo.appStatus.DISABLED.getValue();

        //冻结，解冻
        Map<String, Object> map = new HashMap<>();
        map.put("text", isDisabled ? "解冻" : "冻结");
        map.put("key", "-1");
        list.add(map);

        //移除、添加到冷冻室
        Map<String, Object> map2 = new HashMap<>();
        map2.put("text", pkInfo.getExb2() == 1 ? "移除冷冻室" : "添加到冷冻室");
        map2.put("key", "-2");
        list.add(map2);
        //应用信息
        Map<String, Object> map3 = new HashMap<>();
        map3.put("text", "应用信息");
        map3.put("key", "-3");
        list.add(map3);
        //添加到桌面
        Map<String, Object> map4 = new HashMap<>();
        map4.put("text", "添加到桌面");
        map4.put("key", "-4");
        list.add(map4);
        //收藏
        Map<String, Object> map5 = new HashMap<>();
        map5.put("text", "收藏");
        map5.put("key", "-5");
        list.add(map5);
        //删除
        Map<String, Object> map6 = new HashMap<>();
        map6.put("text", "删除");
        map6.put("key", "-6");
        list.add(map6);
        Map<String, Object> map7 = new HashMap<>();
        map7.put("text", "快捷启动");
        map7.put("key", "-7");
        list.add(map7);
        Map<String, Object> map8 = new HashMap<>();
        map8.put("text", "卸载");
        map8.put("key", "-8");
        list.add(map8);
        Map<String, Object> map9 = new HashMap<>();
        map9.put("text", "彻底冻结");
        map9.put("key", "-9");
        list.add(map9);
        Map<String, Object> map10 = new HashMap<>();
        map10.put("text", "不常用");
        map10.put("key", "-10");
        list.add(map10);
        new BottomSheetDlg(ctx, list, false) {
            @Override
            public void onGridItemClickListener(int position) {

                if (Xutils.listNotNull(like) && position < like.size()) {

                    CardInfo cardInfo = like.get(position);
                    if (cardInfo != null) {
                        clickCard(ctx, cardInfo);
                    }

                }

                switch (position - like.size()) {

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
                        if (pkInfo.getExb2() == 1) {
                            pkInfo.setExb2(0);
                        } else {
                            pkInfo.setExb2(1);
                        }
                        DbUtils.updatePkInfo(pkInfo);
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                        break;
                    case 2:
                        appInfo(ctx, info.getPackageName());
                        break;
                    case 3:
                        Xutils.sendToDesktop(ctx, info);
                        break;
                    case 4:
                        String str = Xutils.getLaunchClassNameByPkName(ctx, info.getPackageName());
                        pkInfo.setStatus(EnumInfo.appStatus.ENABLE.getValue());
                        DbUtils.updatePkInfo(pkInfo);
                        CardInfo tmpCard = new CardInfo(info.getPackageName(), str, null);
                        tmpCard.setExi4(999);
                        tmpCard.setExi1(1);
                        DbUtils.insertCard(tmpCard);
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
                        Xutils.tShort("已收藏~");
                        break;
                    case 5:
                        DbUtils.deletePk(pkInfo);
                        Xutils.tShort("已删除~");
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                        break;
                    case 6:
                        Xutils.sendToDesktop(ctx, info, true);
                        break;
                    case 7:
                        Xutils.unstallApp(ctx, info.getPackageName());
                        DataInitHelper.refApps(EnumInfo.RefreshEnum.APPS.getValue());
                        DataInitHelper.refApps(EnumInfo.RefreshEnum.BAT_STOP.getValue());
                        break;

                    case 8:
                        Xutils.exec("pm enable " + info.getPackageName());
                        Xutils.exec("pm disable " + info.getPackageName());
                        info.setDisabled(true);
                        pkInfo.setStatus(EnumInfo.appStatus.DISABLED.getValue());
                        DbUtils.updatePkInfo(pkInfo);
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                        break;
                    case 9:
                        pkInfo.setExi1(EnumInfo.pkStatus.HIDDEN.getValue());
                        DbUtils.updatePkInfo(pkInfo);
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
                        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
                        break;

                }


            }

            @Override
            public void onGridItemLongClickListener(int position) {

            }

            @Override
            public void onOutClickListener() {

            }
        };


    }

    //批量冻结冷冻室里面的APP 冷冻已经解冻的，冷冻全部处于冷冻室里的，解冻全部
    public static class BatStopTask
            extends AsyncTask<Integer, Void, Void> {
        int type = EnumInfo.typeBatStop.DISABLE_LESS.getValue();

        protected Void doInBackground(Integer... flag) {
            type = flag[0];
            ArrayList<PkInfo> pkInfos = DbUtils.getPkInfosBatStop(1);
            for (PkInfo info : pkInfos
                    ) {
                if (info.getPackageName().equals(Constant.app_pkg)) {
                    continue;
                }
                if (type == EnumInfo.typeBatStop.DISABLE_LESS.getValue()) {
                    if (info.getStatus() == EnumInfo.appStatus.ENABLE.getValue()) {
                        Xutils.exec("pm disable " + info.getPackageName());
                        info.setStatus(EnumInfo.appStatus.DISABLED.getValue());
                    }
                } else if (type == EnumInfo.typeBatStop.DISABLE_ALL.getValue()) {
                    Xutils.exec("pm disable " + info.getPackageName());
                    info.setStatus(EnumInfo.appStatus.DISABLED.getValue());
                } else if (type == EnumInfo.typeBatStop.ENABLE_ALL.getValue()) {
                    Xutils.exec("pm enable " + info.getPackageName());
                    info.setStatus(EnumInfo.appStatus.ENABLE.getValue());
                }
                DbUtils.updatePkInfo(info);
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.BAT_STOP.getValue()));
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.APPS.getValue()));
            if (type == EnumInfo.typeBatStop.DISABLE_LESS.getValue()) {
                Xutils.tShort("已全部冷冻！");
            } else if (type == EnumInfo.typeBatStop.DISABLE_ALL.getValue()) {
                Xutils.tShort("已彻底冷冻！");
            } else if (type == EnumInfo.typeBatStop.ENABLE_ALL.getValue()) {
                Xutils.tShort("已全部解冻！");
            }

        }
    }


    @Override
    public void onRefresh() {

        fillArray();
        swipeLayout.setRefreshing(false);
    }
}
