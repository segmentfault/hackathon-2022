package com.example.gchat.model;

import com.example.gchat.utils.APPglobal;

/**
 * User: xiahao
 * DateTime: 2022/4/14 18:35
 * Description:聊天消息实体类
 */
public class ChatMsgEntity {

    private String name;//消息来自
    private String date;//消息日期
    private String message;//消息内容
    private boolean isComMeg = true;// 是否为收到的消息

    private String sendp;//发送者
    private String receivep;//接收者

    //构造函数
    public ChatMsgEntity() {

    }

    public ChatMsgEntity(String date,String message,String sendp,String receivep){
        this.date=date;
        this.message=message;
        this.receivep=receivep;
        this.sendp=sendp;
        this.isComMeg=!(sendp.equals(APPglobal.NAME));//发送者是否等于当前用户 不是得话就是接受的消息
    }
    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
    }

    //  getter/setter构造器

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }


}
