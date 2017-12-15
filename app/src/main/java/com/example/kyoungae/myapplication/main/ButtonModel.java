package com.example.kyoungae.myapplication.main;

/**
 * Created by kyoungae on 2017-10-26.
 */

public class ButtonModel {
    private String ImageUrl;
    private String pageName;

    public ButtonModel(String imageUrl, String pageName) {
        ImageUrl = imageUrl;
        this.pageName = pageName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    @Override
    public String toString() {
        return "ButtonModel{" +
                "ImageUrl='" + ImageUrl + '\'' +
                ", pageName='" + pageName + '\'' +
                '}';
    }
}
