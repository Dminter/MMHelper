//package com.zncm.dminter.mmhelper.ft;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.dexafree.materialList.card.Card;
//import com.dexafree.materialList.card.CardProvider;
//import com.dexafree.materialList.listeners.OnDismissCallback;
//import com.dexafree.materialList.listeners.RecyclerItemClickListener;
//import com.dexafree.materialList.view.MaterialListView;
//import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
//import com.github.mrengineer13.snackbar.SnackBar;
//import com.zncm.dminter.mmhelper.Constant;
//import com.zncm.dminter.mmhelper.MainActivity;
//import com.zncm.dminter.mmhelper.MyApplication;
//import com.zncm.dminter.mmhelper.OpenInentActivity;
//import com.zncm.dminter.mmhelper.R;
//import com.zncm.dminter.mmhelper.autocommand.AndroidCommand;
//import com.zncm.dminter.mmhelper.data.CardInfo;
//import com.zncm.dminter.mmhelper.data.EnumInfo;
//import com.zncm.dminter.mmhelper.data.FzInfo;
//import com.zncm.dminter.mmhelper.data.db.DbUtils;
//import com.zncm.dminter.mmhelper.utils.Xutils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyFt2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
//    private Context mContext;
//    private MaterialListView mListView;
//    ArrayList<String> wxCmdList;
//    Activity ctx;
//    ArrayList<CardInfo> cardInfos;
//    MaterialDialog dialog;
//    List<Card> cards;
//
//    RecyclerView.LayoutManager layoutManager;
//    String packageName = "";
//    SwipeRefreshLayout swipeLayout;
//    public ArrayList<FzInfo> pkInfos = new ArrayList<>();
//    static MaterialDialog progressDlg;
//    GetDate getDate;
//
//    public static MyFt2 newInstance() {
//        return new MyFt2();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.ft_my, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ctx = getActivity();
//
//        pkInfos = MyApplication.pkInfos;
//
//        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
//        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setProgressViewOffset(false, 100, 400);
//        swipeLayout.setColorSchemeColors(
//                getResources().getColor(R.color.material_purple_accent_400)
//        );
//        mListView = (MaterialListView) view.findViewById(R.id.material_listview);
//        if (getArguments() != null) {
//            Bundle bundle = getArguments();
//            packageName = bundle.getString("packageName");
//        }
//        layoutManager = new GridLayoutManager(getActivity(), 3);
//        mListView.setLayoutManager(layoutManager);
//        mListView.setHasFixedSize(true);
//        mListView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
////        mListView.setItemAnimator(new SlideInLeftAnimator());
////        mListView.getItemAnimator().setAddDuration(300);
////        mListView.getItemAnimator().setRemoveDuration(300);
//        mListView.setOnDismissCallback(new OnDismissCallback() {
//            @Override
//            public void onDismiss(@NonNull Card card, final int position) {
//                final CardInfo info = cardInfos.get(position);
//                if (info != null) {
//                    info.setStatus(EnumInfo.cStatus.DELETE.getValue());
//                    DbUtils.updateCard(info);
//                }
//                cardInfos.remove(position);
//
//
//                new SnackBar.Builder(ctx)
//                        .withOnClickListener(new SnackBar.OnMessageClickListener() {
//                            @Override
//                            public void onMessageClick(Parcelable token) {
//                                info.setStatus(EnumInfo.cStatus.NORMAL.getValue());
//                                DbUtils.updateCard(info);
//                                fillArray();
//                            }
//                        })
//                        .withMessage("移除这张活动卡!")
//                        .withActionMessage("撤销")
//                        .withStyle(SnackBar.Style.DEFAULT)
//                        .withBackgroundColorId(R.color.material_purple_200)
//                        .withDuration(SnackBar.LONG_SNACK)
//                        .show();
//
//
//            }
//
//        });
//        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull Card card, int position) {
//                String tag = (String) card.getTag();
//                if (Xutils.isEmptyOrNull(tag)) {
//                    return;
//                }
//                if (tag.startsWith(Constant.DB_TAG_PRE_)) {
//                    CardInfo info = cardInfos.get(position);
//                    clickCard(ctx, info);
//                }
//
//            }
//
//            @Override
//            public void onItemLongClick(@NonNull Card card, final int position) {
//                Log.d("LONG_CLICK", "" + card.getTag());
//
//                longClick(position);
//
//            }
//        });
//
//
//        fillArray();
//
//
//    }
//
//    private void longClick(final int position) {
//        final CardInfo info = cardInfos.get(position);
//        if (info != null) {
////                    if (info.getType() == EnumInfo.cType.WX.getValue() || info.getType() == EnumInfo.cType.QQ.getValue()) {
////
////
////
////
////                        return;
////                    } else
//            if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
//
//                int status = info.getStatus();
////
////                        String tips = "启用";
////                        if (status == EnumInfo.cStatus.DISABLED.getValue()) {
////                            Xutils.exec(Constant.common_pm_e_p + info.getPackageName());
////                            tips = "启用";
////                        } else {
////                            Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
////                            tips = "停用";
////                        }
////                        Xutils.snTip(ctx, "已" + tips);
////                        onRefresh();
//
//
//                if (packageName.equals(EnumInfo.homeTab.DISABLED.getValue())) {
//                    appInfo(info.getPackageName());
//                    return;
//                }
//
//
//                String title[] = new String[]{"停用", "应用信息"};
//                new MaterialDialog.Builder(getActivity())
//                        .items(title)
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                switch (which) {
//                                    case 0:
//                                        Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
//                                        Xutils.snTip(ctx, "已停用" + info.getTitle());
//                                        cardInfos.remove(position);
//                                        cards.remove(position);
//                                        mListView.getAdapter().notifyDataSetChanged();
//                                        break;
//                                    case 1:
//                                        appInfo(info.getPackageName());
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
//            String title[] = new String[]{like ? "取消收藏" : "收藏", "修改", "删除", index > 0 ? "取消置顶" : "置顶", "添加到桌面", "分组", "停用"};
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
//                                    case 6:
//                                        Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
//                                        Xutils.snTip(ctx, "已停用" + info.getTitle());
//                                        break;
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
//
//    private void appInfo(String packageName) {
//        Uri packageURI = Uri.parse("package:" + packageName);
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//        startActivity(intent);
//    }
//
//    public static void sendToDesk(Activity ctx, CardInfo info) {
//        Drawable drawable = null;
//        if (info.getType() == EnumInfo.cType.URL.getValue()) {
//            drawable = ctx.getResources().getDrawable(R.mipmap.ic_bookmark_border_white_48dp);
//        } else if (Xutils.isNotEmptyOrNull(info.getPackageName())) {
//            drawable = Xutils.getAppIcon(info.getPackageName());
//        }
//        Xutils.sendToDesktop(ctx, OpenInentActivity.class, info.getTitle(),
//                drawable, info.getPackageName(), info.getClassName(), info.getId());
//        Xutils.snTip(ctx, Constant.add_shortcut);
//    }
//
//    public static void clickCard(Activity activity, CardInfo info) {
//        if (info == null) {
//            return;
//        }
//        int ret = 0;
//        if (info.getType() == EnumInfo.cType.WX.getValue()) {
//            ret = Xutils.cmdExe(Constant.wx_am_pre + Constant.wx_ChattingUI + info.getCmd());
//        } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
//            Xutils.openUrl(info.getCmd());
//        } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
//            try {
//                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + info.getCmd();
//                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
////            if (MyApplication.disabled_pkgs.contains(info.getPackageName())) {
////                Xutils.exec(Constant.common_pm_e_p + info.getPackageName());
////                MyApplication.disabled_pkgs.remove(info.getPackageName());
////            }
//
////            progressDlg = new MaterialDialog.Builder(ctx)
////                    .title("正在处理")
////                    .content("请稍后...")
////                    .progress(true, 100).show();
//            RunOpenActivity runOpenActivity = new RunOpenActivity(info, Constant.open_ac_attempt);
//            runOpenActivity.execute();
//        } else if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
//            Xutils.startAppByPackageName(activity, info.getPackageName(), Constant.attempt);
//        }
//        if (ret == AndroidCommand.noRoot) {
//            Xutils.snTip(activity, Constant.no_root);
//        }
//    }
//
//
//    static class RunOpenActivity extends AsyncTask<Void, Integer, Integer> {
//        CardInfo info;
//        int attempt;
//
//        public RunOpenActivity(CardInfo info, int attempt) {
//            this.info = info;
//            this.attempt = attempt;
//        }
//
//        protected Integer doInBackground(Void... params) {
//            int ret = AndroidCommand.noRoot;
//            try {
//                ret = Xutils.cmdExe(Constant.common_am_pre + info.getPackageName() + Constant.common_am_div + info.getClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return ret;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Integer ret) {
//            super.onPostExecute(ret);
//            if (ret == AndroidCommand.appDisable) {
//                if (attempt > 0) {
//                    Xutils.exec(Constant.common_pm_e_p + info.getPackageName());
//                    RunOpenActivity runOpenActivity = new RunOpenActivity(info, --attempt);
//                    runOpenActivity.execute();
//                }
//            } else if (ret == AndroidCommand.noRoot) {
//                Xutils.tShort(Constant.no_root);
//            } else {
////                progressDlg.dismiss();
//            }
//
//        }
//    }
//
//
//    class GetDate extends AsyncTask<Void, Void, Void> {
//
//        protected Void doInBackground(Void... params) {
//            try {
//
//
//                cards = new ArrayList<>();
//                if (Xutils.isNotEmptyOrNull(packageName)) {
//                    if (packageName.equals(EnumInfo.homeTab.ENABLE.getValue()) || packageName.equals(EnumInfo.homeTab.DISABLED.getValue())) {
//                        int status = EnumInfo.cStatus.DISABLED.getValue();
//                        String cmd = Constant.common_pm_e;
//                        if (packageName.equals(EnumInfo.homeTab.ENABLE.getValue())) {
//                            status = EnumInfo.cStatus.ENABLE.getValue();
//                            cmd = Constant.common_pm_e;
//                        } else {
//                            status = EnumInfo.cStatus.DISABLED.getValue();
//                            cmd = Constant.common_pm_d;
//                        }
//
////                if (ctx.bShowSystem) {
////                    cmd += Constant.common_pm_s;
////                } else {
////                    cmd += Constant.common_pm_3;
////                }
//                        cmd += Constant.common_pm_3;
//                        String retSTR = Xutils.exec(cmd);
//                        if (Xutils.isEmptyOrNull(retSTR)) {
//                            return null;
//                        }
//                        retSTR = retSTR.replaceAll("package:", "");
//                        String pkgStr[] = retSTR.split("\\n");
//                        cardInfos = new ArrayList<CardInfo>();
//                        for (int i = 0; i < pkgStr.length; i++) {
//                            cardInfos.add(new CardInfo(pkgStr[i], status));
////                    if (packageName.equals(EnumInfo.homeTab.DISABLED.getValue())) {
////                        MyApplication.disabled_pkgs.add(pkgStr[i]);
////                    }
//                        }
////                Xutils.debug("pkgs==>" + pkgs);
//                    } else {
//                        cardInfos = DbUtils.getCardInfos(packageName);
//                    }
//
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            if (Xutils.listNotNull(cardInfos)) {
//                for (CardInfo info : cardInfos) {
//                    cards.add(cardChatUI(info));
//                }
//                mListView.getAdapter().clearAll();
//                mListView.getAdapter().addAll(cards);
//            }
//        }
//    }
//
//    public void fillArray() {
//
//
//        getDate = new GetDate();
//        getDate.execute();
//
//    }
//
//
//    private Card cardChatUI(CardInfo info) {
//        Card card;
//        String tag = Constant.DB_TAG_PRE_ + info.getId();
//        String title = info.getTitle();
//        String description = info.getDescription();
//        int cardType = info.getCard_type();
//        int bgColor = info.getCard_color();
//        if (bgColor == 0) {
//            bgColor = getResources().getColor(R.color.material_light_white);
//        }
//        String pkgName = info.getPackageName();
//        int cardLayout = R.layout.material_image_with_buttons_card_m2;
////        if (cardType == EnumInfo.cardType.material_basic_buttons_card.getValue()) {
////            cardLayout = R.layout.material_image_with_buttons_card_m1;
//////            cardLayout = R.layout.material_small_image_card_m1;
////
////
////        } else if (cardType == EnumInfo.cardType.material_basic_image_buttons_card_layout.getValue()) {
////            cardLayout = R.layout.material_basic_image_buttons_card_layout;
////        } else if (cardType == EnumInfo.cardType.material_image_with_buttons_card.getValue()) {
////            cardLayout = R.layout.material_image_with_buttons_card;
////        } else if (cardType == EnumInfo.cardType.material_list_card_layout.getValue()) {
////            cardLayout = R.layout.material_list_card_layout;
////        } else if (cardType == EnumInfo.cardType.material_small_image_card.getValue()) {
////            cardLayout = R.layout.material_image_with_buttons_card_m1;
//////            cardLayout = R.layout.material_small_image_card_m1;
////        } else if (cardType == EnumInfo.cardType.material_welcome_card_layout.getValue()) {
////            cardLayout = R.layout.material_welcome_card_layout_m1;
////        }
//
//
//        Drawable drawable = null;
//        if (Xutils.isNotEmptyOrNull(pkgName)) {
//            ApplicationInfo app = Xutils.getAppInfo(pkgName);
//            if (app != null) {
//                PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
//                drawable = app.loadIcon(pm);
//                description = app.loadLabel(pm).toString();
//
//            }
//            if (Xutils.isNotEmptyOrNull(packageName)) {
//                if (packageName.equals(EnumInfo.homeTab.LIKE.getValue()) || packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
//                    if (Xutils.isNotEmptyOrNull(description) && Xutils.isEmptyOrNull(info.getEx1()) && info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
//                        Xutils.debug("info====>>" + info);
//                        info.setEx1(description);
//                        DbUtils.updateCard(info);
//                    }
//                } else {
////                    layoutManager = new GridLayoutManager(getActivity(), 3);
////                    mListView.setLayoutManager(layoutManager);
////                    cardLayout = R.layout.material_image_with_buttons_card_m1;
////                    drawable = null;
//                    if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
//                        title = description;
//                    }
//                    description = "";
//                }
//            }
//
//        }
//
//
//        if (drawable == null) {
//            drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//        }
//
//
//        if (info.getType() == EnumInfo.cType.WX.getValue()) {
//            bgColor = getResources().getColor(R.color.material_green_accent_100);
//        } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
//            bgColor = getResources().getColor(R.color.material_deep_orange_100);
//        } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
//            bgColor = getResources().getColor(R.color.material_orange_100);
//            drawable = getResources().getDrawable(R.mipmap.ic_bookmark_border_white_48dp);
//        }
//
//
//        int titleColor = getResources().getColor(R.color.material_light_black);
//
//        int index = info.getExi2();
//        if (index > 0) {
//            titleColor = getResources().getColor(R.color.material_amber_accent_700);
//        }
//
//        return new Card.Builder(ctx)
//                .setTag(tag)
//                .withProvider(new CardProvider())
//                .setLayout(cardLayout)
//                .setTitle(title)
//                .setTitleColor(titleColor)
//                .setBackgroundColor(bgColor)
//                .setDescription(description)
//                .setDrawable(drawable)
//                .endConfig()
//                .build();
//    }
//
////    @Override
////    public void onActivityCreated(Bundle savedInstanceState) {
////        super.onActivityCreated(savedInstanceState);
////        setHasOptionsMenu(true);
////    }
////
////    @Override
////    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
////        if (ctx instanceof HomeActivity) {
////            ctx.getMenuInflater().inflate(R.menu.main, menu);
////        }
////        super.onCreateOptionsMenu(menu, inflater);
////    }
////
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////
////            case R.id.action_enable:
////                Intent intent = new Intent(ctx, QTActivity.class);
////                intent.putExtra("homeTab", EnumInfo.homeTab.ENABLE.getPosition());
////                startActivity(intent);
////                break;
////
////            case R.id.action_disabled:
////                Intent intent2 = new Intent(ctx, QTActivity.class);
////                intent2.putExtra("homeTab", EnumInfo.homeTab.DISABLED.getPosition());
////                startActivity(intent2);
////                break;
////
////            case R.id.action_add_wxtalk:
////
////                talkUI(null, EnumInfo.cType.WX.getValue(), "微信-直接聊天", "朋友姓名", "朋友微信号");
////
////                break;
////
////            case R.id.action_add_url:
////
////                talkUI(null, EnumInfo.cType.URL.getValue(), "书签-直达网页or应用页面", "标题", "http://");
////
////                break;
////            case R.id.action_add_qqtalk:
////
////                talkUI(null, EnumInfo.cType.QQ.getValue(), "QQ-直接聊天", "好友姓名", "好友QQ号");
////
////                break;
////            case R.id.action_add_openactivity:
////
////
////                initActivity(null);
////                break;
////            case R.id.action_toactivity:
////                startActivity(new Intent(ctx, SettingActivity.class));
////                break;
////            case R.id.action_noroot:
////                startActivity(new Intent(ctx, NoRootActivity.class));
////                break;
////
////
////        }
////        return super.onOptionsItemSelected(item);
////    }
//
//
//    private void initActivity(final CardInfo info) {
//        View view = LayoutInflater.from(ctx).inflate(
//                R.layout.view_card, null);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
//        final EditText activityName = (EditText) view.findViewById(R.id.activityName);
//        final EditText packageName = (EditText) view.findViewById(R.id.packageName);
//        final EditText className = (EditText) view.findViewById(R.id.className);
//
//        if (info != null) {
//            if (Xutils.isNotEmptyOrNull(info.getTitle())) {
//                activityName.setText(info.getTitle());
//            }
//            if (Xutils.isNotEmptyOrNull(info.getPackageName())) {
//                packageName.setText(info.getPackageName());
//            }
//            if (Xutils.isNotEmptyOrNull(info.getClassName())) {
//                className.setText(info.getClassName());
//            }
//        }
//
//        dialog = new MaterialDialog.Builder(ctx)
//                .title("配置活动")
//                .customView(view, false)
//                .positiveText("好")
//                .negativeText("不")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        String acName = activityName.getText().toString();
//                        String pName = packageName.getText().toString();
//                        String cName = className.getText().toString();
//                        if (info != null) {
//                            info.setTitle(acName);
//                            info.setPackageName(pName);
//                            info.setClassName(cName);
//                            DbUtils.updateCard(info);
//                        } else {
//                            CardInfo card = new CardInfo(pName, cName, acName);
//                            DbUtils.insertCard(card);
//                        }
//                        dialog.dismiss();
//                        fillArray();
//                    }
//                })
//                .show();
//    }
//
//
//    @Override
//    public void onRefresh() {
//        fillArray();
//        swipeLayout.setRefreshing(false);
//    }
//}
