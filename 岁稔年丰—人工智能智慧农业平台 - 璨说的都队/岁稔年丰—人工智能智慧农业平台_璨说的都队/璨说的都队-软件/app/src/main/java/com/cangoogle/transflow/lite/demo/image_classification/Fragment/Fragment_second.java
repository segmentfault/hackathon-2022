package com.baidu.paddle.lite.demo.image_classification.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Craft_Activity;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Craft.Craft_first_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Fragment_viewpager_second.Fragment_viewpager_second_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_first_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Fragment_viewpager_second_bean_up_item;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Fragment_frist_s;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Fragment_second_s;

import java.util.ArrayList;
import java.util.List;

public class Fragment_second extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private Fragment_frist_s fragment_frist_s;
    protected Fragment_second_s fragment_second_s;
    protected List<Fragment> fragments = new ArrayList<>();
    private TextView textView1, textView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second,container,false);
        return view;
    }


    public void initViewPage(){
        viewPager = (ViewPager) getView().findViewById(R.id.fragment_second_viewpager_1);
        textView1 = (TextView) getView().findViewById(R.id.ShowWeatherCardTextView_one);
        textView2 = (TextView) getView().findViewById(R.id.ShowWeatherCardTextView_two);



        fragment_frist_s = new Fragment_frist_s();
        fragment_second_s = new Fragment_second_s();

        fragments.add(fragment_frist_s);
        fragments.add(fragment_second_s);

        fragmentPagerAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(fragmentPagerAdapter);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                textView1.setTextColor(Color.parseColor("#000000"));
                textView1.setTextSize(23);
                textView2.setTextColor(Color.parseColor("#888888"));
                textView2.setTextSize(21);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                textView2.setTextColor(Color.parseColor("#000000"));
                textView2.setTextSize(23);
                textView1.setTextColor(Color.parseColor("#888888"));
                textView1.setTextSize(21);
            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0){
                    textView1.setTextColor(Color.parseColor("#000000"));
                    textView1.setTextSize(23);
                    textView2.setTextColor(Color.parseColor("#888888"));
                    textView2.setTextSize(21);
                }else {
                    textView2.setTextColor(Color.parseColor("#000000"));
                    textView2.setTextSize(23);
                    textView1.setTextColor(Color.parseColor("#888888"));
                    textView1.setTextSize(21);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ShowWeatherCardView_one:
//                viewPager.setCurrentItem(0, true);
//                break;
//            case R.id.ShowWeatherCardView_two:
//                viewPager.setCurrentItem(1, true);
//                break;
//            default:
//                break;
//        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPage();
        viewPager.setCurrentItem(1);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public FragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragments = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }



}

