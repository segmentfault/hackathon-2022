package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.graphics.Color;
import android.os.Bundle;
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
import com.baidu.paddle.lite.demo.image_classification.local.Local_ViewPager_first;
import com.baidu.paddle.lite.demo.image_classification.local.Local_ViewPager_second;
import com.baidu.paddle.lite.demo.image_classification.local.Local_ViewPager_third;


import java.util.ArrayList;
import java.util.List;

public class Characteristic_local_Activity extends AppCompatActivity implements View.OnClickListener{
    String[] mTitles = new String[]{
            "亲子教育", "青年体验", "老年回忆"
    };
    private ViewPager viewPager;
    private Local_ViewPager_first local_viewPager_first;
    private Local_ViewPager_second local_viewPager_second;
    private Local_ViewPager_third local_viewPager_third;
    private TextView TextExperience, TextExpert, TextBroad;
    private ImageView imageView1,imageView2,imageView3,back;
    private FragmentAdapter fragmentAdapter;
    private LinearLayout buttonBroad, buttonExperience, buttonExpert;
    private List<Fragment> mFragments = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_local);

        initViewpager();

        back = (ImageView) this.findViewById(R.id.local_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_text_1:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.local_text_2:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.local_text_3:
                viewPager.setCurrentItem(3, true);
                break;
            default:
                break;
        }
    }

    private void initViewpager() {


        TextExperience = (TextView) this.findViewById(R.id.local_text_1);
        TextBroad = (TextView) this.findViewById(R.id.local_text_2);
        TextExpert = (TextView) this.findViewById(R.id.local_text_3);
        TextExperience.setText(mTitles[0]);
        TextBroad.setText(mTitles[2]);
        TextExpert.setText(mTitles[1]);
        imageView1 = (ImageView) this.findViewById(R.id.local_images_1);
        imageView2 = (ImageView) this.findViewById(R.id.local_images_2);
        imageView3 = (ImageView) this.findViewById(R.id.local_images_3);

        TextExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3,true);
            }
        });

        TextExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });

        TextBroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });
        //获取ViewPager
        viewPager = (ViewPager) this.findViewById(R.id.local_Viewpager);

        local_viewPager_first = new Local_ViewPager_first();
        local_viewPager_second = new Local_ViewPager_second();
        local_viewPager_third = new Local_ViewPager_third();

        // 把这三个加入展示列表
        mFragments.add(local_viewPager_first);
        mFragments.add(local_viewPager_second);
        mFragments.add(local_viewPager_third);

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


    private void change(int x) {
        if (x == 0) {
            TextExperience.setTextSize(22);
            TextExpert.setTextSize(19);
            TextBroad.setTextSize(19);
            TextExperience.setTextColor(Color.parseColor("#000000"));
            TextExpert.setTextColor(Color.parseColor("#888888"));
            TextBroad.setTextColor(Color.parseColor("#888888"));
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
        } else if (x == 2) {

            TextExperience.setTextSize(19);
            TextExpert.setTextSize(22);
            TextBroad.setTextSize(19);
            TextExperience.setTextColor(Color.parseColor("#888888"));
            TextExpert.setTextColor(Color.parseColor("#000000"));
            TextBroad.setTextColor(Color.parseColor("#888888"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.VISIBLE);
        } else {
            TextExperience.setTextSize(19);
            TextExpert.setTextSize(19);
            TextBroad.setTextSize(22);
            TextExperience.setTextColor(Color.parseColor("#888888"));
            TextExpert.setTextColor(Color.parseColor("#888888"));
            TextBroad.setTextColor(Color.parseColor("#000000"));
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
        }
    }

}
