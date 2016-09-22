package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class GuidViewActivity extends BaseActivity {

    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTutorial();
    }

    @Override
    protected int getLayoutResource() {
        return -1;
    }

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("没有ROOT，还请卸载！", "你可以添加任何app的任何页面，无忧跳过广告页面，提升工作效率",
                R.color.material_purple_400, R.mipmap.g1, R.mipmap.g1);
        TutorialItem tutorialItem2 = new TutorialItem("设置-打开悬浮窗和无障碍服务", "注意：MIUI等需要在应用权限里开启悬浮窗权限",
                R.color.material_blue_400, R.mipmap.g2, R.mipmap.g2);
        TutorialItem tutorialItem3 = new TutorialItem("添加快速打开计算器", "单击-左上角包名文本，长按文本隐藏悬浮窗",
                R.color.material_brown_400, R.mipmap.g3, R.mipmap.g3);
        TutorialItem tutorialItem4 = new TutorialItem("计算器添加成功", "单击计算器即可直接打开计算器app",
                R.color.material_blue_grey_400, R.mipmap.g4, R.mipmap.g4);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);
        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        if (SettingActivity.getInstance() != null) {
            SettingActivity.getInstance().finish();
        }

    }
}