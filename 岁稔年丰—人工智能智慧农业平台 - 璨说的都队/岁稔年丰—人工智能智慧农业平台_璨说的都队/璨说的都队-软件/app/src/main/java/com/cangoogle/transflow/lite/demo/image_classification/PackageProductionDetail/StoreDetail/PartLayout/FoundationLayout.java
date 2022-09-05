package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail.PartLayout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class FoundationLayout extends CardView {
    private Context context;
    private Activity activity;
    public FoundationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private View view;
    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.store_detail_foundation,this);
        textViewDayTime=view.findViewById(R.id.textViewStoreDetailDayTime);
        textViewMoreTime=view.findViewById(R.id.textViewStoreDetailMoreTime);
        textViewSpace=view.findViewById(R.id.textViewStoreDetailSpace);
        textViewNumber=view.findViewById(R.id.textViewStoreDetailPeopleNumber);
        textViewPhoneNumber=view.findViewById(R.id.textViewStoreDetailPhoneNumber);
    }
    private TextView textViewDayTime,textViewMoreTime;
    private TextView textViewSpace,textViewNumber,textViewPhoneNumber;
    public void setData(String dayTime,String moreTime,String space
            ,String number,String phoneNumber){
        textViewDayTime.setText(dayTime);
        textViewPhoneNumber.setText(phoneNumber);
        textViewNumber.setText(number);
        textViewSpace.setText(space);
        textViewMoreTime.setText(moreTime);
    }
}
