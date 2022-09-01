package com.example.testapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuCaoZuo;
import ShuJuKu.userXinXi;
import userXinWei.BitmapToRound_Util;
import userXinWei.OssService;

public class userActivity<LocalActivity> extends Activity implements View.OnClickListener {
    Bundle bundle=null;
    Bitmap bitmap=null;
    String imagePath = null;
    LoadingDailog dialog;
    public static final int PICK_PHOTO = 102;
    public static final int TAKE_CAMERA = 101;
    private Uri imageUri;
    private BitmapToRound_Util round_Util = new BitmapToRound_Util();
    /**
     * 页面实例化后第一件事是根据用户来设置头像和用户名的显示
     * 目前几个按钮中，完成了修改密码和关于我们和软件版本，剩余消息通知和给个好评
     * 消息通知需要与后续结合，给个好评需要结合应用商店
     */
    private TextView username;
    private ImageView photoUrl;
    private Button tongzhi;
    private Button PinLun;
    private Button XiuGaiMiMa;
    private Button GuanYu;
    private Button HaoPin;
    private Button BanBen;
    private Button YinSi;
    private Button HELP;
    private Button quit;

    /**
     * 导航栏.......
     *
     */
    ImageView imageView7;//主页
    ImageView imageView9;//发布

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        username = findViewById(R.id.username_id);
        photoUrl = findViewById(R.id.photoUrl_id);
        PinLun=findViewById(R.id.wodetiezi);
        tongzhi=findViewById(R.id.notification_et);
        XiuGaiMiMa=findViewById(R.id.change_password_et);
        GuanYu=findViewById(R.id.about_us_et);
        HaoPin=findViewById(R.id.good_comment_et);
        BanBen=findViewById(R.id.software_version_et);
        YinSi=findViewById(R.id.yonghushouce);
        HELP=findViewById(R.id.wodepl);
        quit=findViewById(R.id.sign_out_et);

        tongzhi.setOnClickListener(this);
        PinLun.setOnClickListener(this);
        GuanYu.setOnClickListener(this);
        HaoPin.setOnClickListener(this);
        BanBen.setOnClickListener(this);
        YinSi.setOnClickListener(this);
        HELP.setOnClickListener(this);
        quit.setOnClickListener(this);

        imageView7=findViewById(R.id.imageView7);
        imageView9=findViewById(R.id.imageView9);
        imageView7.setOnClickListener(this);
        imageView9.setOnClickListener(this);

        if(QuanJU.getInstance().isDengLu!=0){
            quit.setText("退出登录");
            photoUrl.setOnClickListener(this);//只有登录之后才能更换头像和换头像和退出登录
            XiuGaiMiMa.setOnClickListener(this);
            //如果登录了就能显示头像了，并且是圆形的，使用了谷歌的glide框架
            Glide.with(this).load(QuanJU.getInstance().touxiang).apply(RequestOptions.bitmapTransform(new CircleCrop())).into( photoUrl);
            username.setText(QuanJU.getInstance().username);
        }
    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.imageView7) {//主页
            Intent i=new Intent(this,ZhuYeActivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.imageView9) {//发布
            Intent i=new Intent(this,FaTieAcivity.class);
            startActivityForResult(i, 1);
        }

        /**
         * 上面是导航栏...
         */
        if(view.getId()==R.id.yonghushouce){//隐私协议
            Intent i=new Intent(this,YinSiActivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.wodepl){
            Intent i=new Intent(this,HELPActivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.notification_et){//我的帖子
            if(QuanJU.getInstance().isDengLu==0){//如果不是登录状态
                Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                return;
            }else{
                Intent i=new Intent(this,WoDeTieZiActivity.class);
                startActivityForResult(i, 1);
            }
        }
        if(view.getId()==R.id.wodetiezi){//我的评论
            if(QuanJU.getInstance().isDengLu==0){//如果不是登录状态
                Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                return;
            }else{
                Intent i=new Intent(this,WoDePinLunActivity.class);
                startActivityForResult(i, 1);
            }
        }
        if(view.getId()==R.id.change_password_et){//修改密码
            Intent i=new Intent(this,XiuGaiMiMaActivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.about_us_et){//关于我们
            Intent i=new Intent(this,GuanYuActivity.class);
            this.startActivity(i);
        }
        if(view.getId()==R.id.good_comment_et){//好评

        }
        if(view.getId()==R.id.software_version_et){//版本
            Intent i=new Intent(this,BanBenActivity.class);
            this.startActivity(i);
        }
        if(view.getId()==R.id.sign_out_et){//退出登录或登录
            if(QuanJU.getInstance().isDengLu==1){//如果是登录状态
                QuanJU.getInstance().isDengLu=0;
                onCreate(null);
            } else{
                Intent i=new Intent(this,MainActivity.class);
                this.startActivity(i);
            }
        }
        if (view.getId() == R.id.photoUrl_id) {//头像
            if (ContextCompat.checkSelfPermission(userActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(userActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO); // 打开相册
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                OssService uploadHelper = new OssService();
                String uploaduel = uploadHelper.uploadPortrait(imagePath,QuanJU.getInstance().shoujihao);// 返回的是头像的保存的路径
                //数据库插入
                try {
                    new ShuJuKuCaoZuo().touxinag(uploaduel);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
            }

        }).start();
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

            BitmapFactory.Options options=new BitmapFactory.Options();
            BitmapFactory.decodeFile(imagePath,options);
            options.inSampleSize=calculateInSampleSize(options,300,300);
            options.inJustDecodeBounds=false;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
            //sdk 大于6.0的判断
            if (Build.VERSION.SDK_INT >= 23) {
                int REQUEST_CODE_CONTACT = 101;
                String[] permissions = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //验证是否许可权限
                for (String str : permissions) {
                    if (userActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限
                        userActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                        return;
                    } else {
                        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/MyTest2";
                        File files = new File(path);
                        if (!files.exists()) {
                            files.mkdirs();
                        }
                        try {
                            BitmapToRound_Util round_Util = new BitmapToRound_Util();
                            bitmap = round_Util.toRoundBitmap(bitmap);
                            photoUrl.setImageBitmap(bitmap);
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

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            dialog.dismiss();// 关闭ProgressDialog
        }
    };
}

