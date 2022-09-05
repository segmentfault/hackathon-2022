package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.R;


public class MoreCommentStarAdaptor extends RecyclerView.Adapter<MoreCommentStarAdaptor.ViewHolder> {
    private int num;
    public MoreCommentStarAdaptor(int num){this.num=num;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_star_more_comment,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return num;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageViewMoreCommentStar);
        }
    }
}
