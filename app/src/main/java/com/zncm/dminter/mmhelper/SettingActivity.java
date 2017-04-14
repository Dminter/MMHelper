//package com.zncm.dminter.mmhelper;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zncm.dminter.mmhelper.data.CardInfo;
//import com.zncm.dminter.mmhelper.data.EnumInfo;
//import com.zncm.dminter.mmhelper.data.RefreshEvent;
//import com.zncm.dminter.mmhelper.data.db.DbUtils;
//import com.zncm.dminter.mmhelper.utils.Xutils;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
//public class SettingActivity extends BaseActivity implements View.OnClickListener {
//
//    private TextView tvFz;
//    private LinearLayout llInport, llOutport, llGuid, llTanks, llAuthor, llFz, llUpdate;
//    private String fzInfo;
//    private boolean isFzUpdate = false;
//    private static SettingActivity instance;
//
//    public static SettingActivity getInstance() {
//        return instance;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        instance = this;
//        toolbar.setTitle("设置");
//        Xutils.verifyStoragePermissions(this);
//        llInport = (LinearLayout) findViewById(R.id.llInport);
//        llOutport = (LinearLayout) findViewById(R.id.llOutport);
//        llGuid = (LinearLayout) findViewById(R.id.llGuid);
//        llInport.setOnClickListener(this);
//        llTanks = (LinearLayout) findViewById(R.id.llTanks);
//        llTanks.setOnClickListener(this);
//        llAuthor = (LinearLayout) findViewById(R.id.llAuthor);
//        llAuthor.setOnClickListener(this);
//        llFz = (LinearLayout) findViewById(R.id.llFz);
//        llUpdate = (LinearLayout) findViewById(R.id.llUpdate);
//        tvFz = (TextView) findViewById(R.id.tvFz);
//        llFz.setOnClickListener(this);
//        llUpdate.setOnClickListener(this);
//        llOutport.setOnClickListener(this);
//        llGuid.setOnClickListener(this);
//        fzInfo = SPHelper.getFzInfo(this);
//        initFzView();
//    }
//
//    private void initFzView() {
//        if (Xutils.isNotEmptyOrNull(fzInfo)) {
//            tvFz.setVisibility(View.VISIBLE);
//            tvFz.setText(fzInfo);
//        } else {
//            tvFz.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_setting;
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.llInport:
//                showFileChooser();
//                break;
//            case R.id.llOutport:
//                try {
//                    ArrayList<CardInfo> cardInfos = DbUtils.getCardInfos(null);
//                    if (Xutils.listNotNull(cardInfos)) {
//                        StringBuffer stringBuffer = new StringBuffer();
//                        for (int i = 0; i < cardInfos.size(); i++) {
//                            CardInfo info = cardInfos.get(i);
//                            if (info != null && info.getType() == EnumInfo.cType.TO_ACTIVITY.getValue()) {
//                                stringBuffer.append(info.getTitle());
//                                stringBuffer.append("|");
//                                stringBuffer.append(info.getPackageName());
//                                stringBuffer.append("|");
//                                stringBuffer.append(info.getClassName());
//                                stringBuffer.append("\n");
//                            }
//
//                        }
//                        String path = Xutils.getSDPath() + "/" + Xutils.getTimeYMDHM_(new Date().getTime()) + ".txt";
//                        Xutils.writeTxtFile(path, stringBuffer.toString());
//                        Xutils.tLong("已导出到" + path);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.llGuid:
//                break;
//            case R.id.llTanks:
//                thank();
//                break;
//            case R.id.llAuthor:
//                Xutils.cmdExe(Constant.wx_am_pre + Constant.wx_ChattingUI + Constant.author_wx);
//                DbUtils.cardXm();
//                break;
//            case R.id.llUpdate:
//                Xutils.openUrl(Constant.update_url);
//                DbUtils.cardUpdate();
//                break;
//            case R.id.llFz:
//                final EditText editText = new EditText(this);
//                editText.setTextColor(getResources().getColor(R.color.material_light_black));
//                if (Xutils.isNotEmptyOrNull(fzInfo)) {
//                    editText.setText(fzInfo);
//                }
//                new MaterialDialog.Builder(ctx)
//                        .title("分组-英文逗号分隔")
//                        .customView(editText, false)
//                        .positiveText("好")
//                        .negativeText("不")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                String name = editText.getText().toString();
//                                boolean flag = true;
//                                try {
//                                    if (Xutils.isNotEmptyOrNull(name)) {
//                                        String[] fzArr = name.split(",");
//                                        for (int i = 0; i < fzArr.length; i++) {
//                                            String tmp = fzArr[i];
//                                            if (Xutils.isEmptyOrNull(tmp)) {
//                                                flag = false;
//                                                break;
//                                            }
//                                        }
//                                        if (fzArr.length > 8) {
//                                            flag = false;
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    flag = false;
//                                    e.printStackTrace();
//                                }
//                                if (flag) {
//                                    if (!fzInfo.equals(name)) {
//                                        isFzUpdate = true;
//                                        fzInfo = name;
//                                        initFzView();
//                                        SPHelper.setFzInfo(SettingActivity.this, name);
//                                        Xutils.tShort("分组修改成功！");
//                                    }
//                                } else {
//                                    Xutils.tShort("分组格式错误，英文逗号分隔中间不能为空且不超过8个~");
//                                }
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//                break;
//        }
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        backDo();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                backDo();
//                break;
//
//        }
//        return false;
//    }
//
//    private void backDo() {
//        finish();
//        if (isFzUpdate) {
//            EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.FZ.getValue()));
//        }
//    }
//
//
//    public void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("text/xml");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(Intent.createChooser(intent, "选择文件导入"), 103);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Xutils.tShort("没有找到文件管理器");
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 103:
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri uri = data.getData();
//                    String path = Xutils.getPathFromUri(SettingActivity.this, uri);
//                    importData(path);
//                }
//                break;
//        }
//    }
//
//
//    private void thank() {
//        new MaterialDialog.Builder(this)
//                .title("特别感谢")
//                .content("com.github.florent37:materialviewpager\ncom.github.dexafree:materiallist\nhttps://github.com/109021017/android-TopActivity\nza.co.riggaroo:materialhelptutorial\ncom.github.mrengineer13:snackbar\ncom.afollestad.material-dialogs:core\normlite\njp.wasabeef:recyclerview-animators\n")
//                .positiveText("知")
//                .show();
//    }
//
//    private void importData(String path) {
//        boolean canImport = false;
//        File file = new File(path);
//        String fileName = "";
//        if (file.exists()) {
//            fileName = file.getName();
//            if (fileName.endsWith(".txt")) {
//                canImport = true;
//            }
//        }
//        if (canImport) {
//            List<String> list = DbUtils.importTxt(new File(path));
//            DbUtils.importCardFromTxt(list, false);
//            Xutils.tShort(fileName + " 导入成功~");
//        } else {
//            Xutils.tShort("文件格式非法~");
//        }
//    }
//
//
//}
