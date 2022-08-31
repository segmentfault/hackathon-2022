package com.example.gchat.utils;

/**
 * User: xiahao
 * DateTime: 2022/4/15 18:05
 * Description:配置系统全局变量
 * 主要是放当前用户的姓名或者其他信息
 */
public class APPglobal {
    public static  int id; // 当前登录用户id
    public static  String USERNAME="";     //当前登录用户用户名
    public static  String NAME="";         //当前登录用户真实姓名

    public static void main(String[] args) {
        System.out.println(USERNAME);
    }

}
