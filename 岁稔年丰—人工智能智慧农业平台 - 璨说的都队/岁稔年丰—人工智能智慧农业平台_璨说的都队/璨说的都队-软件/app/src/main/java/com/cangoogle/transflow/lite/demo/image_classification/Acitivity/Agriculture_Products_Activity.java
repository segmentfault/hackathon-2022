package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_ViewPager_second;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_Viewpager_first;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_Viewpager_fivth;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_Viewpager_fourth;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_Viewpager_sixth;
import com.baidu.paddle.lite.demo.image_classification.local.product.Product_Viewpager_third;


import java.util.ArrayList;
import java.util.List;

public class Agriculture_Products_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private Product_Viewpager_first first;
    private Product_ViewPager_second second;
    private Product_Viewpager_third third;
    private  LinearLayout.LayoutParams lp, lp_normal;
    private Product_Viewpager_fivth fivth;
    private Product_Viewpager_fourth fourth;
    private Product_Viewpager_sixth sixth;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private TextView Text1, Text2, Text3, Text4, Text5, Text6;
    private View imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,back;
    protected TextPaint tp1,tp2,tp3,tp4,tp5,tp6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture_products);

        init_Viewpager();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                change(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        back = (ImageView) this.findViewById(R.id.product_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void change(int x) {

        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, -10, 0, 0);
        lp_normal = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp_normal.setMargins(0, 0, 0, 0);
        tp1 = Text1.getPaint();
        tp2 = Text2.getPaint();
        tp3 = Text3.getPaint();
        tp4 = Text4.getPaint();
        tp5 = Text5.getPaint();
        tp6 = Text6.getPaint();

        if (x == 0) {
            Text1.setTextSize(24);
            Text2.setTextSize(20);
            Text3.setTextSize(20);
            Text4.setTextSize(20);
            Text5.setTextSize(20);
            Text6.setTextSize(20);
            tp1.setFakeBoldText(true);
            tp2.setFakeBoldText(false);
            tp3.setFakeBoldText(false);
            tp4.setFakeBoldText(false);
            tp5.setFakeBoldText(false);
            tp6.setFakeBoldText(false);
            Text1.setLayoutParams(lp);
            Text2.setLayoutParams(lp_normal);
            Text3.setLayoutParams(lp_normal);
            Text4.setLayoutParams(lp_normal);
            Text5.setLayoutParams(lp_normal);
            Text6.setLayoutParams(lp_normal);
            Text1.setTextColor(Color.parseColor("#44EAB3"));
            Text2.setTextColor(Color.parseColor("#000000"));
            Text3.setTextColor(Color.parseColor("#000000"));
            Text4.setTextColor(Color.parseColor("#000000"));
            Text5.setTextColor(Color.parseColor("#000000"));
            Text6.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            imageView5.setVisibility(View.INVISIBLE);
            imageView6.setVisibility(View.INVISIBLE);
        } else if (x == 1) {
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(true);
            tp3.setFakeBoldText(false);
            tp4.setFakeBoldText(false);
            tp5.setFakeBoldText(false);
            tp6.setFakeBoldText(false);
            Text1.setTextSize(20);
            Text2.setTextSize(24);
            Text3.setTextSize(20);
            Text4.setTextSize(20);
            Text5.setTextSize(20);
            Text6.setTextSize(20);
            Text1.setLayoutParams(lp_normal);
            Text2.setLayoutParams(lp);
            Text3.setLayoutParams(lp_normal);
            Text4.setLayoutParams(lp_normal);
            Text5.setLayoutParams(lp_normal);
            Text6.setLayoutParams(lp_normal);
            Text1.setTextColor(Color.parseColor("#000000"));
            Text2.setTextColor(Color.parseColor("#44EAB3"));
            Text3.setTextColor(Color.parseColor("#000000"));
            Text4.setTextColor(Color.parseColor("#000000"));
            Text5.setTextColor(Color.parseColor("#000000"));
            Text6.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            imageView5.setVisibility(View.INVISIBLE);
            imageView6.setVisibility(View.INVISIBLE);
        } else if(x == 2) {
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(false);
            tp3.setFakeBoldText(true);
            tp4.setFakeBoldText(false);
            tp5.setFakeBoldText(false);
            tp6.setFakeBoldText(false);
            Text1.setLayoutParams(lp_normal);
            Text2.setLayoutParams(lp_normal);
            Text3.setLayoutParams(lp);
            Text4.setLayoutParams(lp_normal);
            Text5.setLayoutParams(lp_normal);
            Text6.setLayoutParams(lp_normal);
            Text1.setTextSize(20);
            Text2.setTextSize(20);
            Text3.setTextSize(24);
            Text4.setTextSize(20);
            Text5.setTextSize(20);
            Text6.setTextSize(20);
            Text1.setTextColor(Color.parseColor("#000000"));
            Text2.setTextColor(Color.parseColor("#000000"));
            Text3.setTextColor(Color.parseColor("#44EAB3"));
            Text4.setTextColor(Color.parseColor("#000000"));
            Text5.setTextColor(Color.parseColor("#000000"));
            Text6.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            imageView5.setVisibility(View.INVISIBLE);
            imageView6.setVisibility(View.INVISIBLE);
        }else if(x == 3) {
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(false);
            tp3.setFakeBoldText(false);
            tp4.setFakeBoldText(true);
            tp5.setFakeBoldText(false);
            tp6.setFakeBoldText(false);
            Text1.setTextSize(20);
            Text2.setTextSize(20);
            Text3.setTextSize(20);
            Text4.setTextSize(24);
            Text5.setTextSize(20);
            Text6.setTextSize(20);
            Text1.setLayoutParams(lp_normal);
            Text2.setLayoutParams(lp_normal);
            Text3.setLayoutParams(lp_normal);
            Text4.setLayoutParams(lp);
            Text5.setLayoutParams(lp_normal);
            Text6.setLayoutParams(lp_normal);
            Text1.setTextColor(Color.parseColor("#000000"));
            Text2.setTextColor(Color.parseColor("#000000"));
            Text3.setTextColor(Color.parseColor("#000000"));
            Text4.setTextColor(Color.parseColor("#44EAB3"));
            Text5.setTextColor(Color.parseColor("#000000"));
            Text6.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.VISIBLE);
            imageView5.setVisibility(View.INVISIBLE);
            imageView6.setVisibility(View.INVISIBLE);
        }else if(x == 4){
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(false);
            tp3.setFakeBoldText(false);
            tp4.setFakeBoldText(false);
            tp5.setFakeBoldText(true);
            tp6.setFakeBoldText(false);
            Text1.setTextSize(20);
            Text2.setTextSize(20);
            Text3.setTextSize(20);
            Text4.setTextSize(20);
            Text5.setTextSize(24);
            Text6.setTextSize(20);
            Text1.setLayoutParams(lp_normal);
            Text2.setLayoutParams(lp_normal);
            Text3.setLayoutParams(lp_normal);
            Text4.setLayoutParams(lp_normal);
            Text5.setLayoutParams(lp);
            Text6.setLayoutParams(lp_normal);
            Text1.setTextColor(Color.parseColor("#000000"));
            Text2.setTextColor(Color.parseColor("#000000"));
            Text3.setTextColor(Color.parseColor("#000000"));
            Text4.setTextColor(Color.parseColor("#000000"));
            Text5.setTextColor(Color.parseColor("#44EAB3"));
            Text6.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            imageView5.setVisibility(View.VISIBLE);
            imageView6.setVisibility(View.INVISIBLE);
        }else {
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(false);
            tp3.setFakeBoldText(false);
            tp4.setFakeBoldText(false);
            tp5.setFakeBoldText(false);
            tp6.setFakeBoldText(true);
            Text1.setTextSize(20);
            Text2.setTextSize(20);
            Text3.setTextSize(20);
            Text4.setTextSize(20);
            Text5.setTextSize(20);
            Text6.setTextSize(24);
            Text1.setLayoutParams(lp_normal);
            Text2.setLayoutParams(lp_normal);
            Text3.setLayoutParams(lp_normal);
            Text4.setLayoutParams(lp_normal);
            Text5.setLayoutParams(lp_normal);
            Text6.setLayoutParams(lp);
            Text1.setTextColor(Color.parseColor("#000000"));
            Text2.setTextColor(Color.parseColor("#000000"));
            Text3.setTextColor(Color.parseColor("#000000"));
            Text4.setTextColor(Color.parseColor("#000000"));
            Text5.setTextColor(Color.parseColor("#000000"));
            Text6.setTextColor(Color.parseColor("#44EAB3"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
            imageView5.setVisibility(View.INVISIBLE);
            imageView6.setVisibility(View.VISIBLE);
        }
    }

    private void init_Viewpager() {
        //获取ViewPager
        viewPager = (ViewPager) this.findViewById(R.id.product_viewpager);

        first = new Product_Viewpager_first();
        second = new Product_ViewPager_second();
        third = new Product_Viewpager_third();
        fourth = new Product_Viewpager_fourth();
        fivth = new Product_Viewpager_fivth();
        sixth = new Product_Viewpager_sixth();

        Text1 = (TextView) this.findViewById(R.id.products_local_text_1);
        Text2 = (TextView) this.findViewById(R.id.products_local_text_2);
        Text3 = (TextView) this.findViewById(R.id.products_local_text_3);
        Text4 = (TextView) this.findViewById(R.id.products_local_text_4);
        Text5 = (TextView) this.findViewById(R.id.products_local_text_5);
        Text6 = (TextView) this.findViewById(R.id.products_text_6);

        Text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        Text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        Text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        Text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
        Text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });
        Text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(5);
            }
        });

        imageView1 = (View) this.findViewById(R.id.products_local_images_1);
        imageView2 = (View) this.findViewById(R.id.products_local_images_2);
        imageView3 = (View) this.findViewById(R.id.products_local_images_3);
        imageView4 = (View) this.findViewById(R.id.products_local_images_4);
        imageView5 = (View) this.findViewById(R.id.products_local_images_5);
        imageView6 = (View) this.findViewById(R.id.products_local_images_6);

        mFragments.add(first);
        mFragments.add(second);
        mFragments.add(third);
        mFragments.add(fourth);
        mFragments.add(fivth);
        mFragments.add(sixth);

        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(fragmentAdapter);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> MyFragmentList = new ArrayList<>();

        public FragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.MyFragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return MyFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return MyFragmentList.size();
        }
    }
}
