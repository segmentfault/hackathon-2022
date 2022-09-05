package com.hui.dict.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "dict.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table pywordtb(_id integer primary key autoincrement,id varchar(20),zi varchar(4) unique not null," +
                "py varchar(10),wubi varchar(10),pinyin varchar(10),bushou varchar(4),bihua integer)";
        db.execSQL(sql);
//      创建存储文字详情的表格
        sql = "create table wordtb(_id integer primary key autoincrement,id varchar(20),zi varchar(4) unique not null,py varchar(10),"
                + "wubi varchar(10),pinyin varchar(10),bushou varchar(4),bihua integer,jijie text,xiangjie text)";
        db.execSQL(sql);
        //存储成语的表
        sql = "create table cyutb(_id  integer primary key autoincrement,chengyu varchar(10) unique not null,bushou varchar(4)," +
        "head varchar(4),pinyin varchar(30),chengyujs varchar(100),from_ text,example text,yufa varchar(30),ciyujs text," +
                "yinzhengjs text,tongyi text,fanyi text)";
        db.execSQL(sql);

//        创建收藏汉字的表
        sql = "create table collwordtb(_id integer primary key autoincrement,zi varchar(4) unique not null)";
        db.execSQL(sql);
        // 创建收藏成语的表
        sql = "create table collcyutb(_id integer primary key autoincrement,chengyu varchar(4) unique not null)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
