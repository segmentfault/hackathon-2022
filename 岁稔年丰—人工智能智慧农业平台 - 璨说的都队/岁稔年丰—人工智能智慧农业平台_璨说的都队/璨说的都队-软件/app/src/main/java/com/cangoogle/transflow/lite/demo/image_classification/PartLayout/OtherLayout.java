package com.baidu.paddle.lite.demo.image_classification.PartLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.baidu.paddle.lite.demo.image_classification.R;

import org.w3c.dom.Text;

public class OtherLayout extends CardView {
    private View view;
    private Context context;
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private int screenWidth,screenHeight;
    public OtherLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    private TextView textViewAuthorName,textViewAuthorContent;
    private ImageView imageViewAuthor,imageViewContent;
    private void loadView(){
        view= LayoutInflater.from(context)
                .inflate(R.layout.course_detail_video_other,this);
        textViewAuthorName=view.findViewById(R.id.textViewCourseDetailVideoOtherAuthor);
        textViewAuthorContent=view.findViewById(R.id.textViewCourseDetailVideoOtherAuthorContent);
        imageViewContent=view.findViewById(R.id.imageViewCourseDetailVideoOtherContent);
        imageViewAuthor=view.findViewById(R.id.imageViewCourseDetailVideoOtherAuthor);
    }
    private WindowManager windowManager;

    /**
     *
     * @param name 老师的名字
     * @param authorUrl 老师头像的图片的链接
     * @param authorContent 老师的介绍
     * @param content 课程详情底下的那张海报
     */
    public void setData(String name,String authorUrl,String authorContent,String content){
        windowManager=activity.getWindowManager();
        screenHeight=activity.getWindowManager().getDefaultDisplay().getHeight();
        screenWidth=activity.getWindowManager().getDefaultDisplay().getWidth();
        textViewAuthorContent.setText(authorContent);
        textViewAuthorName.setText(name);
        setImage(imageViewAuthor,authorUrl);
        setImage(imageViewContent,content);
        float k,width=screenWidth-2*getPixelsFromDp(15);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) imageViewContent.getLayoutParams();
        k=params.width/width;
        params.width=(int)width;
        params.height/=k;
        imageViewContent.setLayoutParams(params);

    }
    private void setImage(View object,String url){
        Glide.with(context)
                .asBitmap().load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap>
                                                        transition) {
                        Drawable drawable=new BitmapDrawable(resource);
                        object.setBackground(drawable);
                    }
                });
    }
    private int getPixelsFromDp(int x){
        DisplayMetrics metrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return (x*metrics.densityDpi)/DisplayMetrics.DENSITY_DEFAULT;
    }
}
