package com.zncm.dminter.mmhelper;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.zncm.dminter.mmhelper.floatball.FloatWindowManager;


public class WatchingAccessibilityService extends AccessibilityService {
    private static WatchingAccessibilityService sInstance;
    public String packageName;
    public String className;

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(SPHelper.isShowWindow(this)){
            TasksWindow.show(this, event.getPackageName() + "\n" + event.getClassName());
            packageName = (String) event.getPackageName();
            className = (String) event.getClassName();
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sInstance = this;
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sInstance = null;
        TasksWindow.dismiss(this);
        return super.onUnbind(intent);
    }

    public static WatchingAccessibilityService getInstance(){
        return sInstance;
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
