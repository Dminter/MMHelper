package com.zncm.dminter.mmhelper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.MyReceiver;
import com.zncm.dminter.mmhelper.OpenInentActivity;
import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.SPHelper;
import com.zncm.dminter.mmhelper.autocommand.AndroidCommand;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.statusbar.StatusBarCompat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by dminter on 2016/7/19.
 */

public class Xutils {


    /**
     * 获取默认launcher名
     */
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        //如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;
        if (!srcFile.exists()) {
            return false;
        }
        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    public static File createFile(String path) throws IOException {
        if (isNotEmptyOrNull(path)) {
            File file = new File(path);
            if (!file.exists()) {
                int lastIndex = path.lastIndexOf(File.separator);
                String dir = path.substring(0, lastIndex);
                if (createFolder(dir) != null) {
                    file.createNewFile();
                    return file;
                }
            } else {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    public static File createFolder(String path) {
        if (isNotEmptyOrNull(path)) {
            File dir = new File(path);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    return dir;
                }
            }
            dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }

    public static boolean copyFileTo(InputStream inputStream, File destFile)
            throws IOException {
        if (inputStream == null || destFile == null) {
            return false;
        }
        if (destFile.isDirectory())
            return false;

        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = inputStream.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        inputStream.close();
        return true;
    }

    public static String getApplicationNameByPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        String applicationName = null;
        try {
            applicationName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(
                    packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName;
    }

    public static String getCurrentActivity() {
        try {
            String pkgName = "";
            String className = "";
            String request = exec("dumpsys activity | grep \"mFocusedActivity\"");
            if (Build.VERSION.SDK_INT >= 26) {
                request = exec("dumpsys activity activities | sed -En -e '/Running activities/,/Run #0/p'");
                if (request.contains("TaskRecord") && request.contains(" u0 ") && request.contains(" t")) {
                    request = request.substring(request.indexOf(" u0 ") + 4, request.indexOf(" t"));
                    String mFocusedActivity = request;
                    if (mFocusedActivity.contains("/")) {
                        String arr[] = mFocusedActivity.split("/");
                        if (arr != null && arr.length > 0 && arr.length == 2) {
                            pkgName = arr[0];
                            className = arr[1];
                            if (className.startsWith(".")) {
                                className = pkgName + className;
                            }
                            String pkgClass = pkgName + "\n" + className;
                            return pkgClass;
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(request)) {
                String mFocusedActivity = request;
                if (mFocusedActivity.contains("mFocusedActivity")) {
                    mFocusedActivity = mFocusedActivity.substring(mFocusedActivity.indexOf("{") + 1, mFocusedActivity.indexOf("}"));
                    /**
                     *f70e135 u0 com.zncm.dminter.mmhelper/.SettingNew t15741
                     */
                    if (mFocusedActivity.contains(" ")) {
                        String arr[] = mFocusedActivity.split(" ");
                        if (arr != null && arr.length > 0 && arr.length == 4) {
                            mFocusedActivity = arr[2];
                            if (mFocusedActivity.contains("/")) {
                                arr = mFocusedActivity.split("/");
                                if (arr != null && arr.length > 0 && arr.length == 2) {
                                    pkgName = arr[0];
                                    className = arr[1];
                                    if (className.startsWith(".")) {
                                        className = pkgName + className;
                                    }
                                    String pkgClass = pkgName + "\n" + className;
                                    return pkgClass;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    public static int cmdWxUserExe(String cmd) {
        String str = "com.tencent.mm.ui.chatting.ChattingUI -e Chat_User ";
        return cmdExe("am start -n com.tencent.mm/" + str + cmd);
    }

    public static boolean isNewWx() {
        boolean flag = false;
        int version = 0;
        String str = getAppVersion("com.tencent.mm");
        try {
            if (str.contains(".")) {
                str = str.replaceAll("\\.", "");
            }
            if (str.length() > 3) {
                str = str.substring(0, 3);
            }
            version = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            Xutils.tShort("请检查是否安装微信~");
        }
        if (version >= 656) {
            flag = true;
        }
        return flag;
    }

    public static String getFileSaveTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }


    public static void initBarTheme(Activity activity, Toolbar toolbar) {
        toolbar.setBackgroundColor(SPHelper.getThemeColor(activity));
        if (!SPHelper.isHS(activity)) {
            StatusBarCompat.setStatusBarColor(activity, SPHelper.getThemeColor(activity));
        }
    }

    public static void autoKeyBoardShow(EditText editText) {
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
    }

    public static void hideKeyBoard(EditText editText) {
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public static String getTimeTodayYMD() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    //获取应用主界面
    public static String getLaunchClassNameByPkName(Context context, String pkgName) {
        Intent intent = isExit(context, pkgName);
        if (intent == null) {
            exec("pm enable " + pkgName);
            intent = isExit(context, pkgName);
        }
        return intent.getComponent().getClassName();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void openUrl(String url) {
        try {
            if (isNotEmptyOrNull(url) && !url.startsWith("http")) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().ctx.startActivity(it);
        } catch (Exception e) {
            Xutils.tShort("请检查是否安装浏览器~");
        }
    }

    public static String exec(String command) {
        // execute a shell command, returning output in a string
        try {

            if (Xutils.isNotEmptyOrNull(command) && command.contains("pm disable com.zncm.dminter.mmhelper")) {
                return "";
            }

            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("su");
//            Process process = rt.exec("sh");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            process.waitFor();

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void copyText(Context ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
    }

    public static int cmdExe(String cmdEnd) {
        String commands = cmdEnd;
        int ret = AndroidCommand.noRoot;
        if (Xutils.isNotEmptyOrNull(commands)) {
//            if (commands.contains("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")) {
//                commands = commands.replace("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI", "com.tencent.mm.plugin.sns.ui.En_424b8e16");
//            }
            ret = AndroidCommand.execRooted(commands);
            if (ret == AndroidCommand.noRoot) {
                try {
                    commands = commands.substring(0, commands.lastIndexOf("/"));
                    commands = commands.replaceAll("am start -n ", "");
                    startAppByPackageName(MyApplication.getInstance().getApplicationContext(), commands, Constant.attempt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    public static void sendToDesktop(Activity activity, CardInfo cardInfo) {
        sendToDesktop(activity, cardInfo, false);
    }


    public static void sendToDesktop(Activity activity, CardInfo cardInfo, boolean isShotcut) {
        sendToDesktop(activity, cardInfo, isShotcut, true);
    }

    /**
     * https://blog.csdn.net/rentee/article/details/77005547
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addShortCut(Activity activity, CardInfo cardInfo, boolean isShotcut, boolean isTip) {
        ShortcutManager shortcutManager = (ShortcutManager) activity.getSystemService(Context.SHORTCUT_SERVICE);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            Bitmap bitmap = null;
            Drawable drawable = null;
            if (cardInfo.getType() == EnumInfo.cType.URL.getValue()) {
                drawable = activity.getResources().getDrawable(R.mipmap.ic_url);
            } else if (cardInfo.getType() == EnumInfo.cType.CMD.getValue()) {
                drawable = activity.getResources().getDrawable(R.mipmap.ic_shell);
            } else if (cardInfo.getType() == EnumInfo.cType.SHORT_CUT_SYS.getValue()) {
                drawable = activity.getResources().getDrawable(R.mipmap.ic_shortcut);
                if (cardInfo.getImg() != null) {
                    bitmap = MyFt.bytes2Bimap(cardInfo.getImg());
                }
            }
            BitmapDrawable bitmapDrawable;
            Intent shortIntent = new Intent(activity, OpenInentActivity.class);
            shortIntent.setAction("android.intent.action.VIEW");
            shortIntent.putExtra("android.intent.extra.UID", 0);
            shortIntent.putExtra("pkName", cardInfo.getPackageName());
            shortIntent.putExtra("className", cardInfo.getClassName());
            shortIntent.putExtra("cardId", cardInfo.getId());
            shortIntent.putExtra("isShotcut", isShotcut);
            shortIntent.putExtra("random", new Random().nextLong());
            bitmapDrawable = (BitmapDrawable) drawable;

            try {
                if (bitmapDrawable == null) {
                    bitmapDrawable = (BitmapDrawable) getAppIcon(cardInfo.getPackageName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bitmapDrawable == null) {
                bitmapDrawable = (BitmapDrawable) activity.getResources().getDrawable(R.mipmap.ic_launcher);
            }

            if (bitmap == null) {
                bitmap = bitmapDrawable.getBitmap();
            }
            PersistableBundle bundle =       new PersistableBundle();
            bundle.putString("pkName", cardInfo.getPackageName());
            shortIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错
            ShortcutInfo info = new ShortcutInfo.Builder(activity, (System.currentTimeMillis() + new Random().nextLong()) + "")
                    .setIcon(Icon.createWithBitmap(bitmap))
                    .setShortLabel(cardInfo.getTitle())
                    .setExtras(bundle)
                    .setIntent(shortIntent)
                    .build();
            //当添加快捷方式的确认弹框弹出来时，将被回调
            PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(activity, 0, new Intent(activity, MyReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
            if (isTip) {
                tShort(Constant.add_shortcut);
            }
        } else {
            tShort("添加失败~");
        }

    }

    public static void sendToDesktop(Activity activity, CardInfo cardInfo, boolean isShotcut, boolean isTip) {

        if (Build.VERSION.SDK_INT >= 26) {
            addShortCut(activity, cardInfo, isShotcut, isTip);
            return;
        }


        Bitmap bitmap = null;
        Drawable drawable = null;
        if (cardInfo.getType() == EnumInfo.cType.URL.getValue()) {
            drawable = activity.getResources().getDrawable(R.mipmap.ic_url);
        } else if (cardInfo.getType() == EnumInfo.cType.CMD.getValue()) {
            drawable = activity.getResources().getDrawable(R.mipmap.ic_shell);
        } else if (cardInfo.getType() == EnumInfo.cType.SHORT_CUT_SYS.getValue()) {
            drawable = activity.getResources().getDrawable(R.mipmap.ic_shortcut);
            if (cardInfo.getImg() != null) {
                bitmap = MyFt.bytes2Bimap(cardInfo.getImg());
            }
        }
        Intent intent;
        BitmapDrawable bitmapDrawable;
        intent = new Intent();
        Intent shortIntent = new Intent(activity, OpenInentActivity.class);
        shortIntent.setAction("android.intent.action.VIEW");
        shortIntent.putExtra("android.intent.extra.UID", 0);
        shortIntent.putExtra("pkName", cardInfo.getPackageName());
        shortIntent.putExtra("className", cardInfo.getClassName());
        shortIntent.putExtra("cardId", cardInfo.getId());
        shortIntent.putExtra("isShotcut", isShotcut);
        shortIntent.putExtra("random", new Random().nextLong());
        intent.putExtra("android.intent.extra.shortcut.INTENT", shortIntent);
        intent.putExtra("android.intent.extra.shortcut.NAME", makeShortcutIconTitle(cardInfo.getTitle()));
        bitmapDrawable = (BitmapDrawable) drawable;
        if (bitmapDrawable == null) {
            try {
                intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(activity.createPackageContext(cardInfo.getPackageName(), 0), getAppIconId(cardInfo.getPackageName())));
            } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
                localNameNotFoundException.printStackTrace();
            }
        } else {
            if (bitmapDrawable == null) {
                bitmapDrawable = (BitmapDrawable) activity.getResources().getDrawable(R.mipmap.ic_launcher);
            }
            if (bitmap == null) {
                bitmap = bitmapDrawable.getBitmap();
            }
            intent.putExtra("android.intent.extra.shortcut.ICON", bitmap);

        }
        intent.putExtra("duplicate", false);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        activity.sendBroadcast(intent);
        if (isTip) {
            tShort(Constant.add_shortcut);
        }
    }

    // 发送到桌面快捷方式
//    public static void sendToDesktop(Activity ctx, Class<?> mClass, String title, Drawable drawable, String pkName, String className, int cardId) {
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap bitmap = null;
//        if (bitmapDrawable == null) {
//            bitmapDrawable = (BitmapDrawable) ctx.getResources().getDrawable(R.mipmap.ic_launcher);
//        }
//        if (bitmapDrawable == null) {
//            return;
//        }
//        bitmap = bitmapDrawable.getBitmap();
//        Intent sender = new Intent();
//        Intent shortcutIntent = new Intent(ctx, mClass);
//        shortcutIntent.setAction(Intent.ACTION_VIEW);
//        shortcutIntent.putExtra(Intent.EXTRA_UID, 0);
//        shortcutIntent.putExtra("pkName", pkName);
//        shortcutIntent.putExtra("className", className);
//        shortcutIntent.putExtra("cardId", cardId);
//        sender.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        sender.putExtra(Intent.EXTRA_SHORTCUT_NAME, makeShortcutIconTitle(title));
//        sender.putExtra(Intent.EXTRA_SHORTCUT_ICON,
//                bitmap);
////        sender.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
////                Intent.ShortcutIconResource.fromContext(ctx, resId));
//        sender.putExtra("duplicate", true);
//        sender.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        ctx.sendBroadcast(sender);
//    }

    // 创建快捷方式
    public static String makeShortcutIconTitle(String content) {
        final int SHORTCUT_ICON_TITLE_MAX_LEN = 10;
        final String TAG_CHECKED = String.valueOf('\u221A');
        final String TAG_UNCHECKED = String.valueOf('\u25A1');
        content = content.replace(TAG_CHECKED, "");
        content = content.replace(TAG_UNCHECKED, "");
        return content.length() > SHORTCUT_ICON_TITLE_MAX_LEN ? content.substring(0,
                SHORTCUT_ICON_TITLE_MAX_LEN) : content;
    }

    public static void sendTo(Context ctx, String sendWhat) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sendWhat);
        ctx.startActivity(shareIntent);
    }

//    int attempt = 3;

    /**
     * 通过packagename启动应用
     */
    public static void startAppByPackageName(Context context, String packagename, int attempt) {
        try {
            Intent intent = isExit(context, packagename);
            context.startActivity(intent);
        } catch (Exception e) {
            exec(Constant.common_pm_e_p + packagename);
            if (attempt > 0) {
                startAppByPackageName(context, packagename, --attempt);
            } else {
                Xutils.tShort(Constant.no_open);
            }
        }

    }

    //卸载应用程序
    public static void unstallApp(Context context, String pkgName) {
        Intent uninstall_intent = new Intent();
        uninstall_intent.setAction(Intent.ACTION_DELETE);
        uninstall_intent.setData(Uri.parse("package:" + pkgName));
        context.startActivity(uninstall_intent);
    }

    public static Intent isExit(Context context, String pk_name) {
        if (isEmptyOrNull(pk_name)) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(pk_name);
        return it;
    }

    public static List<String> importTxt(Context context, int rawId) {
        List<String> dataList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            //new FileReader(file)
            InputStream is = context.getResources().openRawResource(rawId);
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }


    /**
     * tab 样式统一初始化
     */
    public static void initTabLayout(Context ctx, TabLayout mTabLayout) {
        mTabLayout.setBackgroundColor(SPHelper.getThemeColor(ctx));
        mTabLayout.setSelectedTabIndicatorColor(ctx.getResources().getColor(R.color.white));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
    }


    public static ApplicationInfo getAppInfo(String pName) {
        ApplicationInfo info;
        try {
            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
            info = pm.getApplicationInfo(pName, 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();

        }
        return null;

    }


    /*
     * 获取程序 图标
     */
    public static Drawable getAppIcon(String packname) {
        try {
            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static int getAppIconId(String packname) {
        try {
            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return R.mipmap.ic_launcher;
    }


    /**
     * 是否安装了xxx应用
     */
    public static boolean hasInstalledApp(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pkgName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
     *获取程序的版本号
     */
    public static String getAppVersion(String packname) {

        try {
            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }


    public static void tShort(String msg) {
        if (isEmptyOrNull(msg)) {
            return;
        }
        Toast.makeText(MyApplication.getInstance().ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void tLong(String msg) {
        if (isEmptyOrNull(msg)) {
            return;
        }
        Toast.makeText(MyApplication.getInstance().ctx, msg, Toast.LENGTH_LONG).show();
    }

    /*
     * 获取程序的名字
     */
    public String getAppName(String packname) {
        try {
            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
            }
        }
        return null;
    }

    public static <T> boolean listNotNull(List<T> t) {
        if (t != null && t.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void debug(Object object) {
        try {
            Log.d("MMHelper", String.valueOf(object));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 10, baos);
        return baos.toByteArray();
    }

    // Drawable转换成Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static byte[] drawable2Bytes(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2Bytes(bitmap);
    }

    /**
     * 对话框主题
     */
    public static MaterialDialog.Builder themeMaterialDialog(Context context) {
        return new MaterialDialog.Builder(context).theme(SPHelper.isNightMode(context) ? Theme.DARK : Theme.LIGHT);
    }


    public static Drawable initIconWhite(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.white).sizeDp(24);
    }


    public static boolean isEmptyOrNull(String string) {

        return string == null || string.trim().length() == 0 || string.equals("null");


    }

    public static boolean isNotEmptyOrNull(String string) {
        return !isEmptyOrNull(string);

    }


    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public static String getTimeYMDHM_(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date(inputDate));
    }

    /**
     * 获取24小时制 小时
     */
    public static int getHour() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
    }


    public static void writeTxtFile(String path, String newStr) {
        FileOutputStream fos = null;
        try {
            byte[] bom = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};

            File file = new File(path);
            if (file.exists()) {
                delFile(file);
            }
            file.createNewFile();
            fos = new FileOutputStream(file);

            fos.write(bom);
            fos.write(newStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean saveBitmap(Bitmap bitmap, String outputPath) {
        try {
            FileOutputStream fileOutputStream = null;
            File file = new File(outputPath);
            if (file.exists()) {
                delFile(file);
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String path = "";
        if (uri == null || "content://media/external/file/-1".equals(uri.toString())) {
            tShort("文件选取失败~");
            return null;
        }
        String[] projection = {"_data"};
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    }
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static boolean delFile(File file) {
        return file != null && file.exists() ? (file.isDirectory() ? false : file.delete()) : false;
    }


}
