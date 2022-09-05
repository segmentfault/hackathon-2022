package com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.paddle.lite.demo.image_classification.PackageProductionDetail.Data.SpecialNote;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialNoteAdapter extends RecyclerView.Adapter<SpecialNoteAdapter.ViewHolder> {
    List<SpecialNote> list=new ArrayList<>();
    public SpecialNoteAdapter(List<SpecialNote> list){this.list=list;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_package_production_detail_introduce_special_note,
                        parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpecialNote specialNote=list.get(position);
        holder.textView.setText(specialNote.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textViewPackageProductionDetailSpecialNote);
        }
    }
}
