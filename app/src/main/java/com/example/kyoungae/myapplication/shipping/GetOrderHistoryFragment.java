package com.example.kyoungae.myapplication.shipping;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.DialogGetOrderHistoryBinding;
import com.example.kyoungae.myapplication.event.ProgressBarEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class GetOrderHistoryFragment extends Fragment implements Serializable {

    private DialogGetOrderHistoryBinding binding;
    private String mHtml;
    private String mPageNum;
    private boolean dd = true;
    private ArrayList<String> dataList;
    private String mTotalPage;


    public static GetOrderHistoryFragment newInstance() {
        GetOrderHistoryFragment fragment = new GetOrderHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.dialog_get_order_history, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        EventBus.getDefault().post(new ProgressBarEvent(true));
        dataList = new ArrayList<>();
        Log.d(TAG, "onActivity: "+1);

        WebSettings webSettings = binding.webView.getSettings();

//        webSettings.setSaveFormData(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); //자바스크립트를 사용하도록 설정
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setGeolocationEnabled(true);

//        if(Build.VERSION.SDK_INT >= 16){
//            webSettings.setAllowFileAccessFromFileURLs(true);
//            webSettings.setAllowUniversalAccessFromFileURLs(true);
//        }
//        if(Build.VERSION.SDK_INT >= 21){
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        binding.webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");


//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        mWebView.setScrollbarFadingEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                EventBus.getDefault().post(new ProgressBarEvent(true));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?")) {
//                if (url.contains("https://h5.m.taobao.com/mlapp/olist.html?tabCode=waitSend")) {
//                    st(url);
//                    Log.d(TAG, "onPageFinished: " + url.toString());
                    view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
//                    try {
//                        Thread.sleep(500);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (dd) {
//                        dd = false;
//                        binding.webView.loadUrl("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?pageNum=" + "2");
////                        binding.webView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html?tabCode=waitSend");
////                        st("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?pageNum=" + "2");
////                        st("https://h5.m.taobao.com/mlapp/olist.html?tabCode=waitSend");
//                    }

                }
            }
        });



//        binding.webView.loadUrl("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
        binding.webView.loadUrl("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?pageNum=1&tabCode=waitConfirm");
