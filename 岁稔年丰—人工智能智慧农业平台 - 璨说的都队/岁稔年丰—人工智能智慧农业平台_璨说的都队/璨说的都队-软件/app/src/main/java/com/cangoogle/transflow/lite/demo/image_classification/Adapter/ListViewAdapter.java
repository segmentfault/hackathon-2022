package com.baidu.paddle.lite.demo.image_classification.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Agritainment_Product_Activity;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.ProductionDetailActivity;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;


    public ListViewAdapter(List<String>list, Context context){
        this.list=list;
        this.context=context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cardview,null);
            viewHolder.tvContent= (TextView) convertView.findViewById(R.id.content);
            viewHolder.btnDelete= (ImageView) convertView.findViewById(R.id.btnDelete);
            viewHolder.swipeMenuLayout= (SwipeMenuLayout) convertView.findViewById(R.id.item_cardviews);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvContent.setText(list.get(position)+"");

        final ViewHolder  finalViewHolder= viewHolder;
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                finalViewHolder.swipeMenuLayout.quickClose();
            }
        });

        viewHolder.button = (Button)convertView.findViewById(R.id.agritainment_card_button);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), Agritainment_Product_Activity.class);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
    class ViewHolder{
        Button button;
        SwipeMenuLayout swipeMenuLayout;
        TextView tvContent;
        ImageView btnDelete;
    }





    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
