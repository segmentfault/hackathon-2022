package com.example.smartos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ForeignKey;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    List<Notes> noteslist = new ArrayList<>();
    static NavController navController = null;
    NavController navController2;
    static shupDownListener  shupDownListener ;
    static OnItemClickListener listener;
    static String group;
    Notes notes;
    static Boolean star;
    static NoteViewModel noteViewModel;
    interface OnItemClickListener {
        void onItemClick(int id);
    }
    interface shupDownListener{
        void downSearch(boolean bu);
    }
    List<OnItemClickListener> listenerList = new ArrayList<>();
    public MyAdapter(NavController nav){
        navController = nav;}
    public MyAdapter(NoteViewModel noteViewModel){
        MyAdapter.noteViewModel = noteViewModel;
    }
    public MyAdapter(){}
    public MyAdapter(shupDownListener Listener){
        shupDownListener = Listener;
    }
    public MyAdapter(OnItemClickListener onItemClickListener){
       listener = onItemClickListener;
   }
    public void setAllNotes(List<Notes> list){
        this.noteslist = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_item,parent,false);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        notes = noteslist.get(position);
        holder.notesview.setText(notes.getTitle());
        holder.data.setText(notes.getDate());
        group = notes.getGroup();
        if (group.equals("StarTagsallTags")){
            holder.starButton.setBackgroundResource(R.drawable.ic_baseline_star_24_buli);
            star = true;
        }else{
            star = false;
            holder.starButton.setBackgroundResource(R.drawable.ic_baseline_star_24_unfilled);
        }
        //带星标签按钮
        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                notes = noteslist.get(position);
                notes = noteslist.get(holder.getAdapterPosition());
                if(notes.getGroup().equals("StarTagsallTags")){
                    star = true;
                }
                star = !star;
                if(star){
                    notes.setGroup("StarTagsallTags");
                    holder.starButton.setBackgroundResource(R.drawable.ic_baseline_star_24_buli);
                } else {
                    notes.setGroup("allTags");
                    holder.starButton.setBackgroundResource(R.drawable.ic_baseline_star_24_unfilled);
                }
                noteViewModel.updatetNotes(notes);
            }
        });
//点击对应对象跳转到指定ID页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int id = notes.getID();
            @Override
            public void onClick(View v) {

                if(navController!=null){
                    navController.navigate(R.id.action_blankFragment_to_blankFragment2);

                }else {
                    navController2 =  Navigation.findNavController(v);
                    navController2.navigate(R.id.action_blankFragment_to_blankFragment2);
                }
                if(listener!=null){
                    listener.onItemClick(id);
                }
                if(shupDownListener!=null){
                    shupDownListener.downSearch(false);
                }

            }
        });
    }



    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView data,notesview;
        ImageButton starButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            data  = itemView.findViewById(R.id.textView6);
            notesview = itemView.findViewById(R.id.textView7);
            starButton = itemView.findViewById(R.id.imageButton);
        }
    }

}
