package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.db.DbUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

public class TasksWindow {

    private static WindowManager.LayoutParams sWindowParams;
    private static WindowManager sWindowManager;
    private static View sView;

    public static void init(final Context context) {
        sWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        sWindowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 2005, 0x18,
                PixelFormat.TRANSLUCENT);
        sWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        sWindowParams.gravity = Gravity.LEFT + Gravity.TOP;
        sView = LayoutInflater.from(context).inflate(R.layout.window_tasks,
                null);

    }


    public static void show(final Context context, final String text) {
        if (sWindowManager == null) {
            init(context);
        }
        TextView textView = (TextView) sView.findViewById(R.id.text);
        try {
            textView.setTextColor(SPHelper.getThemeColor(context));
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
                        title = cName.substring(cName.lastIndexOf("."));
                    }
                    CardInfo card = new CardInfo(pName, cName, title);
                    DbUtils.insertCard(card);
                    Xutils.tShort("已添加 " + title);
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dismiss(context);
                    SPHelper.setIsAcFloat(context, false);
                    return true;
                }
            });
            sWindowManager.addView(sView, sWindowParams);
        } catch (Exception e) {
        }
    }


    public static void dismiss(Context context) {
        try {
            sWindowManager.removeView(sView);
        } catch (Exception e) {
        }
    }

}
