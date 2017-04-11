package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.t9search.T9SearchSupport;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;

import cn.tinkling.t9.T9Matcher;
import cn.tinkling.t9.T9Utils;

/**
 * Created by jiaomx on 2017/4/10.
 */
public class T9SearchActivity extends BaseActivity {
    private static int[] buttonIds = {
            R.id.Button1, R.id.Button2, R.id.Button3,
            R.id.Button4, R.id.Button5, R.id.Button6,
            R.id.Button7, R.id.Button8, R.id.Button9,
            R.id.ButtonClr, R.id.ButtonLaunch, R.id.ButtonBack
    };

    private static String[] charBtns = {"1", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ", "清除", "0", "DEL"};
    //    private static ArrayList<CardInfo> initList = new ArrayList();
    Activity ctx;
    private ArrayList<CardInfo> currentList = new ArrayList();
    EditText editText;
    MyFt fragment;
    private boolean isT9 = true;
    TableLayout keyboardTable;
    private String searchString = new String();


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable editable) {
            StringBuffer stringBuffer = new StringBuffer();
            String str = editText.getText().toString();
            if (Xutils.isNotEmptyOrNull(str)) {
                char[] chars = str.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    stringBuffer.append(T9Utils.formatCharToT9(chars[i]));
                }
            }
            t9Search(stringBuffer.toString());
        }


    };
    int type = EnumInfo.typeT9.APP.getValue();

    private void allInit() {

        if (type == EnumInfo.typeT9.ACTIVITY.getValue()) {
            initAc();
        } else if (type == EnumInfo.typeT9.APP.getValue()) {
            initApp();
        }
        if (type == EnumInfo.typeT9.APP_ACTIVITY.getValue()) {
            initAc();
            initApp();
        }
        MyApplication.t9List = currentList;
    }

    private void clearSearch() {
        searchString = new String();
        fragment.cardInfos = new ArrayList();
        if (Xutils.listNotNull(currentList)) {
            fragment.cardInfos = currentList;
        }
        fragment.cardAdapter.setItems(fragment.cardInfos);
        toolbar.setTitle("");
    }

    /**
     * 添加活动到T9，没有拼音的第一次需要初始化
     */
    private void initAc() {
        ArrayList<CardInfo> cardInfos = DbUtils.getCardInfos(null);
        if (Xutils.listNotNull(cardInfos)) {
            for (CardInfo info : cardInfos
                    ) {
                if ((info != null) && (Xutils.isEmptyOrNull(info.getEx3()))) {
                    info.setEx3(T9SearchSupport.buildT9Key(info.getTitle()));
                    DbUtils.updateCard(info);
                }
                info.setExi5(-2);
                currentList.add(info);
            }
        }
    }

    /**
     * 添加应用到T9，没有拼音的第一次需要初始化
     */
    private void initApp() {
        ArrayList<CardInfo> cardInfos = DbUtils.getPkInfosToCard();
        if (Xutils.listNotNull(cardInfos)) {

            for (CardInfo info : cardInfos
                    ) {
                if ((info != null) && (Xutils.isEmptyOrNull(info.getEx3()))) {
                    PkInfo pkInfo = DbUtils.getPkOne(info.getPackageName());
                    pkInfo.setEx3(T9SearchSupport.buildT9Key(info.getTitle()));
                    DbUtils.updatePkInfo(pkInfo);
                }
                info.setExi5(-1);
                currentList.add(info);
            }
        }
    }

    private void initKeyBoard() {
        isT9 = SPHelper.isT9(ctx);
        if (isT9) {
            editText.setVisibility(View.GONE);
            Xutils.hideKeyBoard(editText);
            keyboardTable.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.VISIBLE);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            Xutils.autoKeyBoardShow(editText);
            keyboardTable.setVisibility(View.GONE);
        }
    }

    private boolean updateList() {
        fragment.cardAdapter.setItems(null);
        matches(searchString);
        return false;
    }

    void addToSearchString(int key) {
        searchString += String.valueOf(key);
        if (Xutils.isNotEmptyOrNull(searchString)) {
            if (searchString.equals("88888888")) {
                SPHelper.setPayOrder(ctx, Xutils.getTimeTodayYMD());
            } else if (searchString.equals("99999999")) {
                SPHelper.setPayOrder(ctx, "");
            }
            updateList();
        }
    }

    public void matches(String paramString) {
        t9Search(paramString);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        fragment = new MyFt();
        Bundle bundle = new Bundle();
        bundle.putString("packageName", EnumInfo.homeTab.APPS.getValue());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        keyboardTable = ((TableLayout) findViewById(R.id.keyboardTable));
        editText = ((EditText) findViewById(R.id.editText));
        editText.addTextChangedListener(textWatcher);
        initKeyBoard();
        for (int i = 0; i < 12; i++) {
            final Button button = (Button) findViewById(buttonIds[i]);
            button.setText(charBtns[i]);
            final int position = i + 1;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (position == 12) {
                        if (searchString.length() > 0) {
                            searchString = searchString.substring(0, -1 + searchString.length());
                            updateList();
                        }
                        return;
                    }
                    if (position == 11) {
                        addToSearchString(0);
                        return;
                    }
                    if (position == 10) {
                        clearSearch();
                        return;
                    }
                    addToSearchString(position);
                }
            });
        }
        type = SPHelper.getTypeT9(ctx);
        currentList = MyApplication.t9List;
        if (!Xutils.listNotNull(currentList)) {
            allInit();
        }
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.ac_t9search;
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        paramMenu.add("keyboard").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_keyboard)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        paramMenu.add("refresh").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_refresh)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        paramMenu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(paramMenu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            finish();
        } else if (item.getTitle().equals("refresh")) {
            currentList = new ArrayList();
            allInit();
            Xutils.tShort("已重建索引~");
        } else if (item.getTitle().equals("keyboard")) {
            SPHelper.setIsT9(ctx, !SPHelper.isT9(ctx));
            initKeyBoard();
        }

        return true;
    }

    protected void onResume() {
        super.onResume();
        if (SPHelper.isT9Clear(ctx)) {
            clearSearch();
        }
    }


    public void t9Search(String key) {
        try {
            toolbar.setTitle(key);
            fragment.cardInfos = new ArrayList();
            fragment.cardAdapter.setItems(fragment.cardInfos);
            for (int i = 0; i < currentList.size(); i++) {
                CardInfo info = currentList.get(i);
                if (T9Matcher.matches(info.getEx3(), key).found()) {
                    fragment.cardInfos.add(info);
                }
            }
            if ((SPHelper.isT9Auto(ctx)) && (Xutils.listNotNull(fragment.cardInfos)) && (fragment.cardInfos.size() == 1)) {
                CardInfo info = fragment.cardInfos.get(0);
                MyFt.clickCard(ctx, info);
            }
            fragment.cardAdapter.setItems(fragment.cardInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
