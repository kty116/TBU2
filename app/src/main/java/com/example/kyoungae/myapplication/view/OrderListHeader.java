package com.example.kyoungae.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.event.OrderListDataBringCheckEvent;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kyoungae on 2017-09-04.
 */

public class OrderListHeader extends RelativeLayout implements View.OnClickListener {

//    @OnClick({R.id.progress_button})
//    void click(View v) {
//        switch (v.getId()) {
//            case R.id.progress_button:
//                EventBus.getDefault().post(new AddDataEvent());
//                //서버랑 통신후 결제 완료 페이지든 토스트든 ㄱ
//                break;
//        }
//    }

    public OrderListHeader(Context context) {
        super(context);
        init(context);
    }

    public OrderListHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderListHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        View view = inflate(context, R.layout.view_list_order_header, this);
        view.findViewById(R.id.all_check_button).setOnClickListener(this);
        view.findViewById(R.id.all_no_check_button).setOnClickListener(this);
//        ButterKnife.bind(view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_check_button:
                EventBus.getDefault().post(new OrderListDataBringCheckEvent(true));
                break;
            case R.id.all_no_check_button:
                EventBus.getDefault().post(new OrderListDataBringCheckEvent(false));
                break;
        }


//        다음 페이지로 이동



        //헤더의 입력된 데이터들 confirmButton 버튼 눌렀을때 서버랑 통신
//        EventBus.getDefault().post(new AddDataEvent());
//        Toast.makeText(v.getContext(), "서버랑 통신", Toast.LENGTH_SHORT).show();
//        getHttp()로;

    }

    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {

//        RequestParams params = new RequestParams();
//        params.put("key","value");

        TheBayRestClient.get(relativeUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리
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
            }
        });
    }
}
