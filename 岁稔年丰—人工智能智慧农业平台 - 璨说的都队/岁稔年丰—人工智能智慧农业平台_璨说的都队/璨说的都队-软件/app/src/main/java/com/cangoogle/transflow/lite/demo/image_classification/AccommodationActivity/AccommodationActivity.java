package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.Adaptor.HotLocationAdapter;
import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.Adaptor.PersonRecommendationAdapter;
import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.data.HotLocation;
import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.data.PersonRecommendation;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.range.RangeActivity;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class AccommodationActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        linearLayout = findViewById(R.id.select_riqi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccommodationActivity.this, RangeActivity.class));
            }
        });
        setSwipeRefresh();

        setHotLocation();
        setPersonRecommendation();
        setActionBar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            View decorView = getWindow().getDecorView();
            int option=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setTitle("特色民宿");
        //actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setTitle("fuck");
//        ColorDrawable colorDrawable=new ColorDrawable();
//        colorDrawable.setAlpha(33);
//        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return false;
            default: return super.onOptionsItemSelected(item);
        }

    }

    //----------------配置刷新控件
    private SwipeRefreshLayout swipeRefreshLayoutAll;
    private void setSwipeRefresh(){
        swipeRefreshLayoutAll=(SwipeRefreshLayout)findViewById
                (R.id.SwipeRefreshLayoutAccommodation);
        swipeRefreshLayoutAll.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayoutAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    //具体的刷新函数
    private void refresh(){
        refreshHotLocation();
        refreshPersonRecommendation();
    }
    private void refreshHotLocation(){

    }
    private void refreshPersonRecommendation(){

    }
    //----------------配置达人推荐
    private GridLayoutManager gridLayoutManagerPersonRecommendation;
    private RecyclerView recyclerViewPersonRecommendation;
    private PersonRecommendationAdapter personRecommendationAdapter;
    private List<PersonRecommendation> personRecommendationList=new ArrayList<>();
    private void initPersonRecommendation(){
        for(int i=1;i<=100;i++){
            personRecommendationList.add(new PersonRecommendation());
        }
        personRecommendationAdapter=new PersonRecommendationAdapter(personRecommendationList);
    }
    private void setPersonRecommendation(){
        initPersonRecommendation();
        recyclerViewPersonRecommendation=(RecyclerView)
                findViewById(R.id.recyclerViewPersonRecommendation);
        gridLayoutManagerPersonRecommendation=new GridLayoutManager(this,2);
        recyclerViewPersonRecommendation.setLayoutManager(gridLayoutManagerPersonRecommendation);
        recyclerViewPersonRecommendation.setAdapter(personRecommendationAdapter);

    }



    //----------------------------------配置热门推荐部分
    private LinearLayoutManager linearLayoutManagerHotLocation
            =new LinearLayoutManager(this);
    private RecyclerView recyclerViewHotLocation;
    private HotLocationAdapter hotLocationAdapter;
    private List<HotLocation> hotLocationList=new ArrayList<>();
    private void initHotLocation(){
        for(int i=1;i<=100;i++){
            hotLocationList.add(new HotLocation());
        }
        hotLocationAdapter=new HotLocationAdapter(hotLocationList);
    }
    private void setHotLocation(){
        initHotLocation();
        recyclerViewHotLocation=(RecyclerView)findViewById(R.id.recyclerViewHotLocation);
        linearLayoutManagerHotLocation.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHotLocation.setLayoutManager(linearLayoutManagerHotLocation);
        recyclerViewHotLocation.setAdapter(hotLocationAdapter);
    }
}