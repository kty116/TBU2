package com.example.kyoungae.myapplication.shipping.model;

/**
 * Created by kyoungae on 2017-11-07.
 */

public class CategoryModel {
    private String id;
    private String ko_name;
    private String en_name;

    public CategoryModel() {
        this.id = "";
        this.ko_name = "";
        this.en_name = "";
    }

    public CategoryModel(String id, String ko_name, String en_name) {
        this.id = id;
        this.ko_name = ko_name;
        this.en_name = en_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKo_name() {
        return ko_name;
    }

    public void setKo_name(String ko_name) {
        this.ko_name = ko_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id='" + id + '\'' +
                ", ko_name='" + ko_name + '\'' +
                ", en_name='" + en_name + '\'' +
                '}';
    }
}
