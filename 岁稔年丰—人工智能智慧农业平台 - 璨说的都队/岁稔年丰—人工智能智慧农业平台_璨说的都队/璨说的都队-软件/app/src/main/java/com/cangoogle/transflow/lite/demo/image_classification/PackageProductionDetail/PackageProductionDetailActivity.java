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
                //刷新
            }
        });
    }
    //--------------------setIntroduce
    private IntroduceLayout introduceLayout;
    private String phoneNumber;
    private void setIntroduceLayout(){
        introduceLayout=findViewById(R.id.IntroduceLayoutPackageProductionDetail);
        introduceLayout.setActivity(PackageProductionDetailActivity.this);
        introduceLayout.setFirstData("48","20","倪哥");
        introduceLayout.setSecond("没钱别来","");
        introduceLayout.setThird("俄罗斯莫斯科极北苦寒伏特加大地","911");
        List<String> tmp=new ArrayList<>();
        /**
         * 如果要实现较好的展示效果，最好先给这些String按照从短到长排个序
         */
        tmp.add("内有猫咪");
        tmp.add("内有恶犬");
        tmp.add("持枪证");
        tmp.add("有枪");
        tmp.add("没倪哥");
        tmp.add("小心别被枪打死");
        tmp.add("喝酒喝不过别硬撑着");
        introduceLayout.setForth("每周周日晚上12:00开始到每周一凌晨0:00结束，其他时间别来",tmp);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    introduceLayout.callPhone();
                } else {
                    Toast.makeText(this, "无法拨打电话，您拒绝授权", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //--------------------设置全部的button
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
        //这里放置上面轮播图的展示图片
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