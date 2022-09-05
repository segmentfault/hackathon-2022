package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Live_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Pic_Words_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Pic_Words_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Pic_Word_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Pic_Words_Adapter adapter;
    protected static List<Pic_Words_Bean> list;
    private CardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_third_pic_word);

        initView();

    }

    private void initView() {

        recyclerView = (RecyclerView) this.findViewById(R.id.pic_word_recyclerview);

        list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Pic_Words_Bean list_bean = new Pic_Words_Bean("15天练就简单动物剪纸技艺，拯救手残党");
            list.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Pic_Word_Activity.this,LinearLayoutManager.VERTICAL,false);
        adapter = new Pic_Words_Adapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
