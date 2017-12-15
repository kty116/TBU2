package com.example.kyoungae.myapplication.dto;

/**
 * Created by kyoungae on 2017-10-17.
 */

public class KeyDTO {
    private String memberCode;
    private String authKey;

    public KeyDTO(String memberCode, String authKey) {
        this.memberCode = memberCode;
        this.authKey = authKey;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

}
