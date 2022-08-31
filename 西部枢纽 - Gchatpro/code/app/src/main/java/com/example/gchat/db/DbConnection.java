package com.example.gchat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: xiahao
 * DateTime: 2022/3/23 15:15
 * Description:
 * SQLiteOpenHelper是包装了SQLite数据库的创建，打开和更新的抽象类，通过实现和使用SQLiteOpenHelper，
 * 可以隐去在数据库打开之前需要判断数据库是否需要创建或更新的逻辑。
 */
public class DbConnection extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION=1;//数据库版本号
    private final static String DATABASE_NAME="simpleqq.db";//数据库名
    private static Context context;         //context对象

    public DbConnection(){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static Context getContext() {
        return context;
    }

    public SQLiteDatabase getConnection(){
        SQLiteDatabase db=getWritableDatabase();
        return db;
    }

    public static void setContext(Context context) {
        DbConnection.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        //tb_person表 id 真实姓名 历史发送的消息
        String sql1="create table tb_person("
                +"id INTEGER not null,"
                +" message TEXT(4000)  default '',"
                +" name  varchar(30) not null);";
        db.execSQL(sql1);

        //tb_mood表 内容 时间 发布者姓名
        String sql2="create table tb_mood("
                +" content varchar(30) not null,"
                +" time varchar(30) not null,"
                +" person  varchar(30) not null);";
        db.execSQL(sql2);
    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close(SQLiteDatabase db){
        db.close();
    }
}
