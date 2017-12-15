package com.example.kyoungae.myapplication.shipping.model;

/**
 * Created by kyoungae on 2017-10-20.
 */

public class AddressModel {
    private String id;
    private String koName;
    private String enName;
    private String post;
    private String koAddress;
    private String koDetailAddress;
    private String enAddress;
    private String enDetailAddress;
    private String phoneNumber;
    private String ccNumber;
    private String ccCode;
    private String MainUse;

    public AddressModel(String id, String koName, String enName, String post, String koAddress, String koDetailAddress, String enAddress, String enDetailAddress, String phoneNumber, String ccNumber, String ccCode, String mainUse) {
        this.id = id;
        this.koName = koName;
        this.enName = enName;
        this.post = post;
        this.koAddress = koAddress;
        this.koDetailAddress = koDetailAddress;
        this.enAddress = enAddress;
        this.enDetailAddress = enDetailAddress;
        this.phoneNumber = phoneNumber;
        this.ccNumber = ccNumber;
        this.ccCode = ccCode;
        MainUse = mainUse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKoName() {
        return koName;
    }

    public void setKoName(String koName) {
        this.koName = koName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getKoAddress() {
        return koAddress;
    }

    public void setKoAddress(String koAddress) {
        this.koAddress = koAddress;
    }

    public String getKoDetailAddress() {
        return koDetailAddress;
    }

    public void setKoDetailAddress(String koDetailAddress) {
        this.koDetailAddress = koDetailAddress;
    }

    public String getEnAddress() {
        return enAddress;
    }

    public void setEnAddress(String enAddress) {
        this.enAddress = enAddress;
    }

    public String getEnDetailAddress() {
        return enDetailAddress;
    }

    public void setEnDetailAddress(String enDetailAddress) {
        this.enDetailAddress = enDetailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcCode() {
        return ccCode;
    }

    public void setCcCode(String ccCode) {
        this.ccCode = ccCode;
    }

    public String getMainUse() {
        return MainUse;
    }

    public void setMainUse(String mainUse) {
        MainUse = mainUse;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "id='" + id + '\'' +
                ", koName='" + koName + '\'' +
                ", enName='" + enName + '\'' +
                ", post='" + post + '\'' +
                ", koAddress='" + koAddress + '\'' +
                ", koDetailAddress='" + koDetailAddress + '\'' +
                ", enAddress='" + enAddress + '\'' +
                ", enDetailAddress='" + enDetailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", ccNumber='" + ccNumber + '\'' +
                ", ccCode='" + ccCode + '\'' +
                ", MainUse='" + MainUse + '\'' +
                '}';
    }
}
