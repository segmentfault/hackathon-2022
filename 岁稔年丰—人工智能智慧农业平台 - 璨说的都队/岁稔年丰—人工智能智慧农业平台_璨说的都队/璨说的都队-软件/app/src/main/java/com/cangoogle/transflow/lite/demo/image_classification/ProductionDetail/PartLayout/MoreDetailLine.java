package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.baidu.paddle.lite.demo.image_classification.R;


public class MoreDetailLine extends LinearLayout {
    private Context context;
    public MoreDetailLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.production_detail_more_detail_line,this);
    }
}
