package com.zncm.dminter.mmhelper.data;

public class MyAppInfo {
    private String packageName;
    private String className;


    public MyAppInfo() {
    }

    public MyAppInfo(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
