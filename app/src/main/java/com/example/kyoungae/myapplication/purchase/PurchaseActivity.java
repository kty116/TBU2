package com.example.kyoungae.myapplication.purchase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivityPurchaseBinding;
import com.example.kyoungae.myapplication.databinding.ListItemRecommendWordBinding;
import com.example.kyoungae.myapplication.databinding.ListItemTaobaoPurchaseBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.lib.HeaderAndFooterRecyclerViewAdapter;
import com.example.kyoungae.myapplication.lib.HeaderSpanSizeLookup;
import com.example.kyoungae.myapplication.lib.RecyclerViewUtils;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.view.ListHeader;
import com.example.kyoungae.myapplication.webview.DefaultWebViewFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static org.greenrobot.eventbus.EventBus.TAG;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPurchaseBinding binding;
    boolean isLoading = false;
    private int mTotalPage;
    private int mCurrentPage = 1;
    private RequestParams mParams;
    private String mUrl = "Taobao/Taobao_L.php";  //상품 검색 url
    private ArrayList<PurchaseModel> mItmeListData;
    private ProductListAdapter mProductListAdapter;
    private boolean isSuccessed = false;
    private String mTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("타오바오 구매");
//        binding.bottomBar.setOnTabSelectListener(this);

        setRecommendWord();  //추천 검색어 가져오기
        setView();  //뷰 셋팅하기

    }

    public void setView() {

        binding.translateButton.setOnClickListener(this);
        binding.filterButton.setOnClickListener(this);
        binding.searchButton.setOnClickListener(this);
        binding.scrollUpButton.setOnClickListener(this);
        binding.searchFilterLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = getCurrentFocus();

                if (view != null) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    view.clearFocus();
                }
                return true;
            }
        });

        binding.productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.computeVerticalScrollOffset() > 2000) {
                    binding.scrollUpButton.show();
                } else {
                    binding.scrollUpButton.hide();
                }
            }
        });

        binding.searchWordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchWord();
//                switch (actionId) {


