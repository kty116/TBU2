package com.example.kyoungae.myapplication.webview;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.WebviewDialogBinding;
import com.example.kyoungae.myapplication.event.SignUpSelectedAddressEvent;

import org.greenrobot.eventbus.EventBus;

public class WebviewDialogFragment extends DialogFragment {


    private WebviewDialogBinding binding;
    public static String sSelectedAddress;
//                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    public static WebviewDialogFragment newInstance() {
        WebviewDialogFragment fragment = new WebviewDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_dialog, container, false);
        binding = DataBindingUtil.bind(view);
        init_webView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void init_webView() {

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
        binding.webView.addJavascriptInterface(new AndroidBridge(), "AddrGet");
        // web client 를 chrome 으로 설정
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setWebViewClient(new WebClient());
        // webview url load
        binding.webView.loadUrl("http://oldapp.yingai.co.kr/Member/AddrGet_S.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3, final String arg4) {

//            EventBus.getDefault().post(new SignUpSelectedAddressEvent(arg1, arg2 + ", " + arg3, arg4));
            Log.d("setAddress: ", arg1 + arg2 + arg3 + arg4);
            getActivity().getSupportFragmentManager().beginTransaction().remove(WebviewDialogFragment.this).commit();
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
