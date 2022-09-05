package com.example.notepad.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.notepad.Entity.Note;
import com.example.notepad.R;
import com.example.notepad.databinding.ActivityNewNoteBinding;
import com.example.notepad.viewModel.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewNoteActivity";
    private String titleEditString;
    private String contentEditString;
    private ActivityNewNoteBinding mActivityNewNoteBinding;
    private NoteViewModel mNoteViewModel;
    private boolean mQuery;
    private Integer mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d(TAG, "Intent->"+intent);
        mActivityNewNoteBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_note);
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        //内容EditText获取焦点
        mActivityNewNoteBinding.editContent.requestFocus();
        initOnClick();
        initEditText();
        //如果是查看旧日记，把笔记信息放到对应的组件上；如果是新增日记，时间设为当前系统时间
        mQuery = intent.getBooleanExtra("query", false);
        if (mQuery){
            Note note = (Note) intent.getSerializableExtra("note");
            //保存旧的笔记的id
            mId = note.getId();
            mActivityNewNoteBinding.setDate(note.getDate().substring(0,17));
            mActivityNewNoteBinding.setContent(note.getContent());
            mActivityNewNoteBinding.setTitle(note.getTitle());
        }else {
            mActivityNewNoteBinding.setDate(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA).format(new Date()));
        }

    }

    private void initOnClick() {
        //返回箭头监听
        mActivityNewNoteBinding.back.setOnClickListener(this);
        //右上角确认图标监听
        mActivityNewNoteBinding.confirm.setOnClickListener(this);
    }

    private void initEditText() {
        //标题EditText监听事件
        mActivityNewNoteBinding.editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titleEditString = charSequence.toString();
                Log.d(TAG, "onTextChanged-->"+charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //内容EditText监听事件
        mActivityNewNoteBinding.editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contentEditString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.confirm:
                updateOrInsertNote();
                finish();
                break;

        }
    }
    //当返回键被按下时
    @Override
    public void onBackPressed() {
        /**
         * 1.如果是旧笔记:
         * (1)如果内容和标题都为空，则删除该旧笔记
         * (2)如果标题为空，弹出提示
         * (3)如果内容为空标题不为空，修改笔记
         * (4)如果都不为空，也修改笔记
         * 2.如果是新笔记:
         * (1)如果内容和标题都为空，则什么不发生
         * (2)如果标题为空，内容不为空，弹出提示
         * (3)如果都不为空，添加笔记
         * (4)如果标题不为空，添加笔记
         */

        if(mQuery){
            if(TextUtils.isEmpty(titleEditString)&&TextUtils.isEmpty(contentEditString)){
                Note note = new Note();
                note.setId(mId);
                mNoteViewModel.deleteNote(note);
                finish();
            }else if(TextUtils.isEmpty(titleEditString)||TextUtils.isEmpty(titleEditString.trim())){
                Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            }else{
                updateNote();
                finish();
            }
        }else {
            if(TextUtils.isEmpty(titleEditString)&&TextUtils.isEmpty(contentEditString)){

            }else if(TextUtils.isEmpty(titleEditString)||TextUtils.isEmpty(titleEditString.trim())){
                Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            }else{
                insertNote();
                finish();
            }
        }

    }

    private void updateOrInsertNote(){
        if(TextUtils.isEmpty(titleEditString)||TextUtils.isEmpty(titleEditString.trim())){
            Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            if (mQuery){
                updateNote();
            }else {
                insertNote();
            }
        }
    }

    private void insertNote(){
        Note note = new Note();
        note.setDate(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA).format(new Date()));
        note.setContent(contentEditString);
        note.setTitle(titleEditString.trim());
        mNoteViewModel.insertNote(note);
    }

    private void updateNote(){
        Note note = new Note();
        note.setDate(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA).format(new Date()));
        note.setContent(contentEditString);
        note.setTitle(titleEditString.trim());
        note.setId(mId);
        mNoteViewModel.updateNote(note);
    }


}