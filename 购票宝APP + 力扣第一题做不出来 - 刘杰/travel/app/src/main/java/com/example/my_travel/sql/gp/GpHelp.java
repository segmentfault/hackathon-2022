package com.example.my_travel.sql.gp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class GpHelp implements GpSqlite.TableCreateInterface {
    //私有化构造方法
    private GpHelp(){
    }
    //初始化实例
    private static GpHelp Gp = new GpHelp();
    //只提供一个实例
    public static GpHelp getInstance(){
        return Gp;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
         //表创建
        db.execSQL("CREATE TABLE IF NOT EXISTS GP(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "quantity TEXT," +
                "money TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         //表更新
        if (oldVersion<newVersion){
            String sql = "DROP TABLE IF EXISTS " +"GP";
            db.execSQL( sql );
            this.onCreate( db );
        }
    }

    public static void  delete(GpSqlite gpSqlite, String id){
        SQLiteDatabase db = gpSqlite.getWritableDatabase();
        db.delete("GP", "_id=?", new String[] { id });
        db.close();
    }
    // 插入
    public static void insertGp( GpSqlite gpSqlite, ContentValues userValues ) {
        SQLiteDatabase db = gpSqlite.getWritableDatabase();
        db.insert("GP", null, userValues);
        db.close();
    }
    //查询
    // 以HashMap<String, Object>键值对的形式获取一条信息
    public static HashMap<String, Object> getGp( GpSqlite gpSqlite, int id ){

        SQLiteDatabase db = gpSqlite.getReadableDatabase();

        HashMap<String, Object> GpMap = new HashMap<String, Object>();
        // 此处要求查询Note._id为传入参数_id的对应记录，使游标指向此记录
        Cursor cursor = db.query("GP", null, "_id" + " =? ", new String[]{ id + "" }, null, null, null);
        cursor.moveToFirst();
        GpMap.put("quantity", cursor.getString(cursor.getColumnIndex("quantity")));
        GpMap.put("money", cursor.getString(cursor.getColumnIndex("money")));
        return GpMap;
    }
    // 获得查询指向Note表的游标
    public static Cursor getAllGpMaps(GpSqlite gpSqlite) {
        SQLiteDatabase db = gpSqlite.getReadableDatabase();
        Cursor cursor = db.query("GP", null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
