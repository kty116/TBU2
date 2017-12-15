package com.example.kyoungae.myapplication.shipping.model;

/**
 * Created by kyoungae on 2017-09-12.
 */

public class OrderListProductModel {
    private boolean isCheck;
    private String orderNumber;  //주문번호
    private String productName;  //상품명
    private String price;  //금액
    private String quantity;  //수량
    private String color;  //색상
    private String size;  //사이즈
    private String goodsUrl;  //상품 url
    private String imageUrl;  //이미지 url

    public OrderListProductModel(boolean isCheck, String orderNumber, String productName, String price, String quantity, String color, String size, String goodsUrl, String imageUrl) {
        this.isCheck = isCheck;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
        this.goodsUrl = goodsUrl;
        this.imageUrl = imageUrl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "OrderListProductModel{" +
                "isCheck=" + isCheck +
                ", orderNumber='" + orderNumber + '\'' +
                ", productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
