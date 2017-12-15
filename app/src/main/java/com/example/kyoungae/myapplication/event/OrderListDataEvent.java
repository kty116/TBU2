package com.example.kyoungae.myapplication.event;

import com.example.kyoungae.myapplication.shipping.model.ShippingProductModel;

import java.util.ArrayList;

/**
 * Created by kyoungae on 2017-11-07.
 */

public class OrderListDataEvent implements MessageEvent{
    private ArrayList<ShippingProductModel> list;

    public OrderListDataEvent(ArrayList<ShippingProductModel> list) {
        this.list = list;
    }

    public ArrayList<ShippingProductModel> getList() {
        return list;
    }

    public void setList(ArrayList<ShippingProductModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OrderListDataEvent{" +
                "list=" + list +
                '}';
    }
}
