package com.zncm.dminter.mmhelper;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.floatball.FloatWindow;
import com.zncm.dminter.mmhelper.utils.NotiHelper;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.Timer;
import java.util.TimerTask;

public class WatchingService extends Service {

    private Handler mHandler = new Handler();
    private static Timer timer;
    public static FloatWindow mFloatWindow;
    static TextView textView;

    public static void show(final Context context, final String text) {
        try {
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String pName = text.split("\\n")[0];
                    final String cName = text.split("\\n")[1];
                    if (Xutils.isEmptyOrNull(pName) || Xutils.isEmptyOrNull(cName)) {
                        return;
                    }
                    String title = cName;
                    if (Xutils.isNotEmptyOrNull(cName) && cName.contains(".")) {
                        title = cName.substring(cName.lastIndexOf(".") + 1);
                    }
                    CardInfo card = new CardInfo(pName, cName, title);
                    DbUtils.insertCard(card);
                    Xutils.tShort("已添加 " + title);
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Xutils.copyText(context,text);
                    Xutils.tShort("已复制~");
                    return true;
                }
            });
        } catch (Exception e) {
        }

    }

    public static void dismiss(Context context) {
        SPHelper.setIsAcFloat(context, false);
        timer.cancel();
        timer = null;
        mFloatWindow.hide();
        NotiHelper.clearNoti(context, Constant.n_id_ac);
    }


    private int getType() {
        int type = WindowManager.LayoutParams.TYPE_PHONE;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        return type;
    }


    private FloatWindow showFloatView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.window_tasks,
                null);
        textView = (TextView) view.findViewById(R.id.text);
//        view.setSystemUiVisibility(
//                view.getSystemUiVisibility()
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        );
        mFloatWindow = new FloatWindow(context);
        mFloatWindow
                .init(view)
                .setLayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        getType(),
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT,
                        Gravity.TOP)
                .attach();

        mFloatWindow.setOnFloatWindowClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mFloatWindow.setOnFloatWindowLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        return mFloatWindow;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showFloatView(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentCopy = new Intent(this, ShortcutActionActivity.class);
        intentCopy.putExtra("action", Constant.SA_GET_ACTIVITY_STOP);
        intentCopy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotiHelper.noti("点击关闭活动采集悬浮窗", "", "", intentCopy, true, false, Constant.n_id_ac);
        timer = new Timer();
        timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        if (mFloatWindow != null) {
            mFloatWindow.show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            if (SPHelper.isAcFloat(WatchingService.this)) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        WatchingService.show(WatchingService.this, Xutils.getCurrentActivity());
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 500,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

}
