package com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.data.PersonRecommendation;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

public class PersonRecommendationAdapter extends RecyclerView.Adapter<PersonRecommendationAdapter.ViewHolder> {
    private List<PersonRecommendation> list;
    public PersonRecommendationAdapter(List<PersonRecommendation> list){this.list=list;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_view_accommodation_person_recommendation
                    ,parent,
                    false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //这里写具体的和图片的配置
        PersonRecommendation recommendation=list.get(position);
        //holder.imageView.setBackgroundResource();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardViewPersonRecommendation);
            imageView=(ImageView)itemView.findViewById(R.id.imageViewPersonRecommendation);
        }
    }
}
