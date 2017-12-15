package com.example.kyoungae.myapplication.shipping;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.FragmentShippingCenterBinding;
import com.example.kyoungae.myapplication.databinding.ListItemHomeBinding;
import com.example.kyoungae.myapplication.event.AddDataEvent;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.ScrollButtonClickEvent;
import com.example.kyoungae.myapplication.event.ShippingNextButtonEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShippingCenterFragment extends Fragment implements View.OnClickListener {

    private FragmentShippingCenterBinding binding;
    private static String mCenterName;
    private static String mAddress;

    //스크롤 방향 체크 변수
    public static ShippingCenterFragment newInstance(String centerName, String address) {
        ShippingCenterFragment fragment = new ShippingCenterFragment();
        mCenterName = centerName;
        mAddress = address;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_center, container, false);
        binding = DataBindingUtil.bind(view);

        binding.centerSelectButton.setText(mCenterName);
        binding.addressText.setText(mAddress);
        binding.nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        RequestParams params = new RequestParams();
//
////        String enId = Security.encrypt(id,"EJQPDL@)!&!))DJR").toString();
////        String enPw = Security.encrypt(password,"EJQPDL@)!&!))DJR").toString();
////        String enTp = Security.encrypt("U","EJQPDL@)!&!))DJR").toString();
//        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());
//
//        if (keyInfo != null) {
//            params.put("AuthKey", keyInfo.getAuthKey());
//            params.put("MemCode", keyInfo.getMemberCode());
//        }
//        // TODO: 2017-10-19 page name 수정
//        params.put("PageNm", "배송대행 신청 > 약관동의");
//        params.put("AppVer", Build.MODEL);
//        params.put("ModelNo", Build.VERSION.RELEASE);
//
//        try {
//            getHttp("Acting/ReqAgree_S.php", params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        // TODO: 2017-10-19 프래그먼트 툴바 아이콘 부분 수정
//        inflater.inflate(R.menu.menu_home, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_setting:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, MainFragment.newInstance()).commit();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event instanceof AddDataEvent) {
            //서버 통신시 프로그래스바

            //서버 통신하고 값이 20개이상이면 버튼 보이게
            //20개 미만이면 버튼 안보이게
//            for (int i = 0; i < 10; i++) {
//                mDataList.add(new ScanInStockModel("" + i, "1", "1", "1", "1", "1"));
//            }
//            mDataAdapter.setData(mDataList);
//            RecyclerViewUtils.removeFooterView(mRecyclerView);
        } else if (event instanceof ScrollButtonClickEvent) {
//            binding.recyclerView.smoothScrollToPosition(0);
//        } else if (event instanceof SearchEvent) {
//            SearchEvent searchEvent = (SearchEvent) event;
//            String searchData = searchEvent.getSearchData();  //서버로 값 넘기기
//        } else if (event instanceof ChangeLanguageEvent) {
//            ChangeLanguageEvent changeLanguageEvent = (ChangeLanguageEvent) event;
//            if (changeLanguageEvent.isKorean()) {
//                //프리퍼런스 랭귀지값이 한국어 일때
//                getActivity().setTitle("입고스캔");
//            } else {
//                getActivity().setTitle("快递单号记录");
//            }
//
//            mDataAdapter = new ListAdapter(getActivity(), mDataList);
//            HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
//            mRecyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            //add a HeaderView
//            RecyclerViewUtils.setHeaderView(mRecyclerView, new TrackingSearchHeader(getContext()));
//        } else if (event instanceof ListHeaderFocusEvent) {
//            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                EventBus.getDefault().post(new ShippingNextButtonEvent());
                break;
        }
    }
}
