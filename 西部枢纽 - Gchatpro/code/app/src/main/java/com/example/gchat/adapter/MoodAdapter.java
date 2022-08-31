package com.example.gchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gchat.R;
import com.example.gchat.model.MoodEntity;

import java.util.List;

/**
 * User: xiahao
 * DateTime: 2022/4/15 16:54
 * Description:说说适配器
 */
public class MoodAdapter extends BaseAdapter {

    //数据
    private List<MoodEntity> list;          //说说列表
    private LayoutInflater mInflater;

    public MoodAdapter(Context context, List<MoodEntity> list) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.qzone_time_item, null);
        }
        ViewHolder holder = getViewHolder(convertView);
        MoodEntity kd = list.get(position);
        holder.tv_content.setText("["+kd.getPerson()+"] 说:"+kd.getContent());//作者+内容
        holder.tv_time.setText(kd.getTime());
        return convertView;
    }

    /**
     * 获得控件管理对象
     * @param view
     * @return
     */
    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    /**
     * 控件管理类
     */
    private class ViewHolder {
        private TextView tv_content, tv_time;
        ViewHolder(View view) {
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
