package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail.PartLayout.FoundationLayout;
import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail.PartLayout.SpecialLayout;
import com.baidu.paddle.lite.demo.image_classification.R;

public class StoreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        setMainName();
        setFoundation();
        setSpecial();
    }
    private TextView textViewName;
    private void setMainName(){
        textViewName=findViewById(R.id.textViewStoreDetailTitle);
        textViewName.setText("wodowajdiwjo");
    }
    private FoundationLayout foundationLayout;
    private void setFoundation(){
        foundationLayout=findViewById(R.id.foundationLayoutStoreDetail);
        foundationLayout.setData(
                "周一到周五",
                "13：00到17:00\n19：00到25:00",
                "100平方米",
                "100人",
                "13399990000");
    }
    private SpecialLayout specialLayout;
    private void setSpecial(){
        specialLayout=findViewById(R.id.specialLayoutStoreDetail);
        specialLayout.setData(
                "acnsid",
                "dwowidoi",
                "awwjdjaw",
                "aiwdjwijd");
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            View decorView = getWindow().getDecorView();
            int option=View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.parseColor("#5DE6D6"));
        }
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_more_comment);
            Resources resources= StoreDetailActivity.this.getResources();
            Drawable drawable=resources.getDrawable(R.drawable.action_bar_radius);
            actionBar.setBackgroundDrawable(drawable);
            ImageButton imageButton=actionBar.getCustomView()
                    .findViewById(R.id.imageButtonActionBarMoreCommentBack);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            TextView textView=findViewById(R.id.textViewActionBarMoreCommentTitle);
            textView.setText("门店信息");

//            actionBar.setBackgroundDrawable(new ColorDrawable
//                    (Color.parseColor("#5DE6D6")));
        }

    }
}