package com.zncm.dminter.mmhelper.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zncm.dminter.mmhelper.OpenInentActivity;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.ColorGenerator;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.Random;

public class SmallAppWidget extends AppWidgetProvider {

    public ArrayList<CardInfo> likeTop10 = new ArrayList();
    ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private void initData(Context paramContext, RemoteViews paramRemoteViews, int paramInt1, int paramInt2) {
        if (!Xutils.listNotNull(this.likeTop10)) {
            this.likeTop10 = DbUtils.getCardInfos(EnumInfo.homeTab.LIKE.getValue(), 10);
            if (!Xutils.listNotNull(this.likeTop10)) {
                return;
            }
        }
        CardInfo cardInfo = (CardInfo) this.likeTop10.get(0);
        if (paramInt2 < this.likeTop10.size()) {
            cardInfo = (CardInfo) this.likeTop10.get(paramInt2);
        }
        paramRemoteViews.setTextViewText(paramInt1, cardInfo.getTitle());
        if (Xutils.isNotEmptyOrNull(cardInfo.getPackageName())) {
            paramRemoteViews.setInt(paramInt1, "setBackgroundColor", this.mColorGenerator.getColor(cardInfo.getPackageName()));
        }
        for (; ; ) {
            Intent localIntent = new Intent(paramContext, OpenInentActivity.class);
            if (Xutils.isNotEmptyOrNull(cardInfo.getPackageName())) {
                localIntent.putExtra("pkName", cardInfo.getPackageName());
            }
            if (Xutils.isNotEmptyOrNull(cardInfo.getClassName())) {
                localIntent.putExtra("className", cardInfo.getClassName());
            }
            localIntent.putExtra("cardId", cardInfo.getId());
            localIntent.putExtra("isShotcut", false);
            localIntent.putExtra("random", new Random().nextLong());
            localIntent.setFlags(268435456);
            paramRemoteViews.setOnClickPendingIntent(paramInt1, PendingIntent.getActivity(paramContext, new Random().nextInt(), localIntent, 134217728));
            return;
//            paramRemoteViews.setInt(paramInt1, "setBackgroundColor", this.mColorGenerator.getColor("cmd"));
        }
    }


}