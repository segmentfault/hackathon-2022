package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.PartLayout.IntroduceLayout;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter.ProductionDetailShufflingAdapter;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ShufflingItem;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.CommentLayout;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.ShufflingViewPager;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.List;

public class PackageProductionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_production_detail);
        setButton();
        setViewPager();
        setIntroduceLayout();
        setSwipeRefreshLayout();
        setCommentLayout();
    }
    //---------------------setCommentLayout
    private CommentLayout commentLayout;
    private void setCommentLayout(){
        commentLayout=findViewById(R.id.commentLayoutPackageProductionDetail);
        commentLayout.setUrl("");
    }
    //---------------------setSwipeRefreshLayout
    private SwipeRefreshLayout refreshLayout;
    private void setSwipeRefreshLayout(){
        refreshLayout=findViewById(R.id.swipeRefreshLayoutPackageProductionDetail);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //??????
            }
        });
    }
    //--------------------setIntroduce
    private IntroduceLayout introduceLayout;
    private String phoneNumber;
    private void setIntroduceLayout(){
        introduceLayout=findViewById(R.id.IntroduceLayoutPackageProductionDetail);
        introduceLayout.setActivity(PackageProductionDetailActivity.this);
        introduceLayout.setFirstData("48","20","??????");
        introduceLayout.setSecond("????????????","");
        introduceLayout.setThird("?????????????????????????????????????????????","911");
        List<String> tmp=new ArrayList<>();
        /**
         * ?????????????????????????????????????????????????????????String???????????????????????????
         */
        tmp.add("????????????");
        tmp.add("????????????");
        tmp.add("?????????");
        tmp.add("??????");
        tmp.add("?????????");
        tmp.add("?????????????????????");
        tmp.add("???????????????????????????");
        introduceLayout.setForth("??????????????????12:00????????????????????????0:00???????????????????????????",tmp);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    introduceLayout.callPhone();
                } else {
                    Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //--------------------???????????????button
    private ImageButton imageButtonBack;
    private void setButton(){
        imageButtonBack=findViewById(R.id.imageButtonPackageProductionDetailBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
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
            int option=View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.parseColor("#5DE6D6"));
        }
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)actionBar.setElevation(-100);
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
        //??????????????????????????????????????????
        shufflingItems=new ArrayList<>();
        for(int i=1;i<=5;i++){
            ShufflingItem shufflingItem=new ShufflingItem();
            shufflingItems.add(shufflingItem);
        }
    }
    private void setViewPager(){
        viewPager=findViewById(R.id.ViewPagerPackageProductionDetail);
        textViewShufflingPosition=findViewById
                (R.id.textViewPackageProductionDetailShufflingPosition);
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