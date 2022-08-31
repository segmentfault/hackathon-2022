package com.example.gchat.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.gchat.MainActivity;
import com.example.gchat.R;
import java.io.File;

/**
 * User: xiahao
 * DateTime: 2022/4/15 16:52
 * Description:保存拍照的图片
 * 需要重写onActivityResult()方法
 */
public class CameraActivity  extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory() + "/QQ_Test/";//自定保存路径
    private String fileName;//文件名称


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        File file = new File(path);//创建文件
        if (!file.exists()) {
            file.mkdir();
        }
        fileName = String.valueOf(System.currentTimeMillis())+".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + fileName)));
        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);

        //返回首页
        Button button= (Button) findViewById(R.id.cgohome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CameraActivity.this, MainActivity.class);
                CameraActivity.this.startActivity(intent);
            }
        });

    }

    /**
     * 想在Activity中得到新打开Activity关闭后返回的数据，你需要使用系统提供的
     * startActivityForResult(Intent intent,int requestCode)方法打开新的Activity，
     * 新的Activity关闭后会向前面的Activity传回数据，为了得到传回的数据，
     * 你必须在前面的Activity中重写onActivityResult(int requestCode, int resultCode,Intent data)方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(CameraActivity.this, "图片已保存，请返回首页！", Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case Activity.DEFAULT_KEYS_DIALER: {
                File file = new File(path + fileName);
                Log.e("mTag", file.length() / 1024 + "");
                break;
            }
        }

        //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到保存的图片了
        File file = new File(path + fileName);
        Uri localUri = Uri.fromFile(file);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        sendBroadcast(localIntent);

    }



}
