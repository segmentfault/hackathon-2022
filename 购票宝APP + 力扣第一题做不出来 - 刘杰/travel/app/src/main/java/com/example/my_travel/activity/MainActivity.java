package com.example.my_travel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_travel.R;
import com.example.my_travel.adapter.ManPageAdapter;
import com.example.my_travel.fragment.HomeFragment;
import com.example.my_travel.fragment.UserFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_cont, ll_home,ll_user;
    private TextView tx_home,  tx_user;
    private ImageView img_home,  img_user;
    private ViewPager2 pager2;
    private ManPageAdapter adapter;
    private HomeFragment fhome;
    private UserFragment fuser;
    private ArrayList<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBnt();
    }

    private void initBnt() {
        //设置导航栏点击事件
        ll_home.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void initView() {
        pager2 = findViewById(R.id.main_page2);
        ll_home = findViewById(R.id.ll_home);
        ll_user = findViewById(R.id.ll_user);
        tx_home = findViewById(R.id.txt_home);
        tx_user = findViewById(R.id.txt_user);
        img_home = findViewById(R.id.img_home);
        img_user = findViewById(R.id.img_user);
        //加载F页面
        initFragment();
        ll_home.setSelected(true);
        tx_home.setTextColor(0xffF44336);
        ll_cont = ll_home;
    }

    private void initFragment() {
        fhome = new HomeFragment();
        fuser = new UserFragment();
        list.add(fhome);
        list.add(fuser);
        //将数组内容添加适配器
        adapter = new ManPageAdapter(getSupportFragmentManager(), getLifecycle(), list);
        pager2.setAdapter(adapter);
        pager2.setUserInputEnabled(false);
        //设置单个点击事件
        pager2.registerOnPageChangeCallback(onClickPage);

    }

    private ViewPager2.OnPageChangeCallback onClickPage = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            PagTitel(position);
        }
    };

    private void PagTitel(int position) {
        ll_cont.setSelected(false);
        restBnt();
        switch (position) {
            case 0:
            case R.id.ll_home:
                pager2.setCurrentItem(0);
                ll_home.setSelected(true);
                tx_home.setTextColor(0xffF44336);
                ll_cont = ll_home;
                break;
            case 1:
            case R.id.ll_user:
                pager2.setCurrentItem(2);
                ll_user.setSelected(true);
                tx_user.setTextColor(0xffF44336);
                ll_cont = ll_user;
                break;
            default:
                break;
        }

    }
    private void restBnt() {
        img_home.setImageResource(R.drawable.home);
        img_user.setImageResource(R.drawable.mi);
        tx_home.setTextColor(0xff333333);
        tx_user.setTextColor(0xff333333);

    }
    @Override
    public void onClick(View view) {
        PagTitel(view.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁监听事件
        pager2.unregisterOnPageChangeCallback(onClickPage);
    }
}
