package com.example.kyoungae.myapplication.purchase;

/**
 * Created by kyoungae on 2017-10-29.
 */

public class PurchaseModel {

    private String id;
    private String detailUrl;
    private String title;
    private String imageUrl;
    private String price;
    private String finalPrice;

    public PurchaseModel(String id, String detailUrl, String title, String imageUrl, String price, String finalPrice) {
        this.id = id;
        this.detailUrl = detailUrl;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.finalPrice = finalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "PurchaseModel{" +
                "id='" + id + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price='" + price + '\'' +
                ", finalPrice='" + finalPrice + '\'' +
                '}';
    }
}
