package com.zncm.dminter.mmhelper;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.FzInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.DataInitHelper;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener, ColorChooserDialog.ColorCallback {
    private MyPagerAdapter adapter;
    private ViewPager mViewPager;
    private int baseTab = 4;
    private int count = baseTab;
    private Toolbar toolbar;
    public ArrayList<FzInfo> fzInfos = new ArrayList<>();
    private DrawerLayout drawer;
    private CheckBox mWindowSwitch;
    private MainActivity ctx;
    public ArrayList<MyFt> fragments = new ArrayList<>();
    MaterialDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.material_light_white));
            setSupportActionBar(toolbar);
        }
        try {
            String fzInfo = SPHelper.getFzInfo(this);
            if (Xutils.isNotEmptyOrNull(fzInfo)) {
                String[] fzArr = fzInfo.split(",");
                for (int i = 0; i < fzArr.length; i++) {
                    String tmp = fzArr[i];
                    if (Xutils.isNotEmptyOrNull(tmp)) {
                        fzInfos.add(new FzInfo(i + 1, tmp));
                    }
                }
            }
            MyApplication.fzInfos.clear();
            MyApplication.fzInfos.addAll(fzInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        count = fzInfos.size() + baseTab;
        mViewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(1);
        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        Xutils.initIndicatorTheme(indicator);
        indicator.setViewPager(mViewPager);
        ArrayList<CardInfo> tmps = DbUtils.getCardInfos(null);
        if (!Xutils.listNotNull(tmps)) {
            DbUtils.cardUpdate();
            DbUtils.cardXm();
            List<String> list = Xutils.importTxt(this, R.raw.init_2016_08_09_13_36);
            DbUtils.importCardFromTxt(list, true);
        }

        ArrayList<PkInfo> pkInfos = DbUtils.getPkInfos(null);
        if (!Xutils.listNotNull(pkInfos)) {
            initApps();
        }

        EventBus.getDefault().register(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mWindowSwitch = (CheckBox) headerView.findViewById(R.id.mWindowSwitch);
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        mWindowSwitch.setOnCheckedChangeListener(this);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Xutils.openUrl(Constant.update_url);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initApps() {

        progressDlg = new MaterialDialog.Builder(this)
                .title("数据初始化中...")
                .show();


        DataInitHelper.MyTask task = new DataInitHelper.MyTask();
        task.execute();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        MyFt fragment = null;
        Bundle bundle = null;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            if (position < baseTab) {
                title = EnumInfo.homeTab.getHomeTab(position).getStrName();
            } else {
                title = fzInfos.get(position - baseTab).getName();
            }
            return title;
        }

        @Override
        public int getCount() {
            return count;
        }


        @Override
        public Fragment getItem(int position) {
            fragment = new MyFt();
            bundle = new Bundle();
            if (position < baseTab) {
                bundle.putString("packageName", EnumInfo.homeTab.getHomeTab(position).getValue());
            } else {
                bundle.putString("packageName", fzInfos.get(position - baseTab).getId() + "");
            }
            fragment.setArguments(bundle);
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            backToDesk(this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public static void backToDesk(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getInstance().isOpenInent) {
//            backToDesk(this);
            MyApplication.getInstance().isOpenInent = false;
        }

        if (SPHelper.isShowWindow(ctx)) {
            resetUI();
//            NotificationActionReceiver.cancelNotification(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(RefreshEvent event) {
        int type = event.type;
        if (type == EnumInfo.RefreshEnum.FZ.getValue()) {
            initActicity();
        }


        if (fragments.size() == 0) {
            initActicity();
        }


        if (type == EnumInfo.RefreshEnum.APPS.getValue() || type == EnumInfo.RefreshEnum.APPSINIT.getValue()) {
            if (type == EnumInfo.RefreshEnum.APPSINIT.getValue()) {
                if (progressDlg.isShowing()) {
                    progressDlg.dismiss();
                }
            }
            MyFt tmp = (MyFt) fragments.get(0);
            tmp.onRefresh();
        }
        if (type == EnumInfo.RefreshEnum.LIKE.getValue()) {
            MyFt tmp = (MyFt) fragments.get(1);
            tmp.onRefresh();
        }
        if (type == EnumInfo.RefreshEnum.ALL.getValue()) {
            MyFt tmp = (MyFt) fragments.get(2);
            tmp.onRefresh();
        }
    }

    private void initActicity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

            case R.id.action_add_wxtalk:
                talkUI(null, EnumInfo.cType.WX.getValue(), "微信-直接聊天", "朋友姓名", "朋友微信号");

                break;

            case R.id.action_add_url:

                talkUI(null, EnumInfo.cType.URL.getValue(), "书签-直达网页or应用页面", "标题", "http://");

                break;
            case R.id.action_add_qqtalk:

                talkUI(null, EnumInfo.cType.QQ.getValue(), "QQ-直接聊天", "好友姓名", "好友QQ号");

                break;
            case R.id.action_add_openactivity:
                initActivity(null);
                break;
            case R.id.action_toactivity:
                startActivity(new Intent(ctx, SettingActivity.class));
                break;
            case R.id.action_share:
                Xutils.sendTo(ctx, Constant.share_content);
                break;
            case R.id.action_init:
                initApps();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        new MaterialDialog.Builder(ctx)
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
                    }
                })
                .show();
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

        new MaterialDialog.Builder(ctx)
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
                        dialog.dismiss();
//                        fillArray();
                    }
                })
                .show();
    }


    private void updateServiceStatus() {
        boolean serviceEnabled = false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.WatchingAccessibilityService")) {
                serviceEnabled = true;
                break;
            }
        }
        mWindowSwitch.setChecked(serviceEnabled);
        if (serviceEnabled) {
            startService(new Intent(this, WatchingService.class));
        }
    }

    private void resetUI() {
        updateServiceStatus();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (SPHelper.isShowWindow(this) && WatchingAccessibilityService.getInstance() == null) {
//            NotificationActionReceiver.showNotification(this, false);
//        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked && buttonView == mWindowSwitch) {
            if (WatchingAccessibilityService.getInstance() == null) {

                new MaterialDialog.Builder(ctx)
                        .title("开启无障碍")
                        .content(R.string.dialog_enable_accessibility_msg)
                        .positiveText("好")
                        .negativeText("不")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent();
                                intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                startActivity(intent);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                resetUI();
                            }
                        })
                        .canceledOnTouchOutside(false)
                        .show();
                SPHelper.setIsShowWindow(this, isChecked);
                return;
            }
        }

        if (buttonView.getId() == R.id.mWindowSwitch) {
            SPHelper.setIsShowWindow(this, isChecked);
            if (!isChecked) {
                TasksWindow.dismiss(this);
            } else {
                TasksWindow.show(this, getPackageName() + "\n" + getClass().getName());
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("search").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(Xutils.initIconWhite(Iconify.IconValue.md_more_vert));
        sub.add(0, 1, 0, "设置");
        if (SPHelper.isNightMode(this.ctx)) {
            sub.add(0, 2, 0, "白天");
        } else {
            sub.add(0, 2, 0, "夜间");
        }
        sub.add(0, 4, 0, "主题配色");
        sub.add(0, 3, 0, "打赏");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("search")) {
            startActivity(new Intent(this.ctx, T9SearchActivity.class));
        }

        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(ctx, SettingNew.class));
                break;
            case 2:
                SPHelper.setIsNightMode(ctx, !SPHelper.isNightMode(ctx));
                //切换模式，重启界面生效
                finish();
                startActivity(new Intent(ctx, MainActivity.class));
                break;
            case 3:
                if (AlipayZeroSdk.hasInstalledAlipayClient(ctx)) {
                    SPHelper.setIsPay(ctx, true);//是否已支付，但不作为Pro依据
                    AlipayZeroSdk.startAlipayClient(this, "aex02461t5uptlcygocfsbc");
                } else {
                    Xutils.tShort("请先安装支付宝~");
                }
                break;
            case 4:
                new ColorChooserDialog.Builder(this, R.string.color_palette)
                        .accentMode(false)
                        .doneButton(R.string.md_done_label)
                        .cancelButton(R.string.md_cancel_label)
                        .backButton(R.string.md_back_label)
                        .preselect(SPHelper.getThemeColor(ctx))
                        .dynamicButtonColor(true)
                        .show();
                break;
        }

        return false;
    }
}

