package com.zncm.dminter.mmhelper;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.utils.Xutils;

/**
 * Created by dminter on 2016/11/1.
 */

public class HelpAc extends MaterialSettings {

    Activity ctx;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        addMyItem(R.string.h1, R.string.a1);
        addMyItem(R.string.h2, R.string.a2);
        addMyItem(R.string.h3, R.string.a3);
        addMyItem(R.string.h4, R.string.a4);
        addMyItem(R.string.h5, R.string.a5);
        addMyItem(R.string.h6, R.string.a6);
        addMyItem(R.string.h7, R.string.a7);
        addMyItem(R.string.h8, R.string.a8);
        addMyItem(R.string.h9, R.string.a9);
        addMyItem(R.string.h10, R.string.a10);
        addMyItem(R.string.h11, R.string.a11);
        addMyItem(R.string.h12, R.string.a12);
        addMyItem(R.string.h13, R.string.a13);
        addMyItem(R.string.h13, R.string.a13);

        addMyItem(R.string.h14, R.string.a14);
        addMyItem(R.string.h15, R.string.a15);
        addMyItem(R.string.h16, R.string.a16);
//        addMyItem(R.string.h17, R.string.a17);
//        addMyItem(R.string.h18, R.string.a18);

    }

    private void addMyItem(final int help, final int help_c) {
        addItem(new TextItem(ctx, "").setTitle(getResources().getString(help)).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                showAnswer(getResources().getString(help), getResources().getString(help_c));
            }
        }));
        addItem(new DividerItem(ctx));
    }


    private void showAnswer(String title, String content) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .positiveText("知")
                .show();
    }


    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            backDo();
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        backDo();
    }


    private void backDo() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

}
