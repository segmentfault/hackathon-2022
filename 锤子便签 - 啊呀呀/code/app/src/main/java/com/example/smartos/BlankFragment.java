package com.example.smartos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BlankFragment extends Fragment implements MyAdapter.shupDownListener{
    NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    RecyclerView searchRecycler;
    Button clearbtn;
    static  saveGroup saveGroup;
    MyAdapter myAdapter;
    View SearchBar;
    ImageView search;
    Button btn;
    static int Flag = 0;
    static boolean Dissflag = true;
    Dialog dialog;
    TextView groupText;
    String noway;
    private LiveData<List<Notes>>filterNotes;
    String [] alltags = {"全部便签","加星标签","回收站"};
    private LiveData<List<Notes>>Notes;

    interface saveGroup{
        void  passGroup(String group);
    }

    public BlankFragment(){
        myAdapter = new MyAdapter(this);
    }
    public BlankFragment(saveGroup saveGroupp){
        saveGroup = saveGroupp;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView = view.findViewById(R.id.recycleview);
        groupText = view.findViewById(R.id.textView5);
        clearbtn = view.findViewById(R.id.clearbutton9);
        //防止首次新建返回空值，先创建一个
        BlankFragment2 blankFragment2 = new BlankFragment2();
        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(NoteViewModel.class);
        myAdapter = new MyAdapter(noteViewModel);
        recyclerView.setLayoutManager( new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(myAdapter);
        search = view.findViewById(R.id.imageView3);
        SearchBar = view.findViewById(R.id.SearchBar);

        groupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagBar(groupText);
            }
        });



//搜索栏点击
        SearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBar.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                NavController navController = Navigation.findNavController(v);
//                navController.navigate(R.id.action_blankFragment_to_blankFragment4);
                showSearchDialog(SearchBar,search,navController);
            }
        });

        btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(Flag==0){
                    System.out.println("0000000000000000000000000000");
                    if (saveGroup!=null){
                        saveGroup.passGroup("allTags");
                    }
                }else if(Flag==1){
                    saveGroup.passGroup("StarTagsallTags");
                }else{
                    saveGroup.passGroup("Rubbish");
                }
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_blankFragment_to_blankFragment2);

            }
        });
//滑动删除
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START|ItemTouchHelper.END) {
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
////                Notes notesFrom = Notes.get(viewHolder.getAdapterPosition());
//
//
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        }).attachToRecyclerView(recyclerView);

        return view;
    }
//搜索的模态框
    @SuppressLint("ResourceAsColor")
    private void showSearchDialog(View SearchBar,ImageView search,NavController nav){

        dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchbar);

        myAdapter = new MyAdapter(nav);
        searchRecycler = dialog.findViewById(R.id.searchrecler);
        searchRecycler.setLayoutManager( new LinearLayoutManager(dialog.getContext()));
//        searchRecycler.setAdapter(myAdapter);
        noteViewModel.getAllNotesLive().observe(this, new Observer<List<Notes>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Notes> notes) {
//                放在刷新页面里面的
                myAdapter.setAllNotes(notes);
                myAdapter.notifyDataSetChanged();
            }
        });
        ImageView backimge = dialog.findViewById(R.id.imageView4);
        Button exitButton = dialog.findViewById(R.id.button8);
        EditText searchText = dialog.findViewById(R.id.searchtext);
        ImageView CancelSearchBtn = dialog.findViewById(R.id.CancelSearchButton);
        CancelSearchBtn.setVisibility(View.INVISIBLE);
        searchText.requestFocus();
        backimge.setVisibility(View.INVISIBLE);
        //显示软键盘
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        if(!Dissflag){
            dialog.dismiss();
        }
//隐藏搜索模态框按钮点击事件
        CancelSearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SearchBar.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