//        binding.webView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html?tabCode=waitSend");
//        binding.webView.loadUrl("https://member1.taobao.com/member/fresh/deliver_address.htm");

    }

    public class MyJavascriptInterface {

        private File mFolder;
        private String mFileName;

        @JavascriptInterface //킷캣 이상에선 어노테이션을 붙여줘야됨
        public void getHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
//            Elements newsHeadlines = doc.select(".order-cont");

//            Log.d(TAG, "getHtml webview: "+html);
//            System.out.println("getHtml"+html);
//            Log.d(TAG, "getHtml: "+html);

            String data = html.substring(html.indexOf("var data") + 23); //11은 var data = 를 제외하기 위한 자리수
//            Log.d(TAG, "getHtml33: "+data);
            String data2 = data.substring(0, data.indexOf("</script>"));
            String data3 = data2.substring(0, data2.lastIndexOf("'"));
            dataList.add(data3.replace("\\",""));

            String result = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(dataList.get(0));
//                Log.d(TAG, "getHtml: "+jsonObject);
//                ArrayList<String> strings = new ArrayList<>();
                JSONArray mainOrdersArray = jsonObject.getJSONArray("mainOrders");
                JSONObject page = jsonObject.getJSONObject("page");
                mTotalPage = page.getString("totalPage");
                Log.d(TAG, "getHtml: "+mTotalPage);
                Log.d(TAG, "getHtml: "+page);
                for (int i = 0; i < mainOrdersArray.length(); i++) {
                    JSONObject object = mainOrdersArray.getJSONObject(i);
//                    orderNum = object.getString("id");
                    Log.d(TAG, "getHtml: "+object.getString("id"));
//                    object
                    Log.d(TAG, "getHtml: "+object);

//                    JSONObject mainOrders = jsonObject.getJSONObject(i);
//                    JSONArray jsonArray1 = mainOrders.getJSONArray("subOrders");
                }

//                Log.d(TAG, "getHtml: "+jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }



//            JSONParser jsonParser = new JSONParser();
//
//
//            try {
//                Toast.makeText(getContext(), "ddddddd", Toast.LENGTH_SHORT).show();
//                JSONObject jsonObject = (JSONObject) jsonParser.parse(data3);
//                Log.d(TAG, "getHtml2: "+jsonObject);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

//            try {
//                JSONObject object = new JSONObject(data3);
//                Log.d(TAG, "getHtml: "+object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String d = "\"error\":\"\",\"extra\"";
//            Log.d(TAG, "getHtml: "+d.replace("\'",""));

//            String d = "var data = {\"error\":\"\",\"extra\":{\"asyncRequestUrl\":\"/trade/itemlist/asyncBought.htm?action=itemlist/BoughtQueryAction&event_submit_do_query=1&_input_charset=utf8\",\"carttaskServerPath\":\"//active.taobao.com\",\"followModulePath\":\"//follow.taobao.com\",\"hasPageList\":true,\"i18n\":\"zh-CN\",";
//            Log.d(TAG, "getHtml: "+d.replace("\'",""));

//            String d = " JSON.parse('{\\\"error\\\":\\\"\\\",\\\"extra\\\":{},\\\"mainOrders\\\":[{\\\"extra\\\":{\\\"batch\\\":true,\\\"batchConfirm\\\":true,\\\"batchGroup\\\":\\\"1\\\",\\\"batchGroupTips\\\":\\\"\\u9884\\u552E\\u5546\\u54C1\\u548C\\u666E\\u901A\\u5546\\u54C1\\u4E0D\\u652F\\u6301\\u5408\\u5E76\\u4ED8\\u6B3E\\\",\\\"batchMaxCount\\\":30,\\\"bizType\\\":200,\\\"currency\\\":\\\"CNY\\\",\\\"currencySymbol\\\":\\\"\\uFFE5\\\",\\\"id\\\":73347930392"

//            String data4 = dataList.get(0).toString().replace("\\\'","");
//            Log.d(TAG, "getHtml:"+data4);
//

//            JsonParser jsonParser = new JsonParser();
//            JsonObject jsonObject = (JsonObject)jsonParser.parse(dataList.get(0));
//
//            Log.d(TAG, "getHtml: "+jsonObject.toString());


//            String data = html.substring(html.indexOf("var data") + 11); //11은 var data = 를 제외하기 위한 자리수
//            String data2 = data.substring(0, data.indexOf("</script>"));


//            Log.d(TAG, "getHtml: " + dataList.get(0).toString());


//            Gson gson = new Gson();
//
//

//            Log.d("onCreate: ", jsonObject.toString());
//            String error = jsonObject.getString("error");
//            JSONObject jsonObject1 = new JSONObject(error);
//            Log.d("onCreate: ", jsonObject1.toString() + "dddddd");





//            String s = gson.toJson(dataList.get(0).toString());
//            Log.d(TAG, "getHtml: "+s);

//            String s = dataList.get(0).toString();
//            String ss = gson.toJson(s);
//            Log.d(TAG, "getHtml: "+s);
//            Log.d(TAG, "getHtml: "+ss);


//            Log.d(TAG, "getHtml: "+dataList.get(0).toString());
//            Data data4 = gson.fromJson(data3,Data.class);
//            Log.d(TAG, "getHtml: "+data4.toString());

//            JSONObject jsonObject = null;
//
//            try {
//                jsonObject = new JSONObject(dataList.get(0).toString());
//                Log.d(TAG, "getHtml: "+jsonObject.toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            JSONArray jsonArray = jsonObject.getJSONArray("mainOrders");


//
//            //로드 작업 다 끝나면 어레이 돌리면서
//
//            if (dd == false) {
//                for (int i = 0; i < dataList.size(); i++) {
//                    String orderData = dataList.get(i);
//                    System.out.println(orderData);
//                    Log.d(TAG, "getHtml: "+orderData);

//                    try {
//                        JSONObject obj = new JSONObject(orderData);
//                        Log.d(TAG, "getHtml: "+obj);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

//                    String ord = orderData.substring(1,orderData.lastIndexOf("]"));
//                    Log.d(TAG, "getHtml: "+orderData);

//                    JsonParser parser = new JsonParser();
//                    Object obj = parser.parse(orderData);

//                    Log.d(TAG, "getHtml: "+orderData.toString());
//                    JSONObject jsonObj = (JSONObject) orderData;
//                    Log.d(TAG, "getHtml: "+jsonObj);
//                }
            }



