package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Employment_list_Adapter extends RecyclerView.Adapter<Employment_list_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Classroom_list_Bean> MyInstanceList;


    public Employment_list_Adapter(List<Classroom_list_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.employment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Classroom_list_Bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getTitle());
        holder.price.setText(instance.getPrice());
        holder.shopname.setText(instance.getShopname());
        holder.username.setText(instance.getUsername());
        holder.types.setText(" "+ "·" + instance.getType());
        Glide.with(ThisContext)
                .load(instance.getId_1())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView,price,username,shopname,types;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.employ_title);
            price = (TextView) itemView.findViewById(R.id.employ_price);
            username = (TextView) itemView.findViewById(R.id.employ_username);
            shopname = (TextView) itemView.findViewById(R.id.employ_shopname);
            types = (TextView)itemView.findViewById(R.id.employ_type);
            imageView = (ImageView) itemView.findViewById(R.id.employ_images);

        }
    }
}
