package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.Bean.CourseItem;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.ViewHolder> {
    private List<CourseItem> courseItems=new ArrayList<>();
    private Context context;
    private View view;
    private ViewHolder pre=null;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public CourseItemAdapter(List<CourseItem> courseItems){this.courseItems=courseItems;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        view= LayoutInflater.from(context)
                .inflate(R.layout.course_detail_video_all_course_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    private boolean haveSet=false;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CourseItem courseItem=courseItems.get(position);
        holder.textViewTime.setText(courseItem.getTime());
        holder.textViewTitle.setText(courseItem.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowThis(holder,courseItem);
            }
        });
        if(position==0&&!haveSet){
            onShowThis(holder,courseItem);
            haveSet=true;
        }
    }
    private void onShowThis(ViewHolder holder,CourseItem courseItem){
        if(pre==holder)return;
        if(pre!=null){
            pre.cardView.setCardElevation(0);
            pre.imageView.setVisibility(View.INVISIBLE);
        }
        holder.cardView.setCardElevation(10);
        holder.imageView.setVisibility(View.VISIBLE);
        listener.onItemChange(courseItem.getUrl());
        pre=holder;
    }

    @Override
    public int getItemCount() {
        return courseItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewTime;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textViewCourseDetailVideoAllCourseItemTitle);
            textViewTime=itemView.findViewById(R.id.textViewCourseDetailVideoAllCourseItemTime);
            imageView=itemView.findViewById(R.id.imageViewCourseDetailVideoAllCourseItemLoad);
            imageView.setVisibility(View.INVISIBLE);
            cardView=itemView.findViewById(R.id.cardViewCourseDetailVideoAllCourseItem);

        }
    }
    public interface Listener{
        public void onItemChange(String url);
    }
}
