package com.example.kyoungae.myapplication.shipping;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivityShippingBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.ShippingNextButtonEvent;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.main.NoticeFragment;
import com.example.kyoungae.myapplication.shipping.model.AddressModel;
import com.example.kyoungae.myapplication.shipping.model.CategoryModel;
import com.example.kyoungae.myapplication.shipping.model.ShippingCenterModel;
import com.example.kyoungae.myapplication.shipping.model.ShippingProductModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShippingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityShippingBinding binding;
    private PagerAdapter pagerAdapter;
    private boolean isMovePageThread = true;
    private String mUrl = "Acting/OrdReq_S.php";
    //    public static HashMap<Integer, ShippingProductModel> sProductModelHashMap;
    public static ArrayList<ShippingProductModel> sProductModelList;
    private String mCenterName;
    private String mAddress;
    private ArrayList<CategoryModel> mCategoryList;
    private AddressModel mAddressModel;
    private String mMemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping);

        setTitle("배송대행신청");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setData();

    }

    public void setData() {

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(this);

        if (keyInfo != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        // TODO: 2017-10-19 page name 수정
        params.put("PageNm", "배송대행신청");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            getHttp(mUrl, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onAcceptSuccess: ", response.toString());

                // TODO: 2017-10-19 데이터 받은거 수정

                ArrayList<ShippingCenterModel> centerList = new ArrayList<>();
                mCategoryList = new ArrayList<>();

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {
                        JSONArray centerArray = response.getJSONArray("Center");
                        JSONArray categoryArray = response.getJSONArray("CtmsItem");
                        Log.d("onSuccess: ", categoryArray.toString());
                        JSONArray addressArray = response.getJSONArray("MemAddr");
                        JSONArray memberInfo = response.getJSONArray("MemInf");
                        mMemberName = memberInfo.getJSONObject(0).getString("MemEnNm");
                        Log.d("onSuccess: ", addressArray.toString());

                        for (int i = 0; i < centerArray.length(); i++) {
                            JSONObject object = centerArray.getJSONObject(i);
                            Log.d("onAcceptSuccess: ", object.getString("CtrSeq").toString());
                            centerList.add(new ShippingCenterModel(object.getString("CtrSeq"), object.getString("CtrCd"),
                                    object.getString("CtrNm"), object.getString("CtrNmCn"), object.getString("Addr")));
                            mCenterName = centerList.get(i).getCtrNm();
                            mAddress = centerList.get(i).getAddr();
//                            binding.centerSelectButton.setText(acceptTermsList.get(i).getCtrNm());
//                            binding.addressText.setText(acceptTermsList.get(i).getAddr());
//                            String centerInfo =

                        }

                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject object = categoryArray.getJSONObject(i);
                            mCategoryList.add(new CategoryModel(object.getString("ArcSeq"), object.getString("KrArc"), object.getString("EnArc")));
                        }

                        Log.d("onAcceptSuccess: ", addressArray.toString());
//                        Log.d("onSuccess: ",mCategoryList.toString());
//                        for (int i = 0; i < addressArray.length(); i++) {
//                            JSONObject object = addressArray.getJSONObject(i);
//                            if (object.getString("MainYn").equals("Y")) {
//                                mAddressModel = new AddressModel(object.getString("AddrSeq"), object.getString("AdrsKr"), object.getString("AdrsEn"), object.getString("Zip"),
//                                        object.getString("Addr1"), object.getString("Addr2"), object.getString("Addr1En"), object.getString("Addr2En"),
//                                        object.getString("MobNo"), object.getString("RrnNo"), object.getString("RrnCd"), object.getString("MainYn"));
//                            }
//                        }

                        //상품 리스트 선언
                        sProductModelList = new ArrayList<>();
                        for (int i = 0; i < 1; i++) {
                            sProductModelList.add(new ShippingProductModel("", mMemberName, new CategoryModel(), "", "", "", "", "", "", "", ""));
                        }
                        setTabLayout();

                    } else {
                        Toast.makeText(ShippingActivity.this, "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ShippingActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(ShippingActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
                Log.d("onFailure: ", responseString.toString());
            }
        });
    }

