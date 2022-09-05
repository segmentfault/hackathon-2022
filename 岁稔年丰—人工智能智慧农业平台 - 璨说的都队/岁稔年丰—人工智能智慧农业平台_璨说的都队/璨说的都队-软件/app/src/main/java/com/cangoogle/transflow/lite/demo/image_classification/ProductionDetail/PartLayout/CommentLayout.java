package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.MoreCommentActivity;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter.CommentDetailAdapter;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ItemComment;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class CommentLayout extends CardView {
    private Context context;
    public CommentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        loadView();
    }
    //--------------------setUrl
    public void setUrl(String url){
        this.url=url;
    }
    private String url;
    private RecyclerView recyclerView;
    private LinearLayout imageButton;
    private CommentDetailAdapter adapter;
    private List<ItemComment> list=new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    public void setList(List<ItemComment> list){this.list=list;}
    private void initList(){
        /**
         * 要先从主活动里面传进来一个URL，
         * 然后在这里，根据这个url从后端获取评论list
         * （或者只获取前三个comment，用于这个页面的展示）
         */

        for(int i=1;i<=3;i++){
            ItemComment itemComment=new ItemComment();
        itemComment.setContent("我陈玥啊，最喜欢小朋友啦咯咯咯~");
            itemComment.setName("中华玥姐");
            itemComment.setSendTime("三天前");
            list.add(itemComment);
        }
    }
    private void loadView(){
        View view= LayoutInflater.from(context)
                .inflate(R.layout.production_detail_comment,this);
        imageButton=view.findViewById(R.id.imageButtonProductionDetailCommentTitle);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 对于更多地comment，
                 * 要等到跳转到更多评论后再获取
                 * 因此这个intent要传入前面那个与后端交互的url
                 */
                Intent intent=new Intent(context, MoreCommentActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);
            }
        });
        recyclerView=view.findViewById(R.id.recyclerViewProductionDetailComment);
        initList();
        adapter=new CommentDetailAdapter(list);
        gridLayoutManager=new GridLayoutManager(context,1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
