package com.animee.health.food_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.animee.health.R;
import com.animee.health.bean.FoodBean;
import com.animee.health.bean.FoodUtils;
import com.animee.health.food_grid.FoodDescActivity;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class InfoListActivity extends AppCompatActivity implements View.OnClickListener{
    EditText searchEt;
    ImageView searchIv,flushIv;
    ListView showLv;
//    ListView内部数据源
    List<FoodBean>mDatas;
    List<FoodBean>allFoodList;
    private InfoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
//        查找控件
        initView();
//        2.找到ListView对应的数据源
        mDatas = new ArrayList<>();
        allFoodList = FoodUtils.getAllFoodList();
        mDatas.addAll(allFoodList);
//        3.创建适配器  BaseAdapter的子类
        adapter = new InfoListAdapter(this, mDatas);
        showLv.setAdapter(adapter); //4.设置适配器
//        设置单向点击监听功能
        setListener();
    }

    private void setListener() {
        showLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodBean foodBean = mDatas.get(position);
                Intent intent = new Intent(InfoListActivity.this, FoodDescActivity.class);
                intent.putExtra("food",foodBean);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        searchEt = findViewById(R.id.info_et_search);
        searchIv = findViewById(R.id.info_iv_search);
        flushIv = findViewById(R.id.info_iv_flush);
        showLv = findViewById(R.id.infolist_lv);
        searchIv.setOnClickListener(this); //添加点击事件的监听器
        flushIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_iv_flush:  //刷新点击
                searchEt.setText("");
                mDatas.clear();
                mDatas.addAll(allFoodList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.info_iv_search:  //搜索点击
//                1.获取输入内容，判断不为空
                String msg = searchEt.getText().toString().trim();  //获取输入信息
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
//                判断所有食物列表的标题是否包含输入内容，如果包含，就添加到小集合中
                List<FoodBean>list = new ArrayList<>();
                for (int i = 0; i < allFoodList.size(); i++) {
                    String title = allFoodList.get(i).getTitle();
                    if (title.contains(msg)) {
                        list.add(allFoodList.get(i));
                    }
                }
                mDatas.clear();   // 清空ListView的适配器数据源内容
                mDatas.addAll(list);  // 添加新的数据到数据源中
                adapter.notifyDataSetChanged(); // 提示适配器更新
                break;
        }
    }
}
