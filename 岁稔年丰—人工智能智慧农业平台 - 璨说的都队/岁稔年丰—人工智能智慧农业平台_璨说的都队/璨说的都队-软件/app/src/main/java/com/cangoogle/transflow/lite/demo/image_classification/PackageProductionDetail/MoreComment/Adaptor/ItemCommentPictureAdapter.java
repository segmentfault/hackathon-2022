package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Data.ItemCommentPicture;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class ItemCommentPictureAdapter extends RecyclerView.Adapter<ItemCommentPictureAdapter.ViewHolder> {
    private List<ItemCommentPicture> list=new ArrayList<>();
    public ItemCommentPictureAdapter(List<ItemCommentPicture> list){this.list=list;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picture_more_comment_detail,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置每个人的评论图片

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageViewMoreCommentPicture);
        }
    }
}
