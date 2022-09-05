package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.paddle.lite.demo.image_classification.R;


import com.baidu.paddle.lite.demo.image_classification.entity.User;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;



public class LoginActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnCode;//验证码登录
    private CheckBox iscormire;//验证码登录
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor;
    private String FILE = "saveUserNamePwd";//用于保存SharedPreferences的文件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


        //主要实现点击登录，存储账号密码功能
        initData();
        //show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    protected void initView() {
        etAccount = findViewById(R.id.login_account);
        etPwd = findViewById(R.id.login_password);
        btnLogin = (Button)findViewById(R.id.login_btn);
        btnCode = (Button)findViewById(R.id.click_login);
        iscormire = (CheckBox) findViewById(R.id.ischeckBox);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

    }

    //展示本地存储中的账号密码
    public void show(){
        pref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("reme_pwd",false);

//        if(remePass.isChecked()){
//            String phone = pref.getString("phone","");
//            String pwd = pref.getString("pwd","");
//            etAccount.setText(phone);
//            etPwd.setText(pwd);
//            remePass.setChecked(true);
//        Log.d("Login",phone);
//        }

    }




    protected void initData() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if( iscormire.isChecked()){
                    if(account != null  && pwd != null){
                        final User user = new User();
                        user.setUsername(account);
                        user.setPassword(pwd);
                        user.login(new SaveListener<User>() {
                            @Override
                            public void done(User bmobUser, BmobException e) {
                                if (e == null) {
                                    User user = BmobUser.getCurrentUser(User.class);
                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this, "用户名密码不为空", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"请确定协议内容",Toast.LENGTH_LONG).show();
                }


            }
        });
    }






}