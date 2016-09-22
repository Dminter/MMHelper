package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by dminter on 2016/7/26.
 */

public abstract class BaseActivity extends MySwipeBackActivity {
    protected Context ctx;
    public Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResource() != -1) {
            setContentView(getLayoutResource());
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(R.color.material_light_white));
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.md_nav_back);
        }
        ctx = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return false;
    }

    protected abstract int getLayoutResource();

}
