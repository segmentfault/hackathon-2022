package com.baidu.paddle.lite.demo.image_classification.Adapter.Craft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_Second_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Craft_Second_Adapter extends RecyclerView.Adapter<Craft_Second_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Craft_Second_Bean> MyInstanceList;


    public Craft_Second_Adapter(List<Craft_Second_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.craft_2_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Craft_Second_Bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.craft_second_image);
        }
    }
}
