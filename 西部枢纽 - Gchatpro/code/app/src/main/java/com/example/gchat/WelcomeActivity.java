package com.example.gchat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.gchat.Service.NotiFicationService;
import com.example.gchat.db.DbConnection;
import com.google.android.exoplayer2.util.Log;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: xiahao
 * DateTime: 2022/4/20 23:58
 * Description:欢迎页面
 */
public class WelcomeActivity extends Activity {
    public static Queue<JSONObject> messageQueue; // 消息队列定义成全局变量

    public static SharedPreferences pref; // 用于保存轻量级数据
    public static SharedPreferences.Editor editor ;

    public static Queue<JSONObject> ApplyFriendQueue; // 好友申请队列

    public  static TCPclient tcPclient ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
       // Log.d("AAA","程序开始");

        DbConnection.setContext(this.getApplicationContext());//创建数据库

        tcPclient = new TCPclient();
        WelcomeActivity.tcPclient.Start();
        messageQueue = new LinkedList<JSONObject>();

        ApplyFriendQueue = new LinkedList<JSONObject>();
        Intent NotificationServiceIntent = new Intent(this, NotiFicationService.class);
        startService(NotificationServiceIntent);
        // 启动通知服务

//        pref = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean islogin = pref .getBoolean("islogin",false);
        final Intent intent=new Intent();
      //  if(islogin){

          //  intent.setClass(WelcomeActivity.this,MainActivity.class);
       // }
       // else {

            intent.setClass(WelcomeActivity.this, LoginActivity.class);
      //  }

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };

        timer.schedule(timerTask,2000);//此处的Delay可以是2*1000，代表两秒

    }

    //
}

/*
    Timer是一种定时器工具，用来在一个后台线程计划执行指定任务。
    TimerTask一个抽象类，它的子类代表一个可以被Timer计划的任务。

    用Timer线程实现和计划执行一个任务的基础步骤：
    1.实现自定义的TimerTask的子类，run方法包含要执行的任务代码。
    2.实例化Timer类，创建计时器后台线程。
    3.制定执行计划。这里用schedule方法，第一个参数是TimerTask对象，第二个参数表示开始执行前的延时时间
     （单位是milliseconds，这里定义了2000）。还有一种方法可以指定任务的执行时间。
 */