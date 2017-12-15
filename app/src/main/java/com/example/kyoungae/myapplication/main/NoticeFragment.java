package com.example.kyoungae.myapplication.main;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.NoticeTextBinding;

public class NoticeFragment extends Fragment {

    private NoticeTextBinding binding;
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "date";

    private String mTitle;
    private String mDate;

    public static NoticeFragment newInstance(String title, String date) {
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mDate = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_text, container, false);
        binding = DataBindingUtil.bind(view);
        binding.title.setText(mTitle);
        binding.date.setText(mDate);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
