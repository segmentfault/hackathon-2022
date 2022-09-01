package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class TianQiActivity extends AppCompatActivity implements View.OnClickListener {
        WebView webView;
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.tianqi);
            webView=findViewById(R.id.pppp);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.clearCache(true);
            loadWeb();
        }

        @Override
        public void onClick(View view) {
            Uri uri= Uri.parse("");
            Intent i=new Intent(Intent.ACTION_VIEW,uri);
            this.startActivity(i);
        }
        public void loadWeb(){
            String url = "https://widget-page.qweather.net/h5/index.html?md=0123456&bg=1&lc=accu&key=3c1542a415594daeaaf9229afdb678b1&v=_1628240822121";
            //此方法可以在webview中打开链接而不会跳转到外部浏览器
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
            if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
                webView.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }
