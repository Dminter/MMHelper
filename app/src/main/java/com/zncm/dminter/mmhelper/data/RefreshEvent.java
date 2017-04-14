package com.zncm.dminter.mmhelper.data;

/**
 * Created by MX on 2014/8/21.
 * 刷新配合eventbus
 */
public class RefreshEvent {

    public int type;

    public RefreshEvent() {
    }

    public RefreshEvent(int type) {
        this.type = type;
    }

}
