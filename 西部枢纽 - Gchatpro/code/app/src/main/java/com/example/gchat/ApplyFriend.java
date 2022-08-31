package com.example.gchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gchat.MainActivity;
import com.example.gchat.R;
import com.example.gchat.WelcomeActivity;
import com.example.gchat.adapter.ApplyFriendAdapter;
import com.example.gchat.adapter.ChatMsgViewAdapter;
import com.example.gchat.model.ApplyFriendModel;
import com.example.gchat.model.ChatMsgEntity;
import com.example.gchat.utils.APPglobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * User: xiahao
 * DateTime: 2022/4/23 18:04
 * Description:申请好友类
 */
public class ApplyFriend extends Activity implements View.OnClickListener {

    private ListView mListView;         // 好友申请列表
    private ApplyFriendAdapter mAdapter;// 好友申请列表视图的Adapter
    private List<ApplyFriendModel> mDataArrays = new ArrayList<ApplyFriendModel>();// 对象数组
    public static Handler myHandler4 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyview);
        initView();// 初始化view
        initData();// 初始化数据
        Log.d("AAA",String.valueOf(mDataArrays.size()));
        mListView.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    protected void onResume() { // activity 处于与用户交互界面
        super.onResume();
        // 从队列缓存中
    }
    /**
     * 初始化view
     * 找出页面的控件
     */
    public void initView() {
        mListView = (ListView) findViewById(R.id.apply);
    }
    /**
     * 加载消息历史，从数据库中读出
     */
    public void initData() {
        // 遍历申请列表
        for(JSONObject jo : WelcomeActivity.ApplyFriendQueue){
            try {
                mDataArrays.add(new ApplyFriendModel(jo.getInt("act"),jo.getInt("id"),jo.getString("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter = new ApplyFriendAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }
    @Override
    public void onClick(View v) {

    }
}
