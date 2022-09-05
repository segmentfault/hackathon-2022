package com.baidu.paddle.lite.demo.image_classification.des;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DES {



    public static String eDES(String string){

        try {

                if (TextUtils.isEmpty(string)) {
                    return "";
                }
                MessageDigest md5 = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                    byte[] bytes = md5.digest(string.getBytes());
                    StringBuilder result = new StringBuilder();
                    for (byte b : bytes) {
                        String temp = Integer.toHexString(b & 0xff);
                        if (temp.length() == 1) {
                            temp = "0" + temp;
                        }
                        result.append(temp);
                    }
                    return result.toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
