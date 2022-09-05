package com.baidu.paddle.lite.demo.image_classification.Adapter.Characteristic_local;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Characteristic_local.char_local_list_Bean_2;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class char_local_list_Adapter_2 extends RecyclerView.Adapter<char_local_list_Adapter_2.ViewHolder>{
    private Context ThisContext;
    private List<char_local_list_Bean_2> MyInstanceList;


    public char_local_list_Adapter_2(List<char_local_list_Bean_2> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.characteristic_local_item_2,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        char_local_list_Bean_2 instance = MyInstanceList.get(position);
        holder.textView.setText(instance.getName1());

    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.local_name_first_2);
        }
    }
}
