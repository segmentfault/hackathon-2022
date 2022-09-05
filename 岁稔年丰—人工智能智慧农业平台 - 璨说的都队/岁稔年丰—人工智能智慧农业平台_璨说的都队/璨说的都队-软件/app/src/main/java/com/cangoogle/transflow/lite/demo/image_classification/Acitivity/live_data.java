package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.baidu.paddle.lite.demo.image_classification.R;

import cn.jzvd.JzvdStd;

public class live_data extends AppCompatActivity {
    protected String title  = "https://www.twoyl.cn/video/QQ%E5%BD%95%E5%B1%8F20210518211149.mp4";
    protected WebView webView;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_two);
        back = findViewById(R.id.live_two_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.videoplayer);
        jzvdStd.setUp("https://www.twoyl.cn/video/QQ%E5%BD%95%E5%B1%8F20210518211149.mp4"
                , "基于乡村振兴战略的农业直播");

        jzvdStd.posterImageView.setImageResource(R.drawable.nyzba);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return true;
    }


}
