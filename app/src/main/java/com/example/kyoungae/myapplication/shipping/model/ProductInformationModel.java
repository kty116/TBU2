package com.example.kyoungae.myapplication.shipping.model;

/**
 * Created by kyoungae on 2017-10-20.
 */

public class ProductInformationModel {
    private String ArcSeq;
    private String KrArc;
    private String EnArc;
    private String CnArc;
    private String HsCode;
    private String CtmYn;

    public ProductInformationModel(String arcSeq, String krArc, String enArc, String cnArc, String hsCode, String ctmYn) {
        ArcSeq = arcSeq;
        KrArc = krArc;
        EnArc = enArc;
        CnArc = cnArc;
        HsCode = hsCode;
        CtmYn = ctmYn;
    }

    public String getArcSeq() {
        return ArcSeq;
    }

    public void setArcSeq(String arcSeq) {
        ArcSeq = arcSeq;
    }

    public String getKrArc() {
        return KrArc;
    }

    public void setKrArc(String krArc) {
        KrArc = krArc;
    }

    public String getEnArc() {
        return EnArc;
    }

    public void setEnArc(String enArc) {
        EnArc = enArc;
    }

    public String getCnArc() {
        return CnArc;
    }

    public void setCnArc(String cnArc) {
        CnArc = cnArc;
    }

    public String getHsCode() {
        return HsCode;
    }

    public void setHsCode(String hsCode) {
        HsCode = hsCode;
    }

    public String getCtmYn() {
        return CtmYn;
    }

    public void setCtmYn(String ctmYn) {
        CtmYn = ctmYn;
    }

    @Override
    public String toString() {
        return "ProductInformationModel{" +
                "ArcSeq='" + ArcSeq + '\'' +
                ", KrArc='" + KrArc + '\'' +
                ", EnArc='" + EnArc + '\'' +
                ", CnArc='" + CnArc + '\'' +
                ", HsCode='" + HsCode + '\'' +
                ", CtmYn='" + CtmYn + '\'' +
                '}';
    }
}
