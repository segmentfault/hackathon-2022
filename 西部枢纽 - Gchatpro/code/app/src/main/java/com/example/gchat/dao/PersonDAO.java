package com.example.gchat.dao;
import com.example.gchat.LoginActivity;
import com.example.gchat.TCPclient;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gchat.TCPclient;
import com.example.gchat.WelcomeActivity;
import com.example.gchat.db.DbConnection;
import com.example.gchat.model.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * User: xiahao
 * DateTime: 2022/4/14 15:50
 * Description:联系人数据访问对象
 */
public class PersonDAO {
    private static ArrayList<Person> personList=null;//保存联系人数据

   // 获取所有联系人
    public static ArrayList<Person> getPersonList(){
        if(null==personList){
            synchronized ( PersonDAO.class ){
                if(null==personList){
                    personList=new ArrayList<Person>();
                }
            }
            //把数据库中已有的数据拿出来
            DbConnection connection=new DbConnection();
            SQLiteDatabase db=connection.getConnection();
            Cursor cursor=db.query("tb_person",null,null,null,null,null,null);
            while ( cursor.moveToNext() ){
                int namenum=cursor.getColumnIndex("name");
                int ID = cursor.getColumnIndex("id");
                String name=cursor.getString(namenum);
                int id = cursor.getInt(ID);
                Person person=new Person(name,id);
                personList.add(person);
                cursor.moveToNext();
            }

        }

        return personList;
    }


    /**
     * 根据id查找用户真实姓名
     * @param
     * @return
     */
    public static String findNameByUsername(int id){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            Person person=personList.get(i);
            if(id==person.getID()){
                return person.getName();
            }
        }
        return "";
    }

    /**
     * 查找id是否存在
     * @param
     * @return
     */
    public boolean checkID(int id){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            Person book=personList.get(i);
            if(id == book.getID()){
                return true;
            }
        }
        return false;
    }

    /**
     * 插入联系人
  //   * @param person
     * @return
     */
    public boolean insert(Person person){ // 插入id , name

        if(checkID(person.getID())){  //如果用户名存在则直接返回失败
            return false;
        }
        try {
            personList.add(person);
            DbConnection conn=new DbConnection();
            SQLiteDatabase db=conn.getConnection();
            String sql="insert into tb_person(id,name) values('"
                    +person.getID()+"','"+person.getName()+"')";
            db.execSQL(sql);
            db.close();
            return true;
        }catch ( Exception e ){
            return false;
        }

    }

}
