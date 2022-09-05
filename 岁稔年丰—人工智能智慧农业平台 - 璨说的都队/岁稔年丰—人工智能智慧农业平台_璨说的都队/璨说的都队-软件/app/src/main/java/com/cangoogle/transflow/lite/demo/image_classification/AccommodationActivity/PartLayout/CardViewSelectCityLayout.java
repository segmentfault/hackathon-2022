package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;



public class CardViewSelectCityLayout extends CardView {
    Context context;
    public CardViewSelectCityLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    public CardViewSelectCityLayout(Context context){
        super(context);
    }

    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.card_accommodation_select_location,this);

    }
}
