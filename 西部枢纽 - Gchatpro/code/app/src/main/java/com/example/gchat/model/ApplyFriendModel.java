package com.example.gchat.model;

import com.example.gchat.utils.APPglobal;

/**
 * User: xiahao
 * DateTime: 2022/4/14 18:35
 * Description:申请好友列表实体类
 */
public class ApplyFriendModel {
    private  int act,id;
    private  String name;
    //构造函数

    public ApplyFriendModel(int act,int  id,String name){
        this.act = act;
        this.id = id;
        this.name = name;
    }
    public String getname(){
        return name;

    }
    public int getAct(){
        return act;
    }
    public int getid(){
        return id;
    }
}
