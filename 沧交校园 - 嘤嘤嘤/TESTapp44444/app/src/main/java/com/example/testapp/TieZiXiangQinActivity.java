package com.example.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuCaoZuo;
import ShuJuKu.TieZiLianJie;
import userXinWei.Brower;
import userXinWei.BrowerAdapter;
import userXinWei.Brower_pinlun;
import userXinWei.TieZiBrowerAdapter;

import static java.lang.Thread.sleep;

public class TieZiXiangQinActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean flag1=false;
    LoadingDailog dialog;
    int nums=0;
    private List<Brower_pinlun> browers=new ArrayList<>();
    /**
     * 帖子主人属性
     */
    private ImageView touxiang;
    private TextView  yonghuming;
    private TextView  neirong;
    private ImageView dianzanTU;
    private ImageView pinlunTU;
    private TextView  dianzanNUMS;
    private TextView  pinlunNUMS;
    private TextView  time;
    private ImageView    imageURL;
    private int isDianZan=0;//默认0表示没点赞过这个帖子，1代表点赞过。
    /**
     * 作为主子线程之间桥梁，更新ui
     */
    private String userimageURL;
    private String neirongimageURL;
    private String yonghuming1;
    private String neirong1;
    private int dianzanNUMS1;
    private int pinlunNUMS1;
    private String time1;

    /**
     * 评论以及发布评论按钮
     *
     */
    private EditText pinlun;
    private Button   fabu;
    /**
     *
     * 帖子ID
     */
    private String tieziID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiezixiangqin);
        /**
         * 绑定属性
         */
        touxiang=findViewById(R.id.imageView2);
        yonghuming=findViewById(R.id.textView13);
        neirong=findViewById(R.id.textView4);
        dianzanTU=findViewById(R.id.imageView4);
        pinlunTU=findViewById(R.id.imageView3);
        dianzanNUMS=findViewById(R.id.textView7);
        pinlunNUMS=findViewById(R.id.textView5);
        time=findViewById(R.id.textView12);
        imageURL=findViewById(R.id.imageView6);
        pinlun=findViewById(R.id.editText5);
        fabu=findViewById(R.id.button5);


        tieziID=QuanJU.getInstance().tieziID;
        if(QuanJU.getInstance().WODianZanDe.indexOf(tieziID)!=-1){//返回值不等于-1说明点赞过。
            isDianZan=1;
            dianzanTU.setImageResource(R.mipmap.zan2);
        };
        /**
         * 注册点击事件
         */
        dianzanTU.setOnClickListener(this);
        fabu.setOnClickListener(this);
       try {
            shuju();//根据id从数据库里面获取这个帖子的详细信息并赋值
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * 评论获取并显示阶段
         */
        ShuaXin();//获取数据库数据
        ShiPeiQi();//适配器适配并显示数据
    }
    @Override
    public void onClick(View view) {
        /**
         * //点赞图
         */
        if(view.getId()==R.id.imageView4){

            if(QuanJU.getInstance().isDengLu==0){
                Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,userActivity.class);
                startActivityForResult(i, 1);
                return;
            }
            if(isDianZan==0){
                isDianZan=1;
                dianzanTU.setImageResource(R.mipmap.zan2);//更换点赞图为已点赞状态
                dianzanNUMS.setText(Integer.parseInt(dianzanNUMS.getText().toString())+1+"" );
            }else if(isDianZan==1){//表示已经点赞了，所以需要取消点赞
                isDianZan=0;
                dianzanTU.setImageResource(R.mipmap.zan1);//更换点赞图为未点赞状态
                dianzanNUMS.setText(Integer.parseInt(dianzanNUMS.getText().toString())-1+"" );
            }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ShuJuKuCaoZuo shuJuKuCaoZuo=new ShuJuKuCaoZuo();
                            shuJuKuCaoZuo.DianZanNUMS(tieziID,isDianZan,Integer.parseInt(dianzanNUMS.getText().toString()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
        }
        /**
         * //发布评论
         */
        if(view.getId()==R.id.button5){

            if(QuanJU.getInstance().isDengLu==0){
                Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,userActivity.class);
                startActivityForResult(i, 1);
                return;
            }
            /* 显示加载等待框 */
            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(TieZiXiangQinActivity.this)
                    .setMessage("加载中...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog=loadBuilder.create();
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String neirong=pinlun.getText().toString();//获取评论内容
                    Date time = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String times = sdf.format(time);//获取当前时间
                    try {
                        new ShuJuKuCaoZuo().PinLun(tieziID,QuanJU.getInstance().username,QuanJU.getInstance().touxiang,neirong,times);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            pinlun.setText(null);

            Toast toast=Toast.makeText(this,"发布成功",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);//设置其显示在屏幕中间
            toast.show();
            handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消息给handler
        }
    }
    /**
     * String url = "";
     *         Glide.with(this)
     *                 .load(url)
     *                 .into(imageView);//加载网络图片
     *子项里只显示头像但不显示帖子图像。
     */
    public void shuju() throws SQLException {//根据id从数据库里面获取这个帖子的详细信息并赋值
         new Thread(new Runnable() {
            @Override
            public void run() {
                TieZiLianJie tiezilianjie=new TieZiLianJie();
                try {
                    tiezilianjie.openDB();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String sql="SELECT * from tu_cao where tieziID=?";
                try {
                    tiezilianjie.ps =tiezilianjie.con.prepareStatement(sql);
                    tiezilianjie.ps.setString(1, tieziID);
                    tiezilianjie.re=tiezilianjie.ps.executeQuery();
                    while (tiezilianjie.re.next()){
                        userimageURL=tiezilianjie.re.getString("userImageUrl");
                        neirongimageURL=tiezilianjie.re.getString("imageURL");
                        yonghuming1=tiezilianjie.re.getString("username");
                        neirong1=tiezilianjie.re.getString("NeiRong");
                        dianzanNUMS1=tiezilianjie.re.getInt("DianZanNUMS");
                        pinlunNUMS1=tiezilianjie.re.getInt("PingLunNUMS");
                        time1=tiezilianjie.re.getString("time");
                    }
                    tiezilianjie.closeResoure();

                    runOnUiThread(new Runnable() {//更新ui
                        @Override
                        public void run() {
                            yonghuming.setText(yonghuming1);
                            neirong.setText(neirong1);
                            dianzanNUMS.setText(dianzanNUMS1+"");
                            pinlunNUMS.setText(pinlunNUMS1+"");
                            time.setText(time1);

                            Glide.with(TieZiXiangQinActivity.this).load(userimageURL).into(touxiang);
                            if(neirongimageURL==null){
                                imageURL.setAlpha(0f);//如果帖子中不包含图像，则设置imageview全透明
                            }else if(neirongimageURL!=null){
                                Glide.with(TieZiXiangQinActivity.this).load(neirongimageURL).into(imageURL);
                            }
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击返回键
        if(keyCode==KeyEvent.KEYCODE_BACK){
                finish();
        }
        return true;
    }

    public  void ShuaXin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TieZiLianJie lianjie = new TieZiLianJie();
                try {
                    lianjie.openDB();//链接数据库
                    String sql = "SELECT * FROM pin_lun where TieZiID=? order by id desc  LIMIT ?";
                    lianjie.ps = lianjie.con.prepareStatement(sql);//预编译
                    lianjie.ps.setString(1,tieziID);
                    lianjie.ps.setInt(2,nums+5);
                    lianjie.re = lianjie.ps.executeQuery();
                    while (lianjie.re.next()) {
                        Brower_pinlun ie = new Brower_pinlun(lianjie.re.getString("username"),lianjie.re.getString("userImageUrl"),lianjie.re.getString("time"),lianjie.re.getString("NeiRong"),lianjie.re.getString("tieziID"));
                        browers.add(ie);
                    }
                    nums=nums+5;
                    flag1=true;
                    lianjie.closeResoure();
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
                            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(TieZiXiangQinActivity.this)
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
