package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.io.ByteArrayOutputStream;

public class VideoCaptureActivity extends AppCompatActivity {
    private final String TAG="VideoCaptureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane);
        context=VideoCaptureActivity.this;
        if(MyLocation.PermissionEnough(context)){
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            MyLocation.getLocationPermission(context);
        }
        screenHeight=getWindowManager().getDefaultDisplay().getHeight();
        screenWidth=getWindowManager().getDefaultDisplay().getWidth();

        setVideo();
        initGetPicture();
    }

    private final String PUBLIC_ID="HE2204072056451348";
    private final String PRIVATE_KEY="7b2a997ae66042f89c1bf71cde783495";
    private String ans="";


    private boolean haveWeatherInit=false;
    private void getWeather(){
        if(!haveWeatherInit){
            HeConfig.init(PUBLIC_ID, PRIVATE_KEY);
            HeConfig.switchToDevService();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAG", "onRequestPermissionsResult: " + Build.VERSION_CODES.M);
        switch (requestCode){
            case 1000:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context,"请先授权",Toast.LENGTH_SHORT);
                        onDestroy();
                    } else {
                        if(MyLocation.PermissionEnough(VideoCaptureActivity.this)){
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(context,"权限不足，请先打开手机设置授权"
                                    ,Toast.LENGTH_SHORT);
                        }
                    }
                }
                break;
        }
    }

    private ImageView button;
    private Bitmap showBitmap;
    private TextureView videoSurfaceView;
    private ImageView imageView;

    private void initGetPicture(){
        videoSurfaceView = (TextureView) playerView.getVideoSurfaceView();
        button=(ImageView)findViewById(R.id.video_capture_button);
        imageView=(ImageView)findViewById(R.id.video_capture_imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBitmap=videoSurfaceView.getBitmap();
                byte[] result;  //将bitmap转化的byte数组
                ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                showBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                result = output.toByteArray();//result是一个bit的资源数组
                Intent intent = new Intent(VideoCaptureActivity.this,aIActivitya.class);
                intent.putExtra(aIActivitya.IMG,result);
                startActivity(intent);

//                imageView.setImageBitmap(showBitmap);
//                getWeather();
            }
        });
    }
    private final String video_path="https://www.twoyl.cn/yaogan.mp4";
    private ExoPlayer player;
    private PlayerView playerView;
    private Activity context;
    private ImageButton imageButtonFullScreen;
    private boolean isFullScreen=false;
    private final int videoHeight=250;
    private int screenWidth,screenHeight;
    private final int timeLen=240,timeLenFull=640;
    private DefaultTimeBar defaultTimeBar;
    private FrameLayout frameLayoutBottom;
    private DataSource.Factory dataSourceFactory;

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
    private FrameLayout frameLayoutControl;
    private View.OnClickListener fullScreenListener;
    private View.OnClickListener backListener;
    private View.OnClickListener goNormalListener;
    private ImageButton imageButtonBack;
    private TextView textViewSpeed;
    private Boolean isTwo=false;
    private void setVideo(){
        frameLayoutControl=findViewById(R.id.frameLayoutCourseDetailVideoControl);
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
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context,"CourseDetailVideoExoPlayer"));


        setCurVideo(video_path);
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