package com.zncm.dminter.mmhelper.ft;

import android.app.Activity;
import android.content.Context;
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
import android.os.Parcelable;
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
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.github.mrengineer13.snackbar.SnackBar;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MainActivity;
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
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cn.nekocode.itempool.Item;
import cn.nekocode.itempool.ItemEvent;
import cn.nekocode.itempool.ItemEventHandler;

public class MyFt extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ItemEventHandler {
    private Context mContext;
    private RecyclerView mListView;
    ArrayList<String> wxCmdList;
    Activity ctx;
    public ArrayList<CardInfo> cardInfos;
    MaterialDialog dialog;
//    List<Card> cards;

    RecyclerView.LayoutManager layoutManager;
    String packageName = "";
    SwipeRefreshLayout swipeLayout;
    public ArrayList<FzInfo> pkInfos = new ArrayList<>();
    static MaterialDialog progressDlg;
    GetDate getDate;
    CardAdapter cardAdapter;

    MaterialDialog pb;

    public static MyFt newInstance() {
        return new MyFt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ft_myft, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = getActivity();

        pkInfos = MyApplication.pkInfos;

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
        layoutManager = new GridLayoutManager(getActivity(), 3);
        mListView.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(ctx) {
            @Override
            public void setData(int position, CardViewHolder holder) {


                if (!Xutils.listNotNull(cardInfos) || position >= cardInfos.size()) {
                    return;
                }

                CardInfo info = cardInfos.get(position);


                String tag = Constant.DB_TAG_PRE_ + info.getId();
                String title = info.getTitle();
                String description = info.getDescription();
                int cardType = info.getCard_type();
                int bgColor = info.getCard_color();
                if (bgColor == 0) {
                    bgColor = getResources().getColor(R.color.material_light_white);
                }
                String pkgName = info.getPackageName();
                int cardLayout = R.layout.material_image_with_buttons_card_m2;

//                Drawable drawable = null;
//                if (Xutils.isNotEmptyOrNull(pkgName)) {
//                    ApplicationInfo app = Xutils.getAppInfo(pkgName);
//                    if (app != null) {
//                        PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
//                        drawable = app.loadIcon(pm);
//                        description = app.loadLabel(pm).toString();
//
//                    }
//                    if (Xutils.isNotEmptyOrNull(packageName)) {
//                        if (packageName.equals(EnumInfo.homeTab.LIKE.getValue()) || packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
//                            if (Xutils.isNotEmptyOrNull(description) && Xutils.isEmptyOrNull(info.getEx1()) && info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
//                                Xutils.debug("info====>>" + info);
//                                info.setEx1(description);
//                                DbUtils.updateCard(info);
//                            }
//                        } else {
////                    layoutManager = new GridLayoutManager(getActivity(), 3);
////                    mListView.setLayoutManager(layoutManager);
////                    cardLayout = R.layout.material_image_with_buttons_card_m1;
////                    drawable = null;
//                            if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
//                                title = description;
//                                if (Xutils.isEmptyOrNull(info.getTitle())) {
//                                    info.setTitle(title);
//                                }
//                            }
//                            description = "";
//                        }
//                    }
//
//                }


//                if (drawable == null) {
//                    drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//                }


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
                        bgColor = getResources().getColor(R.color.material_grey_500);
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
                    Xutils.debug("info.getImg() == null");
                    if (drawable != null) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        info.setImg(bitmap2Bytes(bitmap));
                        DbUtils.updateCard(info);
                    }
                    if (Xutils.isNotEmptyOrNull(pkgName)) {
                        ApplicationInfo app = Xutils.getAppInfo(pkgName);
                        if (app != null) {
                            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                            drawable = app.loadIcon(pm);
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            info.setImg(bitmap2Bytes(bitmap));
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
                        Xutils.debug("position::"+position+" "+cardInfos.size());
                        if (!Xutils.listNotNull(cardInfos)|| position >= cardInfos.size()) {
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

    private void appInfo(String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
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
            RunOpenActivity runOpenActivity = new RunOpenActivity(info, Constant.open_ac_attempt);
            runOpenActivity.execute();
        } else if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
            PkInfo pk = DbUtils.getPkOne(info.getPackageName());

            if (pk != null && pk.getStatus() == EnumInfo.appStatus.DISABLED.getValue()) {
                pk.setStatus(EnumInfo.appStatus.ENABLE.getValue());
                DbUtils.updatePkInfo(pk);
            }
            Xutils.startAppByPackageName(activity, info.getPackageName(), Constant.attempt);
        }
        if (ret == AndroidCommand.noRoot) {
            Xutils.snTip(activity, Constant.no_root);
        }
    }

    @Override
    public void onEvent(@NonNull Class<? extends Item> aClass, @NonNull ItemEvent itemEvent) {

    }


    static class RunOpenActivity extends AsyncTask<Void, Integer, Integer> {
        CardInfo info;
        int attempt;

        public RunOpenActivity(CardInfo info, int attempt) {
            this.info = info;
            this.attempt = attempt;
        }

        protected Integer doInBackground(Void... params) {
            int ret = AndroidCommand.noRoot;
            try {
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
            } else {
//                progressDlg.dismiss();
            }

        }
    }


    class GetDate extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                if (Xutils.isNotEmptyOrNull(packageName)) {
                    if (packageName.equals(EnumInfo.homeTab.APPS.getValue())) {
                        cardInfos = new ArrayList<>();
                        ArrayList<PkInfo> tmps = new ArrayList<>();
                        tmps = DbUtils.getPkInfos();
                        for (PkInfo tmp : tmps
                                ) {
                            Xutils.debug("tmps:" + tmp.getName() + " " + tmp.getStatus());
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

            if (Xutils.listNotNull(cardInfos)) {
//                for (CardInfo info : cardInfos) {
//                    cards.add(cardChatUI(info));
//                }
//                mListView.getAdapter().clearAll();
//                mListView.getAdapter().addAll(cards);

            }
            cardAdapter.setItems(cardInfos);
        }
    }

    private byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void fillArray() {


        getDate = new GetDate();
        getDate.execute();

    }


    private Card cardChatUI(CardInfo info) {
        Card card;
        String tag = Constant.DB_TAG_PRE_ + info.getId();
        String title = info.getTitle();
        String description = info.getDescription();
        int cardType = info.getCard_type();
        int bgColor = info.getCard_color();
        if (bgColor == 0) {
            bgColor = getResources().getColor(R.color.material_light_white);
        }
        String pkgName = info.getPackageName();
        int cardLayout = R.layout.material_image_with_buttons_card_m2;
//        if (cardType == EnumInfo.cardType.material_basic_buttons_card.getValue()) {
//            cardLayout = R.layout.material_image_with_buttons_card_m1;
////            cardLayout = R.layout.material_small_image_card_m1;
//
//
//        } else if (cardType == EnumInfo.cardType.material_basic_image_buttons_card_layout.getValue()) {
//            cardLayout = R.layout.material_basic_image_buttons_card_layout;
//        } else if (cardType == EnumInfo.cardType.material_image_with_buttons_card.getValue()) {
//            cardLayout = R.layout.material_image_with_buttons_card;
//        } else if (cardType == EnumInfo.cardType.material_list_card_layout.getValue()) {
//            cardLayout = R.layout.material_list_card_layout;
//        } else if (cardType == EnumInfo.cardType.material_small_image_card.getValue()) {
//            cardLayout = R.layout.material_image_with_buttons_card_m1;
////            cardLayout = R.layout.material_small_image_card_m1;
//        } else if (cardType == EnumInfo.cardType.material_welcome_card_layout.getValue()) {
//            cardLayout = R.layout.material_welcome_card_layout_m1;
//        }


        Drawable drawable = null;
        if (Xutils.isNotEmptyOrNull(pkgName)) {
            ApplicationInfo app = Xutils.getAppInfo(pkgName);
            if (app != null) {
                PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                drawable = app.loadIcon(pm);
                description = app.loadLabel(pm).toString();

            }
            if (Xutils.isNotEmptyOrNull(packageName)) {
                if (packageName.equals(EnumInfo.homeTab.LIKE.getValue()) || packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
                    if (Xutils.isNotEmptyOrNull(description) && Xutils.isEmptyOrNull(info.getEx1()) && info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
                        Xutils.debug("info====>>" + info);
                        info.setEx1(description);
                        DbUtils.updateCard(info);
                    }
                } else {
//                    layoutManager = new GridLayoutManager(getActivity(), 3);
//                    mListView.setLayoutManager(layoutManager);
//                    cardLayout = R.layout.material_image_with_buttons_card_m1;
//                    drawable = null;
                    if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
                        title = description;
                    }
                    description = "";
                }
            }

        }


        if (drawable == null) {
            drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        }


        if (info.getType() == EnumInfo.cType.WX.getValue()) {
            bgColor = getResources().getColor(R.color.material_green_accent_100);
        } else if (info.getType() == EnumInfo.cType.QQ.getValue()) {
            bgColor = getResources().getColor(R.color.material_deep_orange_100);
        } else if (info.getType() == EnumInfo.cType.URL.getValue()) {
            bgColor = getResources().getColor(R.color.material_orange_100);
            drawable = getResources().getDrawable(R.mipmap.ic_bookmark_border_white_48dp);
        }


        int titleColor = getResources().getColor(R.color.material_light_black);

        int index = info.getExi2();
        if (index > 0) {
            titleColor = getResources().getColor(R.color.material_amber_accent_700);
        }

        return new Card.Builder(ctx)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(cardLayout)
                .setTitle(title)
                .setTitleColor(titleColor)
                .setBackgroundColor(bgColor)
                .setDescription(description)
                .setDrawable(drawable)
                .endConfig()
                .build();
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (ctx instanceof HomeActivity) {
//            ctx.getMenuInflater().inflate(R.menu.main, menu);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_enable:
//                Intent intent = new Intent(ctx, QTActivity.class);
//                intent.putExtra("homeTab", EnumInfo.homeTab.ENABLE.getPosition());
//                startActivity(intent);
//                break;
//
//            case R.id.action_disabled:
//                Intent intent2 = new Intent(ctx, QTActivity.class);
//                intent2.putExtra("homeTab", EnumInfo.homeTab.DISABLED.getPosition());
//                startActivity(intent2);
//                break;
//
//            case R.id.action_add_wxtalk:
//
//                talkUI(null, EnumInfo.cType.WX.getValue(), "微信-直接聊天", "朋友姓名", "朋友微信号");
//
//                break;
//
//            case R.id.action_add_url:
//
//                talkUI(null, EnumInfo.cType.URL.getValue(), "书签-直达网页or应用页面", "标题", "http://");
//
//                break;
//            case R.id.action_add_qqtalk:
//
//                talkUI(null, EnumInfo.cType.QQ.getValue(), "QQ-直接聊天", "好友姓名", "好友QQ号");
//
//                break;
//            case R.id.action_add_openactivity:
//
//
//                initActivity(null);
//                break;
//            case R.id.action_toactivity:
//                startActivity(new Intent(ctx, SettingActivity.class));
//                break;
//            case R.id.action_noroot:
//                startActivity(new Intent(ctx, NoRootActivity.class));
//                break;
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


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
        if (info != null) {
            if (info.getType() == EnumInfo.cType.START_APP.getValue()) {
                if (info.isDisabled()) {

                    appInfo(info.getPackageName());
                    return;
                }
                String title[] = new String[]{"停用", "应用信息","删除"};
                new MaterialDialog.Builder(getActivity())
                        .items(title)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
                                        info.setDisabled(true);
                                        cardInfos.set(position, info);
                                        mListView.getAdapter().notifyDataSetChanged();
                                        PkInfo pk = DbUtils.getPkOne(info.getPackageName());
                                        if (pk != null && pk.getStatus() == EnumInfo.appStatus.ENABLE.getValue()) {
                                            pk.setStatus(EnumInfo.appStatus.DISABLED.getValue());
                                            DbUtils.updatePkInfo(pk);
                                        }
                                        break;
                                    case 1:
                                        appInfo(info.getPackageName());
                                        break;
                                    case 2:
                                         pk = DbUtils.getPkOne(info.getPackageName());
                                        if (pk != null ) {
                                            DbUtils.deletePk(pk);
                                        }
                                        cardInfos.remove(position);
                                        mListView.getAdapter().notifyDataSetChanged();
                                        break;
                                }
                            }
                        })
                        .show();


                return;
            }
            final boolean like = info.getExi1() == 1;
            final int index = info.getExi2();
            String title[] = new String[]{like ? "取消收藏" : "收藏", "修改", "删除", index > 0 ? "取消置顶" : "置顶", "添加到桌面", "分组"};

            try {
                new MaterialDialog.Builder(getActivity())
                        .items(title)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                Toast.makeText(Activity.this, which + ": " + text + ", ID = " + view.getId(), Toast.LENGTH_SHORT).show();
                                switch (which) {
                                    case 0:
                                        if (like) {
                                            info.setExi1(0);
                                        } else {
                                            info.setExi1(1);
                                        }
                                        DbUtils.updateCard(info);

                                        break;
                                    case 1:
                                        if (info.getType() == EnumInfo.cType.WX.getValue() || info.getType() == EnumInfo.cType.QQ.getValue() || info.getType() == EnumInfo.cType.URL.getValue()) {
                                            if (ctx instanceof MainActivity) {
                                                MainActivity tmp = (MainActivity) ctx;
                                                tmp.talkUI(info, 0, "修改", "", "");
                                            }
                                        } else if (info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
                                            initActivity(info);
                                        }
                                        break;
                                    case 2:
//                                            final CardInfo info = cardInfos.get(position);
                                        info.setStatus(EnumInfo.cStatus.DELETE.getValue());
                                        DbUtils.updateCard(info);
                                        cardInfos.remove(position);
                                        new SnackBar.Builder(ctx)
                                                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                                    @Override
                                                    public void onMessageClick(Parcelable token) {
                                                        info.setStatus(EnumInfo.cStatus.NORMAL.getValue());
                                                        DbUtils.updateCard(info);
                                                        fillArray();
                                                    }
                                                })
                                                .withMessage("移除这张活动卡!")
                                                .withActionMessage("撤销")
                                                .withStyle(SnackBar.Style.DEFAULT)
                                                .withBackgroundColorId(R.color.material_purple_200)
                                                .withDuration(SnackBar.LONG_SNACK)
                                                .show();
                                        break;
                                    case 3:
                                        if (index > 0) {
                                            info.setExi2(0);
                                        } else {
                                            info.setExi2(DbUtils.getMaxIndex() + 1);
                                        }
                                        DbUtils.updateCard(info);
                                        break;
                                    case 4:
                                        sendToDesk(ctx, info);
                                        break;
                                    case 5:


                                        final ArrayList<String> fzStr = new ArrayList<String>();
                                        if (!Xutils.listNotNull(pkInfos)) {
                                            return;
                                        }

                                        for (FzInfo tmp : pkInfos
                                                ) {
                                            if (Xutils.isNotEmptyOrNull(tmp.getName())) {
                                                fzStr.add(tmp.getName());
                                            }

                                        }

                                        new MaterialDialog.Builder(ctx)
                                                .items(fzStr)
                                                .itemsCallback(new MaterialDialog.ListCallback() {
                                                    @Override
                                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                        info.setExi3(which + 1);
                                                        DbUtils.updateCard(info);
                                                        Xutils.snTip(ctx, "已添加☞" + fzStr.get(which));
                                                    }
                                                })
                                                .show();
                                        break;


                                }

