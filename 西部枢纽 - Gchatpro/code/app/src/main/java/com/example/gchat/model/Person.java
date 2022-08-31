package com.example.gchat.model;

import com.example.gchat.utils.PinYinUtils;

/**
 * User: xiahao
 * DateTime: 2022/4/15 18:04
 * Description:好友实体类
 */
public class Person {

    private String name; //姓名
    private String pinyin;//拼音
    private String headerWord; //拼音首字母
    private int id;
    private String message;
    public  Person(){}

    public  Person(String name){
        this.name=name;

        this.pinyin = PinYinUtils.getPinyin(name);
        headerWord = pinyin.substring(0, 1);
    }

    public Person(String name,int id) {
       this.id = id;
        this.name = name;
        this.pinyin = PinYinUtils.getPinyin(name);
        headerWord = pinyin.substring(0, 1);
    }
    public int getID() {
        return id;
    }
    public String getPinyin() {
        return pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderWord() {
        return headerWord;
    }

}
