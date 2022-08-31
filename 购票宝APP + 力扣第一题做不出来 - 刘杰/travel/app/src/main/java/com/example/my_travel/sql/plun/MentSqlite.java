package com.example.my_travel.sql.plun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MentSqlite extends SQLiteOpenHelper {
    public MentSqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //表创建接口 有多张表时 方便统一调用
    public static interface TableCreateInterface {
        //创建表
        public void onCreate(SQLiteDatabase db);
        //更新表
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //建表
        MentHelp.getInstance().onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        MentHelp.getInstance().onUpgrade(sqLiteDatabase,i,i1);
    }
}
