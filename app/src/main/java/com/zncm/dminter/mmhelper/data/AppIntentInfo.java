package com.zncm.dminter.mmhelper.data;
/**
 *APP Intent信息实体
 */
public class AppIntentInfo {
    private String packageName;//包名
    private String className;//类名
    private String appName;//应用名称
    private String intentName;//intent名称


    public AppIntentInfo() {
    }

    public AppIntentInfo(String packageName, String className, String intentName) {
        this.packageName = packageName;
        this.className = className;
        this.intentName = intentName;
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

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }
}
