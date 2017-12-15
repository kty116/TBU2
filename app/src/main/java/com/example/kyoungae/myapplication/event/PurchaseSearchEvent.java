package com.example.kyoungae.myapplication.event;

import com.loopj.android.http.RequestParams;

/**
 * Created by kyoungae on 2017-10-29.
 */

public class PurchaseSearchEvent implements MessageEvent {

    private RequestParams params;

    public PurchaseSearchEvent(RequestParams params) {
        this.params = params;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "PurchaseSearchEvent{" +
                "params=" + params +
                '}';
    }
}
