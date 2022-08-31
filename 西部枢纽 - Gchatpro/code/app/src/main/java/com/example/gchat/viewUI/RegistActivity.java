package com.example.gchat.viewUI;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gchat.LoginActivity;
import com.example.gchat.MainActivity;
import com.example.gchat.R;
import com.example.gchat.TCPclient;
import com.example.gchat.WelcomeActivity;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.model.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * User: cy
 * DateTime: 2022/5/3 15:10
 * Description:
 */
public class RegistActivity   extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    private EditText repassword;              //确认密码
    private EditText rename;                  //真实姓名
    public static boolean Low = false;
    public static Handler mHandler2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);//查找用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);      //查找密码控件
        repassword=(EditText) findViewById(R.id.repassword);         //重复密码控件
        rename=(EditText) findViewById(R.id.rename);                 //真实姓名控件

        //注册按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法

            }
        });
    }


    /**
     * 输入的检查
     */
    private void attemptLogin() {

        // 初始化控件错误信息
        mEmailView.setError(null);
        mPasswordView.setError(null);
        repassword.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String mrepassword = repassword.getText().toString();
        String mname=rename.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(!mrepassword.equals(password)){
            repassword.setError("两次密码不一致");
            focusView = repassword;
            cancel = true;
        }

        // 检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        //检查真实姓名
        if(mname.equals("")){
            rename.setError("真实姓名不能为空");
            focusView = rename;
            cancel = true;
        }

        if ( cancel ) {
            focusView.requestFocus();
        } else {
            //注册逻辑实现
            /*-------------------------------*/
            Low = false;
            String mrename=rename.getText().toString();
            String memail=mEmailView.getText().toString();
            String mpassword=repassword.getText().toString();


            WelcomeActivity.tcPclient.jsonObject = new JSONObject();

            try {
                WelcomeActivity.tcPclient.jsonObject.put("act",0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                WelcomeActivity.tcPclient.jsonObject.put("account",memail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                WelcomeActivity.tcPclient.jsonObject.put("password",mpassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                WelcomeActivity.tcPclient.jsonObject.put("name",mrename);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WelcomeActivity.tcPclient.jsonRead = null;
            WelcomeActivity.tcPclient.senddata();
            mHandler2 = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 1:
                            try {
                                if((WelcomeActivity.tcPclient.jsonRead.getInt("act")==3)&&WelcomeActivity.tcPclient.jsonRead.getBoolean("state")==true){
                                    Intent intent=new Intent();
                                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    intent.setClass(RegistActivity.this, LoginActivity.class);
                                    RegistActivity.this.startActivity(intent);
                                }
                                else{
                                    Toast.makeText(RegistActivity.this, "注册失败，有相同账号", Toast.LENGTH_SHORT).show();
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

        }

    }


    /**
     * 密码是否和非法，至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }


}
