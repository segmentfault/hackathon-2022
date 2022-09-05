package com.baidu.paddle.lite.demo.image_classification.Adapter.Fragment_viewpager_second;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Cultural_heritage_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Topic_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_first_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Fragment_viewpager_second_bean_up_item;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Fragment_viewpager_second_Adapter extends RecyclerView.Adapter<Fragment_viewpager_second_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Fragment_viewpager_second_bean_up_item> MyInstanceList;


    public Fragment_viewpager_second_Adapter(List<Fragment_viewpager_second_bean_up_item> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.fragment_second_viewpager_2_up_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fragment_viewpager_second_bean_up_item instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getPic());
        holder.textView.setText("       "+instance.getTitle());
        holder.number.setText(instance.getNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisContext, Topic_Activity.class);
                ThisContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView  textView,number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.fragment_second_up_item_card);
            imageView = (ImageView) itemView.findViewById(R.id.fragment_second_up_item_img);
            textView = (TextView) itemView.findViewById(R.id.fragment_second_up_item_title);
            number = (TextView) itemView.findViewById(R.id.fragment_second_up_item_number);
        }
    }
}
