package com.example.jlu.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChoosePhoto extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private ImageView photo;
    private Uri imageUri;
    private Button goNext;

    private void init()
    {
        TextView tv_about = (TextView)findViewById(R.id.about);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentname = new ComponentName(ChoosePhoto.this, AboutActivity.class);
                Intent intent = new Intent();
                intent.setComponent(componentname);
                startActivity(intent);
            }
        });
        Button camera,album,next;
        camera = findViewById(R.id.take_photo);
        Drawable drawableFirst = getResources().getDrawable(R.drawable.camera);
        drawableFirst.setBounds(0, 0, 90, 90);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        camera.setCompoundDrawables(null, drawableFirst, null, null);//只放上面
        album = findViewById(R.id.choose_photo);
        Drawable drawableSearch = getResources().getDrawable(R.drawable.album);
        drawableSearch.setBounds(0, 0, 90, 90);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        album.setCompoundDrawables(null, drawableSearch, null, null);//只放上面
        next = findViewById(R.id.go_next);
        Drawable drawableNext = getResources().getDrawable(R.drawable.next_trans);
        drawableNext.setBounds(0, 0, 200, 80);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        next.setCompoundDrawables(null, null, drawableNext, null);//只放上面
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        init();
        photo = (ImageView) findViewById(R.id.photo);

        goNext = (Button) findViewById(R.id.go_next);
        goNext.setVisibility(View.GONE);
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = Method.setimage(photo);
                String path = Method.saveImageToCache(bitmap,ChoosePhoto.this);
                Intent intent = new Intent(ChoosePhoto.this,PhotoDisplay.class);
                intent.putExtra("image",path);
                startActivity(intent);
            }
        });

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = FileProvider.getUriForFile(ChoosePhoto.this,
                        "com.example.cameraalbumtest.fileprovider",outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                // 指定图片的输出地址
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        Button choosePhoto = (Button) findViewById(R.id.choose_photo);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ChoosePhoto.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChoosePhoto.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    // openAlbum
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent,CHOOSE_PHOTO);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        photo.setImageBitmap(bitmap);
                        goNext.setVisibility(View.VISIBLE);
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    photo.setImageURI(uri);
                    goNext.setVisibility(View.VISIBLE);
                    Log.wtf("tts",uri.getPath());
                }
                break;
            default:
                break;
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