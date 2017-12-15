package com.example.kyoungae.myapplication.event;

/**
 * Created by kyoungae on 2017-10-27.
 */

public class SignUpSelectedAddressEvent implements MessageEvent {
    private String post;
    private String koAddress;
    private String enAddress;

    public SignUpSelectedAddressEvent(String post, String koAddress, String enAddress) {
        this.post = post;
        this.koAddress = koAddress;
        this.enAddress = enAddress;
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

    public String getEnAddress() {
        return enAddress;
    }

    public void setEnAddress(String enAddress) {
        this.enAddress = enAddress;
    }

    @Override
    public String toString() {
        return "SignUpSelectedAddressEvent{" +
                "post='" + post + '\'' +
                ", koAddress='" + koAddress + '\'' +
                ", enAddress='" + enAddress + '\'' +
                '}';
    }
}
