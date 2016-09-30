package com.zncm.dminter.mmhelper.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dminter on 2016/7/22.
 */

public class CardInfo {
//    packageName:" + info.topActivity.getPackageName() + " className:

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String cmd;
    @DatabaseField
    private String packageName;
    @DatabaseField
    private String className;
    @DatabaseField
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField
    private String icon;
    @DatabaseField
    private String iconUrl;

    @DatabaseField
    private int type;//1微信聊天
    @DatabaseField
    private int status = EnumInfo.cStatus.NORMAL.getValue();//1正常2删除 -1停用，-2启用TMP
    @DatabaseField
    private int index;
    @DatabaseField
    private int card_type;//
    @DatabaseField
    private int card_color;//
    @DatabaseField
    private Long time;

    @DatabaseField
    private String ex1;//appName
    @DatabaseField
    private String ex2;//pinyin
    @DatabaseField
    private String ex3;
    @DatabaseField
    private String ex4;
    @DatabaseField
    private String ex5;


    @DatabaseField
    private int exi1;   //1收藏
    @DatabaseField
    private int exi2;//排序
    @DatabaseField
    private int exi3;//自定义分组 1,2,3,4
    @DatabaseField
    private int exi4;
    @DatabaseField
    private int exi5;


    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] img;



    private boolean isDisabled = false;

    public CardInfo() {
    }


    public CardInfo(String packageName, int status) {
        this.type = EnumInfo.cType.START_APP.getValue();
        this.status = status;
        this.card_type = EnumInfo.cardType.material_small_image_card.getValue();
        this.packageName = packageName;
    }




    public CardInfo(int type, String cmd, String title) {
        this.cmd = cmd;

        if (EnumInfo.cType.WX.getValue() == type) {
            this.packageName = PackageInfo.pk_wx;
            this.className = PackageInfo.pk_wx_ChattingUI;
        } else if (EnumInfo.cType.QQ.getValue() == type) {
            this.packageName = PackageInfo.pk_qq;
            this.className = "";
        } else if (EnumInfo.cType.URL.getValue() == type) {
            this.packageName = "";
            this.className = "";
        }
        this.title = title;
        this.description = cmd;
        this.type = type;
        this.card_type = EnumInfo.cardType.material_basic_buttons_card.getValue();
        this.time = System.currentTimeMillis();
    }

    public CardInfo(String packageName, String className, String title) {
        this.packageName = packageName;
        this.className = className;
        this.title = title;
        this.description = className;
        this.type = EnumInfo.cType.TO_ACTIVITY.getValue();
        this.card_type = EnumInfo.cardType.material_small_image_card.getValue();
        this.time = System.currentTimeMillis();
    }


    public CardInfo(String title, String description, int card_color) {
        this.title = title;
        this.description = description;
        this.card_color = card_color;
        this.type = EnumInfo.cType.JUST_TIPS.getValue();
        this.card_type = EnumInfo.cardType.material_welcome_card_layout.getValue();
        this.time = System.currentTimeMillis();
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public int getCard_color() {
        return card_color;
    }

    public void setCard_color(int card_color) {
        this.card_color = card_color;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getEx1() {
        return ex1;
    }

    public void setEx1(String ex1) {
        this.ex1 = ex1;
    }

    public String getEx2() {
        return ex2;
    }

    public void setEx2(String ex2) {
        this.ex2 = ex2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }

    public String getEx4() {
        return ex4;
    }

    public void setEx4(String ex4) {
        this.ex4 = ex4;
    }

    public String getEx5() {
        return ex5;
    }

    public void setEx5(String ex5) {
        this.ex5 = ex5;
    }

    public int getExi1() {
        return exi1;
    }

    public void setExi1(int exi1) {
        this.exi1 = exi1;
    }

    public int getExi2() {
        return exi2;
    }

    public void setExi2(int exi2) {
        this.exi2 = exi2;
    }

    public int getExi3() {
        return exi3;
    }

    public void setExi3(int exi3) {
        this.exi3 = exi3;
    }

    public int getExi4() {
        return exi4;
    }

    public void setExi4(int exi4) {
        this.exi4 = exi4;
    }

    public int getExi5() {
        return exi5;
    }

    public void setExi5(int exi5) {
        this.exi5 = exi5;
    }


    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
