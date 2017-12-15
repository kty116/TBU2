package com.example.kyoungae.myapplication.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kyoungae.myapplication.purchase.PurchaseActivity;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivityMain2Binding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.my_page.MyPageFragment;
import com.example.kyoungae.myapplication.shipping.TermsFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements Serializable, View.OnClickListener {

    private final long FINISH_INTERVAL_TIME = 2000;
    private int mClickId = -1;
    private long backPressedTime = 0;
    private ActivityMain2Binding binding;
    private ImageView[] mImageViews;
    private int[] mResourceImgs;
    private ArrayList<noticeModel> mNoticeList;
    private boolean isNoticeThread;  // 공지사항 페이지 넘김 쓰레드 설정
    private boolean isNoticeMove;   //공지사항 페이지 움직임 설정
    private ArrayList<ButtonModel> mButtonImageList;
    private boolean isAlreadyStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        setTitle("THE BAY");
        setSupportActionBar(binding.toolbar);
        setMainData();

    }

    @Override
    public void onStart() {
        super.onStart();
        isNoticeThread = true;
        isNoticeMove = true;
        if (isAlreadyStarted) {  //시작 한번 했으면 데이터 있는게 확실함으로 다시 스타트일때 움직임
            movePage();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isNoticeThread = false;
    }

    public void setMainData() {

        mImageViews = new ImageView[]{binding.mainButton1, binding.mainButton2, binding.mainButton3, binding.mainButton4, binding.mainButton5, binding.mainButton6};

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(this);

        if (keyInfo.getAuthKey() != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        params.put("PageNm", "메인");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            getMainDataHttp("Main.php", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

//        if (mClickId != -1) {
//            if (mImageViews[mClickId].getId() != v.getId()) { //전에 눌렀던 얘랑 같지 않을때
//                mImageViews[mClickId].setImageResource(mResourceImgs[mClickId]);
//                Toast.makeText(getApplicationContext(), "ddddddd", Toast.LENGTH_SHORT).show();
//            }
//        }

        switch (v.getId()) {

            case R.id.main_button1:
                rightPage(0);
                break;

            case R.id.main_button2:
                rightPage(1);
                break;

            case R.id.main_button3:
                rightPage(2);
                break;

            case R.id.main_button4:
                rightPage(3);
                break;

            case R.id.main_button5:
                rightPage(4);
                break;

            case R.id.main_button6:
                rightPage(5);
                break;
        }
    }

    public void rightPage(int buttonPosition) {

        switch (mButtonImageList.get(buttonPosition).getPageName()) {
            case "TaobaoWebview":
                startActivity(new Intent(this, PurchaseActivity.class));
                break;
            case "Shipping":
                CommonLib.subActivityIntent(this, TermsFragment.newInstance());
                break;
            case "Home":
                CommonLib.subActivityIntent(this, MyPageFragment.newInstance());
                break;
            case "PayHistory":

                break;
            case "OrderHistory":

                break;
            case "Inquary":

                break;
            case "MessageBox":

                break;
            case "CouponHistory":

                break;
            case "Deposit":

                break;

        }

    }


    private void initViewPager() {

        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 6);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(1);
        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position > 4) {  //5
                    binding.viewPager.setCurrentItem(1, false);

                } else if (position < 1) {  //0
                    binding.viewPager.setCurrentItem(4, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    isNoticeMove = false;
                } else if (state == 0) {
                    isNoticeMove = true;
                }
            }
        });

        movePage();  //공지사항 움직이게
    }

    public void movePage() {
        isAlreadyStarted = true;
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isNoticeThread) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isNoticeMove) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int getSelectedPosition = binding.viewPager.getCurrentItem();  //현재 위치
                                int movePosition = getSelectedPosition + 1;  //이동할 위치
                                binding.viewPager.setCurrentItem(movePosition);
                            }
                        });
                    }
                }
            }
        });
        thread.start();
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private int tabCount;
        NoticeFragment tabFragment;

        public PagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(3).getTit(), mNoticeList.get(3).getInsDate());
                    return tabFragment;
                case 1:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(0).getTit(), mNoticeList.get(0).getInsDate());
                    return tabFragment;
                case 2:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(1).getTit(), mNoticeList.get(1).getInsDate());
                    return tabFragment;
                case 3:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(2).getTit(), mNoticeList.get(2).getInsDate());
                    return tabFragment;
                case 4:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(3).getTit(), mNoticeList.get(3).getInsDate());
                    return tabFragment;
                default:
                    tabFragment = NoticeFragment.newInstance(mNoticeList.get(0).getTit(), mNoticeList.get(0).getInsDate());
                    return tabFragment;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }

    }

    public void getMainDataHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("onMainSuccess: ", response.toString());

                mNoticeList = new ArrayList<>();

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {

                        JSONArray noticeArray = response.getJSONArray("MainNotice");  //공지사항 셋팅
                        for (int i = 0; i < 4; i++) {
                            JSONObject noticeObject = noticeArray.getJSONObject(i);
                            mNoticeList.add(new noticeModel(noticeObject.getString("BbCode"), noticeObject.getString("BbsSeq"),
                                    noticeObject.getString("Ct"), noticeObject.getString("Tit"), noticeObject.getString("InsDate")));
                        }

                        JSONArray toolbarImageArray = response.getJSONArray("MainRolling");  //툴바 이미지 셋팅
                        JSONObject toolbarImageObject = toolbarImageArray.getJSONObject(0);
                        Glide.with(MainActivity.this).load("http://thebay.co.kr" + toolbarImageObject.getString("ImgUrl")).centerCrop().into(binding.toolbarImage);

                        JSONArray buttonImageArray = response.getJSONArray("MainBnr");  //메인 6개 버튼 셋팅
                        mButtonImageList = new ArrayList<>();
                        for (int i = 0; i < buttonImageArray.length(); i++) {
                            JSONObject buttonImageObject = buttonImageArray.getJSONObject(i);
                            mButtonImageList.add(new ButtonModel(buttonImageObject.getString("ImgUrl"), buttonImageObject.getString("AppMenu")));
                            Glide.with(MainActivity.this).load("http://thebay.co.kr" + mButtonImageList.get(i).getImageUrl()).into(mImageViews[i]);
                            mImageViews[i].setOnClickListener(MainActivity.this);
                        }

                        JSONArray customerImageArray = response.getJSONArray("MainSupport");  //하단 고객센터 이미지 셋팅
                        JSONObject customerImageObject = customerImageArray.getJSONObject(0);
                        Glide.with(MainActivity.this).load("http://thebay.co.kr" + customerImageObject.getString("ImgUrl")).centerCrop().into(binding.customerBottomBanner); //고객배너 이미지

                        initViewPager();  //공지사항 뷰 페이져 셋팅
                        binding.parentLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                    Log.d("onMainSuccess: ", mNoticeList.toString());
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
//                EventBus.getDefault().post(new ProgressBarEvent(false));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(getApplicationContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
}


