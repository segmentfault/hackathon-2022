package com.hui.dict.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    /*
    * 剔除汉字
    * */
    public static String removeDigital(String value){
        Pattern p = Pattern.compile("[\\d]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }

    /*
     * 剔除字母
     * */
    public static String removeLetter(String value){
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }
    /*
     * 剔除字母
     * */
    public static String removePunctuation(String value){
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }

    /**
     * 剔除全部数字，字母，标点符号
     * */
    public static String removeAll(String value){
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|a-zA-Z\\d]");
        Matcher matcher = p.matcher(value);
        String result = matcher.replaceAll("");
        return result;
    }
}
