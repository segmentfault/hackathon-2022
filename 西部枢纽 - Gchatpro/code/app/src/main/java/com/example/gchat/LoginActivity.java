package com.example.gchat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.model.Person;
import com.example.gchat.utils.APPglobal;
import com.example.gchat.utils.MessageActivity;
import com.example.gchat.viewUI.RegistActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {

    private static boolean isExit=false;//判断是否直接退出程序

    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    public static Handler mHandler1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);   //用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);         //密码控件

        //setOnEditorActionListener这个方法，并不是在我们点击EditText的时候触发，
        // 也不是在我们对EditText进行编辑时触发，而是在我们编辑完之后点击软键盘上的回车键才会触发。//zwnbspzwnbsp
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ( id == R.id.login || id == EditorInfo.IME_NULL ) {
                    attemptLogin();//调用函数检查登陆信息是否合法
                    return true;
                }
                return false;
            }
        });

        //登录按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });

        //注册按钮
        Button registButton= (Button) findViewById(R.id.register);
        registButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegistActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        Button phoneregi = (Button) findViewById(R.id.phoneregister);
        phoneregi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,PhoneLoginActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });


    }


    /**
     * 输入信息的检查
     */
    private void attemptLogin() {

        // 初始化错误信息为null
        mEmailView.setError(null);
        mPasswordView.setError(null);


        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;
//
//        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
           focusView = mPasswordView;
            cancel = true;
       }

       //  检查邮箱
        if ( TextUtils.isEmpty(email) && !isAccountValid(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if ( cancel ) {//非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        } else {//合法信息
           // 登陆跳转逻辑
            chechLogin(email,password);
        }
    }

    /**
     * 密码是否合法：至少需要4位
     * @param password
     * @return
     */

    private boolean isPasswordValid(String password) {
        return password.length() >= 1&&password.length()<=20;
    }
    private boolean isAccountValid(String account) {
        return account.length() >= 1&&account.length()<=20;
    }

    public void chechLogin(String username,String password){
        // 原来的版本是与数据库交互现在改为与服务器交互

        // 与服务器交互检查用户是否存在

        WelcomeActivity.tcPclient.jsonObject = new JSONObject();

        try {
            WelcomeActivity.tcPclient.jsonObject.put("act",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            WelcomeActivity.tcPclient.jsonObject.put("account",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            WelcomeActivity.tcPclient.jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WelcomeActivity.tcPclient.jsonRead = null;

        mHandler1 = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        try {
                            if((WelcomeActivity.tcPclient.jsonRead.getInt("act")==4)&&WelcomeActivity.tcPclient.jsonRead.getBoolean("state")==true){
                                Intent intent=new Intent();
                                Log.d("AAA","登录");

                                APPglobal.id = WelcomeActivity.tcPclient.jsonRead.getInt("id");
                                APPglobal.NAME = WelcomeActivity.tcPclient.jsonRead.getString("name");

                                if(!WelcomeActivity.tcPclient.jsonRead.isNull("friList")) {
                                    JSONObject FRI = WelcomeActivity.tcPclient.jsonRead.getJSONObject("friList");
                                    PersonDAO personDAO = new PersonDAO();
                                    Iterator iterator = FRI.keys();
                                    while (iterator.hasNext()) {
                                        String id = (String) iterator.next();
                                        String name = FRI.getString(id);
                                        int ID = Integer.valueOf(id).intValue();
                                        Person person = new Person(name, ID);
                                        personDAO.insert(person);
                                    }
                                }
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

//                                WelcomeActivity.editor = WelcomeActivity.pref.edit();
//                                WelcomeActivity.editor.putBoolean("islogin",true);
//                                WelcomeActivity.editor.apply();

                                intent.setClass(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(intent);

                            }
                            else{
                                Toast.makeText(LoginActivity.this, "登录失败，请检查是否注册或密码是否有误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        WelcomeActivity.tcPclient.senddata();
    }

}

