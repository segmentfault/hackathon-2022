package com.example.gchat;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * User: cy
 * DateTime: 2022/5/3 15:10
 * Description:
 */
public class PhoneLoginActivity extends AppCompatActivity {
    private Button timeButton;
    private EditText mphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneregist);
        mphone=(EditText) findViewById(R.id.phone);
        timeButton = (Button) findViewById(R.id.requestAuthCodeButton);
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCountDownTimer.start();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.loginButton);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PhoneLoginActivity.this, MainActivity.class);
                PhoneLoginActivity.this.startActivity(intent);
              //  attemptLogin();//调用函数检查登陆信息是否合法
               // isMobileNO();
            }
        });
    }
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            timeButton.setClickable(false);
            timeButton.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            timeButton.setText("重新获取");
            //设置可点击
            timeButton.setClickable(true);
        }
    }





//    public static boolean isMobileNO() {
//        mphone.setError(null);
//                /**
//                 * 判断字符串是否符合手机号码格式
//                 * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
//                 * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
//                 * 电信号段: 133,149,153,170,173,177,180,181,189
//                 * @param str
//                 * @return 待检测的字符串
//                 */
//        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty()
//            return false;
//        else
//            return mobileNums.matches(telRegex);
//    }
}
