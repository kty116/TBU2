package com.example.kyoungae.myapplication.main;

/**
 * Created by kyoungae on 2017-10-19.
 */

public class noticeModel {
    private String BbCode;
    private String BbsSeq;
    private String Ct;
    private String Tit;
    private String InsDate;

    public noticeModel(String bbCode, String bbsSeq, String ct, String tit, String insDate) {
        BbCode = bbCode;
        BbsSeq = bbsSeq;
        Ct = ct;
        Tit = tit;
        InsDate = insDate;
    }

    public String getBbCode() {
        return BbCode;
    }

    public void setBbCode(String bbCode) {
        BbCode = bbCode;
    }

    public String getBbsSeq() {
        return BbsSeq;
    }

    public void setBbsSeq(String bbsSeq) {
        BbsSeq = bbsSeq;
    }

    public String getCt() {
        return Ct;
    }

    public void setCt(String ct) {
        Ct = ct;
    }

    public String getTit() {

        return Tit;
    }

    public void setTit(String tit) {
        Tit = tit;
    }

    public String getInsDate() {

        String year = InsDate.substring(2,4);
        String month = InsDate.substring(4,6);
        String day = InsDate.substring(6,8);
        return year+"."+month+"."+day;
    }

    public void setInsDate(String insDate) {
        InsDate = insDate;
    }


}
