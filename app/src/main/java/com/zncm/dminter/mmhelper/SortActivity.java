package com.zncm.dminter.mmhelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;


public class SortActivity extends AppCompatActivity {
    private MyPagerAdapter adapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private SortActivity ctx;
    public HashMap<Integer, MyFt> fragments = new HashMap();
    String titles[] = new String[]{EnumInfo.homeTab.APPS.getStrName(), EnumInfo.homeTab.LIKE.getStrName()};
    MaterialDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx = this;
        MyApplication.updateNightMode(SPHelper.isNightMode(ctx));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
            Xutils.initBarTheme(ctx, toolbar);
        }
        if (!MyApplication.isPay) {
            Xutils.tShort("非Pro版本~");
            finish();
            return;
        }
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);

        TabLayout    mTabLayout = (TabLayout)findViewById(R.id.mTabLayout);
        Xutils.initTabLayout(ctx,mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        MyFt fragment = null;
        Bundle bundle = null;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }


        @Override
        public Fragment getItem(int position) {
            fragment = new MyFt();
            bundle = new Bundle();
            if (position == 0) {
                bundle.putString("packageName", EnumInfo.homeTab.APPS.getValue());
            } else if (position == 1) {
                bundle.putString("packageName", EnumInfo.homeTab.LIKE.getValue());
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            finish();
        } else if (item.getTitle().equals("done")) {
            progressDlg = Xutils.themeMaterialDialog(ctx)
                    .title("排序中...")
                    .show();
            SortTask sortTask = new SortTask();
            sortTask.execute();
        }
        return true;
    }


    public class SortTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                MyFt tmp = (MyFt) fragments.get(0);
                MyFt tmp1 = (MyFt) fragments.get(1);
                ArrayList<CardInfo> cardInfos = tmp.cardInfos;
                ArrayList<CardInfo> cardInfos1 = tmp1.cardInfos;
                if (Xutils.listNotNull(cardInfos)) {
                    for (int i = 0; i < cardInfos.size(); i++) {
                        CardInfo cardInfo = cardInfos.get(i);
                        DbUtils.updatePkPos(cardInfo.getPackageName(), i);
                    }
                }
                if (Xutils.listNotNull(cardInfos1)) {
                    for (int i = 0; i < cardInfos1.size(); i++) {
                        CardInfo cardInfo1 = cardInfos1.get(i);
                        DbUtils.updateCardPos(cardInfo1.getId(), i);
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
            if (progressDlg != null && progressDlg.isShowing()) {
                progressDlg.dismiss();
            }
            Xutils.tShort("已排序！");
            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.FZ.getValue()));
            finish();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("done").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_done_all)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


}

