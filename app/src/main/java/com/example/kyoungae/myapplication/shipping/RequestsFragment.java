package com.example.kyoungae.myapplication.shipping;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.FragmentRequestBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.loopj.android.http.RequestParams;


public class RequestsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private FragmentRequestBinding binding;

    //스크롤 방향 체크 변수
    public static RequestsFragment newInstance() {
        RequestsFragment fragment = new RequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        binding = DataBindingUtil.bind(view);
        binding.recieverCheck.setOnCheckedChangeListener(this);
        binding.buyerCheck.setOnCheckedChangeListener(this);
        binding.waitingButton.setOnClickListener(this);
        binding.completeButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RequestParams params = new RequestParams();

//        String enId = Security.encrypt(id,"EJQPDL@)!&!))DJR").toString();
//        String enPw = Security.encrypt(password,"EJQPDL@)!&!))DJR").toString();
//        String enTp = Security.encrypt("U","EJQPDL@)!&!))DJR").toString();
        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());

        if (keyInfo != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        // TODO: 2017-10-19 page name 수정
        params.put("PageNm", "배송대행 신청 > 등록");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

//        try {
//            getHttp("Acting/ReqSvc_S.php", params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

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
//        params.put("PageNm", "마이홈");
//        params.put("AppVer", Build.MODEL);
//        params.put("ModelNo", Build.VERSION.RELEASE);
//
//        try {
//            getHttp("MyHome/MyHome_L.php", params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        mDataList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            mDataList.add(new ScanInStockModel("예치금", "포인트", "결제구분", "미결제금액", "처리일자", "결제내역"));
//        }
//
//        mDataAdapter = new ListAdapter(getActivity(), mDataList);
////        mDataAdapter.setData(mDataList);
//
//        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
//        binding.recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
//
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        //add a HeaderView
////        RecyclerViewUtils.setHeaderView(binding.recyclerView, new TrackingSearchHeader(getContext()));
//
//        //add a FooterView
//        RecyclerViewUtils.setFooterView(binding.recyclerView, new ListFooter(getActivity()));
//        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.d("onScrollStateChanged: ", newState + "");
//
//
//                if (recyclerView.computeVerticalScrollOffset() > 2000) {
//                    Log.d("onScrollStateChanged: ", "호출");
//                    EventBus.getDefault().post(new ScrollingEvent(true));
//                } else {
//                    Log.d("onScrollStateChanged: ", "호출");
//                    EventBus.getDefault().post(new ScrollingEvent(false));
//                }
//            }
//        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // TODO: 2017-10-19 프래그먼트 툴바 아이콘 부분 수정
//        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_home:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, MainFragment.newInstance()).commit();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.buyer_check:
                if (isChecked) {
                    binding.recieverCheck.setChecked(false);
                }
                break;
            case R.id.reciever_check:
                if (isChecked) {
                    binding.buyerCheck.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waiting_button:

                break;
            case R.id.complete_button:
                for (int i = 0; i < ShippingActivity.sProductModelList.size(); i++) {
//                    ShippingActivity.sProductModelList.get(i).to
                }

                break;
        }
    }
}
