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
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

/**
 * Created by dminter on 2016/11/1.
 */

public class AboutAc extends MaterialSettings {

    Activity ctx;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
//        addItem(new TextItem(this.ctx, "").setTitle("使用手册").setOnclick(new TextItem.OnClickListener() {
//            public void onClick(TextItem textItem) {
//                startActivity(new Intent(AboutAc.this.ctx, HelpAc.class));
//            }
//        }));
//        addItem(new DividerItem(this.ctx));

        addItem(new TextItem(this.ctx, "").setTitle("使用手册").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                Xutils.openUrl(Constant.help_url);
            }
        }));
        addItem(new DividerItem(this.ctx));


        addItem(new TextItem(this.ctx, "").setTitle("酷安评分、讨论、更新").setSubtitle("当前版本：" + Xutils.getAppVersion(getPackageName())).setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem paramAnonymousTextItem) {
                Xutils.openUrl(Constant.update_url);
                DbUtils.cardUpdate();
            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("分享给朋友").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                Xutils.sendTo(AboutAc.this.ctx, getString(R.string.app_name) + "    http://www.coolapk.com/apk/com.zncm.dminter.mmhelper");
            }
        }));

        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("BY Dminter").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                Xutils.cmdWxUserExe(Constant.author_wx);
                DbUtils.cardXm();
            }
        }));
        addItem(new DividerItem(this.ctx));
        addItem(new TextItem(this.ctx, "").setTitle("特别感谢").setOnclick(new TextItem.OnClickListener() {
            public void onClick(TextItem textItem) {
                thank();
            }
        }));


    }

    private void thank() {
        new MaterialDialog.Builder(this).title("特别感谢").content(" compile fileTree(include: ['*.jar'], dir: 'libs')\n" +
                "    compile 'com.android.support:appcompat-v7:24.0.1'\n" +
                "    compile 'com.android.support:design:22.2.0'\n" +
                "    compile 'com.android.support:cardview-v7:21.0.2'\n" +
                "    compile files('libs/ormlite-android-4.49-SNAPSHOT.jar')\n" +
                "    compile files('libs/ormlite-core-4.49-SNAPSHOT.jar')\n" +
                "    compile 'com.afollestad.material-dialogs:core:0.8.6.2'\n" +
                "    compile 'com.afollestad.material-dialogs:commons:0.9.1.0'\n" +
                "    compile 'org.greenrobot:eventbus:3.0.0'\n" +
                "    compile 'com.malinskiy:materialicons:1.0.1'\n" +
                "    compile 'com.astuetz:pagerslidingtabstrip:1.0.1'\n" +
                "    compile 'com.github.kenumir:MaterialSettings:v.1.2.2'\n" +
                "    compile 'cn.tinkling.t9:t9search:1.1.0'\n" +
                "    compile 'com.belerweb:pinyin4j:2.5.0'\n" +
                "    compile 'com.github.czy1121:settingscompat:1.1.4'\n" +
                "    compile 'com.github.shell-software:fab:1.1.2'").positiveText("知").show();
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
