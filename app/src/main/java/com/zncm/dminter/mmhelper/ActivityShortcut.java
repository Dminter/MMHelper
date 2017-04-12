package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.MyAppInfo;
import com.zncm.dminter.mmhelper.utils.ApkInfoUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.List;


public class ActivityShortcut extends BaseActivity {

    public static final String INSTALL_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";
    ActivityArrayAdapter activityArrayAdapter;
    EditText activityName;
    Spinner activitySpinner;
    Spinner appSpinner;
    public List<PackageInfo> apps;
    ApplicationArrayAdapter appsListAdapter;
    Button btnAdd;
    Button btnAddDesk;
    Button btnAddLike;
    Button btnPre;
    String className;
    Activity ctx;
    PackageManager packageManager;
    String packageName;
    String preClassName;
    MaterialDialog progressDlg;
    Intent startIntent;
    int type = EnumInfo.typeShortcut.THREE_MORE.getValue();

    List<MyAppInfo> activitys = new ArrayList<>();

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
        startIntent = getIntent();
        packageManager = getPackageManager();
        appSpinner = (Spinner) findViewById(R.id.app_spinner);
        activitySpinner = (Spinner) findViewById(R.id.activity_spinner);


        apps = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
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
                    infos = appInfo.activities;
                    if (infos != null && infos.length > 0) {
                        for (int i = 0; i < infos.length; i++) {
                            acList.add(infos[i].name);
                        }
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
                            className = myAppInfo.getClassName();
                            showClassName();
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            activitySpinner.setAdapter(new ActivityArrayAdapter(ctx, new ArrayList()));
                        }
                    });


                } else {
                    onNothingSelected(parent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        activityName = (EditText) findViewById(R.id.activityName);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAddLike = (Button) findViewById(R.id.btnAddLike);
        btnAddDesk = (Button) findViewById(R.id.btnAddDesk);











    }

    private void showClassName() {
        preClassName = className;
        if ((Xutils.isNotEmptyOrNull(className)) && (className.contains(packageName))) {
            preClassName = className.replace(packageName, "");
        }
        activityName.setText(preClassName);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shortcut;
    }
}