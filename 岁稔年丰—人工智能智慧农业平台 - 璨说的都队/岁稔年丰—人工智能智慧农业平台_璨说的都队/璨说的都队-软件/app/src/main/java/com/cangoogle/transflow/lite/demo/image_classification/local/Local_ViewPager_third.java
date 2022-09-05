package com.baidu.paddle.lite.demo.image_classification.local;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Characteristic_local.char_local_list_Adapter_3;
import com.baidu.paddle.lite.demo.image_classification.Bean.Characteristic_local.char_local_list_Bean_3;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;


public class Local_ViewPager_third extends Fragment {
    protected RecyclerView recyclerView;
    protected char_local_list_Adapter_3 char_local_list_adapter ;
    protected static List<char_local_list_Bean_3> list_beans;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.char_local_third,
                container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //配置recycleView以及下拉刷新
        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.local_firts_recyclerview_3);
        for (int i = 0; i < 10; i++) {
            char_local_list_Bean_3 list_bean = new char_local_list_Bean_3
                    ("套餐A");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        char_local_list_adapter = new char_local_list_Adapter_3(list_beans);
        recyclerView.setAdapter(char_local_list_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

}
