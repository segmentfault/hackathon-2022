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
import com.baidu.paddle.lite.demo.image_classification.Bean.live_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Live_Adapter extends RecyclerView.Adapter<Live_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<live_Bean> MyInstanceList;


    public Live_Adapter(List<live_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.activity_live_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        live_Bean instance = MyInstanceList.get(position);
        holder.title.setText("             " + instance.getTitle());
        Glide.with(ThisContext)
                .load(instance.getImageview())
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,time,count,name,live_time_end,label;
        CircleImageView url;
        ImageView imageview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.live_images);
            label = (TextView) itemView.findViewById(R.id.live_label);
            title = (TextView) itemView.findViewById(R.id.live_title);
            url = (CircleImageView) itemView.findViewById(R.id.live_url);
            time  = (TextView) itemView.findViewById(R.id.live_time);
            count = (TextView) itemView.findViewById(R.id.live_count);
            name = (TextView) itemView.findViewById(R.id.live_name);
            live_time_end = (TextView) itemView.findViewById(R.id.live_time_end);
        }
    }
}