//                    case EditorInfo.IME_ACTION_SEARCH:
//                        Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_LONG).show();
//                        break;
//                    default:
////                        Toast.makeText(getApplicationContext(), "기본", Toast.LENGTH_LONG).show();
//                        return false;
//                }
                return true;
            }
        });
    }

    public void setListItemLoadListner() {
        BaseAttacher attacher = Mugen.with(binding.productsRecyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                loadData();
            }

            @Override
            public boolean isLoading() {
                //데이터 가져오고 있는 중이면 true
                // 아니면 false
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();

        attacher.setLoadMoreOffset(4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (binding.searchFilterLayout.getVisibility() == View.VISIBLE) {
            binding.searchFilterLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    public void loadData() {

        if (mCurrentPage <= mTotalPage) {
            mCurrentPage++;
            mParams.put("Go", mCurrentPage);

            Log.d(TAG, "mTotalPage: " + mTotalPage);
            Log.d(TAG, "mCurrentPage: " + mCurrentPage);

            try {
                getListItemsHttp(mUrl, mParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void setRecommendWord() {
        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(this);

        if (keyInfo.getAuthKey() != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        params.put("PageNm", "타오바오 구매");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);
        try {
            getRecommendWordHttp("/Taobao/TaobaoKey_S.php?", params);
        } catch (JSONException e) {

        }
    }

    public void getRecommendWordHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("onMainSuccess: ", response.toString());


                ArrayList<RecommendWordModel> recommendWordList = new ArrayList<>();

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {
                        JSONArray cateArray = response.getJSONArray("CateCd");
                        for (int i = 0; i < cateArray.length(); i++) {
                            JSONObject cateObject = cateArray.getJSONObject(i);
                            recommendWordList.add(new RecommendWordModel(cateObject.getString("CdNm"), cateObject.getString("Ref1")));
                        }
                        RecommendWordListAdapter dataAdapter = new RecommendWordListAdapter(getApplicationContext(), recommendWordList);
                        binding.recyclerView.setAdapter(dataAdapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(getApplicationContext(), "서버와 통신의 실패로 추천 검색어를 받아 오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scroll_up_button:

                binding.appBarLayout.setExpanded(true, true);
                binding.productsRecyclerView.scrollToPosition(0);
                break;
            case R.id.add_item_layout:
                View cView = getCurrentFocus();
                cView.clearFocus();
                break;

            case R.id.filter_button:
                if (binding.searchFilterLayout.getVisibility() == View.GONE) {
                    binding.searchFilterLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.searchFilterLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.search_button:

                searchWord();  //검색하기

                break;
            case R.id.translate_button:
//                네이버 사전 연결
                CommonLib.subActivityIntent(PurchaseActivity.this, DefaultWebViewFragment.newInstance("중국어 번역", "http://m.translate.naver.com/#/ko/zh-CN"));
                break;

        }
    }

    public void searchWord() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        mItmeListData = new ArrayList<>();

        if (binding.searchFilterLayout.getVisibility() == View.VISIBLE) {
            binding.searchFilterLayout.setVisibility(View.GONE);
        }

        String beginPrice = binding.beginPriceText.getText().toString();
        String endPrice = binding.endPriceText.getText().toString();

        if (binding.searchWordText.emptyCheck() != null) {

            mParams = new RequestParams();

            KeyDTO keyInfo = CommonLib.getKeyInfo(this);

            if (keyInfo.getAuthKey() != null) {
                mParams.put("AuthKey", keyInfo.getAuthKey());
                mParams.put("MemCode", keyInfo.getMemberCode());
            }
//
            mParams.put("PageNm", "타오바오 구매");
            mParams.put("AppVer", Build.MODEL);
            mParams.put("ModelNo", Build.VERSION.RELEASE);
            mParams.put("Query", binding.searchWordText.emptyCheck());

            Log.d("onClick: ", binding.searchWordText.emptyCheck());

            if (binding.beginPriceText.emptyCheck() == null) {
                beginPrice = "0";
            }
            if (binding.endPriceText.emptyCheck() == null) {
                endPrice = "0";
            }

            //가격 빈 값일때 기본값 0 넣기
            int intBeginPrice = Integer.parseInt(beginPrice);
            int intEndPrice = Integer.parseInt(endPrice);

            if (intBeginPrice > intEndPrice) {
                mParams.put("BeginPrice", endPrice);
                mParams.put("EndPrice", beginPrice);
            } else {
                mParams.put("BeginPrice", beginPrice);
                mParams.put("EndPrice", endPrice);
            }

            //통신 시작

            try {
                searchProductsHttp(mUrl, mParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            binding.productsRecyclerView.scrollToPosition(0);
            binding.noResultText.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchProductsHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                isSuccessed = false;
                binding.progressBar.setVisibility(View.VISIBLE);
//                binding.noResultText.setVisibility(View.GONE);
//                EventBus.getDefault().post(new ProgressBarEvent(true));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onMain: ", response.toString());
                isSuccessed = true;
                Log.d(TAG, "onSuccess: dddddd");
                String error = CommonLib.getStringByJsonObject(response, "RstNo");
                Log.d("onMainSuccess: ", "dd"+error);
                if (error.equals("0")) {
                    JSONArray array = CommonLib.getJsonArrayByJsonObject(response, "ProInf");
                    mTotalCount = CommonLib.getStringByJsonObject(response, "TotalCnt");
                    mTotalPage = Integer.parseInt(CommonLib.getStringByJsonObject(response, "TotalPage"));
                    Log.d("onSuccess: ", array.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = CommonLib.getJsonObjectByJsonArray(array, i);
                        mItmeListData.add(new PurchaseModel(CommonLib.getStringByJsonObject(object, "ProId"), CommonLib.getStringByJsonObject(object, "ProUrl"), CommonLib.getStringByJsonObject(object, "Title"),
                                CommonLib.getStringByJsonObject(object, "ImgUrl"), CommonLib.getStringByJsonObject(object, "Price"), CommonLib.getStringByJsonObject(object, "FinalPrice")));
                    }

                    Log.d("onMainSuccess: ", mItmeListData.size() + "");

                    mProductListAdapter = new ProductListAdapter(PurchaseActivity.this, mItmeListData, Glide.with(PurchaseActivity.this));

                    HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mProductListAdapter);
                    binding.productsRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

                    GridLayoutManager manager = new GridLayoutManager(PurchaseActivity.this, 2);
                    manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) binding.productsRecyclerView.getAdapter(), manager.getSpanCount()));
                    binding.productsRecyclerView.setLayoutManager(manager);

                    RecyclerViewUtils.setHeaderView(binding.productsRecyclerView, new ListHeader(PurchaseActivity.this, mTotalCount));
                    binding.productsRecyclerView.setVisibility(View.VISIBLE);
                    setListItemLoadListner();

                } else {
                    binding.noResultText.setVisibility(View.VISIBLE);
                    binding.productsRecyclerView.setVisibility(View.INVISIBLE);
                }


//                GridLayoutManager gridLayoutManager = new GridLayoutManager(PurchaseActivity.this, 2);
//                binding.productsRecyclerView.setLayoutManager(gridLayoutManager);
//                mProductListAdapter = new ProductListAdapter(PurchaseActivity.this, mItmeListData, Glide.with(PurchaseActivity.this));
//                binding.productsRecyclerView.setAdapter(mProductListAdapter);
////                HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mProductListAdapter);
////                binding.productsRecyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
//                RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) findViewById(R.id.recycler_view_header);
//                recyclerHeader.attachTo(binding.productsRecyclerView);
                //add a HeaderView
//                RecyclerViewUtils.setHeaderView(binding.productsRecyclerView, new ListHeader(PurchaseActivity.this, totalCount));
                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
                Log.d("onMain: ", "dd");

                binding.progressBar.setVisibility(View.GONE);
                failLoadData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PurchaseActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

    public void getListItemsHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                isLoading = true;
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onMainSuccess: ", response.toString());

                try {
                    String error = response.getString("RstNo");
                    Log.d("onMainSuccess: ", error);
                    if (error.equals("0")) {
                        JSONArray array = response.getJSONArray("ProInf");


//                            String totalCount = response.getString("TotalCnt");
//                            mListHeader = new ListHeader(getContext(),totalCount);
//                            RecyclerViewUtils.setHeaderView(binding.recyclerView, mListHeader);

                        mTotalPage = Integer.parseInt(response.getString("TotalPage"));
                        Log.d("onSuccess: ", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            mItmeListData.add(new PurchaseModel(object.getString("ProId"), object.getString("ProUrl"), object.getString("Title"),
                                    object.getString("ImgUrl"), object.getString("Price"), object.getString("FinalPrice")));
                        }

                        mProductListAdapter.setData(mItmeListData);
//                        mProductListAdapter.notifyDataSetChanged();

                        Log.d("onMainSuccess: ", mItmeListData.size() + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // 받아온 JSONArray 자료 처리
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
                failLoadData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PurchaseActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

    public void failLoadData() {
        Thread thread = new Thread(new Runnable() {
            Handler handler = new Handler();

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isSuccessed) {
                            Snackbar.make(binding.parentLayout, "데이터를 불러오는데 실패했습니다. 네트워크 연결을 확인하세요.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        try {
        } catch (Exception e) {
        }
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib") && !(s.equals("shared_prefs"))) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public class RecommendWordListAdapter extends RecyclerView.Adapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<RecommendWordModel> mDataList = new ArrayList<>();
        private Map<Integer, Boolean> mCheckListMap = new HashMap<>();
        private int mPosition;
        //        private ArrayList<Boolean> checkedList = new ArrayList<>();
//        private boolean[] checkedList = new boolean[10];

        public RecommendWordListAdapter(Context context, ArrayList<RecommendWordModel> list) {
            this.mDataList = list;
            mLayoutInflater = LayoutInflater.from(context);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_recommend_word, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.recommendWordBinding.word.setText(mDataList.get(position).getKoreanWord());
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ListItemRecommendWordBinding recommendWordBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                recommendWordBinding = DataBindingUtil.bind(itemView);

                recommendWordBinding.word.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                        if (TextUtils.isEmpty(binding.searchWordText.getText().toString())) {
                            binding.searchWordText.setText(mDataList.get(mPosition).getChineseWord());
                        } else {
                            binding.searchWordText.setText(binding.searchWordText.getText().toString() + mDataList.get(mPosition).getChineseWord());
                        }

                        binding.detailSearchKoreanText.setText(mDataList.get(mPosition).getKoreanWord());
                    }
                });
            }
        }
    }

    public class ProductListAdapter extends RecyclerView.Adapter {
        private int mPosition;
        private LayoutInflater mLayoutInflater;
        private ArrayList<PurchaseModel> dataList = new ArrayList<>();
        private final RequestManager glide;
//        private Map<Integer, Boolean> mCheckListMap = new HashMap<>();

        public ProductListAdapter(Context context, ArrayList<PurchaseModel> list, RequestManager glide) {
            this.dataList = list;
            mLayoutInflater = LayoutInflater.from(context);
            this.glide = glide;

        }

        public void setData(ArrayList<PurchaseModel> list) {
            this.dataList = list;
//            notifyDataSetChanged();
            runOnUiThread(updateUI);
        }

        private Runnable updateUI = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };


        public void changeData() {
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_taobao_purchase, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ViewHolder viewHolder = (ViewHolder) holder;
            glide.load(dataList.get(position).getImageUrl())
////                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(400, 400)
                    .centerCrop()
                    .into(viewHolder.listBinding.image);
//            Picasso.with(getActivity())
//                    .load(dataList.get(position).getImageUrl())
//                    .config(Bitmap.Config.RGB_565)
//                    .memoryPolicy(MemoryPolicy.NO_STORE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .centerCrop()
//                    .resize(400,400)
//                    .into(viewHolder.listBinding.image);

            viewHolder.listBinding.titleText.setText(dataList.get(position).getTitle());
            if (!dataList.get(position).getPrice().equals(dataList.get(position).getFinalPrice())) {
                viewHolder.listBinding.beforePriceText.setText("¥" + dataList.get(position).getPrice() + "→");
            }
            viewHolder.listBinding.afterPriceText.setText("¥" + dataList.get(position).getFinalPrice());

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ListItemTaobaoPurchaseBinding listBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                listBinding = DataBindingUtil.bind(itemView);

                listBinding.beforePriceText.setPaintFlags(listBinding.beforePriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                setView();

            }

            public void setView() {

                listBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                        int position = mPosition -1;


                        // TODO: 2017-10-29 url 작성
                        CommonLib.subActivityIntent(PurchaseActivity.this, DefaultWebViewFragment.newInstance("상세보기", dataList.get(position).getDetailUrl()));
//                        Log.d(TAG, "onClick: "+mPosition + dataList.get(mPosition).getDetailUrl());
                    }
                });

            }
        }
    }


//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//
//        String beginPrice = binding.beginPriceText.getText().toString();
//        String endPrice = binding.endPriceText.getText().toString();
//
//        if (!hasFocus) {
//            switch (v.getId()) {
//                case R.id.begin_price_text:
//
//                    if (!TextUtils.isEmpty(beginPrice)) {
//                        String rBeginPrice = removeCNYUnit(beginPrice);
//                        binding.beginPriceText.setText(rBeginPrice + ".00");
//                    }
//
//                    break;
//
//                case R.id.end_price_text:
//                    if (!TextUtils.isEmpty(endPrice)) {
//                        String rEndPrice = removeCNYUnit(endPrice);
//                        binding.endPriceText.setText(rEndPrice + ".00");
//                    }
//                    break;
//            }
//        } else {  //포커스 있을때
//            switch (v.getId()) {
//                case R.id.begin_price_text:
//                    if (!TextUtils.isEmpty(beginPrice)) {
//                        String rBeginPrice = removeCNYUnit(beginPrice);
//                        binding.beginPriceText.setText(rBeginPrice);
//                    }
//                    break;
//
//                case R.id.end_price_text:
//                    if (!TextUtils.isEmpty(endPrice)) {
//                        String rEndPrice = removeCNYUnit(endPrice);
//                        binding.endPriceText.setText(rEndPrice);
//                        break;
//                    }
//            }
//        }
//    }
//
//    public String removeCNYUnit(String viewText) {
//        if (viewText.contains(".00")) {
//            Log.d("removeCNYUnitcontains: ", viewText);
//            viewText = viewText.replace(".00", "");
//            Log.d("removeCNYUnitreplace: ", viewText);
//        }
//        Log.d("removeCNYUnit: ", viewText);
//        return viewText;
//    }

//    @Override
//    public void onTabSelected(@IdRes int tabId) {
//
//        if (tabId == R.id.tab_favorites1) {
//            addCustomImageInputView(R.layout.toolbar_in_taobao_purchase);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, PurchaseFragment.newInstance()).commit();
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearApplicationData();
    }
}
