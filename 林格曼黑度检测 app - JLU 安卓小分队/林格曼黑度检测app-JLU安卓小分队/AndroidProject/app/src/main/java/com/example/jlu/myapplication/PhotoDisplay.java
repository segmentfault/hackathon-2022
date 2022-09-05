package com.example.jlu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dtflys.forest.config.ForestConfiguration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mingle.widget.ShapeLoadingDialog;
import com.yalantis.ucrop.UCrop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import android.graphics.Rect;
import android.widget.Toast;

import org.json.JSONArray;

import id.zelory.compressor.Compressor;

public class PhotoDisplay extends AppCompatActivity {
    private ImageView photo;
    private Uri photoUri;
    private String newUri = "";
    private String op_sd = "";
    private ShapeLoadingDialog shapeLoadingDialog;
    private int offset = 0;
    public static class Msg {
        public int code;
        public String message;
        public Data data;
        public static class Data{
            public String main_img;
            public Smoke[] sub_img;
        }
    }
    private List<Smoke> SmokeList;
    private void init()
    {
        Button crop,confirm;
        crop = findViewById(R.id.go_edit);
        Drawable drawableCamera = getResources().getDrawable(R.drawable.crop);
        drawableCamera.setBounds(0, 0, 90, 90);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        crop.setCompoundDrawables(null, drawableCamera, null, null);//只放上面
        confirm = findViewById(R.id.go_confirm);
        Drawable drawableAlbum = getResources().getDrawable(R.drawable.confirm);
        drawableAlbum.setBounds(0, 0, 90, 90);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        confirm.setCompoundDrawables(null, drawableAlbum, null, null);//只放上面
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_photo_display);
        init();
        photo = (ImageView) findViewById(R.id.photo);
        shapeLoadingDialog = new ShapeLoadingDialog(PhotoDisplay.this);
        shapeLoadingDialog.setLoadingText("Calcing...");
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
        Bitmap bitmap = null;
        Intent intent = getIntent();
        final String path = intent.getStringExtra("image");

        if (path.equals("null"))
            photo.setImageResource(R.drawable.a);
        else {
            try {
                File file = new File(path);
                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(path);
                    photo.setImageBitmap(bitmap);
                    photoUri = Uri.parse("file://" + path);
                }
            } catch (Exception e) {
            }
        }
        Button button = (Button) findViewById(R.id.go_confirm);
        button.setOnClickListener(view -> {
            if(!Method.isNetworkConnected(PhotoDisplay.this))
            {
                Toast.makeText(PhotoDisplay.this, "网络无连接！", Toast.LENGTH_SHORT).show();
                return ;
            }
            final String opd = photoUri.toString().substring(7);
            shapeLoadingDialog.show();
            ForestConfiguration forest = ForestConfiguration.configuration();
            MyClient myClient = forest.createInstance(MyClient.class);
            offset = 0;
            compress(opd);
            new Thread(
                () -> {
                try{
                    String result = myClient.upload(opd, progress -> {
                        System.out.println("total bytes: " + progress.getTotalBytes());   // 文件大小
                        System.out.println("current bytes: " + progress.getCurrentBytes());   // 已上传字节数
                        System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已上传百分比
                        if (progress.isDone()) {   // 是否上传完成
                            System.out.println("--------   Upload Completed!   --------");
                        }
                    });
                    SmokeList = new ArrayList<>();
                    Gson gson = new Gson();
                    Msg msg = new Gson().fromJson(result, Msg.class);
                    //对象中拿到集合
                    Msg.Data data = msg.data;

                    File file;
                    Smoke tmp;
                    op_sd = Method.getTimeStr();

                    file = myClient.downloadFile(
                            getExternalFilesDir(null).getPath(),
                            op_sd + String.valueOf(offset) + ".jpg",
                            progress -> {
                                System.out.println("total bytes: " + progress.getTotalBytes());   // 文件大小
                                System.out.println("current bytes: " + progress.getCurrentBytes());   // 已下载字节数
                                System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已下载百分比
                                if (progress.isDone()) {   // 是否下载完成
                                    System.out.println("--------   Main Img Download Completed!   --------");
                                }
                            }, data.main_img);

                    tmp = new Smoke();
                    tmp.url = getExternalFilesDir(null).getPath() + "/" + op_sd + String.valueOf(offset) + ".jpg";
                    tmp.level = "-1";
                    SmokeList.add(tmp);
                    offset++;

                    for (int i = 0, len = data.sub_img.length; i < len; i++) {
                        String downloadPath = data.sub_img[i].url;
                        Log.v("tts", downloadPath);
                        op_sd = Method.getTimeStr();
                        file = myClient.downloadFile(
                                getExternalFilesDir(null).getPath(),
                                op_sd + String.valueOf(offset) + ".jpg",
                                progress -> {
                                    System.out.println("total bytes: " + progress.getTotalBytes());   // 文件大小
                                    System.out.println("current bytes: " + progress.getCurrentBytes());   // 已下载字节数
                                    System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已下载百分比
                                    if (progress.isDone()) {   // 是否下载完成
                                        System.out.println("--------   Sub Img Download Completed!   --------");
                                    }
                                },
                                downloadPath);

                        tmp = new Smoke();
                        tmp.url = getExternalFilesDir(null).getPath() + "/" + op_sd + String.valueOf(offset) + ".jpg";
                        tmp.level = data.sub_img[i].level;
                        SmokeList.add(tmp);
                        offset++;
                    }

                    runOnUiThread(() -> {
                        Uri downloadUri = Uri.parse("file://" + SmokeList.get(0).url);
                        shapeLoadingDialog.dismiss();
                        final Intent intent2  =  new Intent(PhotoDisplay.this,ShowResult.class);
                        final Gson gson2 = new Gson();
                        intent2.putExtra("data",gson2.toJson(SmokeList));
                        startActivity(intent2);
                        shapeLoadingDialog.setLoadingText("Calcing... ");
                    });
                }catch(Exception  e){
                    shapeLoadingDialog.dismiss();
                    Looper.prepare();
                    Toast.makeText(PhotoDisplay.this, "网络无连接！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return ;
                }
                }).start();
        });
        Button button2 = (Button) findViewById(R.id.go_edit);
        button2.setOnClickListener((view) -> {
            String sd = Method.getTimeStr();
            newUri = "file://" + getExternalFilesDir(null).getPath() + "/" + sd + ".jpg";

            UCrop uCrop = UCrop.of( photoUri,  Uri.parse(newUri));
            UCrop.Options options = new UCrop.Options();

            //设置toolbar颜色
            options.setToolbarColor(ActivityCompat.getColor(PhotoDisplay.this, R.color.colorPrimary));
            //设置状态栏颜色
            options.setStatusBarColor(ActivityCompat.getColor(PhotoDisplay.this, R.color.colorPrimary));
            options.setFreeStyleCropEnabled(true);

            uCrop.withOptions(options);
            uCrop.start(PhotoDisplay.this);
        });
    }

    protected void compress(String path) {
        try {
            File new_file = new Compressor(PhotoDisplay.this).compressToFile(new File(path));
            FileOutputStream fos = new FileOutputStream(new File(path));
            Bitmap bitmap = BitmapFactory.decodeFile(new_file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            photoUri = resultUri;
            photo.setImageURI(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}