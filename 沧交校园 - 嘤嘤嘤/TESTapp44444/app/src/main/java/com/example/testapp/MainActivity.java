package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.SQLException;

import ShuJuKu.QuanJU;
import ShuJuKu.ShuJuKuLianJie;
import ShuJuKu.userXinXi;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 目前注册了用户名、密码、登录、注册四个，剩余一个忘记密码
     */
    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;
    private TextView WangJiMiMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username_et);
        password=findViewById(R.id.password_et);

        login=findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        register=findViewById(R.id.register_btn);
        register.setOnClickListener(this);

        WangJiMiMa=findViewById(R.id.forgot_password_btn);
        WangJiMiMa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_btn) {//登录
            final String username1 = username.getText().toString();
            final String password1 = password.getText().toString();

            if (username1.length() > 9 || username1.length() < 3) {
                Toast.makeText(this, "用户名为3-9个字符", LENGTH_SHORT).show();
                return;
            }
            /**
             * 用户名是3到9个字符，密码是6到18位的字母与数字组合，不能有特殊字符
             */
            if (password1.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")) {
                new Thread(new Runnable() {

                    int fag = 0;          //用来判断是否登录成功,0默认用户不存在，1代表成功

                    @Override
                    public void run() {
                        Looper.prepare();
                        ShuJuKuLianJie lianjie = new ShuJuKuLianJie();
                        try {
                            lianjie.openDB();//链接数据库
                            String sql = "select * from user_inf where username=? ";
                            lianjie.ps = lianjie.con.prepareStatement(sql);//预编译
                            lianjie.ps.setString(1, username1);

                            lianjie.re = lianjie.ps.executeQuery();
                            while (lianjie.re.next()) {
                                if (lianjie.re.getString("password").equals(password1)) {
                                    fag = 1;

                                    String sql1 = "select * from user_inf where username=? ";//登录成功后将个人数据记录到全局里面
                                    lianjie.ps = lianjie.con.prepareStatement(sql1);//预编译
                                    lianjie.ps.setString(1, username1);
                                    lianjie.re = lianjie.ps.executeQuery();
                                    while (lianjie.re.next()){
                                            QuanJU.getInstance().shoujihao=lianjie.re.getString("phone");
                                            QuanJU.getInstance().touxiang=lianjie.re.getString("touxiang");
                                            QuanJU.getInstance().WODianZanDe=lianjie.re.getString("WODianZanDe");
                                    }
                                    QuanJU.getInstance().username=username1;
                                    QuanJU.getInstance().password=password1;

                                    Toast.makeText(getApplicationContext(), "登录成功！", LENGTH_SHORT).show();
                                    QuanJU.getInstance().isDengLu=1;
                                    /**
                                     * 登录成功，则调用userXinXi中的fuzhi方法为用户赋值，并返回一个结果
                                     *用返回的结果当做参数在页面中传输，来实现每个页面都能实时更新用户状态与数据。
                                     */

                                    Intent i=new Intent(MainActivity.this,userActivity.class);
                                    MainActivity.this.startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "密码错误！", LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if (fag == 0) {
                                Toast.makeText(getApplicationContext(), "用户不存在！", LENGTH_SHORT).show();
                                return;
                            }
                            lianjie.closeResoure();//释放链接
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
        }else if(view.getId()==R.id.register_btn){//注册
            Intent i=new Intent(MainActivity.this,registActivity.class);
            MainActivity.this.startActivity(i);
        }else if(view.getId()==R.id.forgot_password_btn){//忘记密码
            Intent i=new Intent(MainActivity.this,WangJiMiMaActivity.class);
            MainActivity.this.startActivity(i);
        }
    }
}
