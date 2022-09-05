package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.Adapter.Agritainment_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Agritainment_bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class Agritainment_Product_Activity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected static List<Agritainment_bean> list;
    protected Agritainment_Adapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agritainment_details_activity);
        init_recyclerveiw();
    }

    private void init_recyclerveiw() {
        list = new ArrayList<>();
//        recyclerView = (RecyclerView) this.findViewById(R.id.agritainment_down_recyclerview);

//        for (int i = 0; i < 10 ; i++) {
//            Agritainment_bean list_bean = new Agritainment_bean
//                    ("套餐一");
//            list.add(list_bean);
//
//        }

//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Agritainment_Product_Activity.this,LinearLayoutManager.HORIZONTAL,false);
//        adapter = new Agritainment_Adapter(list);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.scrollToPosition(0);
    }
}
