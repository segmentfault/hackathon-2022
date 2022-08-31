package com.example.my_travel.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.my_travel.bean.User;

import java.util.ArrayList;

public class UserManage extends SQLiteOpenHelper {
    //声明数据库变量
    private SQLiteDatabase xw;
    /*
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从2开始
     * super(context,"db_test",null,1);
     * db = getReadableDatabase();
     */
    public UserManage(Context context) {
        super(context,"hb",null,2);
        //打开数据库
        xw = getReadableDatabase();
    }
    /*
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     * 所以就有onCreate()、onUpgrade()两个方法
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      //建表
        String sql = "create table User(" +
                     "id integer primary key autoincrement," +
                     "username TEXT," +
                     "userpwd  TEXT)";
        //执行
         sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //数据库添加操作
    public void add(String username, String userpwd){
        xw.execSQL("insert into User(username,userpwd)values(?,?)",new Object[]{username,userpwd});
    }
    //数据库查询操作
    /*
     * 查询表user全部内容的方法
     *定义了一个ArrayList类的list，
     *如果需要用Cursor的话，第一个参数："表名"，中间6个：null，
     * 游标定义好了，接下来写一个while循环，让游标从表头游到表尾
     * 在游的过程中把游出来的数据存放到list容器中
     */
    public ArrayList<User> getAll(){
        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = xw.query("User",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String userpwd = cursor.getString(cursor.getColumnIndex("userpwd"));
            list.add(new User(username,userpwd));
        }
        return list;
    }
}
