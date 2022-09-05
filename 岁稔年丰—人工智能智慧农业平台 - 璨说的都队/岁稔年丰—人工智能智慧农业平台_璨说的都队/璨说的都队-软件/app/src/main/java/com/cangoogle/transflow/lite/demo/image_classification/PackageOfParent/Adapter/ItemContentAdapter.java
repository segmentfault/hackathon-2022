package com.baidu.paddle.lite.demo.image_classification.PackageOfParent.Adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.Data.ItemContent;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class ItemContentAdapter extends RecyclerView.Adapter<ItemContentAdapter.ViewHolder> {
    List<ItemContent> list=new ArrayList<>();
    public ItemContentAdapter(List<ItemContent> list){this.list=list;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_package_of_parent_content,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemContent itemContent=list.get(position);
        holder.textViewTitle.setText(itemContent.getTitle());
        holder.textViewPrice.setText(itemContent.getPrice());
        //imageView用url或者其他的方式来设置
        holder.textViewTime.setText(getFallTime(itemContent.getTime()));
    }
    private String getString(int x){
        Integer k=x;
        return k.toString();
    }
    private String getFallTime(String x){
        return "使用时间: "+x;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewTime;
        TextView textViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageViewItemOfPackageOfParent);
            textViewTitle=(TextView)itemView.findViewById(R.id.textViewItemContentTitleOfPackageOfParent);
            textViewTime=(TextView)itemView.findViewById(R.id.textViewItemContentTimeOfPackageOfParent);
            textViewPrice=(TextView)itemView.findViewById(R.id.textViewItemContentPriceOfPackageOfParent);

        }
    }
}
