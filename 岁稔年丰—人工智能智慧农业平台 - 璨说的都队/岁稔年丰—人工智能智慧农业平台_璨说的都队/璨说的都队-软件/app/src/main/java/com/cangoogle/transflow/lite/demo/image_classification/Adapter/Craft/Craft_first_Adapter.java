package com.baidu.paddle.lite.demo.image_classification.Adapter.Craft;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Cultural_heritage_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Craft.Craft_first_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Craft_first_Adapter extends RecyclerView.Adapter<Craft_first_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Craft_first_Bean> MyInstanceList;


    public Craft_first_Adapter(List<Craft_first_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.craft_1_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Craft_first_Bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
        holder.textView.setText(instance.get_name());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 1){
                    Intent intent=new Intent(v.getContext(), Cultural_heritage_Activity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView imageView;
        TextView  textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.card_click_culture);
            imageView = (ImageView) itemView.findViewById(R.id.craft_image);
            textView = (TextView) itemView.findViewById(R.id.craft_title);
        }
    }
}
