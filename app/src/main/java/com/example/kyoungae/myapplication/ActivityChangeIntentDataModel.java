package com.example.kyoungae.myapplication;

import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by kyoungae on 2017-08-29.
 */
public class ActivityChangeIntentDataModel implements Serializable {
    private Fragment fragment;

    public ActivityChangeIntentDataModel(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return "ActivityChangeIntentDataModel{" +
                "fragment=" + fragment +
                '}';
    }
}