//    public void movePage() {
//        final Handler handler = new Handler();
//        현재 위치
//이동할 위치
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isMovePageThread) {
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int getSelectedPosition = binding.viewPager.getCurrentItem();  //현재 위치
//                            int movePosition = getSelectedPosition + 1;  //이동할 위치
//                            binding.viewPager.setCurrentItem(movePosition);
//                            if (getSelectedPosition == 0 || getSelectedPosition == 1 || getSelectedPosition == 2) {
//                                binding.viewPager.setCurrentItem(movePosition);
//                            } else {
//                                binding.viewPager.setCurrentItem(0,false);
//
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();
//    }


    public void setTabLayout() {

        // Initializing the TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("배송센터"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("상품정보"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("수취인정보"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("요청사항"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Creating TabPagerAdapter adapter
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
//        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {
////                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
////                    params.setScrollFlags(0);
////                    binding.fab.show();
//                } else if (position == 2) {
////                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
////                    params.setScrollFlags(8);
////                    binding.fab.show();
//                } else if (position == 3) {
////                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
////                    params.setScrollFlags(0);
////                    binding.fab.show();
//                } else {
////                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
////                    params.setScrollFlags(8);
////                    binding.fab.show();
//                }

//                if (position ==5) {
//                    binding.viewPager.set
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    binding.viewPager.setCurrentItem(1);
//                    binding.viewPager.setCurrentItem(1,false);
//                    binding.viewPager.setCurrentItem(1,true);
//                    binding.viewPager.setVerticalScrollbarPosition(1);
//                    pagerAdapter.getItem(1);

//                    Log.d("onPageSelected:", "dddddddddddd");
//                }else if (position == 0){
//                    binding.viewPager.setCurrentItem(4,false);
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        // Set TabSelectedListener
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        movePage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        if (getIntent().getStringExtra("member_name") != null) {
//            String mMemberName = getIntent().getStringExtra("member_name");
//
//        }
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event instanceof ShippingNextButtonEvent) {
            int getSelectedPosition = binding.viewPager.getCurrentItem();  //현재 위치
            int movePosition = getSelectedPosition + 1;  //이동할 위치
            binding.viewPager.setCurrentItem(movePosition);
        }

//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentReplaceEvent.getFragment()).addToBackStack("null").commit();
//            setTitle(fragmentReplaceEvent.getFragmentName());
//
//        }
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
        isMovePageThread = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fab:
//                int getSelectedPosition = binding.tabLayout.getSelectedTabPosition();  //현재 위치
//                int movePosition = getSelectedPosition + 1;  //이동할 위치
//                if (getSelectedPosition == 0 || getSelectedPosition == 1 || getSelectedPosition == 2) {
//                    binding.viewPager.setCurrentItem(movePosition);
//                }
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        // Count number of tabs
        private int tabCount;
        NoticeFragment tabFragment;


        public PagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
//
            // Returning the current tabs
            switch (position) {
                case 0:
                    ShippingCenterFragment tabFragment1 = ShippingCenterFragment.newInstance(mCenterName, mAddress);
                    return tabFragment1;
                case 1:
                    ProductInformationFragment tabFragment2 = ProductInformationFragment.newInstance(mCategoryList, mMemberName);
                    return tabFragment2;
                case 2:
                    RecieverInformationFragment tabFragment3 = RecieverInformationFragment.newInstance(mAddressModel);
                    return tabFragment3;
                case 3:
                    RequestsFragment tabFragment4 = new RequestsFragment();
                    return tabFragment4;
                default:
                    return null;
            }
//            switch (position) {
//                case 0:
//                    tabFragment = NoticeFragment.newInstance("3", "3");
//
//                    return tabFragment;
//                case 1:
//                    tabFragment = NoticeFragment.newInstance("0", "0");
//                    Log.d("getItem: ", "position : " + position);
//                    notifyDataSetChanged();
//                    return tabFragment;
//                case 2:
//                    tabFragment = NoticeFragment.newInstance("1", "1");
//                    return tabFragment;
//                case 3:
//                    tabFragment = NoticeFragment.newInstance("2", "2");
//                    return tabFragment;
//                case 4:
//                    tabFragment = NoticeFragment.newInstance("3", "3");
//                    return tabFragment;
//                case 5:
//                    tabFragment = NoticeFragment.newInstance("0", "0");
//                    return tabFragment;
//                default:
//                    return tabFragment;
//            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
