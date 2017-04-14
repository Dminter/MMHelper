package com.zncm.dminter.mmhelper.utils;

import android.os.Environment;

import com.zncm.dminter.mmhelper.Constant;

import java.io.File;

public class MyPath {
    public static final String PATH_CONFIG = "配置";
    public static final String PATH_PICTURE = "截图";

    public static String getFolder(String folderName) {
        if (folderName == null) {
            return null;
        }
        File dir = Xutils.createFolder(folderName);
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }


    public static String getPathFolder(String path) {
        File rootPath = Environment.getExternalStoragePublicDirectory(Constant.PATH_ROOT);
        if (Xutils.isEmptyOrNull(path)) {
            return getFolder(rootPath.getAbsolutePath()) + File.separator;
        } else {
            return getFolder(rootPath + File.separator
                    + path + File.separator);
        }
    }

    public static String getPathConfig() {
        return getPathFolder(PATH_CONFIG);
    }

    public static String getPathPicture() {
        return getPathFolder(PATH_PICTURE);
    }

    public static String getPathRoot() {
        return getPathFolder(null);
    }

}
