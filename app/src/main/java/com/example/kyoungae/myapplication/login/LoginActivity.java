package com.example.kyoungae.myapplication.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.kyoungae.myapplication.main.MainActivity;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.Security;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivityLoginBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.sign_up.SignUpActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private String mLanguageValue;
    private ActivityLoginBinding binding;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static KeyDTO sKeyDTO = null;
    public static String mMemCode;
    public static String mAuthKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.confirmButton.setOnClickListener(this);
        binding.signUpButton.setOnClickListener(this);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPref.edit();

        String id = mPref.getString(CommonLib.id, null);
        binding.idText.setText(id);
    }

    public void getHttp(final String relativeUrl, RequestParams params) throws JSONException {

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

                    if (error.equals("0")) {
                        if ((mPref.getString(CommonLib.authKey, null) != null) || (mPref.getString(CommonLib.memCode, null) != null)) {
                            mEditor.remove(CommonLib.authKey);
                            mEditor.remove(CommonLib.memCode);
                            mEditor.commit();
                        }

                        mEditor.putString(CommonLib.authKey, object.getString("AuthKey")); //현재 로그인 id값
                        mEditor.putString(CommonLib.memCode, object.getString("MemCode"));
                        mEditor.commit();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_up_button:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.confirm_button:
                //서버 통신후 로그인

                String id = binding.idText.getText().toString();
                String password = binding.passwordText.getText().toString();

                //id랑 password 빈 값 확인
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(password)) {
                    if (TextUtils.isEmpty(id)) {
//                        editTextNullCheck(binding.idTextLayout, binding.passwordTextLayout, "아이디를 입력해주세요.");
                        Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if ((mPref.getString(CommonLib.id, null) != null) || (mPref.getString(CommonLib.password, null) != null)) {
                        mEditor.remove(CommonLib.id);
                        mEditor.remove(CommonLib.password);
                        mEditor.commit();
                    }
                    mEditor.putString(CommonLib.id, id); //현재 로그인 id값
                    mEditor.putString(CommonLib.password, password); //현재 로그인 password값
                    mEditor.commit();
                    RequestParams params = new RequestParams();

                    String enId = Security.encrypt(id, "EJQPDL@)!&!))DJR").toString();
                    String enPw = Security.encrypt(password, "EJQPDL@)!&!))DJR").toString();
                    String enTp = Security.encrypt("U", "EJQPDL@)!&!))DJR").toString();

                    params.put("MemId", enId);
                    params.put("MemPw", enPw);
                    params.put("EnterTp", enTp);
                    params.put("AppVer", Build.MODEL);
                    params.put("ModelNo", Build.VERSION.RELEASE);

                    try {
                        getHttp("Login_M.php", params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //로그인
                break;
        }
    }

    public void editTextNullCheck(TextInputLayout nullLayout, TextInputLayout layout, String errorMessage) {
        nullLayout.setError(errorMessage);
        layout.setError(null);
        layout.setErrorEnabled(false);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
}
