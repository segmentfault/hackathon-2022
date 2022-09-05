package com.example.jlu.myapplication;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Share {
    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_SINA = "com.sina.weibo";
    public static final String AUTHORITY = "com.gudd.demo.fileprovider";
    private static final int VERSION_CODE_FOR_WEI_XIN_VER7 = 1380;
    // 判断是否安装指定app
    public static boolean ifInstallApp(Context context, String app_package){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null)
        {
            for (int i = 0; i < pInfo.size(); i++)
            {
                String pn = pInfo.get(i).packageName;
                if (app_package.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 直接分享纯文本内容至QQ好友
     * @param mContext
     */
    public static void shareTextToQQFriend(Context mContext, String content) {
        if (ifInstallApp(mContext, Share.PACKAGE_MOBILE_QQ)) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "您需要安装QQ客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享图片给QQ好友
     *
     * @param bitmap
     */
    public static void shareImageToQQFriend(Context mContext,Bitmap bitmap) {
        if (Share.ifInstallApp(mContext, Share.PACKAGE_MOBILE_QQ)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        mContext.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");

                shareIntent.setComponent(componentName);
                // mContext.startActivity(shareIntent);
                mContext.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (Exception e) {
//            ContextUtil.getInstance().showToastMsg("分享图片到**失败");
            }
        }
    }
    /**
     * 直接分享图片到微信好友
     * @param context
     * @param picFile
     */
    public static void shareImageToWechatFriend(Context mContext, Bitmap bmp) {
        if (Share.ifInstallApp(mContext, Share.PACKAGE_WECHAT)) {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
            String fileName = "share";
            File appDir = new File(file, fileName);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            fileName = System.currentTimeMillis() + ".jpg";
            File currentFile = new File(appDir, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(currentFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<Uri> uris = new ArrayList<>();
            Uri uri = null;
            try {
                ApplicationInfo applicationInfo = mContext.getApplicationInfo();
                int targetSDK = applicationInfo.targetSdkVersion;
                if (targetSDK >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(mContext.getContentResolver(), currentFile.getAbsolutePath(), fileName, null));
                } else {
                    uri = Uri.fromFile(new File(currentFile.getPath()));
                }
                uris.add(uri);
            } catch (Exception ex) {

            }
            ComponentName cop = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
            Intent intent = new Intent();
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (getVersionCode(mContext, PACKAGE_WECHAT) > VERSION_CODE_FOR_WEI_XIN_VER7) {
                // 微信7.0及以上版本
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            mContext.startActivity(Intent.createChooser(intent, "Share"));
        } else {
            Toast.makeText(mContext, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 直接分享文本到微信好友
     *
     * @param context 上下文
     */
    public static void shareTextToWechatFriend(Context mContext, String content) {
        if (Share.ifInstallApp(mContext, Share.PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("android.intent.extra.TEXT", content);
//            intent.putExtra("sms_body", content);
            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 分享多图片到微信朋友圈
     *
     * @param bmp 分享的图片的Bitmap对象
     */
    public static void shareImageToWechat(Context mContext,Bitmap bmp) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        String fileName = "share";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = System.currentTimeMillis() + ".jpg";
        File currentFile = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Uri> uris = new ArrayList<>();
        Uri uri = null;
        try {
            ApplicationInfo applicationInfo = mContext.getApplicationInfo();
            int targetSDK = applicationInfo.targetSdkVersion;
            if (targetSDK >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(mContext.getContentResolver(), currentFile.getAbsolutePath(), fileName, null));
            } else {
                uri = Uri.fromFile(new File(currentFile.getPath()));
            }
            uris.add(uri);
        } catch (Exception ex) {

        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ComponentName comp = new ComponentName(PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setType("image/*");
        if (getVersionCode(mContext, PACKAGE_WECHAT) < VERSION_CODE_FOR_WEI_XIN_VER7) {
            // 微信7.0以下版本
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            // 微信7.0及以上版本
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        mContext.startActivity(intent);
    }
    /**
     * 分享到新浪微博
     *
     * @param photoPath 文件路径
     */
    public static void shareToSinaFriends(Context mContext, String picFile) {
        if (!ifInstallApp(mContext, Share.PACKAGE_SINA)) {
            Toast.makeText(mContext, "新浪微博没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        ComponentName cop = new ComponentName(Share.PACKAGE_SINA, "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(cop);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri uri = null;
        if (picFile != null) {
            //这部分代码主要功能是判断了下文件是否存在，在android版本高过7.0（包括7.0版本）
            // 当前APP是不能直接向外部应用提供file开头的的文件路径，需要通过FileProvider转换一下。否则在7.0及以上版本手机将直接crash。
            try {
                ApplicationInfo applicationInfo = mContext.getApplicationInfo();
                int targetSDK = applicationInfo.targetSdkVersion;
                if (targetSDK >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), new File(picFile).getAbsolutePath(), "pangu", null));
                } else {
                    uri = Uri.fromFile(new File(picFile));
                }
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(intent, "Share"));
    }
    /**
     * 获取制定包名应用的版本的versionCode
     *
     * @param context
     * @param
     * @return
     */
    private static int getVersionCode(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
//  Bitmap image = ((BitmapDrawable) main_photo.getDrawable()).getBitmap();
//  Bitmap bitmap = Bitmap.createBitmap(image);
//  String path = SmokeList.get(0).url;
//  File file = new File(path);
//  Share.shareImageToQQFriend(ShowResult.this,bitmap);
//  Share.shareImageToWechat(Context mContext,Bitmap bmp)
//  Share.shareTextToWechatFriend(Context mContext, String content)