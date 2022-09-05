package com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.Bean.Banner.Classroom_Banner_Bean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class Classroom_Banner_Adapter extends BannerAdapter<Classroom_Banner_Bean, Classroom_Banner_Adapter.BannerViewHolder> {
    public Classroom_Banner_Adapter(List<Classroom_Banner_Bean> mDatas) {
        super(mDatas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Classroom_Banner_Bean data, final int position, int size) {

        Glide.with(holder.imageView.getContext()).load((String)data.imageUrl).into(holder.imageView);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击事件
                        Toast.makeText(v.getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }

}
