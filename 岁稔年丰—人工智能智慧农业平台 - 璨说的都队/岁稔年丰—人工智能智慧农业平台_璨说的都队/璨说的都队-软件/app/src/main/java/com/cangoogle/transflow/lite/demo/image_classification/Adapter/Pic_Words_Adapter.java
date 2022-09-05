package com.baidu.paddle.lite.demo.image_classification.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Pic_Word_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Pic_Word_Details_Activity;
import com.baidu.paddle.lite.demo.image_classification.Bean.Pic_Words_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.live_Bean;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.PartLayout.ContentLayout;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pic_Words_Adapter extends RecyclerView.Adapter<Pic_Words_Adapter.ViewHolder>{
    private Context ThisContext;
    private List<Pic_Words_Bean> MyInstanceList;


    public Pic_Words_Adapter(List<Pic_Words_Bean> NewInstanceList){
        MyInstanceList = NewInstanceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(ThisContext==null)ThisContext=parent.getContext();
        View view= LayoutInflater.from(ThisContext)
                .inflate(R.layout.fragment_third_gridview_third,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pic_Words_Bean instance = MyInstanceList.get(position);
        holder.title.setText("           " + instance.getTitle());
//        Glide.with(ThisContext)
//                .load(instance.getUrl())
//                .into(holder.imageview);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisContext,Pic_Word_Details_Activity.class);
                ThisContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyInstanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, content, name, label;
        CircleImageView url;
        ImageView imageview;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.pic_word_cards);
            imageview = (ImageView) itemView.findViewById(R.id.pi_words_images);
            label = (TextView) itemView.findViewById(R.id.pi_words_label);
            title = (TextView) itemView.findViewById(R.id.pi_words_title);
            url = (CircleImageView) itemView.findViewById(R.id.pi_words_url);
            content = (TextView) itemView.findViewById(R.id.pi_words_content);
            name = (TextView) itemView.findViewById(R.id.pi_words_name);
        }
    }
}
