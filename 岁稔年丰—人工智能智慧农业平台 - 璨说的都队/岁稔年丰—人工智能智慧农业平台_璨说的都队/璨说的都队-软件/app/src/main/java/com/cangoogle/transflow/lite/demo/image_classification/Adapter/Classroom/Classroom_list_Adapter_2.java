package com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.fragment3_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Classroom_list_Adapter_2 extends RecyclerView.Adapter<Classroom_list_Adapter_2.ViewHolder>{
    private Context ThisContext;
    private List<fragment3_Bean> MyInstanceList;


    public Classroom_list_Adapter_2(List<fragment3_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.classroom_free_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fragment3_Bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getTitle());

    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.classroom_name_first_2);
        }
    }
}
