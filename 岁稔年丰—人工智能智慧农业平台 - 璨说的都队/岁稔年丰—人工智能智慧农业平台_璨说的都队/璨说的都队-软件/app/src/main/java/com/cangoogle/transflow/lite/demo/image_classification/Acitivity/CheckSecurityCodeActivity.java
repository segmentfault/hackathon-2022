package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.data.PhoneCodeLayout;


public class CheckSecurityCodeActivity extends AppCompatActivity {
    private String phoneNumber,code;
    private TextView textViewSendMessage,textViewTime;
    private TextView textViewTryAgain;
    private final String messagePre="短信验证码以发送至 ";
    private final String timeSuf="秒后重新获取验证码";
    private PhoneCodeLayout phoneCodeLayout;
    private final int INIT_SECOND=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_security_code);
        Intent intent=getIntent();
        phoneNumber=intent.getStringExtra("PhoneNumber");
        initView();
        sendSecurityCode(phoneNumber);


    }

    private void initView(){
        phoneCodeLayout=(PhoneCodeLayout)findViewById(R.id.phoneCodeLayout);
        phoneCodeLayout.setOnInputListener(new PhoneCodeLayout.OnInputListener() {
            @Override
            public void onSuccess(String code) {
                //这里的code是验证码
                if(checkCode(code)){

                }else{
                    Toast.makeText(CheckSecurityCodeActivity.this
                            , "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onInput() {

            }
        });
        textViewTime=(TextView)findViewById(R.id.textViewTimeCalculate);
        textViewSendMessage=(TextView)findViewById(R.id.textViewSendMessage);
        textViewSendMessage.setText(messagePre+phoneNumber.substring(0,3)
                +"****"+phoneNumber.substring(7,11));
        textViewTryAgain=(TextView)findViewById(R.id.textViewTryAgainGet);
        textViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSecurityCode(phoneNumber);
            }
        });
    }

    private boolean checkCode(String code){
        //发送给后端验证码
        return true;
    }
    private int initSecond;
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            initSecond--;
            if(initSecond==0){
                handler.removeCallbacks(this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewTryAgain.setVisibility(View.VISIBLE);
                        textViewTime.setVisibility(View.INVISIBLE);
                    }
                });
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewTime.setText(initSecond+timeSuf);
                    }
                });
                handler.postDelayed(this,1000);
            }
        }
    };

    public void sendSecurityCode(String number){

        //先把验证码发送给后端,发给后端后开始计时
        textViewTryAgain.setVisibility(View.INVISIBLE);
        initSecond=INIT_SECOND;
        textViewTime.setText(initSecond+timeSuf);
        textViewTime.setVisibility(View.VISIBLE);
        handler.postDelayed(runnable,1000);
    }

}