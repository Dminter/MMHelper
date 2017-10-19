package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.FzInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.floatball.FloatBallService;
import com.zncm.dminter.mmhelper.floatball.FloatBallView;
import com.zncm.dminter.mmhelper.floatball.FloatWindowManager;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.DataInitHelper;
import com.zncm.dminter.mmhelper.utils.NotiHelper;
import com.zncm.dminter.mmhelper.utils.ScreenListener;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
    private MyPagerAdapter adapter;
    private ViewPager mViewPager;
    private int baseTab = 4;
    private int count = baseTab;
    private Toolbar toolbar;
    public ArrayList<FzInfo> fzInfos = new ArrayList<>();
    private MainActivity ctx;
    public HashMap<Integer, MyFt> fragments = new HashMap();
    private LinearLayout topView;
    private ScreenListener screenListener;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx = this;
        MyApplication.updateNightMode(SPHelper.isNightMode(ctx));
        super.onCreate(savedInstanceState);
        if (SPHelper.isHS(ctx)) {
            setTheme(R.style.AppTheme_Translucent);
        }


        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            if (MyApplication.isPay) {
                toolbar.setTitle(getResources().getString(R.string.app_name) + "Pro");
            }
            setSupportActionBar(toolbar);
            Xutils.initBarTheme(ctx, toolbar);
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
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        topView = (LinearLayout) findViewById(R.id.topView);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(count);
        mViewPager.setCurrentItem(SPHelper.getCurTab(ctx));


        /**
         *使用support TabLayout 代替 PagerSlidingTabStrip
         */
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        Xutils.initTabLayout(ctx, mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        initBaseCard(true);


        ArrayList<PkInfo> pkInfos = DbUtils.getPkInfos(null);
        if (!Xutils.listNotNull(pkInfos)) {
            initApps();
        }

        if (SPHelper.isHS(ctx)) {
            toolbar.setVisibility(View.GONE);
            mTabLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Xutils.dip2px(2)));
            mViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Xutils.dip2px(300)));
        }


        if (SPHelper.isAutoStop(ctx)) {
            screenListener = new ScreenListener(ctx);
            screenListener.begin(new ScreenListener.ScreenStateListener() {
                public void onScreenOff() {
                    if (SPHelper.isAutoStop(ctx)) {
                        new MyFt.BatStopTask().execute(EnumInfo.typeBatStop.DISABLE_LESS.getValue());
                    }
                }

                public void onScreenOn() {
                }

                public void onUserPresent() {
                }
            });
        }

        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDesk(ctx);
            }
        });


        EventBus.getDefault().register(this);


        if (SPHelper.isAcFloat(ctx)) {
            startService(new Intent(ctx, WatchingService.class));
        }

    }

    /**
     * 首次安装，初始化活动
     * 不是第一次也可以强制初始化，触发在设置里面【添加建议活动】
     */
    public static void initBaseCard(boolean isFirst) {
        ArrayList<CardInfo> tmps = DbUtils.getCardInfos(null);
        if (!isFirst) {
            tmps = new ArrayList<>();
        }
        if (!Xutils.listNotNull(tmps)) {
//            List<String> list = Xutils.importTxt(this, R.raw.init_2016_08_09_13_36);
//            DbUtils.importCardFromTxt(list, true);
            LinkedHashMap<String, String> items = new LinkedHashMap<>();
            SuggestAc.sugItems(items);
            if (items != null && items.size() > 0) {
                for (Map.Entry<String, String> entry : items.entrySet()
                        ) {
                    String pkName = entry.getKey();
                    if (Xutils.isNotEmptyOrNull(pkName)) {
                        List<CardInfo> cardInfos = new ArrayList();
                        SuggestInfoActivity.allInit(pkName, cardInfos);
                        if (Xutils.listNotNull(cardInfos)) {
//                            Xutils.debug("cardInfos:" + cardInfos);
                            for (CardInfo tmp : cardInfos
                                    ) {
                                DbUtils.addIfNotExist(tmp);
                            }
                        }
                    }
                }
            }
            /**
             *初始化建议的活动
             */
            DbUtils.cardXm();
            DbUtils.cardzfball();
            DbUtils.cardUpdate();
        }
    }


    public static void initBallService(Context ctx) {
        try {
            if (SPHelper.isFloatBall(ctx) && MyApplication.isPay) {
                NotiHelper.clearNoti(ctx, Constant.n_id);
                FloatBallView ballView = FloatWindowManager.mBallView;
                if (ballView != null && ballView.getVisibility() == View.GONE) {
                    ballView.setVisibility(View.VISIBLE);
                } else {
                    ctx.startService(new Intent(ctx, FloatBallService.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initApps() {
        DataInitHelper.MyTask task = new DataInitHelper.MyTask();
        task.execute();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        SPHelper.setThemeColor(ctx, selectedColor);
        EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.FZ.getValue()));
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
            fragments.put(position, fragment);
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

    public void backToDesk(Activity activity) {

        SPHelper.setCurTab(ctx, mViewPager.getCurrentItem());
        if (SPHelper.isHS(ctx)) {
            finish();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (SPHelper.isAcFloat(ctx)) {
            MyFt.updateServiceStatus(ctx);
        }
        initBallService(ctx);
        if (SPHelper.isAutoNight(ctx)) {
            int hour = Xutils.getHour();
            boolean flag = false;
            boolean isNightMode = SPHelper.isNightMode(ctx);

            if (hour >= 6 && hour < 18) {
                if (isNightMode) {
                    flag = true;
                }
                SPHelper.setIsNightMode(ctx, false);
            } else {
                if (!isNightMode) {
                    flag = true;
                }
                SPHelper.setIsNightMode(ctx, true);
            }

            if (flag) {
                initActicity();
            }

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.screenListener != null) {
            this.screenListener.unregisterListener();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(RefreshEvent event) {
        int type = event.type;

        if (type == EnumInfo.RefreshEnum.FZ.getValue()) {
            initActicity();
        }
        if (fragments.size() > 0) {
            if (type == EnumInfo.RefreshEnum.APPS.getValue()) {
                MyFt myFt = (MyFt) fragments.get(Integer.valueOf(EnumInfo.homeTab.APPS.getPosition()));
                if (myFt != null) {
                    myFt.onRefresh();
                }
            }
            if (type == EnumInfo.RefreshEnum.LIKE.getValue()) {
                MyFt myFt = (MyFt) fragments.get(Integer.valueOf(EnumInfo.homeTab.LIKE.getPosition()));
                if (myFt != null) {
                    myFt.onRefresh();
                }
            }
            if (type == EnumInfo.RefreshEnum.ALL.getValue()) {
                MyFt myFt = (MyFt) fragments.get(Integer.valueOf(EnumInfo.homeTab.ALL.getPosition()));
                if (myFt != null) {
                    myFt.onRefresh();
                }
            }
            if (type == EnumInfo.RefreshEnum.BAT_STOP.getValue()) {
                MyFt myFt = (MyFt) fragments.get(Integer.valueOf(EnumInfo.homeTab.BAT_STOP.getPosition()));
                if (myFt != null) {
                    myFt.onRefresh();
                }
            }
        }
    }

    private void initActicity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("search").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(Xutils.initIconWhite(Iconify.IconValue.md_more_vert));
        sub.add(0, 1, 0, "设置");
        if (SPHelper.isNightMode(ctx)) {
            sub.add(0, 2, 0, "白天");
        } else {
            sub.add(0, 2, 0, "夜间");
        }
        sub.add(0, 4, 0, "主题配色");
        sub.add(0, 3, 0, "打赏");
        sub.add(0, 5, 0, "建议活动");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("search")) {
            startActivity(new Intent(ctx, T9SearchActivity.class));
        }

        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(ctx, SettingNew.class));
                break;
            case 2:
                SPHelper.setIsAutoNight(ctx, false);
                SPHelper.setIsNightMode(ctx, !SPHelper.isNightMode(ctx));
                //切换模式，重启界面生效
                finish();
                startActivity(new Intent(ctx, MainActivity.class));
                break;
            case 3:
                pay(ctx);
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
            case 5:
                startActivity(new Intent(ctx, SuggestAc.class));
                break;
        }

        return true;
    }

    public static void pay(Activity ctx) {
        if (AlipayZeroSdk.hasInstalledAlipayClient(ctx)) {
            AlipayZeroSdk.startAlipayClient(ctx, "aex02461t5uptlcygocfsbc");
        } else {
            Xutils.tShort("请先安装支付宝~");
        }
    }
}

