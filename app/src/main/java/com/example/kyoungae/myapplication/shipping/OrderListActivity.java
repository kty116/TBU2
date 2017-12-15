package com.example.kyoungae.myapplication.shipping;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.ActivityOrderListBinding;
import com.example.kyoungae.myapplication.databinding.ListItemOrderListProductBinding;
import com.example.kyoungae.myapplication.event.DataloadEvent;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.OrderListDataBringCheckEvent;
import com.example.kyoungae.myapplication.event.ProgressBarEvent;
import com.example.kyoungae.myapplication.lib.HeaderAndFooterRecyclerViewAdapter;
import com.example.kyoungae.myapplication.lib.RecyclerViewUtils;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.main.MainActivity;
import com.example.kyoungae.myapplication.shipping.model.CategoryModel;
import com.example.kyoungae.myapplication.shipping.model.OrderListProductModel;
import com.example.kyoungae.myapplication.shipping.model.ShippingProductModel;
import com.example.kyoungae.myapplication.view.OrderListHeader;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class OrderListActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityOrderListBinding binding;
    private ArrayList<String> mDataList;
    private int mTotalPage;
    private int mCurrentPage;  //각 버튼 누를 때 1로 값 설정됨
    private int mIndex;
    private boolean isButtonClicked = false;
    private String mUrl;
    private String TAG = "OrderListActivity";
    private ArrayList<OrderListProductModel> mProductList;
    private String mSize;
    private String mColor;
    private String mTragkingNumberUrl = "https://buyertrade.taobao.com/trade/json/transit_step.do?bizOrderId=";
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private String mMemberName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_list);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("오더내역 가져오기");

        if (getIntent().getStringExtra("member_name") != null) {
            mMemberName = getIntent().getStringExtra("member_name");
        }

        mProductList = new ArrayList<>();

        ListAdapter listAdapter = new ListAdapter(this, mProductList, Glide.with(this));
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(listAdapter);

        binding.recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(binding.recyclerView, new OrderListHeader(this));
//        binding.recyclerView.setVisibility(View.VISIBLE);
//        binding.workWebView.setVisibility(View.INVISIBLE);

