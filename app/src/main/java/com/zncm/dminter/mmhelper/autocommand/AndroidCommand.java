package com.zncm.dminter.mmhelper.autocommand;

import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 命令执行
 */
public final class AndroidCommand {
    public static String lastError = "";
    public static int noRoot = -1;
    public static int appDisable = -2;

    public static int execRooted(String command) {
        return exec(command, true);
    }

    protected static int exec(String command, boolean isNeedRoot) {
        try {
            Process androidCommand = null;
            if (isNeedRoot) {
                androidCommand = Runtime.getRuntime().exec("su");
            } else {
                androidCommand = Runtime.getRuntime().exec(command);
            }
            DataOutputStream output = new DataOutputStream(
                    androidCommand.getOutputStream());
            output.writeBytes(command + "\n");
            output.flush();
            output.writeBytes("exit\n");
            output.flush();
            androidCommand.waitFor();

            String error = inputStream2String(androidCommand.getErrorStream());
//            cp: /data/data/com.miui.home/databases/launcher.db: No such file or directory
//            cp: bad '/data/data/com.miui.home/databases/launcher.db': No such file or directory


            if (Xutils.isNotEmptyOrNull(error) && error.contains(Constant.launcher_error)) {
                Xutils.tShort("launcher not support.");
                return androidCommand.exitValue();
            } else if (Xutils.isNotEmptyOrNull(error) && error.contains(Constant.app_is_disable_error)) {
                return appDisable;
            } else {
                return androidCommand.exitValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            lastError = e.getLocalizedMessage();
            return noRoot;
        }
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}
