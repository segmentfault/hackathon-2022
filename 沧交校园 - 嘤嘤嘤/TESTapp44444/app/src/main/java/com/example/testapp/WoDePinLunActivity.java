package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
import userXinWei.Brower_pinlun;
import userXinWei.TieZiBrowerAdapter;

public class WoDePinLunActivity extends AppCompatActivity {
    LoadingDailog dialog;
    Boolean flag1=false;
    int nums=0;
    private List<Brower_pinlun> browers=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wodepinlun);

        ShuaXin();//获取数据库数据
        ShiPeiQi();//适配器适配并显示数据
    }

    public  void ShuaXin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                TieZiLianJie lianjie = new TieZiLianJie();
                try {
                    lianjie.openDB();//链接数据库
                    String sql = "SELECT * FROM pin_lun where username=? order by id desc  LIMIT ?";
                    lianjie.ps = lianjie.con.prepareStatement(sql);//预编译
                    lianjie.ps.setString(1, QuanJU.getInstance().username);
                    lianjie.ps.setInt(2,nums+7);
                    lianjie.re = lianjie.ps.executeQuery();
                    while (lianjie.re.next()) {
                        Brower_pinlun ie = new Brower_pinlun(lianjie.re.getString("username"),lianjie.re.getString("userImageUrl"),lianjie.re.getString("time"),lianjie.re.getString("NeiRong"),lianjie.re.getString("tieziID"));
                        browers.add(ie);
                    }
                    nums=nums+7;
                    flag1=true;
                    lianjie.closeResoure();//释放链接
                    Looper.loop();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void ShiPeiQi(){
        while(flag1==false){
            //因为主线程需要用子线程里获取的数据，所以这个设置一个flag作为子线程是否执行完的标志。
        }
        //初始化适配器
        TieZiBrowerAdapter adapter = new TieZiBrowerAdapter(this, R.layout.borower_pinlun_item, browers);
        ListView listView =  findViewById(R.id.list_view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//这里是点击帖子之后的调用
                Brower_pinlun browser = browers.get(position);//首先获取这个帖子，然后跳转到帖子详情页面

                QuanJU.getInstance().tieziID=browser.getTieziID();//将帖子的id赋给全局里面
                Intent i=new Intent(WoDePinLunActivity.this,TieZiXiangQinActivity.class);
                WoDePinLunActivity.this.startActivity(i);
            }
        });


        flag1=false;
        listView.setAdapter(adapter);
        listView.setSelection(nums);
        adapter.notifyDataSetChanged();//告知ui更新
        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            //是否到底了 设置为false
            boolean atBottom=false;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //到底了 加载后续数据
                        if(atBottom){
                            /* 显示加载等待框 */
                            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(WoDePinLunActivity.this)
                                    .setMessage("加载中...")
                                    .setCancelable(false)
                                    .setCancelOutside(false);
                            dialog=loadBuilder.create();
                            dialog.show();
                            ShuaXin();//获取数据库数据
                            ShiPeiQi();//适配器适配并显示数据
                            handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消息给handler
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
                if(totalItemCount!=0 && firstVisibleItem+visibleItemCount==totalItemCount){
                    Log.i("info", "到底了...."+totalItemCount);
                    //说明到底了
                    atBottom=true;
                }else{
                    //没有到底
                    atBottom=false;
                }
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            dialog.dismiss();// 关闭ProgressDialog
        }
    };
}
