package com.baidu.paddle.lite.demo.image_classification.ProductionDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter.MoreDetailAdapter;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter.ProductionDetailShufflingAdapter;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.MoreDetailImage;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ShufflingItem;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.CommentLayout;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.IntroduceLayout;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.ShufflingViewPager;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class ProductionDetailActivity extends AppCompatActivity {

    public static final String INSTANCE_IMAGE="imgae";
    public static final String INSTANCE_TITLE="title";
    public static final String INSTANCE_PRICE="price";
    public static String image,title,price;
    private CardView textViewBuyNow;
    public static Context thiss = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_detail);
        Intent intent=getIntent();
        image=intent.getStringExtra(INSTANCE_IMAGE);
        title=intent.getStringExtra(INSTANCE_TITLE);
        price=intent.getStringExtra(INSTANCE_PRICE);
        thiss = ProductionDetailActivity.this;
        setViewPager();
        setButton();
        setIntroduceCard();
        setContentCard();
        setCommentCard();
        setMoreDetail();

    }
    //-----------------setSwipeRefreshLayout
    private SwipeRefreshLayout swipeRefreshLayout;
    private void setSwipeRefreshLayout(){
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutProductionDetail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    //-----------------setMoreDetail
    private RecyclerView recyclerViewMoreDetail;
    private List<MoreDetailImage> moreDetailImages=new ArrayList<>();
    private GridLayoutManager gridLayoutManagerMoreDetail;
    private MoreDetailAdapter moreDetailAdapter;
    private void initMoreDetailList(){
        for(int i=1;i<=10;i++){
            MoreDetailImage moreDetailImage=new MoreDetailImage();
            moreDetailImage.setUrl(image);
            moreDetailImages.add(moreDetailImage);
        }
    }
    private void setMoreDetail(){
        initMoreDetailList();
        recyclerViewMoreDetail=findViewById(R.id.recyclerViewProductionDetailMoreDetail);
        moreDetailAdapter=new MoreDetailAdapter(moreDetailImages);
        gridLayoutManagerMoreDetail=new GridLayoutManager(this,1);
        recyclerViewMoreDetail.setLayoutManager(gridLayoutManagerMoreDetail);
        recyclerViewMoreDetail.setAdapter(moreDetailAdapter);
    }
    //-----------------setCommentCard
    private CommentLayout commentLayout;
    private void setCommentCard(){
        commentLayout=findViewById(R.id.commentLayoutProductionDetail);
        commentLayout.setUrl("");
    }
    //-----------------setContentCard
    private void setContentCard(){

    }
    //-----------------setIntroduceCard
    private IntroduceLayout introduceLayout;
    private void setIntroduceCard(){
        introduceLayout=findViewById(R.id.IntroduceLayoutProductionDetail);
        float temp_1 = Float.parseFloat(price);
        int temp_2 = (int)temp_1;
        float temp_3 = temp_1 - temp_2;
        temp_3 = temp_3*100;
        int temp_4 = (int)temp_3;
        String temp_5 = Integer.toString(temp_4);
        introduceLayout.setData(String.valueOf(temp_2),temp_5,title+"今日起特价销售啦！现价打折！！！");

    }
    //--------------------设置全部的button
    private ImageButton imageButtonBack;
    private CardView cardView;
    private void setButton(){
        imageButtonBack=findViewById(R.id.imageButtonProductionDetailBack);
        imageButtonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            View decorView = getWindow().getDecorView();
            int option=View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    //---------------------setViewPager
    private ShufflingViewPager viewPager;
    private boolean onTouchShuffling;

    private ProductionDetailShufflingAdapter shufflingAdapter;
    private TextView textViewShufflingPosition;
    private List<ShufflingItem> shufflingItems;
    private Handler shufflingHandler=new Handler();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        shufflingHandler.post(shufflingRunnable);
    }

    private Runnable shufflingRunnable=new Runnable() {
        @Override
        public void run() {
            int currentItem=viewPager.getCurrentItem();
            viewPager.setCurrentItem(++currentItem,false);
            shufflingHandler.postDelayed(this,2000);
        }
    };
    private void initShufflingList(){
        //这里放置上面轮播图的展示图片
        shufflingItems=new ArrayList<>();
        for(int i=1;i<=5;i++){
            ShufflingItem shufflingItem=new ShufflingItem();
            shufflingItem.setPictureUrl(image);
            shufflingItems.add(shufflingItem);
        }
    }
    private void setViewPager(){
        viewPager=findViewById(R.id.ViewPagerProductionDetail);
        textViewShufflingPosition=findViewById
                (R.id.textViewProductionDetailShufflingPosition);
        shufflingAdapter=new ProductionDetailShufflingAdapter();
        initShufflingList();
        shufflingAdapter.setData(shufflingItems);
        viewPager.setAdapter(shufflingAdapter);
        viewPager.setCurrentItem(shufflingAdapter.getDataRealSize()*100-1,false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition=0;
                if(shufflingAdapter.getDataRealSize()!=0){
                    realPosition=position%shufflingAdapter.getDataRealSize();
                }
                realPosition++;
                textViewShufflingPosition.setText(realPosition+"/"
                        +shufflingAdapter.getDataRealSize());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}