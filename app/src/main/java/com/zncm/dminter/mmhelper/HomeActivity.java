//package com.zncm.dminter.mmhelper;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.github.florent37.materialviewpager.MaterialViewPager;
//import com.github.florent37.materialviewpager.header.HeaderDesign;
//import com.zncm.dminter.mmhelper.data.CardInfo;
//import com.zncm.dminter.mmhelper.data.EnumInfo;
//import com.zncm.dminter.mmhelper.data.FzInfo;
//import com.zncm.dminter.mmhelper.data.RefreshEvent;
//import com.zncm.dminter.mmhelper.data.db.DbUtils;
//import com.zncm.dminter.mmhelper.ft.MyFt;
//import com.zncm.dminter.mmhelper.utils.ColorGenerator;
//import com.zncm.dminter.mmhelper.utils.Xutils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by dminter on 2016/7/26.
// */
//
//public class HomeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, NavigationView.OnNavigationItemSelectedListener {
//    MaterialViewPager mViewPager;
//    public boolean bShowSystem = false;
//    int baseTab = 2;
//    int count = baseTab;
//    Toolbar toolbar;
//    private static HomeActivity instance;
//    ColorGenerator generator;
//    public ArrayList<FzInfo> pkInfos = new ArrayList<>();
//    DrawerLayout drawer;
//    CheckBox mWindowSwitch;
//    Activity ctx;
//
//    public static HomeActivity getInstance() {
//        return instance;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        ctx = this;
//        try {
//            String fzInfo = SPHelper.getFzInfo(this);
//            if (Xutils.isNotEmptyOrNull(fzInfo)) {
//                String[] fzArr = fzInfo.split(",");
//                for (int i = 0; i < fzArr.length; i++) {
//                    String tmp = fzArr[i];
//                    if (Xutils.isNotEmptyOrNull(tmp)) {
//                        pkInfos.add(new FzInfo(i + 1, tmp));
//                    }
//                }
//            }
//            MyApplication.pkInfos.clear();
//            MyApplication.pkInfos.addAll(pkInfos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        count = pkInfos.size() + baseTab;
//        generator = ColorGenerator.MATERIAL;
//        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
//        setTitle("");
//        toolbar = mViewPager.getToolbar();
//        if (toolbar != null) {
//            if (toolbar != null) {
//                setSupportActionBar(toolbar);
//                toolbar.setNavigationIcon(null);
//            }
//            setSupportActionBar(toolbar);
//        }
//        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            MyFt fragment = null;
//            Bundle bundle = null;
//
//            @Override
//            public Fragment getItem(int position) {
//                fragment = new MyFt();
//                bundle = new Bundle();
//                if (position < baseTab) {
//                    bundle.putString("packageName", EnumInfo.homeTab.getHomeTab(position).getValue());
//                } else {
//                    bundle.putString("packageName", pkInfos.get(position - baseTab).getId() + "");
//                }
//                fragment.setArguments(bundle);
//                return fragment;
//            }
//
//            @Override
//            public int getCount() {
//                return count;
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                String title = "";
//                if (position < baseTab) {
//                    title = EnumInfo.homeTab.getHomeTab(position).getStrName();
//                } else {
//                    title = pkInfos.get(position - baseTab).getName();
//                }
//                return title;
//            }
//        });
//
//        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
//            @Override
//            public HeaderDesign getHeaderDesign(final int position) {
////                if (position == 0) {
////                    colorRes = R.color.material_amber_700;
////                } else if (position == 1) {
////                    colorRes = R.color.material_teal_400;
////                }
//                final ImageView logo = (ImageView) findViewById(R.id.logo_white);
//                if (position < baseTab) {
//                    if (position == 0 || position == 1) {
//                        logo.setImageResource(R.mipmap.ic_launcher);
//                    } else {
//                        if (!bShowSystem) {
//                            logo.setImageResource(R.mipmap.ic_launcher);
//                        } else {
//                            logo.setImageResource(R.mipmap.ic_launcher_system);
//                        }
//                    }
//                    logo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (position == 0 || position == 1) {
//                                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
//                            } else {
//                                if (bShowSystem) {
//                                    bShowSystem = false;
//                                    logo.setImageResource(R.mipmap.ic_launcher);
//
//                                } else {
//                                    bShowSystem = true;
//                                    logo.setImageResource(R.mipmap.ic_launcher_system);
//
//                                }
//                            }
//                        }
//                    });
//                    return HeaderDesign.fromColorResAndUrl(
//                            R.color.colorPrimary,
//                            "");
//                } else {
//                    final String packageName = pkInfos.get(position - baseTab).getName();
//                    logo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(HomeActivity.this, SettingActivity.class));
//                        }
//                    });
////                    int color = generator.getColor(packageName);
//                    return HeaderDesign.fromColorResAndUrl(
//                            R.color.colorPrimary,
//                            "");
//                }
//            }
//        });
//        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
//        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
//        ArrayList<CardInfo> tmps = DbUtils.getCardInfos(null);
//        if (!Xutils.listNotNull(tmps)) {
//            DbUtils.cardUpdate();
//            DbUtils.cardXm();
//            List<String> list = Xutils.importTxt(this, R.raw.init_2016_08_09_13_36);
//            DbUtils.importCardFromTxt(list, true);
//            //引导页面
//            startActivity(new Intent(this, GuidViewActivity.class));
//        }
//        EventBus.getDefault().register(this);
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View headerView = navigationView.getHeaderView(0);
//        mWindowSwitch = (CheckBox) headerView.findViewById(R.id.mWindowSwitch);
//        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
//        mWindowSwitch.setOnCheckedChangeListener(this);
//        if (getResources().getBoolean(R.bool.use_watching_service)) {
//            startService(new Intent(this, WatchingService.class));
//        }
//
//        ivLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Xutils.openUrl(Constant.update_url);
//            }
//        });
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
//            backToDesk(this);
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//    public static void backToDesk(Activity activity) {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        activity.startActivity(intent);
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (MyApplication.getInstance().isOpenInent) {
//            backToDesk(this);
//            MyApplication.getInstance().isOpenInent = false;
//        }
//        resetUI();
//        NotificationActionReceiver.cancelNotification(this);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onMessageEventAsync(RefreshEvent event) {
//        int type = event.type;
//        if (type == EnumInfo.RefreshEnum.FZ.getValue()) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
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
//                initActivity(null);
//                break;
//            case R.id.action_toactivity:
//                startActivity(new Intent(ctx, SettingActivity.class));
//                break;
//            case R.id.action_share:
//                Xutils.sendTo(ctx, Constant.share_content);
//                break;
//
//
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
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
//        new MaterialDialog.Builder(ctx)
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
//                    }
//                })
//                .show();
//    }
//
//
//    public void talkUI(final CardInfo info, final int type, String title, String hint1, String hint2) {
//        View wxView = LayoutInflater.from(ctx).inflate(
//                R.layout.view_card, null);
//        wxView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
//        final EditText name = (EditText) wxView.findViewById(R.id.activityName);
//        final EditText wx = (EditText) wxView.findViewById(R.id.className);
//        ((EditText) wxView.findViewById(R.id.packageName)).setVisibility(View.GONE);
//        name.setHint(hint1);
//        wx.setHint(hint2);
//        if (info != null) {
//            if (Xutils.isNotEmptyOrNull(info.getTitle())) {
//                name.setText(info.getTitle());
//            }
//            if (Xutils.isNotEmptyOrNull(info.getCmd())) {
//                wx.setText(info.getCmd());
//            }
//        }
//
//        new MaterialDialog.Builder(ctx)
//                .title(title)
//                .customView(wxView, false)
//                .positiveText("好")
//                .negativeText("不")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        String wxName = name.getText().toString();
//                        String cmd = wx.getText().toString();
//                        if (info != null) {
//                            info.setTitle(wxName);
//                            info.setCmd(cmd);
//                            DbUtils.updateCard(info);
//                        } else {
//                            CardInfo card = new CardInfo(type, cmd, wxName);
//                            DbUtils.insertCard(card);
//                        }
//                        dialog.dismiss();
////                        fillArray();
//                    }
//                })
//                .show();
//    }
//
//
//    private void resetUI() {
//        mWindowSwitch.setChecked(SPHelper.isShowWindow(this));
//        if (getResources().getBoolean(R.bool.use_accessibility_service)) {
//            if (WatchingAccessibilityService.getInstance() == null) {
//                mWindowSwitch.setChecked(false);
//            }
//        }
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (SPHelper.isShowWindow(this) && !(getResources().getBoolean(R.bool.use_accessibility_service) && WatchingAccessibilityService.getInstance() == null)) {
//            NotificationActionReceiver.showNotification(this, false);
//        }
//    }
//
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked && buttonView == mWindowSwitch && getResources().getBoolean(R.bool.use_accessibility_service)) {
//            if (WatchingAccessibilityService.getInstance() == null) {
//
//                new MaterialDialog.Builder(ctx)
//                        .title("开启无障碍")
//                        .content(R.string.dialog_enable_accessibility_msg)
//                        .positiveText("好")
//                        .negativeText("不")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                Intent intent = new Intent();
//                                intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
//                                startActivity(intent);
//                            }
//                        })
//                        .onNegative(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                resetUI();
//                            }
//                        })
//                        .canceledOnTouchOutside(false)
//                        .show();
//                SPHelper.setIsShowWindow(this, isChecked);
//                return;
//            }
//        }
//        if (buttonView == mWindowSwitch) {
//            SPHelper.setIsShowWindow(this, isChecked);
//            if (!isChecked) {
//                TasksWindow.dismiss(this);
//            } else {
//                TasksWindow.show(this, getPackageName() + "\n" + getClass().getName());
//            }
//        }
//    }
//}
