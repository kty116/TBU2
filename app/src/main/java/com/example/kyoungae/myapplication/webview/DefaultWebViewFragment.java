package com.example.kyoungae.myapplication.webview;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.FragmentWebviewBinding;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.WebviewUrlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import static org.greenrobot.eventbus.EventBus.TAG;


public class DefaultWebViewFragment extends Fragment implements Serializable{
    private FragmentWebviewBinding binding;
//    private static final String ARG_PARAM1 = "toolbar_title";
//    private static final String ARG_PARAM2 = "url";

    private static String mToolbarTitle;
    private static String mUrl;

    public static DefaultWebViewFragment newInstance(String toolbarTitle, String url) {
        DefaultWebViewFragment fragment = new DefaultWebViewFragment();
        mToolbarTitle = toolbarTitle;
        mUrl = url;

        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mToolbarTitle = getArguments().getString(ARG_PARAM1);
//            mUrl = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        binding = DataBindingUtil.bind(view);
        getActivity().setTitle(mToolbarTitle);

        Log.d(TAG, "onCreateView: "+mUrl);

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
//        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setWebViewClient(new WebClient());
        // webview url load
        if (mToolbarTitle != null) {
            binding.webView.loadUrl(mUrl);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event instanceof WebviewUrlEvent) {
            WebviewUrlEvent webviewUrlEvent = (WebviewUrlEvent) event;

            binding.webView.loadUrl(webviewUrlEvent.getUrl());
            Log.d(TAG, "onMessageEvent: " + webviewUrlEvent.getUrl());

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
}
