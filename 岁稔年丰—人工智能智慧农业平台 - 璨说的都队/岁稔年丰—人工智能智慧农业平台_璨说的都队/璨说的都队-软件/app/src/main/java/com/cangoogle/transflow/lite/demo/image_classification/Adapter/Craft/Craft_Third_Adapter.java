package com.baidu.paddle.lite.demo.image_classification.Adapter.Craft;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.ScrollGridActivity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_Third_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Craft_Third_Adapter extends RecyclerView.Adapter<Craft_Third_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Craft_Third_Bean> MyInstanceList;


    public Craft_Third_Adapter(List<Craft_Third_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.craft_3_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        ImageView imageView = holder.imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent
                        (ThisContext, ScrollGridActivity.class);
                ThisContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Craft_Third_Bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.craft_third_image);

        }
    }
}
