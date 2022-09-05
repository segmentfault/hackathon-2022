package com.baidu.paddle.lite.demo.image_classification.Adapter.Characteristic_local;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.AccommodationActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Cultural_heritage_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Characteristic_local.char_local_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.PackageOfParent.PackageOfParent;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class char_local_list_Adapter extends RecyclerView.Adapter<char_local_list_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<char_local_list_Bean> MyInstanceList;


    public char_local_list_Adapter(List<char_local_list_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.characteristic_local_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        char_local_list_Bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getTitle());
        holder.price.setText("价格:￥" + instance.getPrize());
        holder.address.setText("地点："+instance.getAdress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取点击的实例
                int pos=holder.getAdapterPosition();
                char_local_list_Bean instance=MyInstanceList.get(pos);
                Intent intent=new Intent(ThisContext, PackageOfParent.class);
                //给后面的活动传入点击的实例的信息
                intent.putExtra(PackageOfParent.INSTANCE_DESC,instance.getDesc());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE1,instance.getType1());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE1_PRICE,instance.getType1price());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE2,instance.getType2());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE2_PRICE,instance.getType2price());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE3,instance.getType3());
                intent.putExtra(PackageOfParent.INSTANCE_TYPE3_PRICE,instance.getType2price());
                ThisContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView,time,address,price;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_local);
            textView = (TextView) itemView.findViewById(R.id.local_name_first);
            time = (TextView) itemView.findViewById(R.id.local_time_first);
            address = (TextView) itemView.findViewById(R.id.local_adress_first);
            price = (TextView) itemView.findViewById(R.id.local_price_first);
        }
    }
}
