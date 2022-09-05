package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ItemComment;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailAdapter extends RecyclerView.Adapter<CommentDetailAdapter.ViewHolder> {
    List<ItemComment> list=new ArrayList<>();
    public CommentDetailAdapter(List<ItemComment> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_comment_detail,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemComment itemComment=list.get(position);
        //图片用url或者别的
        holder.textViewName.setText(itemComment.getName());
        holder.textViewContent.setText(itemComment.getContent());
        holder.textViewTime.setText(itemComment.getSendTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName,textViewTime,textViewContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageViewProductionDetailCommentAuthorPicture);
            textViewName=itemView.findViewById(R.id.textViewProductionDetailCommentAuthorName);
            textViewTime=itemView.findViewById(R.id.textViewProductionDetailCommentSendTime);
            textViewContent=itemView.findViewById(R.id.textViewProductionDetailCommentContent);
        }
    }
}
