package com.animee.health.food_grid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.animee.health.R;
import com.animee.health.bean.FoodBean;

import java.util.List;

public class FoodGridAdapter extends BaseAdapter{
    Context context;
    List<FoodBean>mDatas;

    public FoodGridAdapter(Context context, List<FoodBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        1.声明ViewHolder
        ViewHolder holder = null;
        if (convertView == null) { //2.判断是否有复用的view，如果没有就创建
            convertView = LayoutInflater.from(context).inflate(R.layout.item_foodgrid,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
//        获取指定位置的数据
        FoodBean foodBean = mDatas.get(position);
        holder.iv.setImageResource(foodBean.getPicId());
        holder.tv.setText(foodBean.getTitle());
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        public ViewHolder(View view){
            iv = view.findViewById(R.id.item_grid_iv);
            tv = view.findViewById(R.id.item_grid_tv);
        }
    }
}
