package com.example.kyoungae.myapplication.splash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.Security;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.ActivitySplashBinding;
import com.example.kyoungae.myapplication.dto.MemberDTO;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.main.MainActivity;
import com.example.kyoungae.myapplication.sign_up.SignUpActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectivityManager mManager;
    private NetworkInfo mMobile;
    private NetworkInfo mWifi;
    private ActivitySplashBinding binding;
    private Handler mHandler;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private MemberDTO mMemberDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPref.edit();

        String id = mPref.getString(CommonLib.id, null);
        String password = mPref.getString(CommonLib.password, null);

        mMemberDTO = new MemberDTO(id, password);

        mManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mMobile = mManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mWifi = mManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        binding.loginButton.setOnClickListener(this);
        binding.signUpButton.setOnClickListener(this);

        if (mWifi.isConnected() || mMobile.isConnected()) {  //인터넷 연결 됐을때
            idCheck();
//            permissionCheck();

        } else {
            //인터넷 연결 안됐을때
            noNetwork();
        }
    }

//    private void permissionCheck() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //마시멜로우 이상인지 체크
//
//            int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//            int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
//
//
//            if (permissionCheck1 == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED ) {
//                permissionSetting();
//            } else {
//                //퍼미션값 다 있으면
//                splashThread();
//            }
//        } else {
    //마시멜로우 미만
    //퍼미션 체크 x
//        }
//    }

    private void noNetwork() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SplashActivity.this);
        alert_confirm.setMessage("인터넷 연결 확인 후 다시 시도해주세요.").setCancelable(false).setPositiveButton("재접속",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMobile = mManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                        mWifi = mManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected() || mMobile.isConnected()) {
                            idCheck();
                        } else {
                            noNetwork();
                        }
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

//    public void permissionSetting() {
//
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                idCheck();
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(SplashActivity.this, "권한을 거부하시면 해당 서비스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        };
//
//        new TedPermission(this)
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("사용 권한을 거부하는 경우 이 서비스를 사용할 수 없습니다.\n\n사용 권한을 설정하십시오.[설정] > [사용 권한]")
//                .setPermissions(Manifest.permission.READ_PHONE_STATE)
//                .check();
//    }

    public void idCheck() {

        if (mMemberDTO.getId() == null) {

            //널이면 아이디랑 비밀번호 값 없음

            splashThread(LoginActivity.class, true);

        } else {
            //널 아니면 아이디랑 비밀번호 값 있음
            RequestParams params = new RequestParams();

            String enId = Security.encrypt(mMemberDTO.getId(), "EJQPDL@)!&!))DJR").toString();
            String enPw = Security.encrypt(mMemberDTO.getPassword(), "EJQPDL@)!&!))DJR").toString();
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
    }

    public void splashThread(final Class aClass, final boolean isButtonVisible) {

        mHandler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isButtonVisible) {
                            binding.loginButton.setVisibility(View.VISIBLE);
                            binding.signUpButton.setVisibility(View.VISIBLE);
                        } else {
                            startActivity(new Intent(getApplicationContext(), aClass));
                            finish();
                        }
                    }
                });
            }
        });
        thread.start();
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
                    Log.d("onSuccess: ", response.toString());

                    if (error.equals("0")) {

                        if ((mPref.getString(CommonLib.authKey, null) != null) || (mPref.getString(CommonLib.memCode, null) != null)) {
                            mEditor.remove(CommonLib.authKey);
                            mEditor.remove(CommonLib.memCode);
                            mEditor.commit();
                        }

                        mEditor.putString(CommonLib.authKey, object.getString("AuthKey"));
                        mEditor.putString(CommonLib.memCode, object.getString("MemCode"));
                        mEditor.commit();

                        splashThread(MainActivity.class, false);
                        Toast.makeText(SplashActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SplashActivity.this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SplashActivity.this, "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.sign_up_button:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
                break;
        }
    }
}
