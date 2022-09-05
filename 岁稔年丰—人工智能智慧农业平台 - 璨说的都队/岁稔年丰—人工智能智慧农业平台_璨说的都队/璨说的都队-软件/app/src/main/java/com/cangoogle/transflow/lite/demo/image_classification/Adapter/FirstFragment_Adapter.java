package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.paddle.lite.demo.image_classification.R;

public class FirstFragment_Adapter extends BaseAdapter {
    private Context context;
    private String[] name;
    private int[]   images;

    public FirstFragment_Adapter(Context context, String[] name, int[] images){
        this.context = context;
        this.name = name;
        this.images = images;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_first_gridview_item, null);
        ImageView imageView = view.findViewById(R.id.iv_fragment_gridview);
        TextView textView = view.findViewById(R.id.tv_fragment_gridview);
        imageView.setImageResource(images[position]);
        textView.setText(name[position]);
        return view;

    }
}
