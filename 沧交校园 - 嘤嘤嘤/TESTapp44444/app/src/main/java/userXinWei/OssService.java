package userXinWei;

import android.text.format.DateFormat;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import java.io.File;
import java.util.Date;
import ShuJuKu.QuanJU;

public class OssService {

        //与个人的存储区域有关
        private static final String ENDPOINT = "oss-cn-zhangjiakou.aliyuncs.com";
        //上传仓库名
        private static final String BUCKET_NAME = "tiezi-shuju";

        private static OSS getOSSClient() {
            OSSCredentialProvider credentialProvider =
                    new OSSPlainTextAKSKCredentialProvider("LTAI5tF2NgqSorCkg5sKG1qQ" ,
                            "axguUcq2g69X5YNAGIyF61AaBbKUge");
            return new OSSClient( QuanJU.getContext(), ENDPOINT, credentialProvider);
        }

        /**
         * 上传方法
         *
         * @param objectKey 标识
         * @param path      需上传文件的路径
         * @return 外网访问的路径
         */
        private static String upload(String objectKey, String path) {

            // 构造上传请求
            PutObjectRequest request =
                    new PutObjectRequest(BUCKET_NAME,
                            objectKey, path);
            try {
                //得到client
                OSS client = getOSSClient();
                //上传获取结果
                PutObjectResult result = client.putObject(request);
                //获取可访问的url
                String url = client.presignPublicObjectURL(BUCKET_NAME, objectKey);

                return url;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        /**
         * 上传普通图片
         *
         * @param path 本地地址
         * @return 服务器地址
         */
        public static String uploadImage(String path) {
            String key = getObjectImageKey(path);
            return upload(key, path);
        }

        /**
         * 上传头像
         *
         * @param path 本地地址
         * @return 服务器地址
         */
        public static String uploadPortrait(String path,String name) {
            String key = getObjectPortraitKey(name);
            return upload(key, path);
        }

        /**
         * 上传audio
         *
         * @param path 本地地址
         * @return 服务器地址
         */
        public static String uploadAudio(String path) {
            String key = getObjectAudioKey(path);
            return upload(key, path);
        }


        /**
         * 获取时间
         *
         * @return 时间戳 例如:201805
         */
        private static String getDateString() {
            return DateFormat.format("yyyyMM", new Date()).toString();
        }

        /**
         * 返回key
         *
         * @param path 本地路径
         * @return key
         */
        //格式: image/201805/sfdsgfsdvsdfdsfs.jpg
        private static String getObjectImageKey(String path) {
            String fileMd5 = HashUtil.getMD5String(new File(path));
            String dateString = getDateString();
            return String.format("image/TieZi/%s/%s.jpg", dateString, fileMd5);
        }

        //格式: portrait/201805/19833197397touxiang.jpg,专门用来上传头像
        private static String getObjectPortraitKey(String name) {
            String dateString = getDateString();
            return String.format("TouXiang/%s/%s.jpg", dateString, name+"touxiang");
        }

        //格式: audio/201805/sfdsgfsdvsdfdsfs.mp3
        private static String getObjectAudioKey(String path) {
            String fileMd5 = HashUtil.getMD5String(new File(path));
            String dateString = getDateString();
            return String.format("video/%s/%s.mp3", dateString, fileMd5);
        }
}

