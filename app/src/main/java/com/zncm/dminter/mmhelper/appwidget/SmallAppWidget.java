package com.zncm.dminter.mmhelper.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.OpenInentActivity;
import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.ColorGenerator;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 桌面小插件，直达收藏的活动
 */
public class SmallAppWidget extends AppWidgetProvider {

    public ArrayList<CardInfo> likeTop10 = new ArrayList();
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;


    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widgetsmall);
        initData(context, remoteViews, R.id.activityName1, 0);
        initData(context, remoteViews, R.id.activityName2, 1);
        initData(context, remoteViews, R.id.activityName3, 2);
        initData(context, remoteViews, R.id.activityName4, 3);
        initData(context, remoteViews, R.id.activityName5, 4);
        if (MyApplication.isPay) {
            remoteViews.setViewVisibility(R.id.llPay, View.VISIBLE);
            initData(context, remoteViews, R.id.activityName6, 5);
            initData(context, remoteViews, R.id.activityName7, 6);
            initData(context, remoteViews, R.id.activityName8, 7);
            initData(context, remoteViews, R.id.activityName9, 8);
            initData(context, remoteViews, R.id.activityName10, 9);
        }
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }


    private void initData(Context context, RemoteViews remoteViews, int textView, int position) {
        if (!Xutils.listNotNull(likeTop10)) {
            likeTop10 = DbUtils.getCardInfos(EnumInfo.homeTab.LIKE.getValue(), 10);
            if (!Xutils.listNotNull(likeTop10)) {
                return;
            }
        }
        CardInfo cardInfo = likeTop10.get(0);
        if (position < likeTop10.size()) {
            cardInfo = likeTop10.get(position);
        }
        remoteViews.setTextViewText(textView, cardInfo.getTitle());
        //根据包名统一颜色
        if (Xutils.isNotEmptyOrNull(cardInfo.getPackageName())) {
            remoteViews.setInt(textView, "setBackgroundColor", mColorGenerator.getColor(cardInfo.getPackageName()));
        } else {
            remoteViews.setInt(textView, "setBackgroundColor", mColorGenerator.getColor("cmd"));
        }
        Intent intent = new Intent(context, OpenInentActivity.class);
        if (Xutils.isNotEmptyOrNull(cardInfo.getPackageName())) {
            intent.putExtra("pkName", cardInfo.getPackageName());
        }
        if (Xutils.isNotEmptyOrNull(cardInfo.getClassName())) {
            intent.putExtra("className", cardInfo.getClassName());
        }
        intent.putExtra("cardId", cardInfo.getId());
        intent.putExtra("isShotcut", false);
        intent.putExtra("random", new Random().nextLong());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        remoteViews.setOnClickPendingIntent(textView, PendingIntent.getActivity(context, new Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }


}