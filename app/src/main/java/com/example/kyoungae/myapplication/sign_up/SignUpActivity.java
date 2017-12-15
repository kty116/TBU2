package com.example.kyoungae.myapplication.sign_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.kyoungae.myapplication.main.MainActivity;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.Security;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivitySignUpBinding;
import com.example.kyoungae.myapplication.databinding.IncludeSignUp2Binding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.SignUpSelectedAddressEvent;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.view.CustomEditText;
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

import cz.msebera.android.httpclient.Header;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private String mLanguageValue;
    private ActivitySignUpBinding binding;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static KeyDTO sKeyDTO = null;
    public static String mMemCode;
    public static String mAuthKey;
    private CustomEditText[] mCustomEditTexts;
    private boolean text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        ArrayList<String> subscriptionPathSpinnerList = new ArrayList<>();
        subscriptionPathSpinnerList.add("==== 가입경로 선택 ====");
        subscriptionPathSpinnerList.add("카페내 검색으로");
        subscriptionPathSpinnerList.add("추천을 받음");
        subscriptionPathSpinnerList.add("타 카페를 통해서");
        subscriptionPathSpinnerList.add("네이버 블로그");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text, subscriptionPathSpinnerList);
        binding.signUp2.subscriptionPathSpinner.setAdapter(adapter);

        binding.bottomButton.setOnClickListener(this);
        binding.signUp2.postSearchButton.setOnClickListener(this);

        IncludeSignUp2Binding signUp2Binding = binding.signUp2;
        mCustomEditTexts = new CustomEditText[]{signUp2Binding.idText, signUp2Binding.password1Text, signUp2Binding.password2Text, signUp2Binding.nicText, signUp2Binding.nameText,
                signUp2Binding.englishNameText, signUp2Binding.detailAddressText, signUp2Binding.emailText, signUp2Binding.handPhoneText,
                signUp2Binding.birthText};

