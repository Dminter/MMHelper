package com.zncm.dminter.mmhelper;

import android.app.Activity;
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
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *命令
 */
public class ShellBatActivity extends BaseActivity {

    private Activity ctx;
    private Spinner cmdSpinner;
    private Button btnPre;
    private Button btnAdd;
    private Button btnAddLike;
    private Button btnAddDesk;
    private EditText editText;
    private String cmd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("批量添加Shell");
        ctx = this;
        if (!MyApplication.isPay) {
            Xutils.tShort("非Pro版本~");
            finish();
            return;
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
