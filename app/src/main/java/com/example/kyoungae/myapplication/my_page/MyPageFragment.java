package com.example.kyoungae.myapplication.my_page;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.FragmentMyPageBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.webview.DefaultWebViewFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class MyPageFragment extends Fragment implements Serializable, View.OnClickListener {

    private FragmentMyPageBinding binding;

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        binding = DataBindingUtil.bind(view);
        setMyPageData();
        getActivity().setTitle("마이페이지");
        setView();

        return view;
    }

    public void setView() {

        binding.addressManagementButton.setOnClickListener(this);
        binding.shippingRatesButton.setOnClickListener(this);

        binding.payCountButton.setOnClickListener(this);
        binding.depositButton1.setOnClickListener(this);
        binding.messageButton1.setOnClickListener(this);

        binding.payHistoryButton.setOnClickListener(this);
        binding.orderHistoryButton.setOnClickListener(this);
        binding.depositButton2.setOnClickListener(this);
        binding.pointButton.setOnClickListener(this);
        binding.couponButton.setOnClickListener(this);
        binding.inquiryButton.setOnClickListener(this);
        binding.messageButton2.setOnClickListener(this);
        binding.noticeButton.setOnClickListener(this);
        binding.changeInfoButton.setOnClickListener(this);
        binding.logoutButton.setOnClickListener(this);
        binding.settingButton.setOnClickListener(this);
        binding.termsButton.setOnClickListener(this);
        binding.infoCollectionButton.setOnClickListener(this);
        binding.updateInfoButton.setOnClickListener(this);

    }

    public void setMyPageData() {

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());

        if (keyInfo.getAuthKey() != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        Log.d("onSu: ",keyInfo.getAuthKey());
        params.put("PageNm", "마이페이지");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            getMyPageHttp("MyHome/MyHome_L.php", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.address_management_button:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("address_management").replace(R.id.fragment_layout, DefaultWebViewFragment.newInstance("주소록 관리","url")).commit();
                break;
            case R.id.shipping_rates_button:

                break;
            case R.id.pay_count_button:

                break;
            case R.id.deposit_button1:

                break;
            case R.id.message_button1:

                break;
            case R.id.pay_history_button:

                break;
            case R.id.order_history_button:

                break;
            case R.id.deposit_button2:

                break;
            case R.id.point_button:

                break;
            case R.id.coupon_button:

                break;
            case R.id.inquiry_button:

                break;
            case R.id.message_button2:

                break;
            case R.id.notice_button:

                break;
            case R.id.change_info_button:

                break;
            case R.id.logout_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = mPref.edit();
//                                editor.remove(CommonLib.id);
                                editor.remove(CommonLib.password);
                                editor.remove(CommonLib.authKey);
                                editor.remove(CommonLib.memCode);
                                editor.commit();
                                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //스택 다 없애고 새로운 스택 만듬
                                getActivity().startActivity(intent);
                            }
                        }).setNegativeButton("취소",null);
                builder.create().show();

                break;
            case R.id.setting_button:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("setting").replace(R.id.fragment_layout, AppSettingFragment.newInstance()).commit();
                break;
            case R.id.terms_button:

                break;
            case R.id.info_collection_button:

                break;
            case R.id.update_info_button:

                break;
        }
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_home, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_setting:
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        if (event instanceof ProgressBarEvent) {
//            ProgressBarEvent progressBarEvent = (ProgressBarEvent) event;
//            boolean progressbarShown = progressBarEvent.isShow();
//            if (progressbarShown) {
////                binding.progressBar.setVisibility(View.VISIBLE);
//            } else {
////                binding.progressBar.setVisibility(View.GONE);
//            }
//        }else if (event instanceof PurchaseSearchEvent){
//            PurchaseSearchEvent purchaseSearchEvent = (PurchaseSearchEvent) event;
//            try {
//                getHttp("Taobao/Taobao_L.php",purchaseSearchEvent.getParams());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
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

    public void getMyPageHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
//                EventBus.getDefault().post(new ProgressBarEvent(true));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onMainSuccess: ", response.toString());

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {
                        JSONArray array = response.getJSONArray("MemInf");
                        JSONObject object = array.getJSONObject(0);

                        binding.userNameText.setText(object.getString("MemKrNm"));
                        binding.userGradeText.setText(object.getString("MemLvlNm"));
                        binding.userMailboxText.setText("사서함 "+object.getString("MyPost"));
                        binding.depositText.setText(object.getString("MyDpst"));
                        binding.payCountText.setText(object.getString("PaymentCnt"));
                        binding.messageText.setText(object.getString("NtCnt"));

                        binding.parentLayout.setVisibility(View.VISIBLE);
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
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // 받아온 JSONArray 자료 처리
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(getContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

}
