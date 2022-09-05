package com.baidu.paddle.lite.demo.image_classification.PackageOfParent.PartLayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Local_Activity;
import com.baidu.paddle.lite.demo.image_classification.R;


public class CardViewIntroduceLayout extends CardView {
    Context context;
    public CardViewIntroduceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
        setButtonFunction();
        setTextView();
    }
    public CardViewIntroduceLayout(Context context){
        super(context);
    }
    //-------------------------init
    private ImageButton imageButtonBack,imageButtonCart;
    private CardView cardViewLeading;
    private TextView textViewDetail;
    private TextView textViewNoonTime;
    private TextView textViewAfternoonTime;
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.package_of_parent_introduce,this);
        textViewDetail=view.findViewById(R.id.textViewDetailOfIntroduceOfPackageOfParent);
        textViewAfternoonTime=view.findViewById(R.id.textViewAfternoonTimeOfPackageOfParentOfIntroduce);
        textViewNoonTime=view.findViewById(R.id.textViewNoonTimeOfPackageOfParentOfIntroduce);
        cardViewLeading=view.findViewById(R.id.cardViewLeadingOfPackageOfParentIntroduce);
        imageButtonBack=view.findViewById(R.id.imageButtonBackOfIntroduceOfPackageOfParent);
        imageButtonCart=view.findViewById(R.id.imageButtonShoppingCartOfPackageOfParentIntroduce);

    }
    //--------------------------setButton
    private void setButtonFunction(){
        imageButtonCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "已加入购物车", Toast.LENGTH_SHORT).show();
            }
        });
        imageButtonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                result.onFinish();
            }
        });
        cardViewLeading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到导航活动
                context.startActivity(new Intent(context, Local_Activity.class));
            }
        });
    }
    public interface Result{
        void onFinish();
    }
    Result result;
    public void setResult(Result result){this.result=result;}
    //---------------------setTextView
    private String noonTime;
    private String afternoonTime;
    private String detail;
    public void setInformation(String detail,String noonTime,String afternoonTime){
        this.detail=detail;this.noonTime = noonTime;this.afternoonTime = afternoonTime;
        setTextView();
    }
    private void setTextView(){
        textViewNoonTime.setText(noonTime);
        textViewAfternoonTime.setText(afternoonTime);
        textViewDetail.setText(detail);
    }
}
