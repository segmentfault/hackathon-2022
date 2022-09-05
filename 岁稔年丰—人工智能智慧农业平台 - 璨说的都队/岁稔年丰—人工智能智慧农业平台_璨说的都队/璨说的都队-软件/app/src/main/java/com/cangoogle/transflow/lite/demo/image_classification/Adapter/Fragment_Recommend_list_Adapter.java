package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Bean.Fragment_recommend_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Frist_fragment_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class Fragment_Recommend_list_Adapter extends RecyclerView.Adapter<Fragment_Recommend_list_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Fragment_recommend_list_Bean> MyInstanceList;


    public Fragment_Recommend_list_Adapter(List<Fragment_recommend_list_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.fragment_recomment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fragment_recommend_list_Bean instance = MyInstanceList.get(position);
        holder.imageView.setImageResource(instance.getId_1());
        holder.imageView1.setImageResource(instance.getId_2());
        holder.imageView2.setImageResource(instance.getId_3());
        holder.imageView3.setImageResource(instance.getId_4());
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView,imageView1,imageView2,imageView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recomment_images_1);
            imageView1 = (ImageView) itemView.findViewById(R.id.recomment_images_2);
            imageView2 = (ImageView) itemView.findViewById(R.id.recomment_images_3);
            imageView3 = (ImageView) itemView.findViewById(R.id.recomment_images_4);
        }
    }
}
