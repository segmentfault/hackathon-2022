package com.baidu.paddle.lite.demo.image_classification.Adapter.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Cultural_heritage_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.productBean.productBean1;
import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.PackageOfParent;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.ProductionDetailActivity;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class product_Adapter extends RecyclerView.Adapter<product_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<productBean1> MyInstanceList;
    protected String temp;

    public product_Adapter(List<productBean1> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.product_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final productBean1 type = MyInstanceList.get(position);
        temp = type.getPri();
        float temp_1 = Float.parseFloat(temp);
        int temp_2 = (int)temp_1;
        float temp_3 = temp_1 - temp_2;
        temp_3 = temp_3*100;
        int temp_4 = (int)temp_3;
        String temp_5 = Integer.toString(temp_4);
        Glide.with(ThisContext)
                .load(type.getId_1())
                .into(holder.icon);
        holder.name.setText(type.getTitle());
        holder.price1.setText(temp_2+".");
       holder.price2.setText(temp_5);
        /**在这里直接向后方卡片传实例，可以在api第一次获取数据的时候就缓存全部数据**/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ProductionDetailActivity.class);
                intent.putExtra(ProductionDetailActivity.INSTANCE_IMAGE,type.getId_1());
                intent.putExtra(ProductionDetailActivity.INSTANCE_TITLE,type.getTitle());
                intent.putExtra(ProductionDetailActivity.INSTANCE_PRICE,type.getPri());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView name,price1,price2;
        private CardView cardView;
        public ViewHolder(@NonNull View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.product_card);
            icon = (ImageView) view.findViewById(R.id.product_images);
            name = (TextView) view.findViewById(R.id.product_title);
            price1 = (TextView) view.findViewById(R.id.product_price_1);
            price2 = (TextView) view.findViewById(R.id.product_price_2);
        }
    }
}
