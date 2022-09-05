package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Cultural_heritage_crad_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Cultural_heritage_crad_down_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Culture_heritage_card_bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Culture_heritage_card_down_bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class Cultural_heritage_Activity extends AppCompatActivity {
    protected RecyclerView recyclerView,recyclerView_down;
    protected static List<Culture_heritage_card_bean> list;
    protected static List<Culture_heritage_card_down_bean> down_beanList;
    protected Cultural_heritage_crad_Adapter adapter;
    protected Cultural_heritage_crad_down_Adapter down_adapter;
    protected Banner banner;
    protected ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_heritage);
        init_recyclerveiw();
        init_recyclerveiw_down();

        back = (ImageView) this.findViewById(R.id.heritage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init_recyclerveiw() {
        list = new ArrayList<>();
        recyclerView = (RecyclerView) this.findViewById(R.id.cultural_recyclerview);

        Culture_heritage_card_bean list_bean = new Culture_heritage_card_bean
                (R.drawable.culture_3,"中阳剪纸");
        Culture_heritage_card_bean list_bean1 = new Culture_heritage_card_bean
                (R.drawable.culture_2,"玥姐最帅");
        Culture_heritage_card_bean list_bean2 = new Culture_heritage_card_bean
                (R.drawable.culture_1,"中阳剪纸");
        Culture_heritage_card_bean list_bean3 = new Culture_heritage_card_bean
                (R.drawable.culture_1,"玥姐最帅");
        Culture_heritage_card_bean list_bean4 = new Culture_heritage_card_bean
                (R.drawable.culture_2,"中阳剪纸");
        list.add(list_bean);
        list.add(list_bean1);
        list.add(list_bean2);
        list.add(list_bean3);
        list.add(list_bean4);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Cultural_heritage_Activity.this,LinearLayoutManager.HORIZONTAL,false);
        adapter = new Cultural_heritage_crad_Adapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.scrollToPosition(0);
    }

    private void init_recyclerveiw_down() {
        down_beanList = new ArrayList<>();
        recyclerView_down = (RecyclerView) this.findViewById(R.id.culture_down_recyclerview);

        Culture_heritage_card_down_bean list_bean = new Culture_heritage_card_down_bean
                (R.drawable.wc_1,"立体光影纸雕灯");
        Culture_heritage_card_down_bean list_bean1 = new Culture_heritage_card_down_bean
                (R.drawable.wc_2,"陶瓷茶具套餐");
        Culture_heritage_card_down_bean list_bean2 = new Culture_heritage_card_down_bean
                (R.drawable.wc_3,"立体光影纸雕灯");
        Culture_heritage_card_down_bean list_bean3 = new Culture_heritage_card_down_bean
                (R.drawable.wc_4,"立体光影纸雕灯");
        Culture_heritage_card_down_bean list_bean4 = new Culture_heritage_card_down_bean
                (R.drawable.wc_5,"立体光影纸雕灯");
        down_beanList.add(list_bean);
        down_beanList.add(list_bean1);
        down_beanList.add(list_bean2);
        down_beanList.add(list_bean3);
        down_beanList.add(list_bean4);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Cultural_heritage_Activity.this,LinearLayoutManager.HORIZONTAL,false);
        down_adapter = new Cultural_heritage_crad_down_Adapter(down_beanList);
        recyclerView_down.setAdapter(down_adapter);
        recyclerView_down.setLayoutManager(gridLayoutManager);
    }
}
