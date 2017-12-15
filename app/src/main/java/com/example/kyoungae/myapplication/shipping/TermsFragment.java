package com.example.kyoungae.myapplication.shipping;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.FragmentShippingTermsBinding;

import java.io.Serializable;

public class TermsFragment extends Fragment implements Serializable, View.OnClickListener {

    private FragmentShippingTermsBinding binding;

    public static TermsFragment newInstance() {
        TermsFragment fragment = new TermsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_terms, container, false);
        binding = DataBindingUtil.bind(view);
        getActivity().setTitle("이용 약관");

        setView();

        return view;
    }

    public void setView() {
        binding.cancelButton.setOnClickListener(this);
        binding.confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
        switch (v.getId()) {
            case R.id.confirm_button:
                startActivity(new Intent(getActivity(), ShippingActivity.class));
                break;
        }

    }
}
