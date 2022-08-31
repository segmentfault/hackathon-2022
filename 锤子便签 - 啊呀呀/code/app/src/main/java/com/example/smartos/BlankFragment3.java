package com.example.smartos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment3 extends Fragment implements MyAdapter.OnItemClickListener {
    NoteViewModel noteViewModel;
    MyAdapter myAdapter;
    static int id;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment3() {
        // Required empty public constructor
        myAdapter = new MyAdapter(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment3 newInstance(String param1, String param2) {
        BlankFragment3 fragment = new BlankFragment3();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String LiveData =  "";
        View view = inflater.inflate(R.layout.fragment_blank3, container, false);
        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(NoteViewModel.class);
        TextView textView = view.findViewById(R.id.textView8);
        if(id!=0){
            LiveData = noteViewModel.getNotesbyID(id);
        }
         else LiveData = noteViewModel.getNotesbyID(1);
         textView.setText(LiveData);
         System.out.println("idinFragment:"+id);
        return view;
    }

    @Override
    public void onItemClick(int id) {
        BlankFragment3.id = id;
        System.out.println("77777777777777777777777777777777888:"+id);
    }
}