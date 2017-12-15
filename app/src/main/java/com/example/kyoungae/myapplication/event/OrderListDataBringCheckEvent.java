package com.example.kyoungae.myapplication.event;

/**
 * Created by kyoungae on 2017-11-08.
 */

public class OrderListDataBringCheckEvent implements MessageEvent{
    private boolean isAllChecked;

    public OrderListDataBringCheckEvent(boolean isAllChecked) {
        this.isAllChecked = isAllChecked;
    }

    public boolean isAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(boolean allChecked) {
        isAllChecked = allChecked;
    }
}
