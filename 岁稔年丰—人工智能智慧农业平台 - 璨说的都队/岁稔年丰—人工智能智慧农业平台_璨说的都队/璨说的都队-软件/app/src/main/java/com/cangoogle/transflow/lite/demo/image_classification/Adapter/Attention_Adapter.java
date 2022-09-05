package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Agritainment_bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Attention_bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Attention_Adapter extends RecyclerView.Adapter<Attention_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Attention_bean> MyInstanceList;


    public Attention_Adapter(List<Attention_bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.fragment_second_attention,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attention_bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getTitle());
        holder.name.setText(instance.getName());
        holder.like.setText(instance.getLike());
        holder.circleImageView.setBorderColor(0xFF2EE2C9);
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textView, name, like;
        CircleImageView circleImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.attention_CircleImageView);
            textView = (TextView) itemView.findViewById(R.id.attention_title);
            name = (TextView) itemView.findViewById(R.id.attention_name);
            like = (TextView) itemView.findViewById(R.id.attention_like);
        }
    }
}
