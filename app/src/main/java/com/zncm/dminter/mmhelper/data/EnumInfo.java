package com.zncm.dminter.mmhelper.data;

/**
 * Created by dminter on 2016/7/23.
 */

public class EnumInfo {
    public enum typeShortcut {
        //三方+少，三方+多，全部+少，全部+多
        THREE_MORE(1, "THREE_MORE"), THREE_LESS(2, "THREE_LESS"), ALL_MORE(3, "ALL_MORE"), ALL_LESS(4, "ALL_LESS");
        private int value;
        public String strName;

        private typeShortcut(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum typeT9 {

        APP(1, "应用"), ACTIVITY(2, "活动"), APP_ACTIVITY(3, "应用+活动");
        private int value;
        public String strName;

        private typeT9(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static typeT9 getTypeT9(int value) {
            for (typeT9 tab : typeT9.values()) {
                if (tab.value == value) {
                    return tab;
                }
            }
            return APP;
        }
    }
    public enum cStatus {


        DISABLED(-1, "tmp"), ENABLE(-2, "tmp"), NORMAL(1, "微信聊天"), DELETE(2, "TO_ACTIVITY");
        private int value;
        public String strName;

        private cStatus(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum RefreshEnum {

        BAT_STOP(-1, "BAT_STOP"), FZ(1, "FZ"), APPS(2, "APPS"), ALL(3, "ALL"), LIKE(4, "LIKE"), APPSINIT(5, "APPSINIT");
        private int value;
        private String strName;

        private RefreshEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum pkStatus {

        DELETE(-1, "DELETE"), NORMAL(0, "NORMAL");
        private int value;
        private String strName;

        private pkStatus(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum homeTab {
        BAT_STOP(0, "bat_stop", "冷冻室"), APPS(1, "enable", "应用"), LIKE(2, "like", "收藏"), ALL(3, "all", "所有");
        private String value;
        private int position;
        public String strName;

        private homeTab(int position, String value, String strName) {
            this.position = position;
            this.value = value;
            this.strName = strName;
        }

        public String getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public int getPosition() {
            return position;
        }

        public static homeTab getHomeTab(String value) {
            for (homeTab tab : homeTab.values()) {
                if (tab.value.equals(value)) {
                    return tab;
                }
            }
            return null;
        }

        public static homeTab getHomeTab(int position) {
            for (homeTab tab : homeTab.values()) {
                if (tab.position == position) {
                    return tab;
                }
            }
            return null;
        }
    }


    public enum cType {


        WX(1, "微信聊天"), TO_ACTIVITY(2, "TO_ACTIVITY"), JUST_TIPS(3, "JUST_TIPS"), START_APP(4, "START_APP"), QQ(5, "QQ聊天"), URL(6, "书签"), CMD(7, "CMD"), SHORT_CUT_SYS(8, "快捷方式");

        private int value;
        public String strName;

        private cType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum appStatus {

        ENABLE(1, "启用"), DISABLED(2, "停用");

        private int value;
        public String strName;

        private appStatus(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum appType {

        SYSTEM(1, "SYSTEM"), THREE(2, "THREE");

        private int value;
        public String strName;

        private appType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum cardType {


        material_basic_buttons_card(1, "微信聊天"),
        material_basic_image_buttons_card_layout(2, "微信聊天"),
        material_big_image_card_layout(3, "微信聊天"),
        material_image_with_buttons_card(4, "微信聊天"),
        material_list_card_layout(5, "微信聊天"),
        material_small_image_card(6, "微信聊天"),
        material_welcome_card_layout(7, "微信聊天"),;
        private int value;
        public String strName;

        private cardType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }
}
