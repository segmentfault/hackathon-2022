package com.example.notepad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notepad.Entity.Note;
import com.example.notepad.R;
import com.example.notepad.adapter.MyRecyclerViewAdapter;
import com.example.notepad.databinding.FragmentHomeBinding;
import com.example.notepad.interfaces.DeleteButtonCallBack;
import com.example.notepad.interfaces.SearchCallBack;
import com.example.notepad.view.NewNoteActivity;
import com.example.notepad.viewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private NoteViewModel mNoteViewModel;
    private SearchCallBack mSearchCallBack = new SearchCallBack() {
        @Override
        public void search(List<Note> notes) {
            mMyRecyclerViewAdapter.setNotes(notes);
        }
    };
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;
    private FragmentHomeBinding mFragmentHomeBinding;
    private DeleteButtonCallBack mDeleteButtonCallBack = new DeleteButtonCallBack() {
        @Override
        public void deleteButtonAppear() {
            mFragmentHomeBinding.homeCard.setVisibility(View.GONE);
            mFragmentHomeBinding.buttonDelete.setVisibility(View.VISIBLE);
            mFragmentHomeBinding.editSearchNote.setEnabled(false);
        }

        @Override
        public void deleteButtonDisappear() {
            mFragmentHomeBinding.homeCard.setVisibility(View.VISIBLE);
            mFragmentHomeBinding.buttonDelete.setVisibility(View.GONE);
        }

        @Override
        public void deleteNotes(int... ids) {
            Note[] notes = new Note[ids.length];
            for (int i=0;i<ids.length;i++){
                Note note = new Note();
                note.setId(ids[i]);
                notes[i]=note;
            }
            mNoteViewModel.deleteNote(notes);
        }

        @Override
        public void editable() {
            mFragmentHomeBinding.editSearchNote.setEnabled(true);
        }
    };
    private AlertDialog.Builder mDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteViewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);
        mNoteViewModel.getCheckBoxAllAppear().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mMyRecyclerViewAdapter.setCheckBoxAllAppear(aBoolean);
            }
        });
        mDialog = new AlertDialog.Builder(getActivity());
        mDialog.setTitle("删除笔记");
        mDialog.setMessage("确定要删除这些笔记吗?");
        mDialog.setCancelable(false);
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mMyRecyclerViewAdapter.deleteNotes();
                mFragmentHomeBinding.editSearchNote.setText("");
            }
        });
        mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mFragmentHomeBinding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                startActivity(intent);
            }
        });
        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), mNoteViewModel, mDeleteButtonCallBack);
        mFragmentHomeBinding.homeRecycler.setAdapter(mMyRecyclerViewAdapter);
        mFragmentHomeBinding.homeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoteViewModel.getAllNotes().observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mMyRecyclerViewAdapter.setNotes(notes);
            }
        });
        //搜索框EditText
        mFragmentHomeBinding.editSearchNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    mNoteViewModel.queryByCondition(mSearchCallBack, charSequence.toString());
                    mFragmentHomeBinding.imageClose.setVisibility(View.VISIBLE);
                } else {
                    mMyRecyclerViewAdapter.setNotes(mNoteViewModel.getAllNotes().getValue());
                    mFragmentHomeBinding.imageClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //搜索框右边的"x"，一键清除搜索框
        mFragmentHomeBinding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentHomeBinding.editSearchNote.setText("");
            }
        });
        mFragmentHomeBinding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
        return mFragmentHomeBinding.getRoot();
    }

}