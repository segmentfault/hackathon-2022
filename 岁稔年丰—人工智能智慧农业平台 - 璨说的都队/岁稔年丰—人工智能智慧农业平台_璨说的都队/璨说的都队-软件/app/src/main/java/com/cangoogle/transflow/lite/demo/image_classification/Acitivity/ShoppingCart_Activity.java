package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.Shopping.ShoppingViewpage1;
import com.baidu.paddle.lite.demo.image_classification.View.Shopping.refundViewpage;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView textView,textView1;
    private ImageView imageView,imageView1;
    protected TextPaint tp1,tp2;
    private ShoppingViewpage1 first;
    private refundViewpage second;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);

        InitImageView();
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                change(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(0);
//            }
//        });
//
//        textView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(1);
//            }
//        });

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

    private void InitImageView() {
        textView = (TextView)this.findViewById(R.id.shop_bar_text_first);
//        textView1 = (TextView) this.findViewById(R.id.shop_bar_text_second);
        imageView = (ImageView) this.findViewById(R.id.shop_bar_image_first);
//        imageView1 = (ImageView) this.findViewById(R.id.shop_bar_image_second);

        //获取ViewPager
        viewPager = (ViewPager) this.findViewById(R.id.shopping_viewpager);

        first = new ShoppingViewpage1();
        second = new refundViewpage();
        mFragments.add(first);
//        mFragments.add(second);
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragments);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(fragmentAdapter);

    }


    private void change(int x) {


        tp1 = textView.getPaint();
        tp2 = textView1.getPaint();

        if (x == 0) {
            textView.setTextSize(20);
            textView1.setTextSize(16);
            tp1.setFakeBoldText(true);
            tp2.setFakeBoldText(false);
            textView.setTextColor(Color.parseColor("#000000"));
            textView1.setTextColor(Color.parseColor("#cccccc"));
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
        } else {
            textView.setTextSize(16);
            textView1.setTextSize(20);
            tp1.setFakeBoldText(false);
            tp2.setFakeBoldText(true);
            textView.setTextColor(Color.parseColor("#cccccc"));
            textView1.setTextColor(Color.parseColor("#000000"));
            imageView.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
        }
    }


}
