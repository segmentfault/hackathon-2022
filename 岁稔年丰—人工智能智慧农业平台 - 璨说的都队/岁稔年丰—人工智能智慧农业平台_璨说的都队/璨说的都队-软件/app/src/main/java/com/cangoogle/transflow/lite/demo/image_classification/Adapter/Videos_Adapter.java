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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.CourseDetailVideoActivity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Video_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.fragment3_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Videos_Adapter extends RecyclerView.Adapter<Videos_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Video_Bean> MyInstanceList;
    private String temp;


    public Videos_Adapter(List<Video_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext == null)
            ThisContext = parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.activity_video_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video_Bean instance = MyInstanceList.get(position);
        holder.title.setText("             " + instance.getTitle());
        holder.label.setText(instance.getLabel());
        holder.name.setText(instance.getName());
        holder.count.setText(instance.getCount()+"个人已加入学习");
        holder.time.setText("共"+instance.getTime()+"课时");
        temp = instance.getPrice();
        float temp_1 = Float.parseFloat(temp);
        int temp_2 = (int)temp_1;
        float temp_3 = temp_1 - temp_2;
        temp_3 = temp_3*100;
        int temp_4 = (int)temp_3;
        String temp_5 = Integer.toString(temp_4);
        holder.price1.setText(temp_2+".");
        holder.price2.setText(temp_5);
        Glide.with(ThisContext)
                .load(instance.getImageview().replace("\r","").replace("\n",""))
                .into(holder.video_images);
        Glide.with(ThisContext)
                .load(instance.getUrl())
                .into(holder.url);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisContext, CourseDetailVideoActivity.class);
                intent.putExtra(CourseDetailVideoActivity.title,instance.getTitle());
                intent.putExtra(CourseDetailVideoActivity.name,instance.getName());
                ThisContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,label,time,count,name,price1,price2;
        CircleImageView url;
        ImageView video_images;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.video_title);
            label = (TextView) itemView.findViewById(R.id.video_label);
            time  = (TextView) itemView.findViewById(R.id.video_time);
            count = (TextView) itemView.findViewById(R.id.video_count);
            name = (TextView) itemView.findViewById(R.id.video_name);
            price1 = (TextView) itemView.findViewById(R.id.video_price1);
            price2 = (TextView) itemView.findViewById(R.id.video_price2);
            url= (CircleImageView) itemView.findViewById(R.id.video_url);
            video_images = (ImageView) itemView.findViewById(R.id.video_images);
            cardView = (CardView) itemView.findViewById(R.id.videos_cards);
        }
    }
}
