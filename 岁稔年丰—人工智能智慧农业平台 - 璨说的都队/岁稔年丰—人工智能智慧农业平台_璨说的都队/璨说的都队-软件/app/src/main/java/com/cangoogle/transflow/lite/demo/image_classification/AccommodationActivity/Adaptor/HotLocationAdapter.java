package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.data.HotLocation;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class HotLocationAdapter extends RecyclerView.Adapter<HotLocationAdapter.ViewHolder> {
    List<HotLocation> hotLocationList=new ArrayList<>();
    public HotLocationAdapter(List<HotLocation> list){
        this.hotLocationList=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_accommodation_hot_location,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotLocation hotLocation=hotLocationList.get(position);
        //holder.imageView.setBackgroundResource(hotLocation.getTestId());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里设置跳转点击事件
            }
        });
        //这个后面再写
    }

    @Override
    public int getItemCount() {
        return hotLocationList.size();
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardViewHotLocation);
            imageView=(ImageView)itemView.findViewById(R.id.imageViewHotLocation);
        }
    }
}