//        mProductList.add(new OrderListProductModel(false, "29983143189516803", "【泰康在线】航空意外险（国际版）", "60.00", "1", "", "", "//baoxian.taobao.com/item.htm?id=532179138577", "https://img.alicdn.com/bao/uploaded/i2/2744696515/TB2EXyCppXXXXagXpXXXXXXXXXX_!!2744696515.jpg_sum.jpg"));

        binding.sendButton.setOnClickListener(this);
        binding.confirmButton.setOnClickListener(this);
        binding.getOrderHistoryButton.setOnClickListener(this);
        binding.progressLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        binding.workWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        WebSettings webSettings = binding.workWebView.getSettings();

        webSettings.setSaveFormData(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
//
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mDataList = new ArrayList<>();
        binding.workWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        binding.workWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                binding.progressLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mUrl = url;
                Log.d(TAG, "onPageFinished: "+url);

                if (isButtonClicked) {
                    if (url.contains("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?")) {
//                        EventBus.getDefault().post(new ProgressBarEvent(true));
//                        binding.progressLayout.setVisibility(View.VISIBLE);
                        view.loadUrl("javascript:window.Android.getTaobaoDataHtml(document.getElementsByTagName('body')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                        Log.d(TAG, "getHtml: " + url);

                    } else if (url.contains(mTragkingNumberUrl)) {
                        view.loadUrl("javascript:window.Android.getTrackingDataHtml(document.getElementsByTagName('body')[0].innerHTML);");
//                    if(mIndex <= mProductList.size()){
//                        ++mIndex;
//                        binding.workWebView.loadUrl(mTragkingNumberUrl + mProductList.get(mIndex).getOrderNumber());
//
//                    }
                    }
                } else {
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });

//        binding.workWebView.loadUrl("https://h5.m.taobao.com/mlapp/mytaobao.html#mlapp-mytaobao");
        binding.workWebView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html?tabCode=waitConfirm");
//        binding.workWebView.loadUrl("https://login.m.taobao.com/login_oversea.htm");
    }

    @Override
    public void onClick(View v) {
        isButtonClicked = true;
            switch (v.getId()) {
                case R.id.send_button:
                    mCurrentPage = 1;
                    mProductList.clear();
                    EventBus.getDefault().post(new ProgressBarEvent(true));
//                    binding.progressLayout.setVisibility(View.VISIBLE);
                    mUrl = "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?tabCode=waitSend&pageNum=";
                    binding.workWebView.loadUrl(mUrl + mCurrentPage);
                    break;
                case R.id.confirm_button:
                    mCurrentPage = 1;
                    mProductList.clear();
                    EventBus.getDefault().post(new ProgressBarEvent(true));
//                    binding.progressLayout.setVisibility(View.VISIBLE);
                    mUrl = "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm?tabCode=waitConfirm&pageNum=";
                    binding.workWebView.loadUrl(mUrl + mCurrentPage);
                    break;
                case R.id.get_order_history_button:
                    if (mProductList!= null) {
                        for (int i = 0; i < mProductList.size(); i++) {
                            OrderListProductModel productModel = mProductList.get(i);
                            ShippingActivity.sProductModelList.add(new ShippingProductModel(productModel.getOrderNumber(), mMemberName, null, "", "",
                                    productModel.getPrice(), productModel.getQuantity(), productModel.getColor(), productModel.getSize(), productModel.getGoodsUrl(), productModel.getImageUrl()));
                        }
                    }
                    finish();
//                if (mProductList.get(0) != null) {
//                    EventBus.getDefault().post(new ProgressBarEvent(true));
//                    mIndex = 0;
//                    binding.workWebView.loadUrl(mTragkingNumberUrl + mProductList.get(mIndex).getOrderNumber());
//                    Log.d(TAG, "onClick: " + mProductList.get(mIndex).getOrderNumber());
//                }
                    break;
            }

    }

    public class MyJavascriptInterface {

        private File mFolder;
        private String mFileName;

        @JavascriptInterface //킷캣 이상에선 어노테이션을 붙여줘야됨
        public void getTaobaoDataHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            String data = html.substring(html.indexOf("var data") + 23); //11은 var data = 를 제외하기 위한 자리수
            String data2 = data.substring(0, data.indexOf("</script>"));
            String data3 = data2.substring(0, data2.lastIndexOf("'"));
            String data4 = data3.replace("\\\"", "\"");
            Log.d(TAG, "getHtml33: " + data4);

            Log.d(TAG, "getHtml: " + data4.toString());
//
            JSONObject jsonObject = null;
//
            try {
                jsonObject = new JSONObject(data4);
                JSONArray mainOrdersArray = jsonObject.getJSONArray("mainOrders");
                JSONObject page = jsonObject.getJSONObject("page");
                mTotalPage = Integer.parseInt(page.getString("totalPage"));  //이게 0이면 돌릴필요 없음 토스트로 오더 내역 없다고 표시하기
                Log.d(TAG, "getHtml: page" + mTotalPage);

                for (int i = 0; i < mainOrdersArray.length(); i++) {
                    JSONObject object = mainOrdersArray.getJSONObject(i);
                    JSONArray subOrderList = object.getJSONArray("subOrders");
                    String rOrderId = valuesNullCheck(object.getString("id"));
                    for (int j = 0; j < subOrderList.length(); j++) {
                        JSONObject subOrder = subOrderList.getJSONObject(j);
                        String rQuantity = valuesNullCheck(subOrder.getString("quantity"));
                        JSONObject priceInfo = subOrder.getJSONObject("priceInfo");
                        String rPrice = valuesNullCheck(priceInfo.getString("realTotal"));

                        JSONObject itemInfo = subOrder.getJSONObject("itemInfo");

                        String rTitle = valuesNullCheck(itemInfo.getString("title"));
                        String rItemUrl = "https:" + valuesNullCheck(itemInfo.getString("itemUrl"));
                        String rImageUrl = "";
                        if (itemInfo.toString().contains("\"pic\"")) {
                            rImageUrl = valuesNullCheck(itemInfo.getString("pic"));
                        }
                        if (itemInfo.toString().contains("\"skuText\"")) {
                            JSONArray skuTexts = itemInfo.getJSONArray("skuText");
                            for (int k = 0; k < skuTexts.length(); k++) {
                                JSONObject skuTextObject = skuTexts.getJSONObject(k);

                                if (skuTextsKeyCheck(skuTextObject.getString("name")).equals("size")) {
                                    mSize = valuesNullCheck(skuTextObject.getString("value"));
                                    Log.d(TAG, "getHtml: " + mSize);
                                } else if (skuTextsKeyCheck(skuTextObject.getString("name")).equals("color")) {
                                    mColor = valuesNullCheck(skuTextObject.getString("value"));
                                    Log.d(TAG, "getHtml: " + mColor);
                                }
                            }
                        }
                        mProductList.add(new OrderListProductModel(false, rOrderId, rTitle, rPrice, rQuantity, mColor, mSize, rItemUrl, rImageUrl));
                    }

                }
                Log.d(TAG, "getHtml: " + mProductList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "getTaobaoDataHtml: " + mCurrentPage + "," + mTotalPage);

            if (mCurrentPage >= mTotalPage) {
                EventBus.getDefault().post(new ProgressBarEvent(false));  //작업 마침
            } else {
                EventBus.getDefault().post(new DataloadEvent());  //페이징 작업 계속
            }

        }

        @JavascriptInterface
        public void getTrackingDataHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            Log.d(TAG, "getHtml: " + html);

        }
    }

