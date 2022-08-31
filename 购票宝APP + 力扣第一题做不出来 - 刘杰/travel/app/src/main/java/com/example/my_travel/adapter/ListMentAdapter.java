package com.example.my_travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.my_travel.R;
import com.example.my_travel.bean.Ment;

import java.util.List;

public class ListMentAdapter extends BaseAdapter {
    private List<Ment>list;
    private LayoutInflater inflater;
    private Context context;
    private ViewHolder holder = null;
    public ListMentAdapter(List<Ment> list, Context context){
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i).getUsername();
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }
    //页面刷新
    public void refreshDataSet(){
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.item_layout,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        holder.itemtitle.setText(list.get(i).getUsername());
        holder.itemcontent.setText(list.get(i).getConten());
        return view;
    }
    class ViewHolder{
        public TextView itemtitle,itemcontent;
        View itemView;
        public ViewHolder(View itemView) {
            if (itemView == null){
                throw new IllegalArgumentException("item View can not be null!");
            }
            this.itemView = itemView;
            itemtitle = itemView.findViewById(R.id.item_name);
            itemcontent = itemView.findViewById(R.id.item_content);
        }
    }
}
