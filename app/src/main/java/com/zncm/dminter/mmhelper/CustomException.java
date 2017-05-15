package com.zncm.dminter.mmhelper;

import android.content.Context;

import com.zncm.dminter.mmhelper.utils.Xutils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomException implements Thread.UncaughtExceptionHandler {
    private Context ctx;

    public CustomException(Context context) {
        ctx = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String errorMsg = getErrorInfo(throwable);
        SPHelper.setFcLog(ctx, errorMsg);
        System.exit(0);
    }


    private static String getErrorInfo(Throwable tr) {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        tr.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }
}