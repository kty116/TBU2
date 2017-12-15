package com.example.kyoungae.myapplication.purchase;

/**
 * Created by kyoungae on 2017-10-30.
 */

public class RecommendWordModel {
    private String koreanWord;
    private String chineseWord;

    public RecommendWordModel(String chineseWord, String koreanWord) {
        this.koreanWord = koreanWord;
        this.chineseWord = chineseWord;
    }

    public String getKoreanWord() {
        return koreanWord;
    }

    public void setKoreanWord(String koreanWord) {
        this.koreanWord = koreanWord;
    }

    public String getChineseWord() {
        return chineseWord;
    }

    public void setChineseWord(String chineseWord) {
        this.chineseWord = chineseWord;
    }

    @Override
    public String toString() {
        return "RecommendWordModel{" +
                "koreanWord='" + koreanWord + '\'' +
                ", chineseWord='" + chineseWord + '\'' +
                '}';
    }
}
