package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
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
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.MyPath;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
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
    SQLiteDatabase db;

    public void checkDb() {
        /***/
        Cursor cursor = db.rawQuery(
                "SELECT * FROM favorites", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    String tablename = cursor.getString(0);
                    String tablename2 = cursor.getString(1);
                    Xutils.debug("tablename:" + tablename);
                    Xutils.debug("tablename2:" + tablename2);


//                    String tablename = cursor.getString(0);
//                    boolean isEquals=false;
//                    Class tagetCla=null;
//                    Log.d("sqldb", tablename);
//                    for(Class cla:clazzs)
//                    {
//                        isEquals=cla.getName().replaceAll("\\.", "_").equals(tablename);
////                        Log.d("sqldb",cla.getName().replaceAll("\\.", "_").equals(tablename)+" "+cla.getName().replaceAll("\\.", "_"));
//                        if(isEquals) {
//                            tagetCla=cla;
//                            break;
//                        }
//                    }
//                    if(isEquals==false)
//                    {
//                        continue;
//                    }
//
//                    Log.d("sqldb", "continue");

//                    /**查询表所映射类的信息*/
//                    TableInfo info = TableInfo.get(tagetCla.getName());
//                    Log.d("sqldb", "info"+info);
//                    if (info != null) {
//
////                      cudb.rawQuery("PRAGMA table_info(tbl_sfg_device)", null);
////                      Cursor c=db.rawQuery("PRAGMA table_info("+tablename+")", null);
//                        Iterator<Map.Entry<String, Property>> it = info.propertyMap
//                                .entrySet().iterator();
//                        // 检查该表是否有这个字段
//                        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
//
//
//                        try {
//                            Log.d("sqldb", "PRAGMA table_info("+tablename+")");
//                            Cursor columns = db.rawQuery("PRAGMA table_info("+tablename+")", null);
//                            while(columns.moveToNext())
//                            {
//                                Log.d("sqldb", "--->"+columns.getString(1));
//                                map.put(columns.getString(1), false);
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                            throw new RuntimeException(e);
//                        }
//                        /** 遍历类所有字段 */
//                        while (it.hasNext()) {
//
//                            Map.Entry<String, Property> item = it.next();
//                            String key = item.getKey();
//                            Log.d("sqldb", key+" "+map.get(key));
//                            /**该字段不存在新建*/
//                            if (map.get(key) == null) {
//
//                                db.execSQL("ALTER TABLE " + tablename
//                                        + " ADD COLUMN " + key + " " + "CHAR");
//                                map.put(key, true);
//                            }
//                        }
//                        it = info.propertyMap
//                                .entrySet().iterator();
//                        /**删除未映射字段*/
//                        while(it.hasNext()){
//                            Map.Entry<String, Property> item = it.next();
//                            String key = item.getKey();
//                            /**该字段未被遍历删除*/
//                            if (map.get(key)==true) {
//                                db.execSQL("ALTER TABLE "+tablename+" DROP COLUMN "+key);
//                            }
//                        }
//                    }

                } catch (SQLException e) {
//                    KJLoger.debug(getClass().getName() + e.getMessage());
//                    throw new RuntimeException(e);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

    }


    //   public static ArrayList<ShortcutInfo> getItemsInLocalCoordinates(Context context) {
//        ArrayList<ShortcutInfo> items = new ArrayList<ShortcutInfo>();
//        final ContentResolver cr = context.getContentResolver();
//
//
//
//        Cursor c = cr.query(LauncherSettings.Favorites.CONTENT_URI, new String[] {
//                LauncherSettings.Favorites.ITEM_TYPE, LauncherSettings.Favorites.CONTAINER,
//                LauncherSettings.Favorites.SCREEN, LauncherSettings.Favorites.CELLX, LauncherSettings.Favorites.CELLY,
//                LauncherSettings.Favorites.SPANX, LauncherSettings.Favorites.SPANY ,LauncherSettings.Favorites.TITLE,LauncherSettings.Favorites.INTENT}, null, null, null);
//
//        final int itemTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE);
//        final int containerIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER);
//        final int screenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SCREEN);
//        final int cellXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLX);
//        final int cellYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLY);
//        final int spanXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANX);
//        final int spanYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANY);
//        final int titleIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE);
//        final int intenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
//        try {
//            while (c.moveToNext()) {
//                ShortcutInfo item = new ShortcutInfo();
//                item.cellX = c.getInt(cellXIndex);
//                item.cellY = c.getInt(cellYIndex);
//                item.spanX = Math.max(1, c.getInt(spanXIndex));
//                item.spanY = Math.max(1, c.getInt(spanYIndex));
//                item.container = c.getInt(containerIndex);
//                item.itemType = c.getInt(itemTypeIndex);
//                item.screenId = c.getInt(screenIndex);
//                item.title=c.getString(titleIndex);
//                if(c.getString(intenIndex)!=null){
//                    item.intent=new Intent(c.getString(intenIndex));
//                }
//
//                items.add(item);
//            }
//        } catch (Exception e) {
//            items.clear();
//        } finally {
//            c.close();
//        }
//
//        return items;
//    }
    private String backUpDbDo() {
        try {
            String DATABASE_PATH = "/data/data/ch.deletescape.lawnchair.qa/databases";
            String newPath = MyPath.getPathConfig();
//            boolean flag = Xutils.copyFileTo(new File(DATABASE_PATH), new File(newPath));
//            if (flag) {
//                Xutils.tLong("已备份~ ");
//            } else {
//                Xutils.tShort("数据备份失败~");
//            }

            Xutils.cmdExe("cp -r" + DATABASE_PATH + " " + newPath);


            return newPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


        String DATABASE_PATH = MyPath.getPathConfig() + "/launcher.db";
        File f = new File(DATABASE_PATH);
        if (f.exists()) {
            db = SQLiteDatabase.openOrCreateDatabase(
                    DATABASE_PATH, null);
            SQLiteHelperOrm orm = new SQLiteHelperOrm(ctx, DATABASE_PATH);
            orm.onCreate(db);

            checkDb();
        }
//        Cursor cursor = getContentResolver().query(Uri.parse("content://" + Settings.AUTHORITY +"/favorites?notify=true"), null, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                try {
//                    String tablename = cursor.getString(0);
//                    String tablename2 = cursor.getString(1);
//                    Xutils.debug("tablename:" + tablename);
//                    Xutils.debug("tablename2:" + tablename2);
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }

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
                    MyFt.clickCard(ctx, new CardInfo(EnumInfo.cType.CMD.getValue(), cmd, title));
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
                    CardInfo card = new CardInfo(EnumInfo.cType.CMD.getValue(), cmd, title);
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
                    CardInfo card = new CardInfo(EnumInfo.cType.CMD.getValue(), cmd, title);
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
                    CardInfo card = new CardInfo(EnumInfo.cType.CMD.getValue(), cmd, title);
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
        final String[] cmds = getResources().getStringArray(R.array.cmd_array);
        if (cmds == null || cmds.length == 0) {
            return;
        }
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] from = {"text"};
        int[] to = {R.id.text};
        for (int i = 0; i < cmds.length; i++) {
            String name = "";
            String cmd = "";
            String allStr = cmds[i];
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                if (Xutils.isNotEmptyOrNull(allStr) && allStr.contains("|")) {
                    cmd = allStr.split("\\|")[0];
                    name = allStr.split("\\|")[1];
                    map.put("text", name);
                    map.put("key", cmd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            data_list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx, data_list, R.layout.item_batshell, from, to);
        cmdSpinner.setAdapter(adapter);
        cmdSpinner.setVisibility(View.VISIBLE);
        cmdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String _cmd = cmds[position];
                    if (Xutils.isNotEmptyOrNull(_cmd) && _cmd.contains("|")) {
                        cmd = _cmd.split("\\|")[0];
                        String name = _cmd.split("\\|")[1];
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
        return R.layout.ac_shellbat;
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