//    public void getTrackingHttp(String relativeUrl, RequestParams params) throws JSONException {
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
//                Log.d("onMainSuccess: ", response.toString());
//
//            }
//            @Override
//            public void onFinish() {
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                // 통신 실패시 호출 되는 메소드
//                Toast.makeText(getApplicationContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("onFailure: ", throwable.toString());
//            }
//        });
//    }

    public String valuesNullCheck(String value) {
        return (value == null) ? "" : value.toString();
    }

    public String skuTextsKeyCheck(String key) {
        if (key.equals("颜色分类")) {
            return "color";
        } else if (key.equals("尺寸")) {
            return "size";
        }
        return "";
    }

//    class ShowWebClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
////            view.loadUrl(url);
//            Log.d("shouldOverride", url);
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            binding.progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            binding.progressBar.setVisibility(View.GONE);
//
//        }
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_login:
                binding.workWebView.loadUrl("https://login.m.taobao.com/login_oversea.htm");
                binding.workWebView.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.workWebView.canGoBack()) {
            binding.workWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event instanceof ProgressBarEvent) {
            ProgressBarEvent progressBarEvent = (ProgressBarEvent) event;
            boolean progressbarShown = progressBarEvent.isShow();
            if (progressbarShown) {  //주문 목록 가져오기 작업 시작할때
                Toast.makeText(this, "데이터를 가져올때까지 기다려주세요", Toast.LENGTH_LONG).show();
                binding.workWebView.setVisibility(View.GONE);
                binding.progressLayout.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.noResultText.setVisibility(View.GONE);
            } else { //주문 목록 가져오기 작업 끝났을때
                isButtonClicked = false;
                binding.progressLayout.setVisibility(View.GONE);

                if (mProductList.size() < 1) {  //리스트 데이터 없을때
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.noResultText.setVisibility(View.VISIBLE);
                } else {  //리스트 데이터 있을때
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        } else if (event instanceof OrderListDataBringCheckEvent) {  //주문 목록 리스트 체크박스 이벤트
            OrderListDataBringCheckEvent orderListDataBringCheckEvent = (OrderListDataBringCheckEvent) event;
            if (orderListDataBringCheckEvent.isAllChecked()) {
//                mProductList 모두 체크하면
                for (int i = 0; i < mProductList.size(); i++) {
                    mProductList.get(i).setCheck(true);
                }
                mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < mProductList.size(); i++) {
                    mProductList.get(i).setCheck(false);
                }
                mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
            }
        } else if (event instanceof DataloadEvent) {  //페이징 작업 ㄱ 이벤트
            ++mCurrentPage;
            binding.workWebView.loadUrl(mUrl + mCurrentPage);
            Log.d(TAG, "getHtml: " + mUrl + mCurrentPage);
        }
    }

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

    public class ListAdapter extends RecyclerView.Adapter {
        private int mPosition;
        private LayoutInflater mLayoutInflater;
        private ArrayList<OrderListProductModel> list;
        private final RequestManager glide;

        public ListAdapter(Context context, ArrayList<OrderListProductModel> list, RequestManager glide) {
            this.list = list;
            mLayoutInflater = LayoutInflater.from(context);
            this.glide = glide;

        }

        public void setData(ArrayList<OrderListProductModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_order_list_product, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final ViewHolder viewHolder = (ViewHolder) holder;


            if (list.get(position) != null) {
                if (list.get(position).getImageUrl() != null) {
                    Log.d("onBindViewHolder: ", "ddddddddd");
//                    String url = list.get(position).getImageUrl().replace("//","");
//                    Glide.with(OrderListActivity.this).load(list.get(position).getImageUrl()).centerCrop().into(viewHolder.listBinding.image);
                    glide.load(list.get(position).getImageUrl())
                            .centerCrop()
                            .into(viewHolder.listBinding.image);
                }
//                viewHolder.listBinding.checkbox.setOnCheckedChangeListener(null);
                viewHolder.listBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            list.get(position).setCheck(true);
                        } else {
                            list.get(position).setCheck(false);
                        }
                    }
                });
                int index = position + 1;
                viewHolder.listBinding.checkbox.setText("상품정보 " + index);
                viewHolder.listBinding.checkbox.setChecked(list.get(position).isCheck());
                viewHolder.listBinding.orderNumberEdit.setText(list.get(position).getOrderNumber());
                viewHolder.listBinding.productsNameEdit.setText(list.get(position).getProductName());
                viewHolder.listBinding.priceEdit.setText(list.get(position).getPrice());
                viewHolder.listBinding.quantityEdit.setText(list.get(position).getQuantity());
                viewHolder.listBinding.colorEdit.setText(list.get(position).getColor());
                viewHolder.listBinding.sizeEdit.setText(list.get(position).getSize());
                viewHolder.listBinding.goodsUrlEdit.setText(list.get(position).getGoodsUrl());
                viewHolder.listBinding.imageUrlEdit.setText(list.get(position).getImageUrl());

            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ListItemOrderListProductBinding listBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                listBinding = DataBindingUtil.bind(itemView);

//                setView();
            }

            private void setView() {
                mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                listBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            list.get(mPosition).setCheck(true);
                        } else {
                            list.get(mPosition).setCheck(false);
                        }
                    }
                });
            }
        }
    }
}
