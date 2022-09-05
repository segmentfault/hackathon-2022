package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Culture_heritage_card_bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Culture_heritage_card_down_bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Cultural_heritage_crad_down_Adapter extends RecyclerView.Adapter<Cultural_heritage_crad_down_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Culture_heritage_card_down_bean> MyInstanceList;


    public Cultural_heritage_crad_down_Adapter(List<Culture_heritage_card_down_bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.cultural_heritage_card_donw_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Culture_heritage_card_down_bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
        holder.textView.setText(instance.get_name());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView  textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cultural_heritage_down_image);
            textView = (TextView) itemView.findViewById(R.id.cultural_heritage_down_name);
        }
    }
}
