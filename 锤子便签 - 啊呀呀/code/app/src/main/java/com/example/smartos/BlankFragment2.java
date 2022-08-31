package com.example.smartos;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment2 extends Fragment implements MyAdapter.OnItemClickListener ,BlankFragment.saveGroup{
    static int year,month,day,hour,minute;
    static String groupname;
    static String hourText;
    static String DeleteGroup;
    String notesDataForSave;
    static int AdapterID;
    static int DeleteID;
    static int UpdateID;
    TextView textCount;
    TextView dataText;
    NoteViewModel noteViewModel;
    MyAdapter myAdapter;
    String notegroup;
    Boolean star = false;
    int starUpdata = -1;
    BlankFragment blankFragment;
    NavController navController;
    EditText editText;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment2() {
        // Required empty public constructor
        blankFragment = new BlankFragment(this);
        myAdapter = new MyAdapter(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment2 newInstance(String param1, String param2) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank2, container, false);
        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(NoteViewModel.class);
        Button btn2 = view.findViewById(R.id.button2);
        Button delete = view.findViewById(R.id.button3);
        Button btn4 = view.findViewById(R.id.button4);
        dataText = view.findViewById(R.id.textView2);
        ImageButton starBtn = view.findViewById(R.id.imageButton2);
        editText = view.findViewById(R.id.editTextTextMultiLine);
        textCount = view.findViewById(R.id.textView3);

        Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour<=4)hourText = "凌晨";
        if(hour>4&&hour<12)hourText = "上午";
        if(hour==12)hourText = "中午";
        if(hour>12&&hour<=18)hourText = "下午";
        if(hour>18)hourText = "夜晚";
        minute = calendar.get(Calendar.MINUTE);
        if(AdapterID==0){
            dataText.setText(month+"月"+day+"日"+" "+hourText+hour+":"+minute);
        }
        //初始化
        if(AdapterID!=0){
            String textData = "";
            textData = noteViewModel.getNotesbyID(AdapterID);
            notegroup = noteViewModel.getGroupsbyID(AdapterID);
            starUpdata = AdapterID;
            if(notegroup.equals("StarTagsallTags")){
                star = true;
                starBtn.setBackgroundResource(R.drawable.ic_baseline_star_24_buli);
            }else {
                starBtn.setBackgroundResource(R.drawable.ic_baseline_star_24_unfilled);
            }
            DeleteGroup =  noteViewModel.getGroupsbyID(AdapterID);
            System.out.println("5555555555555"+DeleteGroup);

            editText.setText(textData);
            String data;
            data = noteViewModel.getDatabyID(AdapterID);
            dataText.setText(data);
            textCount.setText(String.valueOf(textData.length()));
            btn2.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.VISIBLE);
            AdapterID = 0;
        }


    //获得字数
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                textCount.setText(String.valueOf(s.length()));
            }
        });




        //触碰到text获得焦点
        editText.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(AdapterID ==0){
                    btn2.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                }else
                    btn2.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                return false;
            }
        });
        //添加带星文件
        starBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                star = !star;
                if(star){
                    starBtn.setBackgroundResource(R.drawable.ic_baseline_star_24_buli);
                    if(starUpdata==-1){
                        groupname = "StarTagsallTags";
                    }else {
                        Notes notes = noteViewModel.findAllNotesByID(starUpdata);
                        notes.setGroup("StarTagsallTags");
                        noteViewModel.updatetNotes(notes);
                    }
                }
                else{
                    starBtn.setBackgroundResource(R.drawable.ic_baseline_star_24_unfilled);
                    if(starUpdata==-1){
                        groupname = "allTags";
                    }else {
                        Notes notes = noteViewModel.findAllNotesByID(starUpdata);
                        notes.setGroup("allTags");
                        noteViewModel.updatetNotes(notes);
                    }
                }



            }
        });
        //编辑完成
        btn2.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                //未编辑直接返回到主菜单
                if(editText.getText().length()==0){
                    if(DeleteID == 0){
                        navController.navigate(R.id.action_blankFragment2_to_blankFragment);
                    }else{
                        Notes notes = new Notes();
                        notes.setID(DeleteID);
                        noteViewModel.deleteNotes(notes);
                        navController.navigate(R.id.action_blankFragment2_to_blankFragment);
                    }

                }else{
                    //按钮切换
                        btn2.setVisibility(View.INVISIBLE);
                        delete.setVisibility(View.VISIBLE);

                    //失去焦点
                    editText.clearFocus();
                    //隐藏输入法
                    InputMethodManager inputMethodManager =   (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    //插入数据库
                    if(UpdateID == 0){
                        String noteText =  editText.getText().toString();
                        System.out.println("noteText:"+noteText);
                        notesDataForSave = month+"月"+day+"日"+" "+hourText+hour+":"+minute;
                        Notes notes = new Notes(noteText,"",notesDataForSave,groupname,0);
                        noteViewModel.insertNotes(notes);
                    }else{
                        String noteText =  editText.getText().toString();
                        notesDataForSave = month+"月"+day+"日"+" "+hourText+hour+":"+minute;
                        Notes notes = new Notes(noteText,notesDataForSave);
                        notes.setID(UpdateID);
                        noteViewModel.updatetNotes(notes);
                        UpdateID = 0;
                        DeleteID = 0;
                    }

                }


            }
        });




        //返回主界面
        btn4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdateID = 0;
                DeleteID = 0;
                navController.navigate(R.id.action_blankFragment2_to_blankFragment);

            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void showDeleteDialog(){
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog_layout);
        Button exitButton = dialog.findViewById(R.id.button6);
        Button sureButton = dialog.findViewById(R.id.button7);

        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认删除
        sureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Notes notes = new Notes();

                if(DeleteID!=0 && DeleteGroup.equals("Rubbish")){
                    notes.setID(DeleteID);
                    noteViewModel.deleteNotes(notes);
                  }
                else if(DeleteID!=0){
                    notes = noteViewModel.findAllNotesByID(DeleteID);
                    String testyi3 = notes.getGroup();
                    System.out.println("88888888888888888888888"+testyi3);
                    String testyi = notes.getNotes();
                    System.out.println("88888888888888888888888"+testyi);
                    notes.setNotes("delete");
                    String testyi2 = notes.getNotes();
                    System.out.println("99999999999999999999"+testyi2);
                    notes.setGroup("Rubbish");
                    noteViewModel.updatetNotes(notes);

                }
                UpdateID = 0;
                DeleteID = 0;
                dialog.dismiss();
                navController.navigate(R.id.action_blankFragment2_to_blankFragment);

            }
        });
//        dialog.setCanceledOnTouchOutside(false);//设置点击其他地方不起作用
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialoganimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

//设置对话框弹出时背景不变暗
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp.dimAmount =0f;
//        dialog.getWindow().setAttributes(lp);
    }





    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(Objects.requireNonNull(this.getActivity()),R.id.fragmentContainerView);
//        View view = getView().findViewById(R.id.view3);
//        Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.textview_tobig);
//        animation.setFillAfter(true);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //do something
//                view.startAnimation(animation);
//            }
//        }, 300);    //延时1s执行

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 300);

        //更新时间

    }

    @Override
    public void onItemClick(int id) {
        BlankFragment2.AdapterID = id;
        BlankFragment2.DeleteID = id;
        BlankFragment2.UpdateID = id;
    }

    @Override
    public void passGroup(String group) {
        groupname = group;
        System.out.println("99999999999999999999999999999999999999999"+groupname);
    }
}