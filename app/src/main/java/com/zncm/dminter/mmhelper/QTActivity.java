//package com.zncm.dminter.mmhelper;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.malinskiy.materialicons.Iconify;
//import com.zncm.dminter.mmhelper.data.CardInfo;
//import com.zncm.dminter.mmhelper.data.EnumInfo;
//import com.zncm.dminter.mmhelper.data.PkInfo;
//import com.zncm.dminter.mmhelper.data.db.DbUtils;
//import com.zncm.dminter.mmhelper.ft.MyFt;
//import com.zncm.dminter.mmhelper.utils.Xutils;
//
//import java.util.ArrayList;
//
//
//public class QTActivity extends BaseActivity {
//    MyFt myFt;
//    public int homeTab = 2;
//    Integer[] chooseItems;
//    ArrayList<CardInfo> tmps;
//    MaterialDialog pb;
//    public boolean isThree = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        homeTab = getIntent().getIntExtra("homeTab", 2);
//        toolbar.setTitle(EnumInfo.homeTab.getHomeTab(homeTab).getStrName());
//        myFt = new MyFt();
//        Bundle bundle = new Bundle();
//        bundle.putString("packageName", EnumInfo.homeTab.getHomeTab(homeTab).getValue());
//        myFt.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container, myFt)
//                .commit();
//
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_ft;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item == null || item.getTitle() == null) {
//            return false;
//        }
//        if (item.getTitle().equals("bat")) {
//
//            tmps = myFt.cardInfos;
//            if (!Xutils.listNotNull(tmps)) {
//                return true;
//            }
//            final ArrayList<String> items = new ArrayList<String>();
//            for (CardInfo info : tmps
//                    ) {
//                items.add(info.getTitle());
//            }
//            try {
//                Xutils.debug("tmps??" + tmps);
//                new MaterialDialog.Builder(ctx)
//                        .title("批量停用")
//                        .items(items)
//                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
//                                StringBuffer sbInfo = new StringBuffer();
//                                for (Integer tmp : which
//                                        ) {
//                                    sbInfo.append(items.get(tmp)).append(" ");
//                                }
//                                pb =
//                                        new MaterialDialog.Builder(ctx)
//                                                .title("正在停用...")
//                                                .content(sbInfo.toString())
//                                                .progress(true, 0)
//                                                .show();
//                                chooseItems = which;
//                                MyTask getDate = new MyTask();
//                                getDate.execute();
//
//                                return true;
//                            }
//                        })
//                        .positiveText("确定")
//                        .negativeText("取消")
//                        .show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (item.getTitle().equals("sys")) {
//            isThree = !isThree;
//            myFt.onRefresh();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void batOp() {
//        for (Integer tmp : chooseItems
//                ) {
//            CardInfo info = tmps.get(tmp);
//            if (info == null) {
//                return;
//            }
//            Xutils.debug("onSelection:" + info.getTitle());
//            Xutils.exec(Constant.common_pm_d_p + info.getPackageName());
//            PkInfo pk = DbUtils.getPkOne(info.getPackageName());
//            if (pk != null && pk.getStatus() == EnumInfo.appStatus.ENABLE.getValue()) {
//                pk.setStatus(EnumInfo.appStatus.DISABLED.getValue());
//                DbUtils.updatePkInfo(pk);
//            }
//        }
//    }
//
//
//    class MyTask extends AsyncTask<Void, Void, Void> {
//
//        protected Void doInBackground(Void... params) {
//            try {
//                batOp();
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
//            myFt.fillArray();
//            pb.dismiss();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("sys").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_android)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        if (homeTab == 2) {
//            menu.add("bat").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_clear_all)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//}
