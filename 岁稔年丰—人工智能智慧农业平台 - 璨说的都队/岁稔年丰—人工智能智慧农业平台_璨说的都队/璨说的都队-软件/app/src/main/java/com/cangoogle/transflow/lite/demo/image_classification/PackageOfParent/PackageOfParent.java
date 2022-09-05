package com.baidu.paddle.lite.demo.image_classification.PackageOfParent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.Adapter.ItemContentAdapter;
import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.Data.ItemContent;
import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.PartLayout.CardViewIntroduceLayout;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class PackageOfParent extends AppCompatActivity {
    public static final String INSTANCE_DESC="desc";
    public static final String INSTANCE_TYPE1="type1";
    public static final String INSTANCE_TYPE1_PRICE="type1price";
    public static final String INSTANCE_TYPE2 ="type2";
    public static final String INSTANCE_TYPE2_PRICE="type2price";
    public static final String INSTANCE_TYPE3="type3";
    public static final String INSTANCE_TYPE3_PRICE="type3price";
    public static String desc,type1,type1price,type2,type2price,type3,type4price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_of_parent);
        Intent intent=getIntent();
        desc=intent.getStringExtra(INSTANCE_DESC);
        type1=intent.getStringExtra(INSTANCE_TYPE1);
        type1price=intent.getStringExtra(INSTANCE_TYPE1_PRICE);
        type2=intent.getStringExtra(INSTANCE_TYPE2);
        type2price=intent.getStringExtra(INSTANCE_TYPE2_PRICE);
        type3=intent.getStringExtra(INSTANCE_TYPE3);
        type4price=intent.getStringExtra(INSTANCE_TYPE3_PRICE);
        getDataFromBack();
        setActionBar();
        setRecycleView();
        setIntroduce();
    }
    //----------------从后端获取数据
    private void initList(){
        ItemContent itemContent=new ItemContent();
            itemContent.setPrice(type1price);
            itemContent.setTime("下午 10:00-11:00");
            itemContent.setTitle(type1);
            ItemContent itemContent1=new ItemContent();
        itemContent1.setPrice(type2price);
        itemContent1.setTime("下午 10:00-11:00");
        itemContent1.setTitle(type2);
            ItemContent itemContent2=new ItemContent();
        itemContent2.setPrice(type4price);
        itemContent2.setTime("下午 10:00-11:00");
        itemContent2.setTitle(type3);
            list.add(itemContent);
        list.add(itemContent1);
        list.add(itemContent2);
    }
    private void getDataFromBack(){
        //这里是从后端获取数据
        introduceAfternoonTime="下午 "+"10:00-11:00";
        introduceNoonTime="上午 "+"10:00-11:00";
        introduceDetail=desc;

    }
    //------------------配置introduce
    private CardViewIntroduceLayout cardViewIntroduceLayout;
    private String introduceAfternoonTime;
    private String introduceNoonTime;
    private String introduceDetail;
    private void setIntroduce(){
        cardViewIntroduceLayout=(CardViewIntroduceLayout)
                findViewById(R.id.CardViewIntroduceLayoutOfPackageOfParent);
        cardViewIntroduceLayout.setResult(new CardViewIntroduceLayout.Result() {
            @Override
            public void onFinish() {
                finish();
            }
        });
        cardViewIntroduceLayout.setInformation(introduceDetail
                ,introduceNoonTime,
                introduceAfternoonTime);


    }
    //-------------------配置content内容
    RecyclerView recyclerView;
    private ImageButton imageButtonBack;
    ItemContentAdapter adapter;
    List<ItemContent> list=new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private void setRecycleView(){
        initList();
        recyclerView=(RecyclerView)
                findViewById(R.id.RecycleViewContentOfPackageOfParent);
        adapter=new ItemContentAdapter(list);
        gridLayoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        Log.d("now","arrive");
        imageButtonBack=findViewById(R.id.imageButtonSmallBackOfPackageOfParent);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    //----------------------配置actionBar的那一套
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
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        actionBar.setTitle("套餐名称（亲子主题）");
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
}