package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.SQLException;

import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuLianJie;
import ShuJuKu.YanZhengMa;
import ShuJuKu.userXinXi;
import static android.widget.Toast.LENGTH_SHORT;

public class XiuGaiMiMaActivity extends AppCompatActivity implements View.OnClickListener {
    private Bundle bundle;
    private EditText  xinmima;
    private EditText  shoujihao;
    private Button    yanzhengma;
    private TextView queding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugaimima);

        xinmima=findViewById(R.id.password_et);
        shoujihao=findViewById(R.id.editText2);
        yanzhengma=findViewById(R.id.button);
        queding=findViewById(R.id.queding);

        yanzhengma.setOnClickListener(this);
        queding.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String xinmima1 = xinmima.getText().toString();
        final String shoujihao1=shoujihao.getText().toString();
        if(view.getId()==R.id.button){//验证码
            //先验证手机号是否合法，如果合法则查询数据库，看看是否是绑定手机号。
            if (shoujihao1.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
                        try {
                            lianjie.openDB();//链接数据库
                            String sql = "SELECT phone from user_inf where username=?";
                            lianjie.ps = lianjie.con.prepareStatement(sql);
                            lianjie.ps.setString(1, QuanJU.getInstance().username);
                            lianjie.re = lianjie.ps.executeQuery();
                            if (lianjie.re.next()) {
                                String photo=lianjie.re.getString("phone");
                                if(photo.equals(shoujihao1)){//手机号绑定正确，进入验证码阶段
                                    QuanJU.getInstance().isYanzheng=0;
                                    /**
                                     * 手机号如果合法就发送验证码，同时将按钮变为不可用状态一分钟
                                     */

                                    //发送验证码的调用：YanZhengMa a=new YanZhengMa();
                                    //                  a.sendYanzhengma();


                                    new Thread(new MyCountDownTimer()).start();//将按钮变为不可用60s
                                    Toast.makeText(XiuGaiMiMaActivity.this, "验证码已发送请耐心等待", LENGTH_SHORT).show();
                                    /**
                                     * 这里接下来的代码是跳转到一个专门的填写验证码的界面，填写正确则返回，并将全局变量里面是否填写正确验证码值置1
                                     */
                                    Intent intent = new Intent(XiuGaiMiMaActivity.this, YanZhengMaActivity.class);
                                    startActivityForResult(intent, 1);
                                }else{
                                    Toast.makeText(getApplicationContext(), "此手机号未绑定当前用户", LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            new Thread(new Timer()).start();//为验证码设置60s的有效期
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            lianjie.closeResoure();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }).start();

            }else {
                Toast.makeText(this, "手机号不合法", LENGTH_SHORT).show();
                return;
            }
        }
        if(view.getId()==R.id.queding){
            if (QuanJU.getInstance().isYanzheng != 0) {//填写了验证码才能继续
                QuanJU.getInstance().isYanzheng=0;//将验证码归零
                if (xinmima1.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")) {//密码合乎规范，则继续修改
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
                            try {
                                lianjie.openDB();//链接数据库
                                String sql = "update user_inf set password=? where username=?";
                                lianjie.ps = lianjie.con.prepareStatement(sql);
                                lianjie.ps.setString(1,xinmima1);
                                lianjie.ps.setString(2,QuanJU.getInstance().username);
                                if(lianjie.ps.executeUpdate()>0){
                                    Toast.makeText(XiuGaiMiMaActivity.this, "修改成功，请重新登录", LENGTH_SHORT).show();
                                    Intent i = new Intent(XiuGaiMiMaActivity.this, MainActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//setFlags是跳转后清空之前的所有页面
                                    XiuGaiMiMaActivity.this.startActivity(i);
                                }else{
                                    Toast.makeText(XiuGaiMiMaActivity.this, "修改失败！！！", LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            try {
                                lianjie.closeResoure();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }).start();
                }else {
                    Toast.makeText(this, "密码是6到18位的字母与数字组合，不能有特殊字符", LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }


    /**
     * 自定义倒计时类，实现Runnable接口
     */
    class MyCountDownTimer implements Runnable {
        public int T=60;
        private Handler mHandler = new Handler();
        @Override
        public void run() {

            //倒计时开始，循环
            while (T > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        yanzhengma.setClickable(false);
                        yanzhengma.setText(T + "秒后可用");
                    }
                });
                try {
                    Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                T--;
            }

            //倒计时结束，也就是循环结束
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    yanzhengma.setClickable(true);
                    yanzhengma.setText("获取验证码");
                }
            });
            T = 60; //最后再恢复倒计时时长
        }
    }

    class Timer implements Runnable {
        public int T=60;
        private Handler mHandler = new Handler();
        @Override
        public void run() {

            //倒计时开始，循环
            while (T > 0) {
                T--;
            }

            //倒计时结束，也就是循环结束
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    QuanJU.getInstance().isYanzheng=0;
                }
            });
            T = 60; //最后再恢复倒计时时长
        }
    }

}
