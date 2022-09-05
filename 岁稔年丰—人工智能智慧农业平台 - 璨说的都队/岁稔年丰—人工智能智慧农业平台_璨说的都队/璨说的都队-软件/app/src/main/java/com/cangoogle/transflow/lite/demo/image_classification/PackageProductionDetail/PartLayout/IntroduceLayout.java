package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.PartLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.Adaptor.SpecialNoteAdapter;
import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.Data.SpecialNote;
import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.StoreDetail.StoreDetailActivity;
import com.baidu.paddle.lite.demo.image_classification.R;


import java.util.ArrayList;
import java.util.List;

public class IntroduceLayout extends CardView {
    private Context context;
    private Activity activity;
    public IntroduceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private View view;
    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.package_production_detail_introduce,this);
        loadFirst();
        loadSecond();
        loadThird();
        loadForth();

    }
    public void setActivity(Activity activity){this.activity=activity;}
    //--------------------加载第一组数据，设置第一组数据
    private TextView textViewHeightPrice,textViewLowPrice;
    private TextView textViewDetail;
    private void loadFirst(){
        textViewDetail=view.findViewById(R.id.textViewDetailIntroduceProductionDetail);
        textViewHeightPrice=view.findViewById(R.id.textViewHeightPriceProductionDetail);
        textViewLowPrice=view.findViewById(R.id.textViewLowPriceProductionDetail);
    }
    public void setFirstData(String high,String low,String content){
        textViewLowPrice.setText("."+low);
        textViewHeightPrice.setText(high);
        textViewDetail.setText(content);
    }
    //---------------------setSecond
    private TextView textViewFullBackgroundNote;
    private ImageButton imageButtonFullBackgroundLink;
    private ImageView imageViewFullBackgroundShow;
    private void loadSecond(){
        textViewFullBackgroundNote=view.findViewById
                (R.id.textViewPackageProductionDetailFullBackgroundNote);
        imageButtonFullBackgroundLink=view.findViewById
                (R.id.imageViewPackageProductionDetailFullBackgroundLink);
        imageViewFullBackgroundShow=view.findViewById
                (R.id.imageViewPackageProductionDetailFullBackgroundShow);
        imageButtonFullBackgroundLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到全景页面的详情
            }
        });
    }
    public void setSecond(String backgroundNote,String url){
        textViewFullBackgroundNote.setText(backgroundNote);
        //然后设置图片的展示
    }
    //----------------------setForth
    private TextView textViewProcessDetail;
    private RecyclerView recyclerViewSpecialNote;
    private ImageButton imageButtonProcessLead;
    List<SpecialNote> specialNotes=new ArrayList<>();
    public void setForth(String processDetail,List<String> notes){
        if(processDetail.length()>=10){
            processDetail=processDetail.substring(0,10)+"...";
        }
        textViewProcessDetail.setText(processDetail);
        for(String e:notes){
            SpecialNote specialNote=new SpecialNote();
            specialNote.setName(e);
            specialNotes.add(specialNote);
        }
    }
    private void initSpecialNotes(){
        for(int i=1;i<=4;i++){
            SpecialNote specialNote=new SpecialNote();
            specialNote.setName("内有猫咪");
            specialNotes.add(specialNote);
        }
    }
    private static final int MAX=6;
    private static final int MIN=3;
    private final String TAG="wocao";
    private int getRealCount(int pos){
//        Log.d(TAG, "getRealCount: "+specialNotes.get(pos).getName().length());
        if(specialNotes.get(pos).getName().length()>=MAX){
            //放一个
            return 6;
        }
        else if(specialNotes.get(pos).getName().length()<=MIN){
            //放三个
            return 2;
        }else return 3;//放2个
    }
    private void loadForth(){
        textViewProcessDetail=view.findViewById(R.id.textViewPackageProductionDetailProcessDetail);
        recyclerViewSpecialNote=view.findViewById(R.id.RecycleViewPackageProductionDetailSpecialNote);
        imageButtonProcessLead=view.findViewById(R.id.imageButtonPackageProductionDetailProcessLead);
        imageButtonProcessLead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到店铺详情
                /**
                 * 从这里传一个url过去
                 * 再从后端获取店铺的详情
                 */
                Intent intent=new Intent(context, StoreDetailActivity.class);
                context.startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,6);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d(TAG, "getSpanSize: "+getRealCount(position));
                return getRealCount(position);
            }
        });
        recyclerViewSpecialNote.setLayoutManager(gridLayoutManager);

        SpecialNoteAdapter adapter=new SpecialNoteAdapter(specialNotes);
        recyclerViewSpecialNote.setAdapter(adapter);
    }
    //-----------------设置Third
    public void callPhone(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity
                    ,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            try{
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                activity.startActivity(intent);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }
    private TextView textViewLocation;
    private ImageButton imageButtonCarLeading,imageButtonPhone;
    private String phoneNumber;
    public void setThird(String location,String phoneNumber){
        this.phoneNumber=phoneNumber;
        textViewLocation.setText(location);
    }
    private void loadThird(){
        textViewLocation=view.findViewById(R.id.textViewPackageProductionDetailLocation);
        imageButtonCarLeading=view.findViewById(R.id.imageButtonPackageProductionDetailCarLeading);
        imageButtonCarLeading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到导航
            }
        });
        imageButtonPhone=view.findViewById(R.id.imageButtonPackageProductionDetailPhone);
        imageButtonPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone();
            }
        });

    }


}
