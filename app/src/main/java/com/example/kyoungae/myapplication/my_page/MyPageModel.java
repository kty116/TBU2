package com.example.kyoungae.myapplication.my_page;

/**
 * Created by kyoungae on 2017-10-29.
 */

public class MyPageModel {
    private String name;
    private String gradeNum;
    private String gradeName;
    private String post;
    private String deposit;
    private String paymentCount;
    private String paymentMoney;
    private String messageCount;

    public MyPageModel(String name, String gradeNum, String gradeName, String post, String deposit, String paymentCount, String paymentMoney, String messageCount) {
        this.name = name;
        this.gradeNum = gradeNum;
        this.gradeName = gradeName;
        this.post = post;
        this.deposit = deposit;
        this.paymentCount = paymentCount;
        this.paymentMoney = paymentMoney;
        this.messageCount = messageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(String gradeNum) {
        this.gradeNum = gradeNum;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(String paymentCount) {
        this.paymentCount = paymentCount;
    }

    public String getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(String paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String toString() {
        return "MyPageModel{" +
                "name='" + name + '\'' +
                ", gradeNum='" + gradeNum + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", post='" + post + '\'' +
                ", deposit='" + deposit + '\'' +
                ", paymentCount='" + paymentCount + '\'' +
                ", paymentMoney='" + paymentMoney + '\'' +
                ", messageCount='" + messageCount + '\'' +
                '}';
    }
}
