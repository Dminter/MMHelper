package com.zncm.dminter.mmhelper;

import android.os.Bundle;
import android.os.PersistableBundle;

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


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_t9search;
    }
}
