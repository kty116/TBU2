package com.example.kyoungae.myapplication.dto;

/**
 * Created by kyoungae on 2017-10-10.
 */

public class MemberDTO {
    private String id;
    private String password;

    public MemberDTO(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
