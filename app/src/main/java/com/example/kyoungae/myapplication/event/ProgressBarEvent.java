package com.example.kyoungae.myapplication.event;

/**
 * Created by kyoungae on 2017-10-19.
 */

public class ProgressBarEvent implements MessageEvent {
    private boolean isShow;

    public ProgressBarEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
