package com.example.kyoungae.myapplication.shipping;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.FragmentRecieverInformationBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.event.GetSaveAddressEvent;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.RecieverInformationSelectAddressEvent;
import com.example.kyoungae.myapplication.event.ShippingNextButtonEvent;
import com.example.kyoungae.myapplication.event.SignUpSelectedAddressEvent;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.shipping.dialog.GetAddressDialogFragment;
import com.example.kyoungae.myapplication.shipping.model.AddressModel;
import com.example.kyoungae.myapplication.webview.WebviewDialogFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RecieverInformationFragment extends Fragment implements View.OnClickListener {

    private FragmentRecieverInformationBinding binding;
    private static AddressModel mAddressModel;
    private ArrayList<AddressModel> mGetAddressModels;

    //스크롤 방향 체크 변수
    public static RecieverInformationFragment newInstance(AddressModel addressModel) {
        RecieverInformationFragment fragment = new RecieverInformationFragment();
        mAddressModel = addressModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reciever_information, container, false);
        binding = DataBindingUtil.bind(view);

        binding.postSearchButton.setOnClickListener(this);
        binding.nextButton.setOnClickListener(this);
        binding.getOrderListButton.setOnClickListener(this);
//        binding.enTransButton.setOnClickListener(this);

        if (mAddressModel != null) {
            binding.koNameEdit.setText(mAddressModel.getKoName());
            binding.enNameEdit.setText(mAddressModel.getEnName());
            binding.customsClearanceEdit.setText(mAddressModel.getCcNumber());
            if (mAddressModel.getCcCode().equals("1")) {
                binding.personCheck.setChecked(true);
            } else {
                binding.businessCheck.setChecked(true);
            }
            binding.phoneEdit.setText(mAddressModel.getPhoneNumber());
            binding.postEdit.setText(mAddressModel.getPost());
            binding.addressEdit.setText(mAddressModel.getKoAddress());
            binding.detailAddressEdit.setText(mAddressModel.getKoDetailAddress());
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());

        if (keyInfo != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        // TODO: 2017-10-19 page name 수정
        params.put("PageNm", "배송대행 신청> 주소록 가져오기");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            getHttp("Acting/DlvrAddr_S.php", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
//                EventBus.getDefault().post(new ProgressBarEvent(true));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onHomeSuccess: ", response.toString());

                // TODO: 2017-10-19 데이터 받은거 수정

                mGetAddressModels = new ArrayList<>();

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {
                        JSONArray array = response.getJSONArray("MemAddr");
                        Log.d("onHomeSuccess: ", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            mGetAddressModels.add(new AddressModel(object.getString("AddrSeq"), object.getString("AdrsKr"),
                                    object.getString("AdrsEn"), object.getString("Zip"), object.getString("Addr1"),
                                    object.getString("Addr2"), object.getString("Addr1En"), object.getString("Addr2En")
                                    , object.getString("MobNo"), object.getString("RrnNo"), object.getString("RrnCd"),
                                    object.getString("MainYn")));
//                            Log.d("onHomeSuccess: ",homeModelList.toString());
//                            binding.addressText.setText(getAddressModels.toString());
                        }
//                        binding.parentLayout.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(getContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
//                EventBus.getDefault().post(new ProgressBarEvent(false));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(getContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        // TODO: 2017-10-19 프래그먼트 툴바 아이콘 부분 수정
////        inflater.inflate(R.menu.menu_home, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.action_home:
////                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, MainFragment.newInstance()).commit();
////                return true;
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
        if (event instanceof SignUpSelectedAddressEvent) {
            SignUpSelectedAddressEvent signUpSelectedAddressEvent = (SignUpSelectedAddressEvent) event;
            binding.postEdit.setText(signUpSelectedAddressEvent.getPost());
            binding.addressEdit.setText(signUpSelectedAddressEvent.getKoAddress());
        } else if (event instanceof GetSaveAddressEvent) {
            GetSaveAddressEvent getSaveAddressEvent = (GetSaveAddressEvent) event;
            // TODO: 2017-11-07 이벤트 변수값에 넣어서 setText해주기

        }else if (event instanceof RecieverInformationSelectAddressEvent) {
            RecieverInformationSelectAddressEvent recieverInformationSelectAddressEvent = (RecieverInformationSelectAddressEvent) event;
             AddressModel selectedAddressModel = recieverInformationSelectAddressEvent.getAddressModel();
            binding.koNameEdit.setText(selectedAddressModel.getKoName());
            binding.enNameEdit.setText(selectedAddressModel.getEnName());
            binding.customsClearanceEdit.setText(selectedAddressModel.getCcNumber());
            if (selectedAddressModel.getCcCode().equals("1")) {
                binding.personCheck.setChecked(true);
            } else {
                binding.businessCheck.setChecked(true);
            }
            binding.phoneEdit.setText(selectedAddressModel.getPhoneNumber());
            binding.postEdit.setText(selectedAddressModel.getPost());
            binding.addressEdit.setText(selectedAddressModel.getKoAddress());
            binding.detailAddressEdit.setText(selectedAddressModel.getKoDetailAddress());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_search_button:
                if(mGetAddressModels.get(0)!=null) {
                    FragmentManager fm1 = getActivity().getSupportFragmentManager();
                    WebviewDialogFragment webviewDialogFragment = new WebviewDialogFragment();
                    webviewDialogFragment.show(fm1, "webviewDialogFragment");
                }else {
                    Toast.makeText(getActivity(), "주소록이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.get_order_list_button:
                FragmentManager fm2 = getActivity().getSupportFragmentManager();
                GetAddressDialogFragment getAddressDialogFragment = new GetAddressDialogFragment();
                getAddressDialogFragment.show(fm2, "getAddressDialogFragment");
                break;
            case R.id.next_button:
                EventBus.getDefault().post(new ShippingNextButtonEvent());
                break;
//            case R.id.en_trans_button:
//                RequestParams params = new RequestParams();
//                if (TextUtils.isEmpty(binding.koNameEdit.getText().toString())) {
//                    params.put("test", binding.koNameEdit.getText().toString());
//                    params.put("PageNm", "배송대행신청");
//
//                    try {
//                        getHttp("Lib/HangulPronunciationToEnglish.php?", params);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
        }
    }

//    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {
//
//        TheBayRestClient.get(relativeUrl, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // 받아온 JSONObject 자료 처리
//
//                Log.d("onAcceptSuccess: ", response.toString());
//
//                try {
//                    String enText = response.getString("EnglishText");
//                    binding.enNameEdit.setText(enText.toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                // 통신 실패시 호출 되는 메소드
//                Toast.makeText(getActivity(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("onFailure: ", throwable.toString());
//                Log.d("onFailure: ", responseString.toString());
//            }
//        });
//    }
}
