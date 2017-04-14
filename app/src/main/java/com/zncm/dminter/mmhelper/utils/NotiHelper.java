package com.zncm.dminter.mmhelper.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.R;

import java.util.Random;

public class NotiHelper {
    public static void noti(String title, String content, String ticker, Intent intent, boolean autoCancel, boolean bRing, int notifyId) {
        Context ctx = MyApplication.getInstance().ctx;
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content)
                .setContentIntent(getDefalutIntent(ctx, intent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(autoCancel)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台笔记本,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        if (bRing) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    .setSound(uri);       //Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }


    public static PendingIntent getDefalutIntent(Context ctx, Intent intent, int flags) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, new Random().nextInt(), intent, flags);
        return pendingIntent;
    }


    public static void clearNoti(Context ctx, int notifyId) {
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notifyId);
    }
}
