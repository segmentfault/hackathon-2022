package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.paddle.lite.demo.image_classification.R;



public class LinerLayoutOtherFunctionLayout extends LinearLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout linearLayoutRank,linearLayoutFavorite
            ,linearLayoutOrder,linearLayoutService;
    public LinerLayoutOtherFunctionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    protected void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.liner_layout_accommodation_other_function,this);
        linearLayoutRank=view.findViewById(R.id.LinerLayoutAccommodationOtherFunctionRank);
        linearLayoutFavorite=view.findViewById(R.id.LinerLayoutAccommodationOtherFunctionFavorite);
        linearLayoutOrder=view.findViewById(R.id.LinerLayoutAccommodationOtherFunctionOrder);
        linearLayoutService=view.findViewById(R.id.LinerLayoutAccommodationOtherFunctionOnlineService);

        linearLayoutService.setOnClickListener(this);
        linearLayoutRank.setOnClickListener(this);
        linearLayoutFavorite.setOnClickListener(this);
        linearLayoutOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LinerLayoutAccommodationOtherFunctionRank:
                //排行榜

            case R.id.LinerLayoutAccommodationOtherFunctionFavorite:
                //我的收藏

            case R.id.LinerLayoutAccommodationOtherFunctionOrder:
                //我的订单

            case R.id.LinerLayoutAccommodationOtherFunctionOnlineService:
                //在线客服
        }
    }
}
