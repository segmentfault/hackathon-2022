package com.baidu.paddle.lite.demo.image_classification.HappyFarm.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.baidu.paddle.lite.demo.image_classification.HappyFarm.Data.ItemYard;
import com.baidu.paddle.lite.demo.image_classification.R;


import java.util.ArrayList;
import java.util.List;

public class YardAdapter extends RecyclerView.Adapter<YardAdapter.ViewHolder> {
    private List<ItemYard> itemYards=new ArrayList<>();
    public YardAdapter(List<ItemYard> list){this.itemYards=list;}
    private View view;
    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.happy_farm_life_yard_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemYard itemYard=itemYards.get(position);
        holder.textView.setText(itemYard.getName());
        Glide.with(context)
                .asBitmap().load(itemYard.getUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap>
                                                        transition) {
                        Drawable drawable=new BitmapDrawable(resource);
                        holder.frameLayout.setBackground(drawable);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemYards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout=itemView.findViewById(R.id.frameLayoutHappyFarmLifeYard);
            textView=itemView.findViewById(R.id.textViewHappyFarmLifeYardName);
        }
    }
}
