package com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ShufflingItem;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class ProductionDetailShufflingAdapter extends PagerAdapter {
    private List<ShufflingItem> list;
    @Override
    public int getCount() {
        if(list!=null){
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position%list.size();
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ShufflingItem shufflingItem = list.get(realPosition);
        /**
         * 底下这里配置轮播图的每张图片的具体内容
         */
        //imageView.setImageResource(list.get(realPosition));
        Log.d("TAGaaa", "instantiateItem: "+shufflingItem.getPictureUrl());
        Glide.with(container.getContext()).load(shufflingItem.getPictureUrl()).into(imageView);
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public void setData(List<ShufflingItem> nlist) {
        this.list = new ArrayList<>();
        this.list.addAll(nlist);
        notifyDataSetChanged();
    }

    public int getDataRealSize(){
        if(list!=null){
            return list.size();
        }
        return 0;
    }



}
