package com.zncm.dminter.mmhelper.data;
/**
 *APP信息实体
 */
public class MyAppInfo {
    private String packageName;//包名
    private String className;//类名
    private String appName;//应用名称


    public MyAppInfo() {
    }

    public MyAppInfo(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public MyAppInfo(String packageName, String className, String appName) {
        this.packageName = packageName;
        this.className = className;
        this.appName = appName;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
