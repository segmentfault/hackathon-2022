package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class SpecialLayout extends CardView {
    private Context context;
    public SpecialLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    View view;

    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.store_detail_special,this);
        textViewRegion=view.findViewById(R.id.textViewStoreDetailRegion);
        textViewAnimal=view.findViewById(R.id.textViewStoreDetailAnimal);
        textViewActivity=view.findViewById(R.id.textViewStoreDetailActivity);
        textViewNote=view.findViewById(R.id.textViewStoreDetailNote);
    }
    private TextView textViewRegion,textViewAnimal
            ,textViewActivity,textViewNote;
    public void setData(String region,String animal,String activity,String note){
        textViewRegion.setText(region);
        textViewAnimal.setText(animal);
        textViewNote.setText(note);
        textViewActivity.setText(activity);
    }
}
