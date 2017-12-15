package com.example.kyoungae.myapplication.webview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.ActivityWebViewBinding;


public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.getStringExtra("title") != null) {
            String title = getIntent().getStringExtra("title");
            setTitle(title);
        }else {
            setTitle("오류");
        }

        binding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings webSettings = binding.webView.getSettings();

        webSettings.setSaveFormData(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
//
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        binding.webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        // web client 를 chrome 으로 설정
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setWebViewClient(new WebClient());
        if (intent.getStringExtra("url") != null) {
            String url = getIntent().getStringExtra("url");
            binding.webView.loadUrl(url);
        }else {
            Toast.makeText(this, "네트워크를 연결을 확인하세요.", Toast.LENGTH_LONG).show();
        }
    }

    class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.progressBar.setVisibility(View.GONE);

        }
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

        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.webView.canGoBack()) {
            binding.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        if (event instanceof ProgressBarEvent) {
//            ProgressBarEvent progressBarEvent = (ProgressBarEvent) event;
//            boolean progressbarShown = progressBarEvent.isShow();
//            if (progressbarShown) {
//                binding.progressBar.setVisibility(View.VISIBLE);
//            } else {
//                binding.progressBar.setVisibility(View.GONE);
//            }
//        }
//        if (event instanceof FragmentReplaceEvent) {
//            FragmentReplaceEvent fragmentReplaceEvent = (FragmentReplaceEvent) event;
//            if (fragmentReplaceEvent.isToolbarHide()) {
//                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//                Toast.makeText(this, "숨김", Toast.LENGTH_SHORT).show();
//            } else {
//                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//                params.setScrollFlags(0);
//                Toast.makeText(this, "보임", Toast.LENGTH_SHORT).show();
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentReplaceEvent.getFragment()).addToBackStack("null").commit();
//            setTitle(fragmentReplaceEvent.getFragmentName());
//
//        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//        String url = getIntent().getStringExtra("url");
//        if (url != null) {
//            EventBus.getDefault().post(new WebviewUrlEvent(url));
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
}
