package com.baidu.paddle.lite.demo.image_classification.HappyFarm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Adaptor.LifeAdapter;

import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Adaptor.YardAdapter;
import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Data.ItemLife;
import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Data.ItemYard;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class HappyFarmLifeFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.happy_farm_life_fragment_layout
                ,container,false);
        context = getContext();activity=getActivity();
        setMain();
        setMiddle();
        setBottom();
        return view;
    }
    private TextView textViewMore;
    private void setMiddle(){
        textViewMore=view.findViewById(R.id.textViewHappyFarmLifeMore);
        textViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    //--------------------setBottom
    private RecyclerView recyclerViewBottom;

    private void setBottom(){
        recyclerViewBottom=view.findViewById(R.id.recyclerViewHappyFarmLifeYard);
        LinearLayoutManager layoutManager=new LinearLayoutManager
                (context,RecyclerView.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        List<ItemYard> list=new ArrayList<>();
        for(int i=1;i<=6;i++){
            ItemYard itemYard=new ItemYard();
            itemYard.setName("云深不知处");
            itemYard.setUrl("http://www.twoyl.cn/fj.png");
            list.add(itemYard);
        }
        YardAdapter adapter=new YardAdapter(list);
        recyclerViewBottom.setAdapter(adapter);
        recyclerViewBottom.setLayoutManager(layoutManager);

    }
    //-----------------------设置上面那个
    private int mScreenWidth;
    private static final float MIN_SCALE = 0.95f;
    private static final float MAX_SCALE = 1.15f;
    private LinearLayoutManager mLinearLayoutManager;
    private int HEIGHT=350,WIDTH=250;
    //这是设置大小,单位是dp！
    private RecyclerView recyclerViewMain;
    private void setMain(){
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclerViewMain = (RecyclerView) view.findViewById(R.id.recyclerViewHappyFarmLifeFragment);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewMain.setLayoutManager(mLinearLayoutManager);
        recyclerViewMain.addOnScrollListener(mOnScrollListener);//设置监听器
        List<ItemLife> lives=new ArrayList<>();
        for(int i=1;i<=10;i++){
            ItemLife itemLife=new ItemLife();
            itemLife.setUrl("http://www.twoyl.cn/agr_fris_back.png");
            itemLife.setTitle("云深不知处");
            itemLife.setContent("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            lives.add(itemLife);
        }
        LifeAdapter adapter=new LifeAdapter(lives);
        recyclerViewMain.setAdapter(adapter);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {//监听器方法
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int childCount = recyclerView.getChildCount();//获取可见item数量
            // Log.e("tag", childCount + "");
            for (int i = 0; i < childCount; i++) {
                CardView child = (CardView) recyclerView.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                lp.rightMargin = 3;
                lp.height=getPixelsFromDp(HEIGHT);
                lp.width = getPixelsFromDp(WIDTH);

                int left = child.getLeft();
                int right = mScreenWidth - child.getRight();
                final float percent = left < 0 || right < 0 ? 0 : Math.min(left, right) * 1f / Math.max(left, right);
                // Log.e("tag", "percent = " + percent);
                float scaleFactor = MIN_SCALE + Math.abs(percent) * (MAX_SCALE - MIN_SCALE);//放大算法
                // int width = (int) (mMinWidth + Math.abs(percent) * (mMaxWidth - mMinWidth));


                child.setLayoutParams(lp);
                child.setScaleY(scaleFactor);//将高度放大
                child.setScaleX(scaleFactor);//将宽度放大
            }
        }
    };
    private int getPixelsFromDp(int x){
        DisplayMetrics metrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (x*metrics.densityDpi)/DisplayMetrics.DENSITY_DEFAULT;
    }
}
