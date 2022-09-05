package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.MoreComment.Data.ItemCommentPicture;
import com.baidu.paddle.lite.demo.image_classification.ProductionDetail.Data.ItemComment;
import com.baidu.paddle.lite.demo.image_classification.R;


import java.util.ArrayList;
import java.util.List;

public class MoreCommentAdapter extends RecyclerView.Adapter<MoreCommentAdapter.ViewHolder> {
    private List<ItemComment> list=new ArrayList<>();
    public MoreCommentAdapter(List<ItemComment> list){this.list=list;}
    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_more_comment,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    private GridLayoutManager gridLayoutManagerPicture;
    private ItemCommentPictureAdapter pictureAdapter;
    private MoreCommentStarAdaptor starAdaptor;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemComment itemComment=list.get(position);
        holder.textViewTime.setText(itemComment.getSendTime());
        holder.textViewContent.setText(itemComment.getContent());
        holder.textViewAuthor.setText(itemComment.getName());
        starAdaptor=new MoreCommentStarAdaptor(itemComment.getGoodNum());
        holder.recyclerViewStar.setLayoutManager(new LinearLayoutManager
                (context,RecyclerView.HORIZONTAL,false));
        holder.recyclerViewStar.setAdapter(starAdaptor);

        List<ItemCommentPicture> listPicture=new ArrayList<>();

        if(itemComment.getUrls() != null){
            for(String e:itemComment.getUrls()){
                ItemCommentPicture itemCommentPicture=new ItemCommentPicture();
                itemCommentPicture.setUrl(e);
                listPicture.add(itemCommentPicture);
            }
        }

        pictureAdapter=new ItemCommentPictureAdapter(listPicture);
        gridLayoutManagerPicture=new GridLayoutManager(context,3);
        holder.recyclerViewPicture.setLayoutManager(gridLayoutManagerPicture);
        holder.recyclerViewPicture.setAdapter(pictureAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAuthor;
        TextView textViewAuthor,textViewContent,textViewTime;
        RecyclerView recyclerViewStar,recyclerViewPicture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAuthor=itemView.findViewById(R.id.imageViewMoreCommentAuthorPicture);
            textViewAuthor=itemView.findViewById(R.id.textViewMoreCommentAuthor);
            textViewContent=itemView.findViewById(R.id.textViewMoreCommentContent);
            textViewTime=itemView.findViewById(R.id.textViewMoreCommentTime);
            recyclerViewStar=itemView.findViewById(R.id.recycleViewMoreCommentStar);
            recyclerViewPicture=itemView.findViewById(R.id.recycleViewMoreCommentPicture);
        }
    }
}
