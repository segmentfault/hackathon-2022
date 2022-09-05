package com.animee.health.guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.animee.health.HomeMenuActivity;
import com.animee.health.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager guideVp;
    TextView tv1,tv2,tv3;
    Button guideBtn;
    List<View>viewList;  //ViewPager的数据源
    List<TextView>numList;  //表示页码的集合
    int resId[] = {R.mipmap.pic1,R.mipmap.pic2,R.mipmap.pic3};  //所显示的图片资源数组
    private GuideAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guideVp = findViewById(R.id.guide_vp);
        tv1 = findViewById(R.id.guide_tv1);
        tv2 = findViewById(R.id.guide_tv2);
        tv3 = findViewById(R.id.guide_tv3);
        guideBtn = findViewById(R.id.guide_btn);
        guideBtn.setOnClickListener(this);  //设置按钮的监听器
        viewList = new ArrayList<>();
        numList = new ArrayList<>();
        numList.add(tv1);
        numList.add(tv2);
        numList.add(tv3);
//        初始化ViewPager的页面资源
        for (int i = 0; i < resId.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_guide1,null);
            view.setBackgroundResource(resId[i]);
            viewList.add(view);
        }
//        创建适配器对象
        adapter = new GuideAdapter(viewList);
//        设置适配器
        guideVp.setAdapter(adapter);
        tv1.setTextColor(Color.RED);
//        设置ViewPager的监听
        setVPListener();
    }

    private void setVPListener() {
        guideVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < numList.size(); i++) {
                    numList.get(i).setTextColor(Color.WHITE);
                }
                numList.get(position).setTextColor(Color.RED);
//                在进入到第三个页面时，立即进入的按钮就显示出来，否则不显示
                if (position == 2) {
                    guideBtn.setVisibility(View.VISIBLE);
                }else {
                    guideBtn.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_btn:
                Intent intent = new Intent(GuideActivity.this, HomeMenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
