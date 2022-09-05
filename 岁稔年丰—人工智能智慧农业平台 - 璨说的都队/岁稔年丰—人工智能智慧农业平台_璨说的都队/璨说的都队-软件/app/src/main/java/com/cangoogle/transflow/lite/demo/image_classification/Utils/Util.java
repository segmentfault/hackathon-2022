package com.baidu.paddle.lite.demo.image_classification.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * 工具类
 */
public class Util {
    private Util() {
    }
    public static boolean isPhone(String phone){

        String str="^([0-9]{11})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phone);
        return m.matches();
    }



}