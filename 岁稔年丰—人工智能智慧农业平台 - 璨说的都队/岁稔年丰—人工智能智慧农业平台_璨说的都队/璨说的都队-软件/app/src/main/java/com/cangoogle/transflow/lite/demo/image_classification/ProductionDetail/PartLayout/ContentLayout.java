package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class ContentLayout extends CardView {
    private Context context;
    public ContentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private TextView textViewSelectDetail,textViewSelectNum;
    private TextView textViewSendPlace,textViewSendOtherNote;
    private TextView textViewPromise;
    private ImageView imageView1,imageView2;
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.production_detail_content,this);
        textViewSelectDetail=view.findViewById(R.id.textViewProductionDetailContentSelectDetail);
        textViewSelectNum=view.findViewById(R.id.TextViewProductionDetailContentSelectNum);
        textViewSendPlace=view.findViewById(R.id.textViewProductionDetailContentSendPlace);
        textViewSendOtherNote=view.findViewById(R.id.textViewProductionDetailContentSendOtherNote);
        textViewPromise=view.findViewById(R.id.textViewProductionDetailContentPromise);
        imageView1=view.findViewById(R.id.imageViewProductionDetailContentSelectPicture1);
        imageView2=view.findViewById(R.id.imageViewProductionDetailContentSelectPicture2);
    }
    public void setImage(String url1,String url2){

    }
    public void setTextView(String selectDetail,String selectNum
            ,String sendPlace,String sendOtherNote
            ,String promise){
        textViewSelectDetail.setText(selectDetail);
        textViewSelectNum.setText(selectNum);
        textViewSendPlace.setText(sendPlace);
        textViewSendOtherNote.setText(sendOtherNote);
        textViewPromise.setText(promise);
    }

}
