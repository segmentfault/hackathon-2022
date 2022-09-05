package com.example.notepad.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Entity.Note;
import com.example.notepad.R;
import com.example.notepad.databinding.ItemBinding;
import com.example.notepad.interfaces.DeleteButtonCallBack;
import com.example.notepad.view.NewNoteActivity;
import com.example.notepad.viewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "MyRecyclerViewAdapter";
    private List<Note> mNotes = new ArrayList<>();
    private Context mContext;
    private int checkedCheckBoxNumber;
    private boolean checkBoxAllAppear;
    private NoteViewModel mNoteViewModel;
    private DeleteButtonCallBack mDeleteButtonCallBack;
    private List<Integer> ids = new ArrayList<>();

    public void setCheckBoxAllAppear(boolean checkBoxAllAppear) {
        this.checkBoxAllAppear = checkBoxAllAppear;
        notifyDataSetChanged();
    }

    public MyRecyclerViewAdapter(Context context, NoteViewModel noteViewModel, DeleteButtonCallBack deleteButtonCallBack) {
        mContext = context;
        mNoteViewModel = noteViewModel;
        mDeleteButtonCallBack = deleteButtonCallBack;
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public void deleteNotes(){
        int[] allId = new int[ids.size()];
        for (int i=0;i<ids.size();i++){
            allId[i]=ids.get(i);
        }
        mDeleteButtonCallBack.deleteNotes(allId);
        mDeleteButtonCallBack.deleteButtonDisappear();
        checkBoxAllAppear = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.mItemBinding.textTitle.setText(note.getTitle());
        //如果笔记的内容为空，则卡片不显示内容和那一条竖着的分割线
        if (TextUtils.isEmpty(note.getContent())) {
            holder.mItemBinding.textContent.setVisibility(View.GONE);
            holder.mItemBinding.divider.setVisibility(View.GONE);
        } else {
            holder.mItemBinding.textContent.setVisibility(View.VISIBLE);
            holder.mItemBinding.divider.setVisibility(View.VISIBLE);
            holder.mItemBinding.textContent.setText(note.getContent());
            holder.mItemBinding.textDate.setText(note.getDate().substring(0, 11));
        }
        holder.mItemBinding.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果checkBox不可见，则跳转页面；否则判断是否被勾选
                if (holder.mItemBinding.checkbox.getVisibility() == View.GONE) {
                    Intent intent = new Intent(mContext, NewNoteActivity.class);
                    intent.putExtra("note", note);
                    intent.putExtra("query", true);
                    mContext.startActivity(intent);
                } else {
                    if (!holder.mItemBinding.checkbox.isChecked()) {
                        checkedCheckBoxNumber++;
                        holder.mItemBinding.checkbox.setChecked(true);
                        ids.add(mNotes.get(position).getId());
                    } else {
                        checkedCheckBoxNumber--;
                        holder.mItemBinding.checkbox.setChecked(false);
                        ids.remove(mNotes.get(position).getId());
                    }
                }
                if (checkedCheckBoxNumber==0){
                    mDeleteButtonCallBack.deleteButtonDisappear();
                }else {
                    mDeleteButtonCallBack.deleteButtonAppear();
                }
                Log.d(TAG, "集合中的元素-->"+ids);
                Log.d(TAG, "Number的值为-->"+checkedCheckBoxNumber);
            }
        });
        holder.mItemBinding.cardItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick.....");
                if (holder.mItemBinding.checkbox.getVisibility() == View.GONE) {
                    mNoteViewModel.getCheckBoxAllAppear().setValue(true);
                    holder.mItemBinding.checkbox.setChecked(true);
                    checkedCheckBoxNumber++;
                    ids.add(mNotes.get(position).getId());
                }else{
                    if (!holder.mItemBinding.checkbox.isChecked()) {
                        checkedCheckBoxNumber++;
                        holder.mItemBinding.checkbox.setChecked(true);
                        ids.add(mNotes.get(position).getId());
                    } else {
                        checkedCheckBoxNumber--;
                        holder.mItemBinding.checkbox.setChecked(false);
                        ids.remove(mNotes.get(position).getId());
                    }
                }
                if (checkedCheckBoxNumber==0){
                    mDeleteButtonCallBack.deleteButtonDisappear();
                }else {
                    mDeleteButtonCallBack.deleteButtonAppear();
                }
                Log.d(TAG, "集合中的元素-->"+ids);
                Log.d(TAG, "Number的值为-->"+checkedCheckBoxNumber);
                return true;
            }
        });
        if (checkBoxAllAppear) {
            holder.mItemBinding.checkbox.setVisibility(View.VISIBLE);
        }else {
            holder.mItemBinding.checkbox.setVisibility(View.GONE);
            holder.mItemBinding.checkbox.setChecked(false);
            mDeleteButtonCallBack.deleteButtonDisappear();
            mDeleteButtonCallBack.editable();
            checkedCheckBoxNumber=0;
        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemBinding mItemBinding;

        public MyViewHolder(@NonNull ItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mItemBinding = itemBinding;
        }

    }

}
