package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Agritainment_bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Culture_heritage_card_bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Agritainment_Adapter extends RecyclerView.Adapter<Agritainment_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Agritainment_bean> MyInstanceList;


    public Agritainment_Adapter(List<Agritainment_bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.agritainment_activity_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Agritainment_bean instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getName());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.agritainment_types);
        }
    }
}
