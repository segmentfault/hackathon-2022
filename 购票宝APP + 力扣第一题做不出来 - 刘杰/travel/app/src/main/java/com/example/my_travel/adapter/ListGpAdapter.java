package com.example.my_travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.my_travel.R;
import com.example.my_travel.bean.GP;

import java.util.List;

public class ListGpAdapter extends BaseAdapter {
    private List<GP>list;
    private LayoutInflater inflater;
    private Context context;
    private ViewHolder holder = null;
    public ListGpAdapter(List<GP> list, Context context){
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i).getQuantity();
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
            view = inflater.inflate(R.layout.item_gp_layout,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        holder.gp_name.setText("购票数量:"+list.get(i).getQuantity());
        holder.gp_content.setText("购买金额:"+list.get(i).getMoney());

        return view;
    }
    class ViewHolder{
        public TextView gp_name,gp_content;
        private LinearLayout ll;
        View itemView;
        public ViewHolder(View itemView) {
            if (itemView == null){
                throw new IllegalArgumentException("item View can not be null!");
            }
            this.itemView = itemView;
            gp_name = itemView.findViewById(R.id.item_gp_name);
            ll = itemView.findViewById(R.id.ll);
            gp_content = itemView.findViewById(R.id.item_gp_content);
        }
    }
}
