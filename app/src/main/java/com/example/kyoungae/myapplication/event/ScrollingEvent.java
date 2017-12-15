package com.example.kyoungae.myapplication.event;

/**
 * Created by kyoungae on 2017-09-20.
 */

public class ScrollingEvent implements MessageEvent {
    private boolean isShown;

    public ScrollingEvent(boolean isShown) {
        this.isShown = isShown;
    }

    public boolean isShown() {
        return isShown;
    }
}
