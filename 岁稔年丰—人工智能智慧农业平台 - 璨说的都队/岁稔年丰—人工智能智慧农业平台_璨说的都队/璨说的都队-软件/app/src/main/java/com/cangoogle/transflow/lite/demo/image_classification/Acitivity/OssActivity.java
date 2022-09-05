package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.entity.User;
import com.baidu.paddle.lite.demo.image_classification.entity.t_community;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OssActivity extends AppCompatActivity {
//    private TextView tv,detail;
    private Button camerabutton,playbutton,selectvideo;
    private ProgressBar pb;
    private String path,objectname;
    private EditText filename;
    private String b;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private String typeName;//文件后缀名
    private String getTime;
    private ImageView imageView01;
    public static final int PICK_PHOTO = 102;
    private EditText editText;
    private String content;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oss);
        imageView01 = findViewById(R.id.button1);
        editText = findViewById(R.id.sq_text);
        back = findViewById(R.id.oss_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
            }
        });
        getTime = String.valueOf(Calendar.getInstance().getTimeInMillis());//获取系统当前时间

        findbyid();
        verifyStoragePermissions(OssActivity.this);
    }
    private void findbyid() {
        // TODO Auto-generated method stub
        selectvideo= (Button) findViewById(R.id.camerabutton);
//        tv = (TextView) findViewById(R.id.text);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        camerabutton = (Button) findViewById(R.id.camerabutton);



        camerabutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                beginupload();
            }
        });
    }

    public void select(View view)
    {
        //跳到图库
        Intent intent = new Intent(Intent.ACTION_PICK);
        //选择的格式为视频,图库中就只显示视频（如果图片上传的话可以改为image/*，图库就只显示图片）
        intent.setType("image/*;video/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//        //打开相册
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK_PHOTO); // 打开相册

    }



    @TargetApi(19)
    private void handleImageOmKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果document类型是U日，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是普通类型 用普通方法处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果file类型位uri直街获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection){
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView01.setImageBitmap(bitmap);
        }else {
            Toast.makeText(OssActivity.this,"fail to get image",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("WrongConstant")
    public void beginupload(){
        String myAndroidDeviceId = Settings.Secure.getString(OssActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件,让文件名 + 系统当前时间戳，防止冲突
        String testname = b.replace('.','n');
        objectname="xcsq/" + testname + myAndroidDeviceId +"."+typeName; //名字 + 设备id + 图片
        if(objectname==null||objectname.equals("")){
            Toast.makeText(OssActivity.this, "文件名不能为空", 2000).show();
            return;
        }
        //填写OSS外网域名
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        //填写明文accessKeyId和accessKeySecret
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI5tPcT4tbnNY8dv73Bz3f", "0YYCvgueMgpvZhlXtjiubvuXRKMP5N");
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        //下面3个参数依次为bucket名，Object名，上传文件路径
        Log.d("TAG", "beginupload: " + path);
        PutObjectRequest put = new PutObjectRequest("zhongbeianquan", objectname, path);
        if(path==null||path.equals("")){
//            detail.setText("请选择上传文件");
            return;
        }
//        tv.setText("正在上传中....");
        pb.setVisibility(View.VISIBLE);
        // 异步上传，可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 在这里可以实现进度条展现功能
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                pb.setMax((int) totalSize);
                pb.setProgress((int) currentSize);
            }
        });

        @SuppressWarnings("rawtypes")
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                //去UI线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        tv.setText("UploadSuccess");
                        pb.setVisibility(ProgressBar.INVISIBLE);
//                        initData(objectname);
                        Toast.makeText(OssActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                        User user = User.getCurrentUser(User.class);
                        t_community community = new t_community();
                        community.setContent(content);
                        community.setImg_url("http://www.twoyl.cn/" + objectname);
                        community.setUser(user);
                        community.setUsername(user.getUsername());
                        community.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if(e==null){
                                    Log.d("Bmob", "done: " + "成功");
                                    Intent intent = new Intent(OssActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }else{
                                    Log.d("Bmob", "done: " + "失败");
                                    Toast.makeText(OssActivity.this,"上传失败，请检查网络",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pb.setVisibility(ProgressBar.INVISIBLE);
//                        tv.setText("Uploadfile,localerror");
                        Toast.makeText(OssActivity.this,"Uploadfile,localerror",Toast.LENGTH_LONG).show();

                    }
                });
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Toast.makeText(OssActivity.this,"Uploadfile,servererror",Toast.LENGTH_LONG).show();
//                    tv.setText("Uploadfile,servererror");
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    //返回关闭图库后得到的数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到视频的全路径
                Uri uri = data.getData();
                getRealFilePath(OssActivity.this,uri);
            }
        }

        if (resultCode == RESULT_OK) { // 判断手机系统版本号
            if (Build.VERSION.SDK_INT >= 19) {
                // 4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            } else {
                // 4.4以下系统使用这个方法处理图片
                handleImageBeforeKitKat(data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
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
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // 根据图片路径显示图片
        displayImage(imagePath);
    }


    /* 通过Uri获取路径以及文件名，比如得到的路径 /xx/xx/xx/xx/xxx.jpg，
                             通过索引最后一个/就可以在String中截取了*/
    public  void getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return ;
        final String scheme = uri.getScheme(); //返回当前链接使用的协议
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        path=data;

        b = path.substring(path.lastIndexOf("/") + 1, path.length());
        Log.d("TAG", "getRealFilePath: " + b);
        int dot = b.lastIndexOf('.');
        if ((dot >-1) && (dot < (b.length() - 1))) {
            typeName=b.substring(dot + 1);
        }
//        detail.setText(b);



    }

    private String logPath;


    private void initData(String name) {
        verifyStoragePermissions(OssActivity.this);
        String filePath = "/sdcard/Test/";
        String fileName = "log.txt";
        logPath=filePath+fileName;
        writeTxtToFile(name, filePath, fileName);
        PutObjectRequest put2 = new PutObjectRequest("zhongbeianquan", fileName, logPath);
        put2.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });
        //填写OSS外网域名
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        //填写明文accessKeyId和accessKeySecret
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI5tPcT4tbnNY8dv73Bz3f", "0YYCvgueMgpvZhlXtjiubvuXRKMP5N");
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        OSSAsyncTask task = oss.asyncPutObject(put2, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                //去UI线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        tv.setText("UploadSuccess");
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pb.setVisibility(ProgressBar.INVISIBLE);
//                        tv.setText("Uploadfile,localerror");
                    }
                });
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
//                    tv.setText("Uploadfile,servererror");
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }
}
