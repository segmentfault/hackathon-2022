package com.example.my_travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_travel.R;
import com.example.my_travel.bean.list.ListImg;
import com.example.my_travel.utils.MyItem;

import java.util.List;

public class RecycListAdapter extends RecyclerView.Adapter<RecycListAdapter.ViewHolder> {
    //设置上下文参数
    private Context context;
    private List<ListImg> list;
    private View view;
    //开放方法
    public RecycListAdapter(Context context,List<ListImg> list){
        this.context = context;
        this.list = list;
    }
    //使用自定义接口，减少代码耦合
    private MyItem item;
    public void MyItem(MyItem item){
        this.item = item;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.reyce_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //将传入内容显示在页面上
             holder.img.setImageResource(list.get(position).getImg());
             holder.txt.setText(list.get(position).getContent());
             //设置点击事件
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (item!=null){
                         int code = getItemViewType(position);
                         if (code!=RecyclerView.NO_POSITION){
                             item.OnClick(view,position);
                         }
                     }
                 }
             });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.recyc_item_txt);
            img = itemView.findViewById(R.id.recyc_item_img);
        }
    }
}
