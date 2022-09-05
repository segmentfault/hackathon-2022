package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Adaptor.MoreCommentAdapter;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ItemComment;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class MoreCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_comment);
        Intent intent=getIntent();
        this.url=intent.getStringExtra("url");
        getList();
        setView();
    }
    private String url;
    private void getList(){
        //从后端获取数据
    }
    private RecyclerView recyclerView;
    private MoreCommentAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private void setView(){
        //以下为测试代码，该list应该从服务器获取
        List<ItemComment> list=new ArrayList<>();
        for(int i=1;i<=3;i++){
            ItemComment itemComment=new ItemComment();
            itemComment.setSendTime("2021年9月11日");
            itemComment.setName("社会玥姐");
            itemComment.setGoodNum(5);
            itemComment.setContent("我玥姐啊，最喜欢阿巴阿巴阿巴阿巴阿巴阿巴啦~\n");
            String[] s={"","",""};
            itemComment.setUrls(s);
            list.add(itemComment);
        }
        ItemComment itemComment=new ItemComment();
        itemComment.setSendTime("2021年9月11日");
        itemComment.setName("社会玥姐");
        itemComment.setGoodNum(5);
        itemComment.setContent("我玥姐啊，最喜欢阿巴阿巴阿巴阿巴阿巴阿巴啦~\n");
        list.add(itemComment);

        recyclerView=findViewById(R.id.recyclerViewMoreComment);
        adapter=new MoreCommentAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (MoreCommentActivity.this,RecyclerView.VERTICAL,false));


        refreshLayout=findViewById(R.id.swipeRefreshLayoutMoreComment);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            getWindow().setStatusBarColor(Color.parseColor("#5DE6D6"));
        }
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_more_comment);
            Resources resources=MoreCommentActivity.this.getResources();
            Drawable drawable=resources.getDrawable(R.drawable.action_bar_radius);
            actionBar.setBackgroundDrawable(drawable);
            ImageButton imageButton=actionBar.getCustomView()
                    .findViewById(R.id.imageButtonActionBarMoreCommentBack);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            TextView textView=findViewById(R.id.textViewActionBarMoreCommentTitle);
            textView.setText("用户评论");
//            actionBar.setBackgroundDrawable(new ColorDrawable
//                    (Color.parseColor("#5DE6D6")));
        }

    }


}