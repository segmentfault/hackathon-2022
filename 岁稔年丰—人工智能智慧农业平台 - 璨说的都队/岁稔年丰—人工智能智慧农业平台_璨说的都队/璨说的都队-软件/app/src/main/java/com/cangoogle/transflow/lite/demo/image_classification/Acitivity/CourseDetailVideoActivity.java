package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.paddle.lite.demo.image_classification.Bean.CourseItem;
import com.baidu.paddle.lite.demo.image_classification.PartLayout.AllCourseLayout;
import com.baidu.paddle.lite.demo.image_classification.PartLayout.IntroduceLayout;
import com.baidu.paddle.lite.demo.image_classification.PartLayout.OtherLayout;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.ArrayList;
import java.util.List;

public class CourseDetailVideoActivity extends AppCompatActivity {
    public static final String title = "1";
    public static final String name = "2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * ??????????????????????????????????????????????????????????????????????????????????????????bug
         */
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#BAB6B6"));
        setContentView(R.layout.activity_course_detail_video);
        if(getSupportActionBar()!=null)getSupportActionBar().hide();
        context=CourseDetailVideoActivity.this;
        isFullScreen=false;
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();
        screenWidth=getWindowManager().getDefaultDisplay().getWidth();
        initTest();
        setIntroduce();
        setBottom();
        setVideo();
        setAllCourse();
        setOther();
    }

    /**
     * test?????????
     */
    //------------------test??????
    private final String video_path="http://qiubai-video.qiushibaike.com/91B2TEYP9D300XXH_3g.mp4";
    private String testImage="https://gd3.alicdn.com/imgextra/i3/4122288859/O1CN01QwxTYU2FJTQ9whlJz_!!4122288859.jpg";
    private String testContent;
    private void initTest(){
        testContent="???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????85????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
    }
    //------------------setOther
    private OtherLayout otherLayout;
    private void setOther(){

        otherLayout=findViewById(R.id.otherLayoutCourseDetailVideo);
        otherLayout.setActivity(CourseDetailVideoActivity.this);
        /**
         * ?????????????????????????????????
         * ????????????????????????????????????????????????
         */
        otherLayout.setData(getIntent().getStringExtra(name),testImage,testContent,testImage);
    }

    //------------------setAllCourse
    private AllCourseLayout allCourseLayout;
    private void setAllCourse(){
        allCourseLayout=findViewById(R.id.allCourseLayoutCourseDetailVideo);
        allCourseLayout.setListener(new AllCourseLayout.Listener() {
            @Override
            public void onItemChange(String url) {
                setCurVideo(url);
            }
        });
        /**
         * ????????????????????????????????????
         * setTime ????????????
         * setUrl ??????????????????
         * setTitle ????????????????????????
         * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * ????????????????????????????????? allCourseLayout??????
         */
        List<CourseItem> courseItemList=new ArrayList<>();
        //???????????????????????????????????????????????????????????????
        for(int i=1;i<=3;i++){
            CourseItem courseItem=new CourseItem();
            courseItem.setTime("2:02:50");
            courseItem.setTitle("???????????????");
            courseItem.setUrl(video_path);
            courseItemList.add(courseItem);
        }
        allCourseLayout.setData(courseItemList.size(),courseItemList);
    }
    //------------------setIntroduce
    private LinearLayout linearLayoutIntroduce;
    private IntroduceLayout introduceLayout;
    private void setIntroduce(){
        linearLayoutIntroduce=findViewById(R.id.linerLayoutCourseDetailVideoOther);
        introduceLayout=findViewById(R.id.introduceLayoutCourseDetailVideo);
        /**
         * ???????????????????????????????????????????????????????????????
         * ????????????????????????????????????????????????
         */
        String titles = getIntent().getStringExtra(title);
        String names = getIntent().getStringExtra(name);
        introduceLayout.setData(titles,names,testContent,30,225L);
    }
    //------------------setBottom
    private FrameLayout frameLayoutVideo;
    private CardView cardViewBottom;
    private void setBottom(){
//        frameLayoutBottom=findViewById(R.id.frameLayoutCourseDetailVideoBottom);
//        cardViewBottom=findViewById(R.id.cardViewCourseDetailVideoBottom);
//        cardViewBottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * ?????????????????????????????????
//                 */
//            }
//        });

    }

    /**
     * ?????????????????????????????????????????????~
     */
    //------------------setVideo
    private ExoPlayer player;
    private PlayerView playerView;
    private Context context;
    private ImageButton imageButtonFullScreen;
    private boolean isFullScreen=false;
    private final int videoHeight=250;
    private int screenWidth,screenHeight;
    private final int timeLen=240,timeLenFull=640;
    private DefaultTimeBar defaultTimeBar;
    private FrameLayout frameLayoutBottom;
    private DataSource.Factory dataSourceFactory;
    //------------------setCurVideo
    private void setCurVideo(String url){
        if(player!=null){
            player.release();
            player=null;
        }
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url));
        player=new SimpleExoPlayer.Builder(context).build();
        player.setPlayWhenReady(true);
        player.prepare(mediaSource);
        playerView.setPlayer(player);
    }
    private final int flagsFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    private NestedScrollView nestedScrollView;
    private void goFullScreen(){
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
        getWindow().addFlags(flagsFullScreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        frameLayoutControl.setBackgroundColor(Color.BLACK);
        frameLayoutVideo.setLayoutParams(new
                LinearLayout.LayoutParams(screenHeight,screenWidth));
        defaultTimeBar.setLayoutParams(new
                LinearLayout.LayoutParams(getPixelsFromDp(timeLenFull),-1));
        frameLayoutBottom.setVisibility(View.INVISIBLE);
        linearLayoutIntroduce.setVisibility(View.INVISIBLE);
        imageButtonBack.setOnClickListener(goNormalListener);
        imageButtonFullScreen.setVisibility(View.INVISIBLE);
        textViewSpeed.setVisibility(View.VISIBLE);
        isFullScreen=true;
    }
    private void goNormal(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~flagsFullScreen);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        frameLayoutControl.setBackgroundColor(Color.TRANSPARENT);
        frameLayoutVideo.setLayoutParams(new LinearLayout.LayoutParams
                (screenWidth,getPixelsFromDp(videoHeight)));
        defaultTimeBar.setLayoutParams(new
                LinearLayout.LayoutParams(getPixelsFromDp(timeLen),-1));
        frameLayoutBottom.setVisibility(View.VISIBLE);
        linearLayoutIntroduce.setVisibility(View.VISIBLE);
        imageButtonBack.setOnClickListener(backListener);
        imageButtonFullScreen.setVisibility(View.VISIBLE);
        textViewSpeed.setVisibility(View.INVISIBLE);
        isFullScreen=false;
    }
    private FrameLayout frameLayoutControl;
    private View.OnClickListener fullScreenListener;
    private View.OnClickListener backListener;
    private View.OnClickListener goNormalListener;
    private ImageButton imageButtonBack;
    private TextView textViewSpeed;
    private Boolean isTwo=false;
    @SuppressLint("RestrictedApi")
    private void setVideo(){
        nestedScrollView=findViewById(R.id.nestedScrollViewCourseDetailVideo);
        frameLayoutControl=findViewById(R.id.frameLayoutCourseDetailVideoControl);
        imageButtonBack=findViewById(R.id.imageButtonCourseDetailVideoBack);
        frameLayoutControl.setBackgroundColor(Color.TRANSPARENT);
        defaultTimeBar=findViewById(R.id.exo_progress);
        playerView=findViewById(R.id.playerViewCourseDetailVideo);
        textViewSpeed=findViewById(R.id.textViewCourseDetailVideoSpeed);
        textViewSpeed.setVisibility(View.INVISIBLE);isTwo=false;
        textViewSpeed.setText("1x");
        textViewSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTwo){
                    PlaybackParameters playbackParameters=new PlaybackParameters(1f,1f);
                    if(player!=null)player.setPlaybackParameters(playbackParameters);
                    isTwo=false;
                    textViewSpeed.setText("1x");
                }else{
                    PlaybackParameters playbackParameters=new PlaybackParameters(2f,1f);
                    if(player!=null)player.setPlaybackParameters(playbackParameters);
                    isTwo=true;
                    textViewSpeed.setText("2x");
                }
            }
        });
        imageButtonFullScreen=findViewById(R.id.imageButtonCourseDetailFullScreen);
        imageButtonFullScreen.setVisibility(View.VISIBLE);
        frameLayoutVideo=findViewById(R.id.frameLayoutCourseDetailPlayerView);
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context,"CourseDetailVideoExoPlayer"));
        fullScreenListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFullScreen==false){
                    goFullScreen();
                }else{
                    goNormal();
                }
            }
        };
        backListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        goNormalListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNormal();
            }
        };
        imageButtonFullScreen.setOnClickListener(fullScreenListener);
        imageButtonBack.setOnClickListener(backListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player!=null){
            player.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player!=null){
            player.setPlayWhenReady(false);
        }
    }
    private int getPixelsFromDp(int x){
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (x*metrics.densityDpi)/DisplayMetrics.DENSITY_DEFAULT;
    }

}