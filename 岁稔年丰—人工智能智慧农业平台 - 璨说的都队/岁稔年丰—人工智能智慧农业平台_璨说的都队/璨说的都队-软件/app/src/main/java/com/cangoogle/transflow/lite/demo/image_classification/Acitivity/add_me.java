package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.paddle.lite.demo.image_classification.R;


public class add_me extends AppCompatActivity {

    private static final String location="http://wpa.qq.com/msgrd?v=3&uin=1554366868&site=qq&menu=yes";
    private  WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_me);
        webView=(WebView)findViewById(R.id.webview);
        webView.loadUrl(location);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

    }
}