                                fillArray();
                            }
                        })
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
//                cardInfos = new ArrayList<CardInfo>();
//                DbUtils.deletePk();
                initPkInfo(EnumInfo.appStatus.ENABLE.getValue());
                initPkInfo(EnumInfo.appStatus.DISABLED.getValue());

                cardInfos = new ArrayList<>();
                ArrayList<PkInfo> tmps = new ArrayList<>();
                tmps = DbUtils.getPkInfos();
                for (PkInfo tmp : tmps
                        ) {
                    Xutils.debug("tmps:" + tmp.getName() + " " + tmp.getStatus());
                    CardInfo info = new CardInfo();
                    info.setTitle(tmp.getName());
                    info.setPackageName(tmp.getPackageName());
                    info.setImg(tmp.getIcon());
                    info.setType(EnumInfo.cType.START_APP.getValue());
                    info.setDisabled(tmp.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
                    cardInfos.add(info);
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
//            pb.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        if (Xutils.isNotEmptyOrNull(packageName)) {
            if (packageName.equals(EnumInfo.homeTab.APPS.getValue())) {
//                pb =
//                        new MaterialDialog.Builder(ctx)
//                                .title("正在初始化...")
//                                .cancelable(false)
//                                .show();
                MyTask myTask = new MyTask();
                myTask.execute();
            } else {
                fillArray();
            }
        } else {
            fillArray();
        }


//        cardInfos = new ArrayList<CardInfo>();
//        for (int i = 0; i < pkgStr.length; i++) {
//            cardInfos.add(new CardInfo(pkgStr[i], status));
//            //String packageName, String className, String name, Bitmap icon, int status, int type
//            PkInfo tmp = new PkInfo(pkgStr[i], "", pkgStr[i], null, 1, 1);
//            DbUtils.insertPkInfo(tmp);
//        }

        swipeLayout.setRefreshing(false);
    }

    private void initPkInfo(int status) {
        if (packageName.equals(EnumInfo.homeTab.APPS.getValue())) {
            String cmd;
            if (status == EnumInfo.appStatus.ENABLE.getValue()) {
                cmd = Constant.common_pm_e;
            } else {
                cmd = Constant.common_pm_d;
            }

            cmd += Constant.common_pm_3;
            String retSTR = Xutils.exec(cmd);
            retSTR = retSTR.replaceAll("package:", "");
            String pkgStr[] = retSTR.split("\\n");

            for (int i = 0; i < pkgStr.length; i++) {
                String pkgName = pkgStr[i];
                String description = "";
                Drawable drawable = null;
                if (Xutils.isNotEmptyOrNull(pkgName)) {
                    ApplicationInfo app = Xutils.getAppInfo(pkgName);
                    if (app != null) {
                        PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                        drawable = app.loadIcon(pm);
                        description = app.loadLabel(pm).toString();
                    }
                }
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                PkInfo tmp = new PkInfo(pkgName, description, bitmap2Bytes(bitmap), status, EnumInfo.appType.THREE.getValue());
                DbUtils.insertPkInfo(tmp);
                CardInfo info = new CardInfo();
                info.setTitle(tmp.getName());
                info.setPackageName(tmp.getPackageName());
                info.setImg(tmp.getIcon());
                info.setType(EnumInfo.cType.START_APP.getValue());
                info.setDisabled(status == EnumInfo.appStatus.DISABLED.getValue());
//                cardInfos.add(info);
            }

        }
    }
}
