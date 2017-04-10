package com.zncm.dminter.mmhelper.ft;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.OpenInentActivity;
import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.adapter.CardAdapter;
import com.zncm.dminter.mmhelper.adapter.MxItemClickListener;
import com.zncm.dminter.mmhelper.autocommand.AndroidCommand;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.FzInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.BottomSheetDlg;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFt extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mListView;
    private Activity ctx;
    public ArrayList<CardInfo> cardInfos;
    private MaterialDialog dialog;
    private RecyclerView.LayoutManager layoutManager;
    private String packageName = "";
    private SwipeRefreshLayout swipeLayout;
    public ArrayList<FzInfo> pkInfos = new ArrayList<>();
    private GetDate getDate;
    private CardAdapter cardAdapter;

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
                getResources().getColor(R.color.material_purple_accent_400)
        );
        mListView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            packageName = bundle.getString("packageName");
        }
        layoutManager = new GridLayoutManager(getActivity(), 4);
        mListView.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(ctx) {
            @Override
            public void setData(int position, CardViewHolder holder) {
                if (!Xutils.listNotNull(cardInfos) || position >= cardInfos.size()) {
                    return;
                }
                CardInfo info = cardInfos.get(position);
                String title = info.getTitle();
                int bgColor = info.getCard_color();
                if (bgColor == 0) {
                    bgColor = getResources().getColor(R.color.material_light_white);
                }
                String pkgName = info.getPackageName();
                Drawable drawable = null;
                if (info.getType() == EnumInfo.cType.WX.getValue()) {
                    bgColor = getResources().getColor(R.color.material_green_accent_100);
                } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
                    bgColor = getResources().getColor(R.color.material_deep_orange_100);
                } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
                    bgColor = getResources().getColor(R.color.material_orange_100);
                    drawable = getResources().getDrawable(R.mipmap.ic_bookmark_border_white_48dp);
                } else if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
                    if (info.isDisabled()) {
                        bgColor = getResources().getColor(R.color.material_grey_200);
                    } else {
                        bgColor = getResources().getColor(R.color.material_light_white);
                    }
                }
                int titleColor = getResources().getColor(R.color.material_light_black);
                int index = info.getExi2();
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
                Bitmap bitmap = bytes2Bimap(info.getImg());
                if (bitmap != null) {
                    holder.image.setImageBitmap(bitmap);
                }
                if (bgColor != 0) {
                    holder.llBg.setBackgroundColor(bgColor);
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
    }

    private Bitmap bytes2Bimap(byte[] b) {
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

    public static void sendToDesk(Activity ctx, CardInfo info) {
        Drawable drawable = null;
        if (info.getType() == EnumInfo.cType.URL.getValue()) {
            drawable = ctx.getResources().getDrawable(R.mipmap.ic_bookmark_border_white_48dp);
        } else if (Xutils.isNotEmptyOrNull(info.getPackageName())) {
            drawable = Xutils.getAppIcon(info.getPackageName());
        }
        Xutils.sendToDesktop(ctx, OpenInentActivity.class, info.getTitle(),
                drawable, info.getPackageName(), info.getClassName(), info.getId());
        Xutils.snTip(ctx, Constant.add_shortcut);
    }

    public static void clickCard(Activity activity, CardInfo info) {
        if (info == null) {
            return;
        }
        int ret = 0;
        if (info.getType() == EnumInfo.cType.WX.getValue()) {
            ret = Xutils.cmdExe(Constant.wx_am_pre + Constant.wx_ChattingUI + info.getCmd());
        } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
            Xutils.openUrl(info.getCmd());
        } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + info.getCmd();
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
            appNewStatus(info);
            RunOpenActivity runOpenActivity = new RunOpenActivity(info, Constant.open_ac_attempt);
            runOpenActivity.execute();
        } else if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
            appNewStatus(info);
            Xutils.startAppByPackageName(activity, info.getPackageName(), Constant.attempt);
        }
        if (ret == AndroidCommand.noRoot) {
            Xutils.snTip(activity, Constant.no_root);
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
                    if (packageName.equals(EnumInfo.homeTab.APPS.getValue()) || packageName.equals(EnumInfo.homeTab.BAT_STOP.getValue())) {
                        cardInfos = new ArrayList<>();
                        ArrayList<PkInfo> tmps = new ArrayList<>();
//                        tmps = DbUtils.getPkInfos();
                        if (packageName.equals(EnumInfo.homeTab.APPS.getValue())) {
                            tmps = DbUtils.getPkInfos();
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


    private void initActivity(final CardInfo info) {
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
        dialog = new MaterialDialog.Builder(ctx)
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
                        } else {
                            CardInfo card = new CardInfo(pName, cName, acName);
                            DbUtils.insertCard(card);
                        }
                        dialog.dismiss();
                        fillArray();
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
        }


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

        //冻结，解冻
        Map<String, Object> map = new HashMap<>();
        map.put("text", info.isDisabled() ? "解冻" : "冻结");
        map.put("key", "-1");
        list.add(map);

        //移除、添加到冷冻室
        final PkInfo pkInfo = DbUtils.getPkOne(info.getPackageName());
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
        //删除
        Map<String, Object> map7 = new HashMap<>();
        map7.put("text", "shortcut");
        map7.put("key", "-7");
        list.add(map7);
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
                        if (info.isDisabled()) {
                            info.setDisabled(false);
//                            Xutils.exec("pm able " + info.getPackageName());
                        } else {
                            Xutils.exec("pm disable " + info.getPackageName());
                            info.setDisabled(true);
                        }
                        MyFt.this.cardInfos.set(position, info);
                        MyFt.this.mListView.getAdapter().notifyDataSetChanged();
                        pkInfo.setStatus(!info.isDisabled() ? EnumInfo.appStatus.ENABLE.getValue() : EnumInfo.appStatus.DISABLED.getValue());
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

                        String str = Xutils.getLaunchClassNameByPkName(MyFt.this.ctx, info.getPackageName());
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


//    private void longClick(final CardInfo info, final int position) {
//        if (info != null) {
//            if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
//                String title[] = new String[]{"停用", "应用信息", "删除"};
//                new MaterialDialog.Builder(getActivity())
//                        .items(title)
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                switch (which) {
//                                    case 0:
//                                        Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
//                                        info.setDisabled(true);
//                                        cardInfos.set(position, info);
//                                        mListView.getAdapter().notifyDataSetChanged();
//                                        PkInfo pk = DbUtils.getPkOne(info.getPackageName());
//                                        if (pk != null && pk.getStatus() == EnumInfo.appStatus.ENABLE.getValue()) {
//                                            pk.setStatus(EnumInfo.appStatus.DISABLED.getValue());
//                                            DbUtils.updatePkInfo(pk);
//                                        }
//                                        break;
//                                    case 1:
//                                        appInfo(info.getPackageName());
//                                        break;
//                                    case 2:
//                                        pk = DbUtils.getPkOne(info.getPackageName());
//                                        if (pk != null) {
//                                            DbUtils.deletePk(pk);
//                                        }
//                                        cardInfos.remove(position);
//                                        mListView.getAdapter().notifyDataSetChanged();
//                                        break;
//                                }
//                            }
//                        })
//                        .show();
//
//
//                return;
//            }
//            final boolean like = info.getExi1() == 1;
//            final int index = info.getExi2();
//            String title[] = new String[]{like ? "取消收藏" : "收藏", "修改", "删除", index > 0 ? "取消置顶" : "置顶", "添加到桌面", "分组"};
//
//            try {
//                new MaterialDialog.Builder(getActivity())
//                        .items(title)
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
////                                Toast.makeText(Activity.this, which + ": " + text + ", ID = " + view.getId(), Toast.LENGTH_SHORT).show();
//                                switch (which) {
//                                    case 0:
//                                        if (like) {
//                                            info.setExi1(0);
//                                        } else {
//                                            info.setExi1(1);
//
//                                            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.LIKE.getValue()));
//
//                                        }
//                                        DbUtils.updateCard(info);
//
//                                        break;
//                                    case 1:
//                                        if (info.getType() == EnumInfo.cType.WX.getValue() || info.getType() == EnumInfo.cType.QQ.getValue() || info.getType() == EnumInfo.cType.URL.getValue()) {
//                                            if (ctx instanceof MainActivity) {
//                                                MainActivity tmp = (MainActivity) ctx;
//                                                tmp.talkUI(info, 0, "修改", "", "");
//                                            }
//                                        } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
//                                            initActivity(info);
//                                        }
//                                        break;
//                                    case 2:
////                                            final CardInfo info = cardInfos.get(position);
//                                        info.setStatus(EnumInfo.cStatus.DELETE.getValue());
//                                        DbUtils.updateCard(info);
//                                        cardInfos.remove(position);
//                                        new SnackBar.Builder(ctx)
//                                                .withOnClickListener(new SnackBar.OnMessageClickListener() {
//                                                    @Override
//                                                    public void onMessageClick(Parcelable token) {
//                                                        info.setStatus(EnumInfo.cStatus.NORMAL.getValue());
//                                                        DbUtils.updateCard(info);
//                                                        fillArray();
//                                                    }
//                                                })
//                                                .withMessage("移除这张活动卡!")
//                                                .withActionMessage("撤销")
//                                                .withStyle(SnackBar.Style.DEFAULT)
//                                                .withBackgroundColorId(R.color.material_purple_200)
//                                                .withDuration(SnackBar.LONG_SNACK)
//                                                .show();
//                                        break;
//                                    case 3:
//                                        if (index > 0) {
//                                            info.setExi2(0);
//                                        } else {
//                                            info.setExi2(DbUtils.getMaxIndex() + 1);
//                                        }
//                                        DbUtils.updateCard(info);
//                                        break;
//                                    case 4:
//                                        sendToDesk(ctx, info);
//                                        break;
//                                    case 5:
//
//
//                                        final ArrayList<String> fzStr = new ArrayList<String>();
//                                        if (!Xutils.listNotNull(pkInfos)) {
//                                            return;
//                                        }
//
//                                        for (FzInfo tmp : pkInfos
//                                                ) {
//                                            if (Xutils.isNotEmptyOrNull(tmp.getName())) {
//                                                fzStr.add(tmp.getName());
//                                            }
//
//                                        }
//
//                                        new MaterialDialog.Builder(ctx)
//                                                .items(fzStr)
//                                                .itemsCallback(new MaterialDialog.ListCallback() {
//                                                    @Override
//                                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                                        info.setExi3(which + 1);
//                                                        DbUtils.updateCard(info);
//                                                        Xutils.snTip(ctx, "已添加☞" + fzStr.get(which));
//                                                    }
//                                                })
//                                                .show();
//                                        break;
//
//
//                                }
//
//                                fillArray();
//                            }
//                        })
//                        .show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }


    @Override
    public void onRefresh() {
        fillArray();
        swipeLayout.setRefreshing(false);
    }
}
