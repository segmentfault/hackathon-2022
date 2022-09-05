package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Live_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.live_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class LiveActivity extends AppCompatActivity {
    protected RecyclerView recyclerView1;
    protected Live_Adapter live_adapter;
    protected static List<live_Bean> list_beans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        initRecyclerView();
    }

    private void initRecyclerView() {
        list_beans = new ArrayList<>();
        recyclerView1 = (RecyclerView) findViewById(R.id.lives_recyclerview);

        for (int i = 0; i < 5; i++) {
            live_Bean list_bean = new live_Bean
                    ("15天练就简单动物剪纸技艺，拯救手残党","https://www.twoyl.cn/xczxpt/banner/a3.jpg");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(LiveActivity.this,LinearLayoutManager.VERTICAL,false);
        live_adapter = new Live_Adapter(list_beans);
        recyclerView1.setAdapter(live_adapter);
        recyclerView1.setLayoutManager(gridLayoutManager);
    }
}