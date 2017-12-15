package com.example.kyoungae.myapplication.shipping.model;

import android.text.TextUtils;

/**
 * Created by kyoungae on 2017-09-12.
 */

public class ShippingProductModel {
    private String shoppingMall; //쇼핑몰 url
    private String orderNumber;  //주문번호
    private String buyer;  //구매자
    private CategoryModel categoryItem;  //품목 카테고리
    private String productName;
    private String trackingNumber;  //트래킹번호
    private String price;  //금액
    private String quantity;  //수량
    private String color;  //색상
    private String size;  //사이즈
    private String goodsUrl;  //상품 url
    private String imageUrl;  //이미지 url
//    private boolean isNull;


    public ShippingProductModel(String orderNumber, String buyer, CategoryModel categoryItem, String productName, String trackingNumber, String price, String quantity, String color, String size, String goodsUrl, String imageUrl) {
        this.shoppingMall = "taobao.com";
        this.orderNumber = orderNumber;
        this.buyer = buyer;
        this.categoryItem = categoryItem;
        this.productName = productName;
        this.trackingNumber = trackingNumber;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
        this.goodsUrl = goodsUrl;
        this.imageUrl = imageUrl;
    }

    public String getShoppingMall() {
        return shoppingMall;
    }

    public void setShoppingMall(String shoppingMall) {
        this.shoppingMall = shoppingMall;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public CategoryModel getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(CategoryModel categoryItem) {
        this.categoryItem = categoryItem;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
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
        return "ShippingProductModel{" +
                "shoppingMall='" + shoppingMall + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", buyer='" + buyer + '\'' +
                ", categoryItem=" + categoryItem +
                ", productName='" + productName + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    /**
     * 상품 리스트 널 체크하는 메소드
     * param의 trackingNumberCheck는 트래킹번호를 널 체크에 포함 시킬건지 여부
     *
     * @param trackingNumberCheck
     * @return 빈 값의 항목 이름 or 빈 값이 없으면 "ok"
     */

    public String itemNullCheck(boolean trackingNumberChecked) {

//        String[] key = {shoppingMall,buyer,categoryItem.getId(),productName}
        //널 값이 있는 목록으로 뷰로 포지션 이동
//        shoppingMall buyer categoryItem productName trackingNumber price quantity goodsUrl
        if (TextUtils.isEmpty(shoppingMall)) {
            return "쇼핑몰";
        } else if (TextUtils.isEmpty(buyer)) {
            return "구매자 이름";
        } else if (TextUtils.isEmpty(categoryItem.getId())) {
            return "품목";
        } else if (TextUtils.isEmpty(productName)) {
            return "상품명";
        } else if (trackingNumberChecked) {  // 트래킹번호체크하기가 트루면 체크
            if (TextUtils.isEmpty(trackingNumber)) {
                return "트래킹번호";
            }
        } else if (TextUtils.isEmpty(price)) {
            return "가격";
        } else if (TextUtils.isEmpty(quantity)) {
            return "수량";
        } else if (TextUtils.isEmpty(goodsUrl)) {
            return "상품 url";
        } else {
            return "ok";
        }
        return null;
    }
}
