package com.zncm.dminter.mmhelper.floatball;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;

import ezy.assist.compat.SettingsCompat;

public class FloatBallService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            FloatWindowManager.addBallView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (FloatWindowManager.mBallView != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                FloatWindowManager.mBallView.setVisibility(View.GONE);
            } else {
                FloatWindowManager.mBallView.setVisibility(View.VISIBLE);
            }
        }
    }
}