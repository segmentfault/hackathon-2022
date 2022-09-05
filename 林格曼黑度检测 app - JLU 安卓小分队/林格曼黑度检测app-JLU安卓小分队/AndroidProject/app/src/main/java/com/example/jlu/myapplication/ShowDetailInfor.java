package com.example.jlu.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowDetailInfor extends AppCompatActivity {

    com.example.smokedetector.CircleProgressBar cpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_show_detail_infor);
        Gson gson = new Gson();
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Smoke item = gson.fromJson(data, Smoke.class);
        ImageView photo = (ImageView) findViewById(R.id.img);
        TextView text = (TextView) findViewById(R.id.txt);
        photo.setImageURI(Uri.parse("file://" + item.url));

        String[] split = item.level.split(" ");
        int level = Integer.valueOf(split[split.length - 1]).intValue(); //返回的烟雾污染等级的值
        ArrayList<Integer> arrColor = new ArrayList<Integer>();
        arrColor.add(Color.WHITE);
        arrColor.add(Color.rgb(207, 207, 207));
        arrColor.add(Color.rgb(156, 156, 156));
        arrColor.add(Color.rgb(130, 130, 130));
        arrColor.add(Color.rgb(79, 79, 79));
        arrColor.add(Color.rgb(28, 28, 28));
        ArrayList<Float> arrFloat = new ArrayList<Float>();
        arrFloat.add(0f);
        arrFloat.add(20f);
        arrFloat.add(40f);
        arrFloat.add(60f);
        arrFloat.add(80f);
        arrFloat.add(100f);
        ArrayList<String> arrString = new ArrayList<String>();
        arrString.add("0级");
        arrString.add("1级");
        arrString.add("2级");
        arrString.add("3级");
        arrString.add("4级");
        arrString.add("5级");
        cpb = (com.example.smokedetector.CircleProgressBar)findViewById(R.id.cpb);
        cpb.setCircleColor(arrColor.get(level));
        cpb.setCurrentProgress(arrFloat.get(level));
        cpb.setText(true, arrString.get(level));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}