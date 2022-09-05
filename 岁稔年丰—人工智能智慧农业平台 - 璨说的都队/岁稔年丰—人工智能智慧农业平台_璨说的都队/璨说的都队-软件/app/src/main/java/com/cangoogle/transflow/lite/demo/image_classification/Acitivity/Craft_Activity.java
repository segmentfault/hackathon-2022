package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Craft.Craft_Banner_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Craft.Craft_Second_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Craft.Craft_Third_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Craft.Craft_first_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Banner.Craft_Banner_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_Second_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_Third_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_first_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.BannerIndicator;
import com.youth.banner.Banner;
import com.youth.banner.transformer.RotateYTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

public class Craft_Activity extends AppCompatActivity{
    protected Banner banner;
    protected RecyclerView recyclerView,recyclerView_1,recyclerView_2;
    protected Craft_first_Adapter craft_first_adapter ;
    protected Craft_Second_Adapter craft_second_adapter;
    protected Craft_Third_Adapter craft_third_adapter;
    protected static List<Craft_first_Bean> list;
    protected static List<Craft_Second_Bean> craft_second_beanList;
    protected static List<Craft_Third_Bean> craft_third_beanList;
    protected ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft);
        initBanner();
        init_craft_first();
        init_craft_second();
        init_craft_third();

        back = (ImageView) this.findViewById(R.id.craft_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init_craft_third() {
        craft_third_beanList = new ArrayList<>();
        recyclerView_2 = (RecyclerView) this.findViewById(R.id.craft_third_recyclerview);

        Craft_Third_Bean list_bean = new Craft_Third_Bean
                (R.drawable.sgy1);
        Craft_Third_Bean list_bean1 = new Craft_Third_Bean
                (R.drawable.sgy2);
        Craft_Third_Bean list_bean2 = new Craft_Third_Bean
                (R.drawable.sgy3);
        Craft_Third_Bean list_bean3 = new Craft_Third_Bean
                (R.drawable.sgy1);
        Craft_Third_Bean list_bean4 = new Craft_Third_Bean
                (R.drawable.sgy2);
        craft_third_beanList.add(list_bean);
        craft_third_beanList.add(list_bean1);
        craft_third_beanList.add(list_bean2);
        craft_third_beanList.add(list_bean3);
        craft_third_beanList.add(list_bean4);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Craft_Activity.this,LinearLayoutManager.HORIZONTAL,false);
        craft_third_adapter = new Craft_Third_Adapter(craft_third_beanList);
        recyclerView_2.setAdapter(craft_third_adapter);
        recyclerView_2.setLayoutManager(gridLayoutManager);

    }

    private void init_craft_second() {
        craft_second_beanList = new ArrayList<>();
        recyclerView_1 = (RecyclerView) this.findViewById(R.id.craft_second_recyclerview);

        Craft_Second_Bean list_bean = new Craft_Second_Bean
                (R.drawable.yc1);
        Craft_Second_Bean list_bean1 = new Craft_Second_Bean
                (R.drawable.yc2);
        Craft_Second_Bean list_bean2 = new Craft_Second_Bean
                (R.drawable.yc3);
        Craft_Second_Bean list_bean3 = new Craft_Second_Bean
                (R.drawable.yc1);
        Craft_Second_Bean list_bean4 = new Craft_Second_Bean
                (R.drawable.yc2);
        craft_second_beanList.add(list_bean);
        craft_second_beanList.add(list_bean1);
        craft_second_beanList.add(list_bean2);
        craft_second_beanList.add(list_bean3);
        craft_second_beanList.add(list_bean4);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Craft_Activity.this,LinearLayoutManager.HORIZONTAL,false);
        craft_second_adapter = new Craft_Second_Adapter(craft_second_beanList);
        recyclerView_1.setAdapter(craft_second_adapter);
        recyclerView_1.setLayoutManager(gridLayoutManager);
    }

    private void init_craft_first() {
        list = new ArrayList<>();
        recyclerView = (RecyclerView) this.findViewById(R.id.craft_first_recyclerview);

        Craft_first_Bean list_bean = new Craft_first_Bean
                    (R.drawable.xiaochi,"民间小吃");
        Craft_first_Bean list_bean1 = new Craft_first_Bean
                (R.drawable.zaji,"民间技艺");
        Craft_first_Bean list_bean2 = new Craft_first_Bean
                (R.drawable.meishu,"民间美术");
        Craft_first_Bean list_bean3 = new Craft_first_Bean
                (R.drawable.wudao,"民间舞蹈");
        Craft_first_Bean list_bean4 = new Craft_first_Bean
                (R.drawable.xiju,"民间戏剧");
        list.add(list_bean);
        list.add(list_bean1);
        list.add(list_bean2);
        list.add(list_bean3);
        list.add(list_bean4);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Craft_Activity.this,LinearLayoutManager.HORIZONTAL,false);
        craft_first_adapter = new Craft_first_Adapter(list);
        recyclerView.setAdapter(craft_first_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initBanner() {
        banner = (Banner)this.findViewById(R.id.fy_banner);
        Craft_Banner_Adapter adapter = new Craft_Banner_Adapter(Craft_Banner_Bean.getTestData());

        banner.setAdapter(adapter)//设置适配器
                .setCurrentItem(3,false)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setBannerRound(BannerUtils.dp2px(13))//圆角
                .setLoopTime(3500)
                .addPageTransformer(new RotateYTransformer())//添加切换效果
                .setIndicator(new BannerIndicator(Craft_Activity.this));//设置指示器
    }
}
