package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.data.db.SQLiteHelperOrm;
import com.zncm.dminter.mmhelper.data.favorites;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.MyPath;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 从Launcher获取快捷方式
 */
public class LauncherDbActivity extends BaseActivity {

    private Activity ctx;
    private Spinner cmdSpinner;
    private Button btnPre;
    private Button btnAdd;
    private Button btnAddLike;
    private Button btnAddDesk;
    private EditText editText;
    private String cmd;
    byte[] icon;
    SQLiteDatabase db;
    ArrayList<favorites> intentList = new ArrayList<>();

    public void checkDb() {
        Cursor cursor = db.rawQuery("SELECT * FROM favorites", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String intent = cursor.getString(cursor.getColumnIndex("intent"));
                    String iconPackage = cursor.getString(cursor.getColumnIndex("iconPackage"));
                    byte[] icon = cursor.getBlob(cursor.getColumnIndex("icon"));
                    favorites favorite = new favorites(title, intent, iconPackage, icon);
                    intentList.add(favorite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

    }

    private void copyLuncherDbToSdcard() {
        try {

            String luncherPkg = Xutils.getLauncherPackageName(ctx);
            if (Xutils.isEmptyOrNull(luncherPkg)) {
                Xutils.tShort("获取默认luncher失败~");
                return;
            }
            String DATABASE_PATH = "/data/data/" + luncherPkg + "/databases/launcher.db";
            String newPath = MyPath.getPathConfig();
            Xutils.cmdExe("cp -r " + DATABASE_PATH + " " + newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("从桌面获取快捷方式");
        ctx = this;
        if (!MyApplication.isPay) {
            Xutils.tShort("非Pro版本~");
            finish();
            return;
        }
        copyLuncherDbToSdcard();
        String DATABASE_PATH = MyPath.getPathConfig() + "/launcher.db";
        File f = new File(DATABASE_PATH);
        if (f.exists()) {
            db = SQLiteDatabase.openOrCreateDatabase(
                    DATABASE_PATH, null);
            SQLiteHelperOrm orm = new SQLiteHelperOrm(ctx, DATABASE_PATH);
            orm.onCreate(db);
            checkDb();
        }


        editText = (EditText) findViewById(R.id.editText);
        cmdSpinner = (Spinner) findViewById(R.id.cmdSpinner);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAddLike = (Button) findViewById(R.id.btnAddLike);
        btnAddDesk = (Button) findViewById(R.id.btnAddDesk);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(cmd)) {
                    String title = editText.getText().toString();
                    MyFt.clickCard(ctx, new CardInfo(EnumInfo.cType.SHORT_CUT_SYS.getValue(), cmd, title, icon));
                } else {
                    Xutils.tShort("选择要执行的Shell~");
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(cmd)) {
                    String title = editText.getText().toString();
                    CardInfo card = new CardInfo(EnumInfo.cType.SHORT_CUT_SYS.getValue(), cmd, title, icon);
                    DbUtils.insertCard(card);
                    Xutils.tShort("已添加~");
                }
            }
        });
        btnAddLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xutils.isNotEmptyOrNull(cmd)) {
                    String title = editText.getText().toString();
                    CardInfo card = new CardInfo(EnumInfo.cType.SHORT_CUT_SYS.getValue(), cmd, title, icon);
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
                if (Xutils.isNotEmptyOrNull(cmd)) {
                    String title = editText.getText().toString();
                    CardInfo card = new CardInfo(EnumInfo.cType.SHORT_CUT_SYS.getValue(), cmd, title, icon);
                    //exi4
                    card.setExi4(Constant.sort_apps);
                    card.setExi1(1);
                    DbUtils.insertCard(card);
                    CardInfo tmp = DbUtils.getCardInfoByCmd(cmd);
                    if (tmp != null) {
                        Xutils.sendToDesktop(ctx, tmp);
                        Xutils.tShort("已添加到桌面~");
                    }
                }
            }
        });
        initData();
    }


    private void initData() {
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] from = {"text"};
        int[] to = {R.id.text};
        if (Xutils.listNotNull(intentList)) {
            for (favorites tmp : intentList
                    ) {
                Map<String, Object> map = new HashMap<String, Object>();
                try {
                    String cmd = tmp.getIntent();
                    String name = tmp.getTitle();
                    map.put("text", name);
                    map.put("key", cmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                data_list.add(map);
            }
        }


        SimpleAdapter adapter = new SimpleAdapter(ctx, data_list, R.layout.item_batshell, from, to);
        cmdSpinner.setAdapter(adapter);
        cmdSpinner.setVisibility(View.VISIBLE);
        cmdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    favorites favorite = intentList.get(position);
                    if (favorite != null) {
                        cmd = favorite.getIntent();
                        String name = favorite.getTitle();
                        icon = favorite.getIcon();
                        editText.setText(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_launcherdb;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("back")) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
