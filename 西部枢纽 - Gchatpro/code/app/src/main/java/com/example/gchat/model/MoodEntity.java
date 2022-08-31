package com.example.gchat.model;

import com.example.gchat.utils.APPglobal;

/**
 * User: xiahao
 * DateTime: 2022/4/15 18:04
 * Description:说说实体类
 */
public class MoodEntity {
    private String content;//说说内容
    private String time;   //说说发布时间

    private String person;//发布者姓名

    public MoodEntity(){}

    public MoodEntity(String content, String time) {
        this.content = content;
        this.time = time;
        this.person= APPglobal.NAME;//获取当前登陆的用户姓名
        //this.person=APPglobal.readFromPre();
    }

    public MoodEntity(String content, String time,String personname) {
        this.person=personname;
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
