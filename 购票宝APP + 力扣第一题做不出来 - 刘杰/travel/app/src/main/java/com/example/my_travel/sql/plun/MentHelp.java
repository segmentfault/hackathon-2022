package com.example.my_travel.sql.plun;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class MentHelp implements MentSqlite.TableCreateInterface {
    //私有化构造方法
    private MentHelp(){
    }
    //初始化实例
    private static MentHelp ment = new MentHelp();
    //只提供一个实例
    public static MentHelp getInstance(){
        return ment;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
         //表创建
        db.execSQL("CREATE TABLE IF NOT EXISTS ment(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "conten TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         //表更新
        if (oldVersion<newVersion){
            String sql = "DROP TABLE IF EXISTS " +"ment";
            db.execSQL( sql );
            this.onCreate( db );
        }
    }
    // 插入
    public static void insertMent( MentSqlite mentSqlite, ContentValues userValues ) {
        SQLiteDatabase db = mentSqlite.getWritableDatabase();
        db.insert("Ment", null, userValues);
        db.close();
    }
    //查询
    // 以HashMap<String, Object>键值对的形式获取一条信息
    public static HashMap<String, Object> getMent(MentSqlite mentSqlite, int id ){

        SQLiteDatabase db = mentSqlite.getReadableDatabase();

        HashMap<String, Object> MentMap = new HashMap<String, Object>();
        // 此处要求查询Note._id为传入参数_id的对应记录，使游标指向此记录
        Cursor cursor = db.query("Ment", null, "_id" + " =? ", new String[]{ id + "" }, null, null, null);
        cursor.moveToFirst();
        MentMap.put("username", cursor.getString(cursor.getColumnIndex("username")));
        MentMap.put("conten", cursor.getString(cursor.getColumnIndex("conten")));
        return MentMap;
    }
    // 获得查询指向Note表的游标
    public static Cursor getAllMents(MentSqlite mentSqlite) {
        SQLiteDatabase db = mentSqlite.getReadableDatabase();
        Cursor cursor = db.query("Ment", null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
