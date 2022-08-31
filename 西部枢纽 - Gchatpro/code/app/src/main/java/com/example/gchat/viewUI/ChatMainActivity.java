package com.example.gchat.viewUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gchat.LoginActivity;
import com.example.gchat.MainActivity;
import com.example.gchat.R;
import com.example.gchat.WelcomeActivity;
import com.example.gchat.adapter.ChatMsgViewAdapter;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.db.DbConnection;
import com.example.gchat.model.ChatMsgEntity;
import com.example.gchat.model.Person;
import com.example.gchat.utils.APPglobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * User: xiahao
 * DateTime: 2022/4/15 19:45
 * Description: 聊天消息主界面
 */
public class ChatMainActivity extends Activity implements View.OnClickListener {
    private int FriendId; // 当前好友ID
    private Button mBtnSend;            // 发送btn
    private Button mBtnBack;            // 返回btn
    private EditText mEditTextContent;  // 内容框
    private ListView mListView;         // 聊天记录列表
    private ChatMsgViewAdapter mAdapter;// 聊天记录视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 聊天记录对象数组
    public static Handler myHandler3 ;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);
        Log.d("AAA","进入ChatMainActivity,启动onCreat");
        initView();// 初始化view
        initData();// 初始化数据
        mListView.setSelection(mAdapter.getCount() - 1);

        TextView textView= (TextView) findViewById(R.id.chatname);
        Bundle bundle = this.getIntent().getExtras();

        name = bundle.getString("name");
        FriendId =  bundle.getInt("id");

        textView.setText(bundle.getString("name")); //解析传递过来的参数
    }


    @Override
    protected void onStop() {
        super.onStop();
        myHandler3 = null;
    }

    @Override
    protected void onResume() { // activity 处于与用户交互界面
        super.onResume();
        Log.d("AAA","进入Resume");
        // 从队列缓存中

        //-----------接收回复消息-------------
        for (JSONObject Fmessage : WelcomeActivity.messageQueue) {
            try {
                if (Fmessage.getInt("id") == FriendId) {
                    // 查出是当前好友
                    ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
                    chatMsgEntity.setName(name);
                    chatMsgEntity.setDate(getDate());  //设置格式化的发送时间
                    chatMsgEntity.setMessage(Fmessage.getString("text"));
                    chatMsgEntity.setMsgType(true);      //设置消息类型，true 接受的 false发送的
                    mDataArrays.add(chatMsgEntity);

                    mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
                    mListView.setSelection(mListView.getCount() - 1); // 接收一条消息时，ListView显示选择最后一项
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int len = WelcomeActivity.messageQueue.size();
        while (len > 0) {
            try {
                if (WelcomeActivity.messageQueue.peek().getInt("id") == FriendId) {
                    WelcomeActivity.messageQueue.remove();
                } else {
                    JSONObject jsonObject = WelcomeActivity.messageQueue.peek();
                    WelcomeActivity.messageQueue.remove();
                    WelcomeActivity.messageQueue.offer(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            len--;
        }

        // 两个方案，一个是即时接收消息，但是会出现接收者在打开聊天界面之前丢掉消息的情况,另一种是写入数据库中每次从数据库中提取消息
        myHandler3 = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        // 处理messagequeue
                        for (JSONObject Fmessage : WelcomeActivity.messageQueue) {
                            try {
                                if (Fmessage.getInt("id") == FriendId) {
                                    // 查出是当前好友
                                    ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
                                    chatMsgEntity.setName(name);
                                    chatMsgEntity.setDate(getDate());  //设置格式化的发送时间
                                    chatMsgEntity.setMessage(Fmessage.getString("text"));
                                    chatMsgEntity.setMsgType(true);      //设置消息类型，true 接受的 false发送的
                                    mDataArrays.add(chatMsgEntity);

                                    mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
                                    mListView.setSelection(mListView.getCount() - 1); // 接收一条消息时，ListView显示选择最后一项
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        int len = WelcomeActivity.messageQueue.size();
                        while (len > 0) {
                            try {
                                if (WelcomeActivity.messageQueue.peek().getInt("id") == FriendId) {
                                    WelcomeActivity.messageQueue.remove();
                                } else {
                                    JSONObject jsonObject = WelcomeActivity.messageQueue.peek();
                                    WelcomeActivity.messageQueue.remove();
                                    WelcomeActivity.messageQueue.offer(jsonObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            len--;
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
    /**
     * 初始化view
     * 找出页面的控件
     */
    public void initView() {
        mListView= (ListView) findViewById(R.id.listview);
        mBtnSend=(Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.chatGoHome);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }


    //消息数组
    private String[] msgArray = new String[] { "您好" };
    //时间数组
    private String[] dataArray = new String[] { getDate()};

    /**
     * 加载消息历史，从数据库中读出
     */
    public void initData() {


        //把数据库中已有的数据拿出来
        //后面再写
//        DbConnection connection=new DbConnection();
//        SQLiteDatabase db=connection.getConnection();
//
//        Cursor cursor=db.query("tb_person",null,null,null,null,null,null);
//        while ( cursor.moveToNext() ){ // 后面需要改成查表，而不是遍历
//            int namenum=cursor.getColumnIndex("name");
//            int ID = cursor.getColumnIndex("id");
//            String name=cursor.getString(namenum);
//            int id = cursor.getInt(ID);
//
//            Person person=new Person(name,id);
//           // personList.add(person);
//            cursor.moveToNext();
//        }
//        for (int i = 0; i < msgArray.length; i++) {
//            ChatMsgEntity entity = new ChatMsgEntity();
//            entity.setDate(dataArray[i]);
//            if (i % 2 == 0) {
//
//                Bundle bundle = this.getIntent().getExtras();//解析传递过来的参数
//                String name = bundle.getString("name");
//
//                entity.setName(name);   //设置对方姓名
//                entity.setMsgType(true); // 收到的消息
//
//            } else {
//
//                entity.setName(APPglobal.NAME);   //设置自己姓名
//                entity.setMsgType(false);// 发送的消息
//
//            }
//            entity.setMessage(msgArray[i]);//消息内容
//            mDataArrays.add(entity);
//        }
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
            case R.id.chatGoHome:// 返回按钮点击事件
                Intent intent = new Intent();
                intent.setClass(ChatMainActivity.this, MainActivity.class);
                ChatMainActivity.this.startActivity(intent);
                break;
        }
    }


    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {

            //-----------发送者-------------

            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName(APPglobal.NAME);    //设置发送消息消息者姓名
            entity.setDate(getDate());  //设置格式化的发送时间
            entity.setMessage(contString); //设置发送内容
            entity.setMsgType(false);      //设置消息类型，true 接受的 false发送的
            WelcomeActivity.tcPclient.jsonObject = new JSONObject();
            try {
                WelcomeActivity.tcPclient.jsonObject.put("act",2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                WelcomeActivity.tcPclient.jsonObject.put("id",FriendId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                WelcomeActivity.tcPclient.jsonObject.put("text",contString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            WelcomeActivity.tcPclient.senddata();

            mDataArrays.add(entity);

            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            mEditTextContent.setText("");// 清空编辑框数据
            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
            Log.d("AAA","发送OK");
        }
    }
    /**
     * 发送消息时，获取当前事件
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date());
    }
}
