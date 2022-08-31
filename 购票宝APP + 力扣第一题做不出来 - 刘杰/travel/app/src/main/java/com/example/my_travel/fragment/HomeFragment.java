package com.example.my_travel.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_travel.R;
import com.example.my_travel.activity.home.Home_List1Activity;
import com.example.my_travel.activity.home.Home_List2Activity;
import com.example.my_travel.adapter.RecycListAdapter;
import com.example.my_travel.bean.list.ListImg;
import com.example.my_travel.utils.MyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyc;
    private List<ListImg> list = new ArrayList<>();
    private RecycListAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return view;
    }
    //更新UI不在主线程更新
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    recyc.setAdapter(adapter);
                    //点击事件
                    adapter.MyItem(new MyItem() {
                        @Override
                        public void OnClick(View view, int position) {
                            Toast.makeText(getContext(),"点击的是"+position,Toast.LENGTH_SHORT).show();
                            switch (position){
                                case 0:
                                   startActivity(new Intent(getContext(), Home_List1Activity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(getContext(), Home_List2Activity.class));
                                    break;
                                    default:
                                        break;
                            }
                        }
                    });
                    break;
            }
            return false;
        }
    });
    private void initView() {
        recyc = view.findViewById(R.id.home_recyc);
        //设置网格布局
        LinearLayoutManager manager = new GridLayoutManager(getContext(),2);
        recyc.setLayoutManager(manager);
        list.addAll(ListImg.getlist());
        adapter = new RecycListAdapter(getContext(),list);
        handler.sendEmptyMessage(1);
    }

}
