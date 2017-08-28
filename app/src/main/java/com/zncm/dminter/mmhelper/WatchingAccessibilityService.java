package com.zncm.dminter.mmhelper;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.zncm.dminter.mmhelper.utils.Xutils;


public class WatchingAccessibilityService extends AccessibilityService {
    private static WatchingAccessibilityService sInstance;

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /**
         *界面切换才会刷新，而不是之前的轮询1秒会导致卡顿
         */
        if (SPHelper.isAcFloat(this)) {
            WatchingService.show(this, Xutils.getCurrentActivity());
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
        return super.onUnbind(intent);
    }

    public static WatchingAccessibilityService getInstance(){
        return sInstance;
    }


}
