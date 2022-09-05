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


import com.baidu.paddle.lite.demo.image_classification.Fragment.Agritainment_Fragment.Agritainment_Fragment_Second;
import com.baidu.paddle.lite.demo.image_classification.HappyFarm.HappyFarmLifeFragment;
import com.baidu.paddle.lite.demo.image_classification.R;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Agritainment_Activity extends AppCompatActivity {
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private ViewPager viewPager;
    private HappyFarmLifeFragment first;
    private Agritainment_Fragment_Second second;
    private TextView textView,textView1;
    private ImageView imageView,imageView1;
    protected TextPaint tp1,tp2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture);

        InitImageView();

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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }


    private void InitImageView() {
        textView = (TextView)this.findViewById(R.id.agr_bar_text_first);
        textView1 = (TextView) this.findViewById(R.id.agr_bar_text_second);
        imageView = (ImageView) this.findViewById(R.id.agr_bar_image_first);
        imageView1 = (ImageView) this.findViewById(R.id.agr_bar_image_second);

        //获取ViewPager
        viewPager = (ViewPager) this.findViewById(R.id.agriculture_viewpager);

        first = new HappyFarmLifeFragment();
        second = new Agritainment_Fragment_Second();
        mFragments.add(first);
        mFragments.add(second);
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
