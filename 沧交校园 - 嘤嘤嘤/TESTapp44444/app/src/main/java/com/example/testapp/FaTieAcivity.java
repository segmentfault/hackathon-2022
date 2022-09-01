package com.example.testapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.android.tu.loadingdialog.LoadingDailog;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuCaoZuo;
import userXinWei.OssService;

public class FaTieAcivity extends AppCompatActivity implements View.OnClickListener{
    final int maxNum = 145;
    TextView leftNum ;//剩余字数
    EditText ShuRu ;//内容输入框
    Spinner spinner;//下拉框
    ImageView imageView;//显示图片的地方
    ImageButton imageButton;//添加图片的地方
    ImageButton imageShanChu;//删除图片
    int nums=0;//默认为0，也就是没有上传过图片，上传后置为1，不能再上传；
    Button button;
    /**
     * 下面定义的属性是与上传图片有关的
     */
    LoadingDailog dialog;
    private Uri imageUri;
    public static final int PICK_PHOTO = 102;
    public static final int TAKE_CAMERA = 101;
    Bitmap bitmap=null;
    String imagePath = null;
    /**
     * 导航栏......
     */
    ImageView imageView7;//主页
    ImageView imageView10;//个人

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fatie);

        leftNum=findViewById(R.id.textView12);
        ShuRu=findViewById(R.id.textView9);
        imageView=findViewById(R.id.imageView);
        imageButton=findViewById(R.id.imageButton);
        imageShanChu=findViewById(R.id.imageButton2);
        button=findViewById(R.id.button4);
        imageButton.setOnClickListener(this);
        imageShanChu.setOnClickListener(this);
        button.setOnClickListener(this);

        imageView7=findViewById(R.id.imageView7);
        imageView10=findViewById(R.id.imageView10);
        imageView7.setOnClickListener(this);
        imageView10.setOnClickListener(this);

        ShuRu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                leftNum.setText("剩余字数："+ (maxNum-s.length()));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.imageButton&&nums==0){//点击上传图片并且没有选择过图片
            if (ContextCompat.checkSelfPermission(FaTieAcivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FaTieAcivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO); // 打开相册
            }
        }
        if(view.getId()==R.id.imageButton&&nums==1){//点击上传图片并且选择了一张图片
            Toast.makeText(FaTieAcivity.this,"只能上传一张图片哦",Toast.LENGTH_SHORT).show();
            return;
        }
        if(view.getId()==R.id.imageButton2&&nums==1){//点击删除图片并且选择了一张图片
            imageView.setImageBitmap(null);
            imageShanChu.setAlpha(0f);
            nums=0;
        }
        if(view.getId()==R.id.button4){//点击发布,未登录则跳转个人界面等待登录
            if(QuanJU.getInstance().isDengLu==0){
                Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,userActivity.class);
                startActivityForResult(i, 1);
                return;
            }
            final String NeiRong=ShuRu.getText().toString();
            /* 显示加载等待框 */
            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                    .setMessage("加载中...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog=loadBuilder.create();
            dialog.show();
            /* 开启一个新线程，在新线程里执行耗时的上传方法 */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String imageURL=null;
                    if(nums==1){//代表有图片
                         OssService uploadHelper = new OssService();
                         imageURL= uploadHelper.uploadImage(imagePath);// 返回的是图片的网络路径
                    }

                    //数据库插入帖子
                    try {
                        Date time = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String times = sdf.format(time);//获取当前时间
                        new ShuJuKuCaoZuo().fatie(QuanJU.getInstance().username,times,NeiRong,imageURL,QuanJU.getInstance().touxiang);


                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消息给handler
                }

            }).start();
        }
        if(view.getId()==R.id.imageView7){//主页
            Intent i=new Intent(this,ZhuYeActivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.imageView10){//个人中心
            Intent i=new Intent(this,userActivity.class);
            startActivityForResult(i, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 判断手机系统版本号
            if (Build.VERSION.SDK_INT >= 19) {
                // 4.4及以上系统使用这个方法处理图片
                try {
                    handleImageOnKitKat(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 4.4以下系统使用这个方法处理图片
                try {
                    handleImageBeforeKitKat(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) throws IOException {

        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
            System.out.println("111"+imagePath);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // 根据图片路径显示图片
        displayImage(imagePath);
    }
    /**
     * android 4.4以前的处理方式
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) throws IOException {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) throws IOException {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            //sdk 大于6.0的判断
            if (Build.VERSION.SDK_INT >= 23) {
                int REQUEST_CODE_CONTACT = 101;
                String[] permissions = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //验证是否许可权限
                for (String str : permissions) {
                    if (FaTieAcivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限
                        FaTieAcivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                        return;
                    } else {
                        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/MyTest2";
                        File files = new File(path);
                        if (!files.exists()) {
                            files.mkdirs();
                        }
                        try {
                            imageView.setImageBitmap(bitmap);
                            nums=1;
                            imageShanChu.setAlpha(1f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            ShuRu.setText("请输入内容");
            imageView.setImageBitmap(null);
            imageShanChu.setAlpha(0f);
            nums=0;
            dialog.dismiss();// 关闭ProgressDialog
            Toast.makeText(FaTieAcivity.this,"发布成功！",Toast.LENGTH_SHORT).show();
        }
    };
}