//        text = true;
//
//        binding.signUp2.homePhoneText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (text) {
//                    text = false;
//                } else {
//                    String ss = s.toString();
//                    String text = ss.replace("-", "");
//
//                    if (text.length() < 4) {
//                        binding.signUp2.homePhoneText.setText(text);
//                    } else if (text.length() >= 4 && text.length() < 7) {
//                        binding.signUp2.homePhoneText.setText(text.substring(0, 2) + "-" + text.substring(3));
//                    } else if (text.length() >= 7 && text.length() < 11) {
//                        binding.signUp2.homePhoneText.setText(text.substring(0, 2) + "-" + text.substring(3, 5) + "-" + text.substring(6));
//                    } else if (text.length() >= 11) {
//                        binding.signUp2.homePhoneText.setText(text.substring(0, 2) + "-" + text.substring(3, 6) + "-" + text.substring(7));
//                    }
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    public void getHttp(final String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String error = response.getString("RstNo");
                    Log.d("onSuccess: ", response.toString());
                    if (error.equals("0")) {
                        Toast.makeText(SignUpActivity.this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();

                        binding.termsBar.setVisibility(View.INVISIBLE);
                        binding.infoBar.setVisibility(View.INVISIBLE);
                        binding.confirmBar.setVisibility(View.VISIBLE);
                        binding.signUp1.parentLayout.setVisibility(View.INVISIBLE);
                        binding.signUp2.parentLayout.setVisibility(View.INVISIBLE);
                        binding.signUp3.parentLayout.setVisibility(View.VISIBLE);
                        binding.bottomButton.setText("더베이 메인화면으로");

                    } else if (error.equals("-1")) {
                        Toast.makeText(SignUpActivity.this, "입력하신 핸드폰번호가 이미 존재하는 번호 입니다.", Toast.LENGTH_SHORT).show();
                    } else if (error.equals("-2")) {
                        Toast.makeText(SignUpActivity.this, "입력하신 닉네임이 이미 존재하는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                    } else if (error.equals("-3")) {
                        Toast.makeText(SignUpActivity.this, "입력하신 아이디가 이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // 받아온 JSONArray 자료 처리
//                try {
//                    JSONObject object = null;
//                    for (int i = 0; i < response.length(); i++) {
//                        object = response.getJSONObject(i);
//                    }
//                    String error = object.getString("RstNo");
//                    Log.d("onSuccess: ", response.toString());

//                    if (error.equals("0")) {
//                        if ((mPref.getString(CommonLib.authKey, null) != null) || (mPref.getString(CommonLib.memCode, null) != null)) {
//                            mEditor.remove(CommonLib.authKey);
//                            mEditor.remove(CommonLib.memCode);
//                            mEditor.commit();
//                        }
//
//                        mEditor.putString(CommonLib.authKey, object.getString("AuthKey")); //현재 로그인 id값
//                        mEditor.putString(CommonLib.memCode, object.getString("MemCode"));
//                        mEditor.commit();
//
//                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                        finish();
//
//                    } else {
//                        Toast.makeText(SignUpActivity.this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
//                    }
//                    Log.d("onSuccess: ", response.getString(0).toString());
//                } catch (JSONException e) {
//                }
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SignUpActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", responseString);
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (binding.signUp2.parentLayout.getVisibility() == View.VISIBLE) {
            binding.termsBar.setVisibility(View.VISIBLE);
            binding.infoBar.setVisibility(View.INVISIBLE);
            binding.confirmBar.setVisibility(View.INVISIBLE);
            binding.signUp1.parentLayout.setVisibility(View.VISIBLE);
            binding.signUp2.parentLayout.setVisibility(View.INVISIBLE);
            binding.signUp3.parentLayout.setVisibility(View.INVISIBLE);
            binding.bottomButton.setText("다음 페이지");
        } else {
            super.onBackPressed();
        }

//        public InputFilter englishFilte = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
//                if (!ps.matcher(source).matches()) {
//                    return "";  //영어 아닐때 , 숫자 아닐때 토스트
//                }
//                return null;
//            }
    }

    @Override
    public void onClick(View v) {

        String id = binding.signUp2.idText.getText().toString().trim();
        String password1 = binding.signUp2.password1Text.getText().toString().trim();
        String password2 = binding.signUp2.password2Text.getText().toString().trim();
        String nic = binding.signUp2.nicText.getText().toString().trim();
        String name = binding.signUp2.nameText.getText().toString().trim();
        String englishName = binding.signUp2.englishNameText.getText().toString().trim();
        String post = binding.signUp2.postText.getText().toString().trim();
        String address = binding.signUp2.addressText.getText().toString();
        String detailAddress = binding.signUp2.detailAddressText.getText().toString();
        String email = binding.signUp2.emailText.getText().toString().trim();
        String homePhone = binding.signUp2.homePhoneText.getText().toString().trim();
        if (TextUtils.isEmpty(homePhone)) {
            homePhone = "";
        }
        String handPhone = binding.signUp2.handPhoneText.getText().toString().trim();
        String birth = binding.signUp2.birthText.getText().toString().trim();

        String subscriptPath = binding.signUp2.subscriptionPathSpinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(subscriptPath)) {
            subscriptPath = "";
        }
        String recommender = binding.signUp2.recommenderText.getText().toString().trim();
        if (TextUtils.isEmpty(recommender)) {
            recommender = "";
        }

        RequestParams params;
        switch (v.getId()) {

            case R.id.post_search_button:
                FragmentManager fm = getSupportFragmentManager();
                WebviewDialogFragment dialogFragment = new WebviewDialogFragment();
                dialogFragment.show(fm, "address");
//                startActivity(new Intent(this, DialogActivity.class));
                break;

            case R.id.bottom_button:

                if (binding.signUp1.parentLayout.getVisibility() == View.VISIBLE) {
                    if (binding.signUp1.checkbox1.isChecked() && binding.signUp1.checkbox2.isChecked()) {
                        binding.termsBar.setVisibility(View.INVISIBLE);
                        binding.infoBar.setVisibility(View.VISIBLE);
                        binding.confirmBar.setVisibility(View.INVISIBLE);
                        binding.signUp1.parentLayout.setVisibility(View.INVISIBLE);
                        binding.signUp2.parentLayout.setVisibility(View.VISIBLE);
                        binding.signUp3.parentLayout.setVisibility(View.INVISIBLE);
                        binding.bottomButton.setText("회원 가입");
                    } else {
                        Toast.makeText(this, "약관에 동의에 모두 체크 하셔야 가입할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (binding.signUp2.parentLayout.getVisibility() == View.VISIBLE) {

                    Toast.makeText(this, "dddddd", Toast.LENGTH_SHORT).show();

                    boolean isEmptyCheck = false;
                    for (int i = 0; i < mCustomEditTexts.length; i++) {
                        if (mCustomEditTexts[i].emptyCheck() == null) {
                            Toast.makeText(this, "" + mCustomEditTexts[i].getName() + " 을(를) 입력해주세요.", Toast.LENGTH_SHORT).show();
                            i = mCustomEditTexts.length;
                            isEmptyCheck = true;
                        }
                    }

                    if (!isEmptyCheck) {

                        if (TextUtils.isEmpty(post)) {
                            Toast.makeText(this, "우편번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(address)) {
                            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (id.length() < 5 || id.length() > 20) {
                            Toast.makeText(this, "아이디는 5 ~ 20자로만 가능합니다", Toast.LENGTH_SHORT).show();
//                        }else if () {  //아이디 중복확인을 해주세요.
//                            Toast.makeText(this, "아이디 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (password1.length() < 6 || password1.length() > 20) {
                            Toast.makeText(this, "비밀번호는 6 ~ 20자로만 가능합니다", Toast.LENGTH_SHORT).show();
                        } else if (!password1.equals(password2)) {
                            Toast.makeText(this, "비밀번호 재입력 값이 다릅니다..", Toast.LENGTH_SHORT).show();
                        } else if (nic.length() < 3) {
                            Toast.makeText(this, "닉네임은 3자 이상이어야합니다.", Toast.LENGTH_SHORT).show();
                        } else if (!email.contains("@")) {
                            Toast.makeText(this, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else if (handPhone.length() < 10 || handPhone.length() > 11) {
                            Toast.makeText(this, "핸드폰번호 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            params = new RequestParams();
                            switch (binding.signUp2.emailCheck.getCheckedRadioButtonId()) {
                                case R.id.email_check_yes:
                                    params.put("EMAIL_RCV_YN", "Y");
                                    break;
                                case R.id.email_check_no:
                                    params.put("EMAIL_RCV_YN", "N");
                                    break;
                            }

                            switch (binding.signUp2.smsCheck.getCheckedRadioButtonId()) {
                                case R.id.sms_check_yes:
                                    params.put("SMS_RCV_YN", "Y");
                                    break;
                                case R.id.sms_check_no:
                                    params.put("SMS_RCV_YN", "N");
                                    break;
                            }

                            params.put("ID", id);
                            params.put("PASSWORD", password1);
                            params.put("NICKNAME", nic);
                            params.put("MEM_NM_KR", name);
                            params.put("MEM_NM_EN", englishName);
                            params.put("ZIP", post);
                            params.put("ADDR_1", address);
                            params.put("ADDR_2", detailAddress);
                            params.put("MEM_EMAIL", email);
                            params.put("TEL_NO", homePhone);
                            params.put("HP_NO", handPhone);
                            params.put("MEM_BIRTHDAY", birth);
                            params.put("MEM_JOIN_ROUTE", subscriptPath);
                            params.put("MEM_REC_ID", recommender);

                            try {
                                getHttp("Member/MemJoin_I.php", params);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            Toast.makeText(this, "서버로 데이터 보내기", Toast.LENGTH_SHORT).show();
//                            //서버로 데이터 보내기
//                            binding.termsBar.setVisibility(View.INVISIBLE);
//                            binding.infoBar.setVisibility(View.INVISIBLE);
//                            binding.confirmBar.setVisibility(View.VISIBLE);
//                            binding.signUp1.parentLayout.setVisibility(View.INVISIBLE);
//                            binding.signUp2.parentLayout.setVisibility(View.INVISIBLE);
//                            binding.signUp3.parentLayout.setVisibility(View.VISIBLE);
//                            binding.bottomButton.setText("더베이 메인화면으로");
                        }
                    }
                } else if (binding.signUp3.parentLayout.getVisibility() == View.VISIBLE) {

                    // TODO: 2017-11-07 로그인 하고 메인으로 이동

                        if ((mPref.getString(CommonLib.id, null) != null) || (mPref.getString(CommonLib.password, null) != null)) {
                            mEditor.remove(CommonLib.id);
                            mEditor.remove(CommonLib.password);
                            mEditor.commit();
                        }
                        mEditor.putString(CommonLib.id, id); //현재 로그인 id값
                        mEditor.putString(CommonLib.password, password1); //현재 로그인 password값
                        mEditor.commit();
                        params = new RequestParams();

                        String enId = Security.encrypt(id, "EJQPDL@)!&!))DJR").toString();
                        String enPw = Security.encrypt(password1, "EJQPDL@)!&!))DJR").toString();
                        String enTp = Security.encrypt("U", "EJQPDL@)!&!))DJR").toString();

                        params.put("MemId", enId);
                        params.put("MemPw", enPw);
                        params.put("EnterTp", enTp);
                        params.put("AppVer", Build.MODEL);
                        params.put("ModelNo", Build.VERSION.RELEASE);

                        try {
                            loginHttp("Login_M.php", params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                // TODO: 2017-10-27 1. 값 받은거 서버로 넘기는 작업 , 아이디 체크 서버 연동
                break;

//            case R.id.confirm_button:
//                //서버 통신후 로그인
////                String id = binding.signUp2.idText.getText().toString().trim();
////                String password1 = binding.signUp2.password1Text.getText().toString().trim();
//
//                //id랑 password 빈 값 확인
//                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(password)) {
//                    if (TextUtils.isEmpty(id)) {
////                        editTextNullCheck(binding.idTextLayout, binding.passwordTextLayout, "아이디를 입력해주세요.");
//                        Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    } else if (TextUtils.isEmpty(password)) {
//                        Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                    if ((mPref.getString(id, null) != null) || (mPref.getString(password, null) != null)) {
//                        mEditor.remove(id);
//                        mEditor.remove(password);
//                        mEditor.commit();
//                    }
//                    mEditor.putString(id, id); //현재 로그인 id값
//                    mEditor.putString(password, password); //현재 로그인 password값
////                    mEditor.putString("loginPageId", id);
////                    mEditor.putString("loginPagePassword", password);
//                    mEditor.commit();
//                    params = new RequestParams();
//
//                    String enId = Security.encrypt(id, "EJQPDL@)!&!))DJR").toString();
//                    String enPw = Security.encrypt(password, "EJQPDL@)!&!))DJR").toString();
//                    String enTp = Security.encrypt("U", "EJQPDL@)!&!))DJR").toString();
//
//                    params.put("MemId", enId);
//                    params.put("MemPw", enPw);
//                    params.put("EnterTp", enTp);
//                    params.put("AppVer", Build.MODEL);
//                    params.put("ModelNo", Build.VERSION.RELEASE);
//
//                    try {
//                        getHttp("Login_M.php", params);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //로그인
//                break;
        }
    }

    public void loginHttp(final String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // 받아온 JSONArray 자료 처리
                try {
                    JSONObject object = null;
                    for (int i = 0; i < response.length(); i++) {
                        object = response.getJSONObject(i);
                    }
                    String error = object.getString("RstNo");
                    Log.d("onSuccess: ", response.toString());

                    if (error.equals("0")) {
                        if ((mPref.getString(CommonLib.authKey, null) != null) || (mPref.getString(CommonLib.memCode, null) != null)) {
                            mEditor.remove(CommonLib.authKey);
                            mEditor.remove(CommonLib.memCode);
                            mEditor.commit();
                        }

                        mEditor.putString(CommonLib.authKey, object.getString("AuthKey")); //현재 로그인 id값
                        mEditor.putString(CommonLib.memCode, object.getString("MemCode"));
                        mEditor.commit();

                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("onSuccess: ", response.getString(0).toString());
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SignUpActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editTextNullCheck(TextInputLayout nullLayout, TextInputLayout layout, String errorMessage) {
        nullLayout.setError(errorMessage);
        layout.setError(null);
        layout.setErrorEnabled(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (WebviewDialogFragment.sSelectedAddress != null) {
            binding.signUp2.addressText.setText(WebviewDialogFragment.sSelectedAddress);
        }
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
            binding.signUp2.postText.setText(signUpSelectedAddressEvent.getPost());
            binding.signUp2.addressText.setText(signUpSelectedAddressEvent.getKoAddress());
        }
    }
}
