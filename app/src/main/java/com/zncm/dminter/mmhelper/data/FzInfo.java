package com.zncm.dminter.mmhelper.data;

/**
 * Created by dminter on 2016/7/27.
 * 分组
 */

public class FzInfo {
    private int id;//id
    private String name;//名称


    public FzInfo() {


    }

    public FzInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
