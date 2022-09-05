package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Utils.StringUtils;
import com.baidu.paddle.lite.demo.image_classification.Utils.Util;
import com.baidu.paddle.lite.demo.image_classification.api.Api;
import com.baidu.paddle.lite.demo.image_classification.api.ApiConfig;
import com.baidu.paddle.lite.demo.image_classification.api.TtitCallback;
import com.baidu.paddle.lite.demo.image_classification.des.DES;
import com.baidu.paddle.lite.demo.image_classification.entity.User;

import java.util.HashMap;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity_login {

    private EditText loginNick;//用户昵称
    private EditText etAccount;//注册手机号
    private EditText etPwd;//注册密码
    private EditText etPwd2;//再次输入
    private Button btnResiter;//注册
    public static String postId;
    private Button loginBtn;//返回登录
    private CheckBox checkBox;
    private String Sname,Sphone,Spassword1,Spassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initData();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        loginNick = (EditText) findViewById(R.id.regist_name);
        etAccount = findViewById(R.id.regist_account);
        etPwd = findViewById(R.id.regist_password);
        etPwd2 = findViewById(R.id.regist_password2);
        checkBox = findViewById(R.id.checkBox_rege);
        btnResiter = (Button) findViewById(R.id.regist_btn);
        loginBtn = (Button) findViewById(R.id.login_btn2);

    }

    @Override
    protected void initData() {

        loginNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Sname = s.toString().trim();
            }
        });

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Sphone = s.toString().trim();
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Spassword1 = s.toString().trim();
            }
        });
        etPwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Spassword2 = s.toString().trim();
            }
        });
        //注册
        btnResiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Spassword1.equals(Spassword2)){
                    Toast.makeText(RegisterActivity.this,"用户名密码不匹配",Toast.LENGTH_LONG).show();
                }else {
                    if(checkBox.isChecked()){
                        if(Sname != null && Spassword1!=null && Sphone != null && Spassword2 !=null){
                            final User user = new User();
                            user.setUsername(Sname);
                            user.setPassword(Spassword1);
                            user.setMobilePhoneNumber(Sphone);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "内容不为空", Toast.LENGTH_LONG).show();

                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "请点击阅读同意", Toast.LENGTH_LONG).show();

                    }
                    }


            }
        });
        //直接验证码登陆
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    private void register(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        if (!Util.isPhone(account)) {//验证邮箱格式是否符合


            Toast.makeText(RegisterActivity.this, "输入手机号格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", account);
        params.put("password", pwd);

        DES des = new DES();
        postId = des.eDES(account);
        Log.d("postId", postId );
        params.put("userid", postId);

        Api.config(ApiConfig.REGISTER, params).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(res);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.toString());
            }
        });
    }
}