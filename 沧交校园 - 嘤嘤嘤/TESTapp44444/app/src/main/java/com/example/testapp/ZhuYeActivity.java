package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuLianJie;
import ShuJuKu.TieZiLianJie;
import ShuJuKu.userXinXi;
import userXinWei.Brower;
import userXinWei.BrowerAdapter;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Thread.sleep;

public class ZhuYeActivity extends AppCompatActivity implements View.OnClickListener{

    char[] leixing={'k','i','j','h','d','c','b','a'};//名言警句里面的几个类型
    String[] value=new String[2];//用来保存获取到的名言和出处，作为ui更新之间的桥梁

    private TextView TianQi;
    Boolean flag=false;
    LoadingDailog dialog;
    int nums=0;
    private TextView MinYan_NeiRong;
    private TextView MinYan_ZuoZhe;
    ImageView imageView;
    ImageView imageView7;//主页
    ImageView imageView9;//发布
    ImageView imageView10;//个人
    /**
     * 主页的几个功能
     */
    private Button YiQin;



    private List<Brower> browers=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        TianQi=findViewById(R.id.weather_ed);
        TianQi.setOnClickListener(this);
        MinYan_NeiRong=findViewById(R.id.saying_ed);
        MinYan_ZuoZhe=findViewById(R.id.saying_ed1);
        /**
         * 主页功能
         */
        YiQin=findViewById(R.id.notify_ed);




        YiQin.setOnClickListener(this);
        /**
         * 导航栏....
         */
        imageView7=findViewById(R.id.imageView7);
        imageView9=findViewById(R.id.imageView9);
        imageView10=findViewById(R.id.imageView10);

        imageView7.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        imageView10.setOnClickListener(this);

        SuiJiMinYan();//获取一句名言
        ShuaXin();//获取数据库数据
        ShiPeiQi();//适配器适配并显示数据
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.weather_ed) {//天气
            Intent intent = new Intent(this, TianQiActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.notify_ed) {//疫情
            Intent intent = new Intent(this, YiQinActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.imageView7) {//主页
            //本身在主页就不跳了
        }
        if(view.getId()==R.id.imageView9) {//发布
            Intent i=new Intent(this,FaTieAcivity.class);
            startActivityForResult(i, 1);
        }
        if(view.getId()==R.id.imageView10) {//个人中心
            Intent i=new Intent(this,userActivity.class);
            startActivityForResult(i, 1);
        }
    }
    public  void ShuaXin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TieZiLianJie lianjie = new TieZiLianJie();
                try {
                    lianjie.openDB();//链接数据库
                    String sql = "SELECT * FROM tu_cao order by id desc LIMIT ?";
                    lianjie.ps = lianjie.con.prepareStatement(sql);//预编译
                    lianjie.ps.setInt(1,nums+7);
                    lianjie.re = lianjie.ps.executeQuery();
                    while (lianjie.re.next()) {
                        Brower ie = new Brower(lianjie.re.getString("username"),lianjie.re.getString("userImageUrl"),lianjie.re.getString("imageURL"),lianjie.re.getString("time"),lianjie.re.getString("NeiRong"),lianjie.re.getInt("DianZanNUMS"),lianjie.re.getInt("PingLunNUMS"),lianjie.re.getString("tieziID"));
                        browers.add(ie);
                    }
                    nums=nums+7;
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
                QuanJU.getInstance().TieZiRuKou=0;
                Intent i=new Intent(ZhuYeActivity.this,TieZiXiangQinActivity.class);
                startActivityForResult(i, 1);
            }
        });

        flag=false;
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
                            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(ZhuYeActivity.this)
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


    /**
     *生成一个随机数来随机请求一个类型的名言，并且句子长度最大是25
     */
    public void SuiJiMinYan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url1 = "https://v1.hitokoto.cn?c="+leixing[new Random().nextInt(8)]+"&max_length=25";
                    URL url = new URL(url1);
                    //得到connection对象。
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //连接
                    connection.connect();
                    //得到响应码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //得到响应流
                        InputStream inputStream = connection.getInputStream();
                        //将响应流转换成字符串
                        String result = toConvertString((inputStream));//将流转换为字符串。
                        JSONObject jsonObject = new JSONObject(result);
                        value[0]= jsonObject.optString("hitokoto");//内容
                        value[1]= jsonObject.optString("from");//出处


                        runOnUiThread(new Runnable() {//更新ui
                            @Override
                            public void run() {
                                MinYan_NeiRong.setText(value[0]);
                                MinYan_ZuoZhe.setText("——"+value[1]);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line + "");
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            dialog.dismiss();// 关闭ProgressDialog
        }
    };
}
