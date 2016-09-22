package com.zncm.dminter.mmhelper.data;

/**
 * Created by dminter on 2016/7/23.
 */

public class EnumInfo {

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

        FZ(1, "FZ");
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

    public enum homeTab {
        APPS(0, "enable", "应用"),LIKE(1, "like", "收藏"), ALL(2, "all", "所有");
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


        WX(1, "微信聊天"), TO_ACTIVITY(2, "TO_ACTIVITY"), JUST_TIPS(3, "JUST_TIPS"), START_APP(4, "START_APP"), QQ(5, "QQ聊天"), URL(6, "书签");

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

        ENABLE(1, "启用"), DISABLED(2,  "停用");

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

        SYSTEM(1, "SYSTEM"), THREE(2,  "THREE");

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
