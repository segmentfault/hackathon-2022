package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.tu.loadingdialog.LoadingDailog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ShuJuKu.QuanJU;
import ShuJuKu.TieZiLianJie;
import userXinWei.Brower;
import userXinWei.BrowerAdapter;

public class WoDeTieZiActivity extends AppCompatActivity {

    LoadingDailog dialog;
    Boolean flag=false;
    private List<Brower> browers=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wodetiezi);


        ShuaXin();//获取数据库数据
        ShiPeiQi();//适配器适配并显示数据
    }
    public  void ShuaXin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TieZiLianJie lianjie = new TieZiLianJie();
                try {
                    lianjie.openDB();//链接数据库
                    String sql = "SELECT * FROM tu_cao   where username=?  order by id desc";
                    lianjie.ps = lianjie.con.prepareStatement(sql);//预编译
                    lianjie.ps.setString(1,QuanJU.getInstance().username);
                    lianjie.re = lianjie.ps.executeQuery();
                    while (lianjie.re.next()) {
                        Brower ie = new Brower(lianjie.re.getString("username"),lianjie.re.getString("userImageUrl"),lianjie.re.getString("imageURL"),lianjie.re.getString("time"),lianjie.re.getString("NeiRong"),lianjie.re.getInt("DianZanNUMS"),lianjie.re.getInt("PingLunNUMS"),lianjie.re.getString("tieziID"));
                        browers.add(ie);
                    }
                    flag=true;
                    lianjie.closeResoure();//释放链接
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void ShiPeiQi(){
        while(flag==false){
            //因为主线程需要用子线程里获取的数据，所以这个设置一个flag作为子线程是否执行完的标志。
        }
        //初始化适配器
        BrowerAdapter adapter = new BrowerAdapter(this, R.layout.brower_item, browers);
        ListView listView =  findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//这里是点击帖子之后的调用
                Brower browser = browers.get(position);//首先获取这个帖子，然后跳转到帖子详情页面
                QuanJU.getInstance().tieziID=browser.getTieZiID();//将帖子的id赋给全局里面

                QuanJU.getInstance().TieZiRuKou=1;
                Intent i=new Intent(WoDeTieZiActivity.this,TieZiXiangQinActivity.class);
                startActivityForResult(i, 1);
            }
        });

        flag=false;
        listView.setAdapter(adapter);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            dialog.dismiss();// 关闭ProgressDialog
        }
    };
}
