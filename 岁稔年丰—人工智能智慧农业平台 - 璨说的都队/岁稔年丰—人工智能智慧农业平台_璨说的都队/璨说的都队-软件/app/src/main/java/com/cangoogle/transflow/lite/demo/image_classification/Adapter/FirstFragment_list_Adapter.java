package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Topic_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Frist_fragment_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class FirstFragment_list_Adapter extends RecyclerView.Adapter<FirstFragment_list_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Frist_fragment_list_Bean> MyInstanceList;


    public FirstFragment_list_Adapter(List<Frist_fragment_list_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.fragment_first_list_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Frist_fragment_list_Bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisContext.startActivity(new Intent(ThisContext, Topic_Activity.class));
            }
        });
        holder.imageView1.setImageResource(instance.getId_2());
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisContext.startActivity(new Intent(ThisContext, Topic_Activity.class));
            }
        });
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisContext.startActivity(new Intent(ThisContext, Topic_Activity.class));
            }
        });
        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisContext.startActivity(new Intent(ThisContext, Topic_Activity.class));
            }
        });
        holder.imageView2.setImageResource(instance.getId_3());
        holder.imageView3.setImageResource(instance.getId_4());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        ImageView imageView,imageView1,imageView2,imageView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.container);
            imageView = (ImageView) itemView.findViewById(R.id.fragment_first_down_images_1);
            imageView1 = (ImageView) itemView.findViewById(R.id.fragment_first_down_images_2);
            imageView2 = (ImageView) itemView.findViewById(R.id.fragment_first_down_images_3);
            imageView3 = (ImageView) itemView.findViewById(R.id.fragment_first_down_images_4);
        }
    }
}
