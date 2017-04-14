package com.zncm.dminter.mmhelper;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;


public class WatchingAccessibilityService extends AccessibilityService {
    private static WatchingAccessibilityService sInstance;
    public String packageName;
    public String className;

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (SPHelper.isAcFloat(this)) {
            TasksWindow.show(this, event.getPackageName() + "\n" + event.getClassName());
        }
        packageName = (String) event.getPackageName();
        className = (String) event.getClassName();
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


}
