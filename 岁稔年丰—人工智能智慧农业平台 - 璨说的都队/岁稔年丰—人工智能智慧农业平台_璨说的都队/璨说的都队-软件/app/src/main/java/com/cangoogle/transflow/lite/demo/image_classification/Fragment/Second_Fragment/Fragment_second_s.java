package com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Fragment_viewpager_second.Fragment_viewpager_second_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Fragment_viewpager_second_bean_up_item;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Fragment_second;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment.Fragment_live;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment.Fragment_product;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment.Fragment_recommend;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment.Fragment_video;
import com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment.Fragment_village;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_second_s extends Fragment {
    private ViewPager viewPager_down;
    private FragmentPagerAdapter fragmentPagerAdapter_down;
    private Fragment_recommend fragment_recommend;
//    private Fragment_product fragment_product;
//    private Fragment_live fragment_live;
//    private Fragment_video fragment_video;
//    private Fragment_village fragment_village;
    private List<Fragment> fragments_down = new ArrayList<>();
    protected Fragment_viewpager_second_Adapter fragment_viewpager_second_adapter;
    public static List<Fragment_viewpager_second_bean_up_item> list;
    protected RecyclerView recyclerView;
    public static int Person_num[];
    public static List<String> persons_num_str;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second_viewpager_2,container,false);
        return view;
    }


    public void init_fragment_second_up_item() {
        list = new ArrayList<>();
        Person_num = new int[1000];
        Person_num[0] = 1000003;
        Person_num[1] = 1000004;
        recyclerView =  (RecyclerView)  getView().findViewById(R.id.fragment_second_viewpager_recyclerview);
        for (int i = 0; i < Person_num.length; i++) {
            if (Person_num[i] < 10000) {
//                persons_num_str.add(i, Person_num[i] + "人");
            } else {
//                int temp = Person_num[i] / 10000;
//                persons_num_str.add(i, temp + "万");
            }
        }



        Fragment_viewpager_second_bean_up_item list_bean = new Fragment_viewpager_second_bean_up_item
                ("#"+"一起来分享大家的剪纸作品","10.1万",R.drawable.fragment_second_up_1);
        Fragment_viewpager_second_bean_up_item list_bean1 = new Fragment_viewpager_second_bean_up_item
                ("#"+"打工人的一天","10.1万",R.drawable.fragment_second_up_2);
        list.add(list_bean);
        list.add(list_bean1);


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        fragment_viewpager_second_adapter = new Fragment_viewpager_second_Adapter(list);
        recyclerView.setAdapter(fragment_viewpager_second_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    public void initviewPage_down(){
        viewPager_down = (ViewPager)getView().findViewById(R.id.fragment_second_down_page_1);

        fragment_recommend = new Fragment_recommend();
//        fragment_live = new Fragment_live();
//        fragment_product = new Fragment_product();
//        fragment_video = new Fragment_video();
//        fragment_village = new Fragment_village();

        fragments_down.add(fragment_recommend);
//        fragments_down.add(fragment_video);
//        fragments_down.add(fragment_live);
//        fragments_down.add(fragment_product);
//        fragments_down.add(fragment_village);

        fragmentPagerAdapter_down = new FragmentAdapter_down(getActivity().getSupportFragmentManager(), fragments_down);
        viewPager_down.setOffscreenPageLimit(1);
        viewPager_down.setCurrentItem(0);
        viewPager_down.setAdapter(fragmentPagerAdapter_down);

        viewPager_down.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviewPage_down();
        init_fragment_second_up_item();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class FragmentAdapter_down extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public FragmentAdapter_down(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
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
