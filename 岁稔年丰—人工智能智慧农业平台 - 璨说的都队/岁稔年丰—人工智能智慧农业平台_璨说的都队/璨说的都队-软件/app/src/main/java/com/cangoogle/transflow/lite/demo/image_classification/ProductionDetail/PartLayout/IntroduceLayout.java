package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class IntroduceLayout extends CardView {
    private Context context;
    public IntroduceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private TextView textViewHeightPrice,textViewLowPrice;
    private TextView textViewDetail;
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.production_detail_introduce,this);
        textViewDetail=view.findViewById(R.id.textViewDetailIntroduceProductionDetail1);
        textViewHeightPrice=view.findViewById(R.id.textViewHeightPriceProductionDetail1);
        textViewLowPrice=view.findViewById(R.id.textViewLowPriceProductionDetail1);
    }
    public void setData(String high, String low, String content){
        textViewLowPrice.setText("."+low);
        textViewHeightPrice.setText(high);
        textViewDetail.setText(content);
    }
}
