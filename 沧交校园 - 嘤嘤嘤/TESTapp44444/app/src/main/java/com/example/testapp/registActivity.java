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
import static android.widget.Toast.LENGTH_SHORT;

public class registActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private EditText phone;
    private Button yanzhengma;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username_et);
        password=findViewById(R.id.password_et);
        phone=findViewById(R.id.editText2);
        yanzhengma=findViewById(R.id.button);
        yanzhengma.setOnClickListener(this);
        register=findViewById(R.id.register_btn);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String username1 = username.getText().toString();
        final String password1 = password.getText().toString();
        final String phone1=phone.getText().toString();
        if(view.getId()==R.id.button){//点击获取验证码按钮
            //先验证手机号
            if (phone1.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")){
                /**
                 * 手机号如果合法就先验证是否注册过；然后判断发不发发送验证码，同时将按钮变为不可用状态一分钟
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
                        try {
                            lianjie.openDB();//链接数据库
                            String sql = "SELECT * from user_inf where phone=?";
                            lianjie.ps = lianjie.con.prepareStatement(sql);//预编译sql,先判断手机号是否注册过
                            lianjie.ps.setString(1, phone1);
                            lianjie.re = lianjie.ps.executeQuery();
                            if (lianjie.re.next()) {
                                Toast.makeText(getApplicationContext(), "此手机号已经注册过！", LENGTH_SHORT).show();
                                return;
                            } else {//手机号没有注册过
                                QuanJU.getInstance().isYanzheng=0;
                                /**
                                 * 发送验证码，同时将按钮变为不可用状态一分钟
                                 */

                                //发送验证码的调用：YanZhengMa a=new YanZhengMa();
                                //                  a.sendYanzhengma();


                                new Thread(new MyCountDownTimer()).start();//将按钮变为不可用60s
                                Toast.makeText(registActivity.this, "验证码已发送请耐心等待", LENGTH_SHORT).show();
                                /**
                                 * 这里接下来的代码是跳转到一个专门的填写验证码的界面，填写正确则返回，并将全局变量里面是否填写正确验证码值置1
                                 */
                                Intent intent = new Intent(registActivity.this, YanZhengMaActivity.class);
                                startActivityForResult(intent, 1);
                            }
                            lianjie.closeResoure();
                            Looper.loop();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else {
                Toast.makeText(this, "手机号不合法", LENGTH_SHORT).show();
                return;
            }
            new Thread(new Timer()).start();//为验证码设置60s的有效期
            return;
        }
        if(view.getId()==R.id.register_btn) {//点击注册按钮
            if (QuanJU.getInstance().isYanzheng != 0) {//填写了验证码才能继续
                QuanJU.getInstance().isYanzheng=0;//将验证码归零
                if (username1.length() > 9 || username1.length() < 3) {
                    Toast.makeText(this, "用户名为3-9个字符", LENGTH_SHORT).show();
                    return;
                }
                if (password1.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
                            try {
                                lianjie.openDB();//链接数据库
                                String sql1 = "INSERT INTO user_inf (password, username, phone) VALUES (?,?,?) ";
                                String sql2 = "SELECT * from user_inf where username=?";
                                lianjie.ps = lianjie.con.prepareStatement(sql2);//预编译sql2,先判断用户名是否存在
                                lianjie.ps.setString(1, username1);
                                lianjie.re = lianjie.ps.executeQuery();
                                if (lianjie.re.next()) {
                                    Toast.makeText(getApplicationContext(), "用户名已存在！", LENGTH_SHORT).show();
                                    return;
                                } else {
                                    lianjie.ps = lianjie.con.prepareStatement(sql1);
                                    lianjie.ps.setString(1, password1);
                                    lianjie.ps.setString(2, username1);
                                    lianjie.ps.setString(3, phone1);
                                    if (lianjie.ps.executeUpdate() != 0) {
                                        Toast.makeText(getApplicationContext(), "注册成功！", LENGTH_SHORT).show();
                                        Intent i = new Intent(registActivity.this, MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//setFlags是跳转后清空之前的所有页面
                                        registActivity.this.startActivity(i);
                                    }
                                }
                                lianjie.closeResoure();
                                Looper.loop();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "密码是6到18位的字母与数字组合，不能有特殊字符", LENGTH_SHORT).show();
                    return;
                }
            }else{
                Toast.makeText(this, "尚未填写验证码", LENGTH_SHORT).show();
                return;
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
