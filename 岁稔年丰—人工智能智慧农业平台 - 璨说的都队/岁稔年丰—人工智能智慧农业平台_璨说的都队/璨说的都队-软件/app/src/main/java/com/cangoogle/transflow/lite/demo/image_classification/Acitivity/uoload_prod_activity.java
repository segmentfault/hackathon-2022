package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.entity.User;
import com.baidu.paddle.lite.demo.image_classification.entity.order_buy;
import com.google.android.exoplayer2.text.ssa.SsaDecoder;

import java.util.concurrent.BlockingDeque;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class uoload_prod_activity extends AppCompatActivity {
    public static final String INSTANCE_TITLE="title";
    public static final String INSTANCE_PRICE="price";
    private EditText name,phone,address,number;
    private CardView button;
    private String Sname,Sphone,Saddress,Snumber,Sprotitle,Sprice;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prod);
        Intent intent=getIntent();
        Sprotitle=intent.getStringExtra(INSTANCE_TITLE);
        Sprice=intent.getStringExtra(INSTANCE_PRICE);
        name = (EditText) findViewById(R.id.buy_name);
        phone = (EditText) findViewById(R.id.buy_phone);
        address = (EditText) findViewById(R.id.buy_adress);
        number = (EditText) findViewById(R.id.buy_number);
        button = (CardView) findViewById(R.id.buy_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Sname != null && Sphone != null && Saddress != null && Snumber != null && Sprotitle != null && Sprice != null){
                    order_buy p2 = new order_buy();
                    p2.setName(Sname);
                    p2.setPhone(Sphone);
                    p2.setAddress(Saddress);
                    p2.setNumber(Snumber);
                    p2.setProdtitle(Sprotitle);
                    int temp_price = Integer.parseInt(Sprice);
                    int temp_number = Integer.parseInt(Snumber);
                    String t_price = String.valueOf(temp_price * temp_number);
                    User user = User.getCurrentUser(User.class);
                    p2.setPrice(t_price);
                    p2.setUserid(user);
                    p2.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                Log.d("Bmob", "done: " + "成功");
                                finish();
                            }else{
                                Log.d("Bmob", "done: " + "失败");
                                Toast.makeText(uoload_prod_activity.this,"上传失败，请检查网络",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Sname = s.toString();
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Sphone = s.toString();
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Saddress = s.toString();
            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Snumber = s.toString();
            }
        });

    }
}
