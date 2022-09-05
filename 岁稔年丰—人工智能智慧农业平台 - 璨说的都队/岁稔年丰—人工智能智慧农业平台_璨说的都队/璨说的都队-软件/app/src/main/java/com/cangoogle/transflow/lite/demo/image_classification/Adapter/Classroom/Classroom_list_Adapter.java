package com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.CourseDetailVideoActivity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.fragment3_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Classroom_list_Adapter extends RecyclerView.Adapter<Classroom_list_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<fragment3_Bean> MyInstanceList;


    public Classroom_list_Adapter(List<fragment3_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.classroom_good_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fragment3_Bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisContext, CourseDetailVideoActivity.class);
                intent.putExtra(CourseDetailVideoActivity.title,instance.getTitle());
                intent.putExtra(CourseDetailVideoActivity.name,"陈玥");
                ThisContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.classroom_name_first);
            cardView = (CardView) itemView.findViewById(R.id.shouye_kecheng);
        }
    }
}
