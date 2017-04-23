package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.MyAppInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.ApkInfoUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量添加活动
 */
public class BatActivitys extends BaseActivity {
    private ActivityArrayAdapter activityArrayAdapter;
    private EditText activityName;
    private Spinner activitySpinner;
    private Spinner appSpinner;
    private List<PackageInfo> apps;
    private ApplicationArrayAdapter appsListAdapter;
    private Button btnAdd;
    private Button btnAddDesk;
    private Button btnAddLike;
    private Button btnPre;
    private String className;
    private Activity ctx;
    private PackageManager packageManager;
    private String packageName;
    private String preClassName;
    private MaterialDialog progressDlg;
    private int type = EnumInfo.typeShortcut.THREE_MORE.getValue();
    private List<MyAppInfo> activitys = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("批量添加活动");
        ctx = this;
        if (!MyApplication.isPay) {
            Xutils.tShort("非Pro版本~");
            finish();
            return;
        }
        initViews();
        loadData();
    }

    public void initViews() {
        packageManager = getPackageManager();
        appSpinner = (Spinner) findViewById(R.id.app_spinner);
        activitySpinner = (Spinner) findViewById(R.id.activity_spinner);
        activityName = (EditText) findViewById(R.id.activityName);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAddLike = (Button) findViewById(R.id.btnAddLike);
        btnAddDesk = (Button) findViewById(R.id.btnAddDesk);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(className)) {
                    String title = activityName.getText().toString();
                    MyFt.clickCard(ctx, new CardInfo(packageName, className, title));
                } else {
                    Xutils.tShort("请先选择活动~");
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(className)) {
                    String title = activityName.getText().toString();
                    CardInfo card = new CardInfo(packageName, className, title);
                    DbUtils.insertCard(card);
                    Xutils.tShort("已添加~");
                }
            }
        });
        btnAddLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(className)) {
                    String title = activityName.getText().toString();
                    CardInfo card = new CardInfo(packageName, className, title);
                    //exi4
                    card.setExi4(Constant.sort_apps);
                    card.setExi1(1);
                    DbUtils.insertCard(card);
                    Xutils.tShort("已添加，并收藏~");
                }
            }
        });
        btnAddDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(className)) {
                    String title = activityName.getText().toString();
                    CardInfo card = new CardInfo(packageName, className, title);
                    card.setExi4(Constant.sort_apps);
                    card.setExi1(1);
                    DbUtils.insertCard(card);
                    CardInfo tmp = DbUtils.getCardInfoByClassName(className);
                    if (tmp != null) {
                        Xutils.sendToDesktop(ctx, tmp);
                        Xutils.tShort("已添加到桌面~");
                    }
                }
            }
        });
    }

    private void loadData() {
        progressDlg = new MaterialDialog.Builder(this).title("请稍后...").show();
        new MyGetAppsTask().execute();
    }


    /**
     * 初始化数据
     */
    class MyGetAppsTask extends AsyncTask<Void, Void, Void>

    {

        protected Void doInBackground(Void... params) {
            try {
                PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(0);
                List<PackageInfo> tmpApp = new ArrayList<>();
                List<PackageInfo> tmpAppSys = new ArrayList<>();
                /**
                 *是否系统应用
                 */
                for (PackageInfo packageInfo : packages) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
                    {
                        tmpApp.add(packageInfo);
                    } else {
                        tmpAppSys.add(packageInfo);
                    }
                }


                if (type == EnumInfo.typeShortcut.THREE_MORE.getValue() || type == EnumInfo.typeShortcut.THREE_LESS.getValue()) {
                    apps = tmpApp;
                } else if (type == EnumInfo.typeShortcut.ALL_MORE.getValue() || type == EnumInfo.typeShortcut.ALL_LESS.getValue()) {
                    apps = tmpAppSys;
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
            initData();
        }
    }


    private void initData() {
//        apps = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        appsListAdapter = new ApplicationArrayAdapter(this, apps);
        appSpinner.setAdapter(appsListAdapter);
        appSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo appInfo = appsListAdapter.getItem(position);
                //三方+少，三方+多，全部+少，全部+多
//                THREE_MORE(1, "THREE_MORE"), THREE_LESS(2, "THREE_LESS"), ALL_MORE(3, "ALL_MORE"), ALL_LESS(4, "ALL_LESS");
                activitys = new ArrayList<>();
                ActivityInfo[] infos;
                ArrayList<String> acList = new ArrayList<String>();

                if ((type == EnumInfo.typeShortcut.ALL_LESS.getValue()) || (type == EnumInfo.typeShortcut.THREE_LESS.getValue())) {
                    try {
                        infos = getPackageManager().getPackageInfo(appInfo.packageName, PackageManager.GET_ACTIVITIES).activities;
                        infos = appInfo.activities;
                        if (infos != null && infos.length > 0) {
                            for (int i = 0; i < infos.length; i++) {
                                acList.add(infos[i].name);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if ((type == EnumInfo.typeShortcut.ALL_MORE.getValue()) || (type == EnumInfo.typeShortcut.THREE_MORE.getValue())) {
                    acList = (ArrayList<String>) ApkInfoUtils.getActivitiesByPackageName(ctx, appInfo.packageName);
                }
                if (Xutils.listNotNull(acList)) {

                    for (String className : acList
                            ) {
                        activitys.add(new MyAppInfo(appInfo.packageName, className));
                    }
                    activityArrayAdapter = new ActivityArrayAdapter(ctx, activitys);
                    activitySpinner.setAdapter(activityArrayAdapter);
                    activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            MyAppInfo myAppInfo = activityArrayAdapter.getItem(position);
                            packageName = myAppInfo.getPackageName();
                            className = myAppInfo.getClassName();
                            showClassName();
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            activitySpinner.setAdapter(new ActivityArrayAdapter(ctx, new ArrayList()));
                        }
                    });


                } else {
                    activitySpinner.setAdapter(new ActivityArrayAdapter(ctx, new ArrayList()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showClassName() {
        if (Xutils.isNotEmptyOrNull(className)) {
            preClassName = className;
            if (Xutils.isNotEmptyOrNull(packageName) && className.contains(packageName)) {
                preClassName = className.replace(packageName, "");
            }
            activityName.setText(preClassName);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shortcut;
    }


    public boolean onCreateOptionsMenu(Menu paramMenu) {
        paramMenu.add("filter").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_filter_list)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        paramMenu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(paramMenu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            finish();
        } else if (item.getTitle().equals("filter")) {
            try {
                String[] items = new String[]{EnumInfo.typeShortcut.THREE_MORE.getStrName(), EnumInfo.typeShortcut.THREE_LESS.getStrName(), EnumInfo.typeShortcut.ALL_MORE.getStrName(), EnumInfo.typeShortcut.ALL_LESS.getStrName()};
                new MaterialDialog.Builder(ctx).items(items).itemsCallback(new MaterialDialog.ListCallback() {

                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        type = (position + 1);
                        loadData();
                    }
                }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}