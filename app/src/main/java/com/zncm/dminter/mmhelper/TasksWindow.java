package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
        ImageView image = (ImageView) sView.findViewById(R.id.image);
//        textView.setText(text);


        try {


//            DoubleClickImp.registerDoubleClickListener(textView,
//                    new DoubleClickImp.OnDoubleClickListener() {
//                        @Override
//                        public void OnSingleClick(View v) {
////                            final String pName = text.split("\\n")[0];
////                            final String cName = text.split("\\n")[1];
////                            CardInfo card = new CardInfo(pName, cName, null);
////
////                            DbUtils.insertCard(card);
////                            Xutils.tShort("已添加~");
//                        }
//
//                        @Override
//                        public void OnDoubleClick(View v) {
//                            NotificationActionReceiver.showNotification(context, true);
//                            dismiss(context);
//                            SPHelper.setIsShowWindow(context, false);
//
//                        }
//                    });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String pName = text.split("\\n")[0];
                    final String cName = text.split("\\n")[1];
                    CardInfo card = new CardInfo(pName, cName, null);

                    DbUtils.insertCard(card);
                    Xutils.debug("card====>>"+pName+" "+cName);
                    Xutils.tShort("已添加~");
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    final String pName = text.split("\\n")[0];
//                    final String cName = text.split("\\n")[1];
//                    CardInfo card = new CardInfo(pName, cName, null);
//                    DbUtils.insertCard(card);
//                    Xutils.tShort("已添加~");
//                    NotificationActionReceiver.showNotification(context, true);
                    dismiss(context);
                    SPHelper.setIsShowWindow(context, false);
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
