package com.zncm.dminter.mmhelper.floatball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.zncm.dminter.mmhelper.SPHelper;
import com.zncm.dminter.mmhelper.WatchingAccessibilityService;

public class FloatWindowManager {
    public static FloatBallView mBallView;

    private static WindowManager mWindowManager;


    public static void addBallView(FloatBallService context) {
        if (mBallView == null) {
            WindowManager windowManager = getWindowManager(context);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            mBallView = new FloatBallView(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            int ballX = SPHelper.getBigBallX(context);
            int ballY = SPHelper.getBigBallY(context);
            if (ballX==-1&&ballY==-1){
                ballX = screenWidth;
                ballY = screenHeight / 2;
            }
            params.x = ballX;
            params.y = ballY;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mBallView.setLayoutParams(params);
            windowManager.addView(mBallView, params);
        }
    }

    public static void removeBallView(Context context) {
        if (mBallView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mBallView);
            mBallView = null;
        }
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}