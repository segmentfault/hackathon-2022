package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.MoreDetailImage;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class MoreDetailAdapter extends RecyclerView.Adapter<MoreDetailAdapter.ViewHolder> {
    List<MoreDetailImage> list=new ArrayList<>();
    private Context ThisContext;
    public MoreDetailAdapter(List<MoreDetailImage> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.item_view_production_detail_more_detail_image,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoreDetailImage moreDetailImage=list.get(position);
        //别忘了在这里设置图片
        Glide.with(ThisContext).load(moreDetailImage.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageViewProductionDetailMoreDetail);
        }
    }
}
