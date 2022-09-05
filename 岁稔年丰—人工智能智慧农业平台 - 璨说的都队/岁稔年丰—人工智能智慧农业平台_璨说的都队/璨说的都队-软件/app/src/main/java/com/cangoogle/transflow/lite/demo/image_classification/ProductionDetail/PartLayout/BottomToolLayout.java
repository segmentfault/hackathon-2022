package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.LoginActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.uoload_prod_activity;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.ProductionDetailActivity;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.entity.User;

import cn.bmob.v3.BmobUser;


public class BottomToolLayout extends LinearLayout {
    private Context context;
    public BottomToolLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private LinearLayout imageButtonService;
    private CardView textViewBuyNow;
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.bottom_tool_production_detail,this);
       imageButtonService=view.findViewById(R.id.imageButtonServiceProductionDetail);
        textViewBuyNow=view.findViewById(R.id.textViewBuyNowProductionDetail);

        imageButtonService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //去客服那里
                Toast.makeText(context, "service", Toast.LENGTH_SHORT).show();
            }
        });
        textViewBuyNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //去立即购买那里
//                getContext().startActivity(new Intent(getContext(),uoload_prod_activity.class));
                if (BmobUser.isLogin()) {
                    Intent intent=new Intent(getContext(), uoload_prod_activity.class);
                    intent.putExtra(uoload_prod_activity.INSTANCE_PRICE,ProductionDetailActivity.price);
                    intent.putExtra(ProductionDetailActivity.INSTANCE_TITLE,ProductionDetailActivity.title);
                    getContext().startActivity(intent);
                } else {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });

    }
}