//搜索框的文字输入监听器
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchRecycler.setAdapter(myAdapter);
                if(searchText.getText().length()==0){
                    searchRecycler.setAdapter(null);
                    backimge.setVisibility(View.INVISIBLE);
                    CancelSearchBtn.setVisibility(View.INVISIBLE);
                }else{
                    searchRecycler.setAdapter(myAdapter);
                    backimge.setVisibility(View.VISIBLE);
                    CancelSearchBtn.setVisibility(View.VISIBLE);
                    String patten = searchText.getText().toString().trim();
                    filterNotes = noteViewModel.findNotesByPatten(patten);
                    filterNotes.observe(requireActivity(), new Observer<List<Notes>>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onChanged(List<Notes> notes) {
                            int temp = myAdapter.getItemCount();
                            myAdapter.setAllNotes(notes);
                            if(temp!=notes.size()){
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
        //确认删除
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.getWindow().setGravity(Gravity.TOP);

//设置对话框弹出时背景不变暗
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp.dimAmount =0f;
//        dialog.getWindow().setAttributes(lp);
    }


    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    private void showTagBar(TextView text){
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tagbar);
        View viewalltag = dialog.findViewById(R.id.tagall);
        View starview = dialog.findViewById(R.id.startagview);
        View rubbishview = dialog.findViewById(R.id.rubbishview);
        ImageView firstDown = dialog.findViewById(R.id.imageView5);
        ImageView secDown = dialog.findViewById(R.id.imageView6);
        ImageView thirdtDown = dialog.findViewById(R.id.imageView7);
        if(Flag==0){
            firstDown.setVisibility(View.VISIBLE);
            secDown.setVisibility(View.INVISIBLE);
            thirdtDown.setVisibility(View.INVISIBLE);
        }else if (Flag ==1){
            firstDown.setVisibility(View.INVISIBLE);
            secDown.setVisibility(View.VISIBLE);
            thirdtDown.setVisibility(View.INVISIBLE);
        }else if (Flag==2){
            firstDown.setVisibility(View.INVISIBLE);
            secDown.setVisibility(View.INVISIBLE);
            thirdtDown.setVisibility(View.VISIBLE);
        }
        viewalltag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbtn.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                firstDown.setVisibility(View.VISIBLE);
                secDown.setVisibility(View.INVISIBLE);
                thirdtDown.setVisibility(View.INVISIBLE);
                flag(0);
                text.setText(alltags[Flag]);
                LiveData<List<Notes>> filterNotes1 = noteViewModel.findNotesByGroup("allTags");
                    filterNotes1.observe(getViewLifecycleOwner(), new Observer<List<Notes>>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onChanged(List<Notes> notes) {

                            if(Flag==0){
                                int temp = myAdapter.getItemCount();
                                myAdapter.setAllNotes(notes);
                                if(temp!=notes.size()){
                                    System.out.println("inallTags"+Flag);
                                    myAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });

                dialog.dismiss();
            }
        });
        starview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbtn.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                firstDown.setVisibility(View.INVISIBLE);
                secDown.setVisibility(View.VISIBLE);
                thirdtDown.setVisibility(View.INVISIBLE);
                flag(1);
                LiveData<List<Notes>> filterNotes2 = noteViewModel.findNotesByGroup("StarTags");
                    filterNotes2.observe(getViewLifecycleOwner(), new Observer<List<Notes>>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onChanged(List<Notes> notes) {
                            if(Flag == 1){
                                System.out.println("inStar"+Flag);
                                myAdapter.setAllNotes(notes);
                                myAdapter.notifyDataSetChanged();
                            }


                        }
                    });

                text.setText(alltags[Flag]);
                dialog.dismiss();
            }
        });
        rubbishview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbtn.setVisibility(View.VISIBLE);
                btn.setVisibility(View.INVISIBLE);
                firstDown.setVisibility(View.INVISIBLE);
                secDown.setVisibility(View.INVISIBLE);
                thirdtDown.setVisibility(View.VISIBLE);
                List<Integer> deleteID = noteViewModel.getDeleteID();
                Object[] DeleteID =  deleteID.toArray();
                flag(2);
                text.setText(alltags[Flag]);


                LiveData<List<Notes>> filterNotes3 = noteViewModel.findNotesInDelete();
//                filterNotes = noteViewModel.findNotesByGroup("Rubbish");
                filterNotes3.observe(getViewLifecycleOwner(), new Observer<List<Notes>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChanged(List<Notes> notes){
                        int temp = myAdapter.getItemCount();
                        myAdapter.setAllNotes(notes);
                        if(temp!=notes.size()&&Flag==2){
                            System.out.println("inDelete"+Flag);
                            myAdapter.notifyDataSetChanged();
                        }
//                        myAdapter.notifyDataSetChanged();
                    }
                });
                clearbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Object o : DeleteID) {
                            Notes notes = noteViewModel.findAllNotesByID((int)o);
                            noteViewModel.deleteNotes(notes);
                            Flag = 0;
                            text.setText(alltags[0]);
                            btn.setVisibility(View.VISIBLE);
                            clearbtn.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                dialog.dismiss();
            }
        });

//        dialog.setCanceledOnTouchOutside(false);//设置点击其他地方不起作用
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.getWindow().setGravity(Gravity.TOP);

//设置对话框弹出时背景不变暗
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.dimAmount =0f;
        dialog.getWindow().setAttributes(lp);

    }

    void flag(int i){
        Flag = i;
    }


    @Override
    public void onStart() {
        super.onStart();
        Flag = 0;

        noteViewModel.findNotesByGroup("allTags").observe(this, new Observer<List<Notes>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Notes> notes) {
//                放在刷新页面里面的

                if(Flag==0){
                    System.out.println("inOnstar"+Flag);
                    myAdapter.setAllNotes(notes);
                    myAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void downSearch(boolean bu) {
        Dissflag = bu;
        if(dialog!=null){
            dialog.dismiss();
        }

    }
}