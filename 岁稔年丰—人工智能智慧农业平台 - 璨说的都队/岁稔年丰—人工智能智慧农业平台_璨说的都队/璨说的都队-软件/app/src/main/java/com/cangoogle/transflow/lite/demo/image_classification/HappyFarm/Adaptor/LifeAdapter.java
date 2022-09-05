package com.baidu.paddle.lite.demo.image_classification.HappyFarm.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Data.ItemLife;
import com.baidu.paddle.lite.demo.image_classification.R;


import java.util.ArrayList;
import java.util.List;

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHolder> {
    private List<ItemLife> lives=new ArrayList<>();
    private View view;
    private Context context;


    public LifeAdapter(List<ItemLife> lives){this.lives=lives;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        view= LayoutInflater.from(context)
                .inflate(R.layout.happy_farm_life_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.linearLayout.setBackground();
        //设置图片的

        ItemLife itemLife=lives.get(position);
        holder.textViewTitle.setText(itemLife.getTitle());
        holder.textViewContent.setText(itemLife.getContent());
        Glide.with(context)
                .asBitmap().load(itemLife.getUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap>
                                                        transition) {
                        Drawable drawable=new BitmapDrawable(resource);
                        holder.linearLayout.setBackground(drawable);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return lives.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewContent;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textViewHappyFarmLifeTitle);
            textViewContent=itemView.findViewById(R.id.textViewHappyFarmLifeContent);
            linearLayout=itemView.findViewById(R.id.LinerLayoutHappyFarmLife);

        }
    }
}
