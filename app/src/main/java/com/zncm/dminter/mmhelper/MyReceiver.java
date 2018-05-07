package com.zncm.dminter.mmhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zncm.dminter.mmhelper.utils.Xutils;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Xutils.debug("onReceive");
    }
}