//            if (dd) {
//                binding.webView.loadUrl("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?pageNum=" + "2");
//                dd = false;
//            }

            //if(){ 처음 로드하고 페이지수 알고 페이지수 까지 로드 하기

//            int page = Integer.parseInt(mPageNum) + 1;
//            binding.webView.loadUrl("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?pageNum=" + page);
//        }
//        }

    }

//    public class DataModel{
//        private String dd;
//
//        @Override
//        public String toString() {
//            return dd
//        }
//    }


    public class Data {
        ArrayList<MainOrdersModel> mainOrders;
        Map<String, String> page;

        @Override
        public String toString() {
            return "Data{" +
                    "mainOrders=" +
                    "" + mainOrders +
                    '}';
        }
    }

    public class MainOrdersModel {
        ArrayList<String> subOrders;

        @Override
        public String toString() {
            return "MainOrdersModel{" +
                    "subOrders=" + subOrders +
                    '}';
        }
    }

//    public class OrderData {
//        ArrayList<String> orderData;
//
//        @Override
//        public String toString() {
//            return "OrderData{" +
//                    "orderData=" + orderData +
//                    '}';
//        }
//    }

//    public void saveBitmapToJpeg(String albumFileName, byte[] htmlBytes) {
//        //외부스토리지에 저장하면 앱이 지워져도 사진은 그대로
//
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOWNLOADS), albumFileName);
//
//        String dd = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
//
//        if (!file.isDirectory()) {
//            file.mkdirs();
//        }
//        FileOutputStream out = null;
////            FileInputStream in = null;
//        try {
////                in = new FileInputStream(mFolder + "/" + "타오바오 전체 스크립트");
//            out = new FileOutputStream(file + "/" + dd);
//
//            out.write(htmlBytes);
//            out.flush();
//            out.close();
//            //파일 저장 후 미디어 스캐닝
//            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                    Uri.parse("file://" + file + "/" + dd)));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    public void Threadd(){
//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }

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

//    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {
//
//        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onStart() {
//                super.onStart();
////                EventBus.getDefault().post(new ProgressBarEvent(true));
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // 받아온 JSONObject 자료 처리
//
//                Log.d("onHomeSuccess: ", response.toString());
//
//                // TODO: 2017-10-19 데이터 받은거 수정
//
//                ArrayList<GetAddressModel> homeModelList = new ArrayList<>();
//
//                try {
//                    String error = response.getString("RstNo");
//                    if (error.equals("0")) {
//                        JSONArray array = response.getJSONArray("MemAddr");
//                        Log.d("onHomeSuccess: ", array.toString());
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            homeModelList.add(new GetAddressModel(object.getString("AddrSeq"), object.getString("AdrsKr"),
//                                    object.getString("AdrsEn"), object.getString("Zip"), object.getString("Addr1"),
//                                    object.getString("Addr2"), object.getString("Addr1En"), object.getString("Addr2En")
//                                    , object.getString("MobNo"), object.getString("RrnNo"), object.getString("RrnCd"),
//                                    object.getString("MainYn")));
////                            binding.addressText.setText(homeModelList.toString());
//                        }
//                        binding.parentLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        Toast.makeText(getContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), LoginActivity.class));
//                        getActivity().finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//
////                Log.d("onHomeSuccess: ", response.toString());
//                // 받아온 JSONArray 자료 처리
//
////                try {
////                    JSONObject object = null;
////                    for (int i = 0; i < response.length(); i++) {
////                        object = response.getJSONObject(i);
////                    }
////                    String error = object.getString("RstNo");
////                    Log.d("onSuccess: ",response.toString());
////
////                    if (error.equals("0")) {
////                        Toast.makeText(getContext(), "성공", Toast.LENGTH_SHORT).show();
//////                        LoginActivity.mMemCode= object.getString("MemCode");
//////                        mAuthKey = object.getString("AuthKey");
//////                        getKeyInfo();
////
////                    }else {
////                        Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
////                    }
////                    Log.d("onSuccess: ", response.getString(0).toString());
////                } catch(JSONException e) {
////                }
//            }
//
//            @Override
//            public void onFinish() {
//                // 끝나면 호출 되는 메소드
////                EventBus.getDefault().post(new ProgressBarEvent(false));
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                // 통신 실패시 호출 되는 메소드
//                Toast.makeText(getContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("onFailure: ", throwable.toString());
//            }
//        });
//    }
}
