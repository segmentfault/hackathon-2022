package com.example.gchat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.gchat.dao.PersonDAO;
import com.example.gchat.utils.APPglobal;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * User: xiahao
 * DateTime: 2022/4/19 14:24
 * Description:加好友
 */
public class AddFriendActivity extends AppCompatActivity {
    private Handler addFriendHandler;
    // 发起添加朋友
    public static Handler myhandlerAddFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriendactivity);

        EditText Search = (EditText) findViewById(R.id.friends_id);

        Button addFri = (Button) findViewById(R.id.add_friendbutton);
        addFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = Search.getText().toString();
                if (account.isEmpty()) {
                    Toast.makeText(AddFriendActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    WelcomeActivity.tcPclient.jsonObject = new JSONObject();
                    try {
                        WelcomeActivity.tcPclient.jsonObject.put("act", 5);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        WelcomeActivity.tcPclient.jsonObject.put("account", account);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    WelcomeActivity.tcPclient.jsonRead = null;
                    WelcomeActivity.tcPclient.senddata();
                    myhandlerAddFriend = new Handler() {
                        @SuppressLint("HandlerLeak")
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 1:
                                    try {
                                        if (WelcomeActivity.tcPclient.jsonRead.getInt("state") == 0) {
                                            Toast.makeText(AddFriendActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        } else if (WelcomeActivity.tcPclient.jsonRead.getInt("state") == -1) {
                                            Toast.makeText(AddFriendActivity.this, "没有这个账号", Toast.LENGTH_SHORT).show();
                                        } else if (WelcomeActivity.tcPclient.jsonRead.getInt("state") == -2) {
                                            Toast.makeText(AddFriendActivity.this, "不能添加自己", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AddFriendActivity.this, "你们已经是好友呢", Toast.LENGTH_SHORT).show();
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

                    // 之后由用户自己按返回键返回
                }
            }
        });


    }
}