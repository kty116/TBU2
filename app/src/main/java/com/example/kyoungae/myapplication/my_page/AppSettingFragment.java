package com.example.kyoungae.myapplication.my_page;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.FragmentAppSettingBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class AppSettingFragment extends Fragment implements Serializable, View.OnClickListener {

    private FragmentAppSettingBinding binding;

    public static AppSettingFragment newInstance() {
        AppSettingFragment fragment = new AppSettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_setting, container, false);
        binding = DataBindingUtil.bind(view);
        getActivity().setTitle("앱설정");

        setView();

        return view;
    }

    public void setView() {

    }

    public void setMyPageData() {

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());

        if (keyInfo.getAuthKey() != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        params.put("PageNm", "앱설정");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            // TODO: 2017-11-01 앱설정 api 연결 하기
            setAppSettingHttp("MyHome/MyHome_L.php", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

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

    public void setAppSettingHttp(String relativeUrl, RequestParams params) throws JSONException {

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

//                        binding.userNameText.setText(object.getString("MemKrNm"));
//                        binding.userGradeText.setText(object.getString("MemLvlNm"));
//                        binding.userMailboxText.setText("사서함 "+object.getString("MyPost"));
//                        binding.depositText.setText(object.getString("MyDpst"));
//                        binding.payCountText.setText(object.getString("PaymentCnt"));
//                        binding.messageText.setText(object.getString("NtCnt"));

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
