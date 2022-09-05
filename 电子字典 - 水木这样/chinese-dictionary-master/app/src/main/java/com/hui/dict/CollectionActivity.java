package com.hui.dict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hui.dict.collect_frag.CollectFragmentAdapter;
import com.hui.dict.collect_frag.ZiFragment;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager collectVp;
    String []titles = {"汉字","成语"};
    List<Fragment>mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        tabLayout = findViewById(R.id.collect_tabs);
        collectVp = findViewById(R.id.collect_vp);
        initPager();
    }
    /* 初始化ViewPager页面的操作*/
    private void initPager() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Fragment frag = new ZiFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type",titles[i]);
            frag.setArguments(bundle);
            mDatas.add(frag);
        }
        CollectFragmentAdapter adapter = new CollectFragmentAdapter(getSupportFragmentManager(), mDatas, titles);
        collectVp.setAdapter(adapter);
//        将上下绑定
        tabLayout.setupWithViewPager(collectVp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collect_iv_back:
                finish();
                break;
        }
    }
}
