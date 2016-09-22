package com.zncm.dminter.mmhelper.data;

/**
 * Created by dminter on 2016/7/27.
 */

public class FzInfo {
    private int id;
    private String name;


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
