package com.zncm.dminter.mmhelper.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dminter on 2016/7/27.
 */

public class PkInfo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String packageName;
    @DatabaseField
    private String className;
    @DatabaseField
    private String name;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] icon;
    @DatabaseField
    private int status = EnumInfo.appStatus.ENABLE.getValue();

    @DatabaseField
    private int type = EnumInfo.appType.THREE.getValue();

    @DatabaseField
    private String ex1;//appName
    @DatabaseField
    private String ex2;//pinyin -paixu
    @DatabaseField
    private String ex3;
    @DatabaseField
    private String ex4;
    @DatabaseField
    private String ex5;


    @DatabaseField
    private int exi1;
    @DatabaseField
    private int exi2;
    @DatabaseField
    private int exi3;
    @DatabaseField
    private int exi4;
    @DatabaseField
    private int exi5;


    @DatabaseField
    private int exb1;
    @DatabaseField
    private int exb2;
    @DatabaseField
    private int exb3;
    @DatabaseField
    private int exb4;
    @DatabaseField
    private int exb5;


    public PkInfo(String packageName, String name, byte[] icon, int status, int type) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.status = status;
        this.type = type;
    }

    public PkInfo(String packageName, String name, byte[] icon) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
    }

    public PkInfo() {


    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getExb1() {
        return exb1;
    }

    public void setExb1(int exb1) {
        this.exb1 = exb1;
    }

    public int getExb2() {
        return exb2;
    }

    public void setExb2(int exb2) {
        this.exb2 = exb2;
    }

    public int getExb3() {
        return exb3;
    }

    public void setExb3(int exb3) {
        this.exb3 = exb3;
    }

    public int getExb4() {
        return exb4;
    }

    public void setExb4(int exb4) {
        this.exb4 = exb4;
    }

    public int getExb5() {
        return exb5;
    }

    public void setExb5(int exb5) {
        this.exb5 = exb5;
    }
}
