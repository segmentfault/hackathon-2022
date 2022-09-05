package com.baidu.paddle.lite.demo.image_classification.Fragment.Agritainment_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.Agritainment_Product_Activity;
import com.baidu.paddle.lite.demo.image_classification.Adapter.ListViewAdapter;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

public class Agritainment_Fragment_Second extends Fragment {
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.agritainment_fragment_second,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView)getView().findViewById(R.id.agritainment_listview);


        List<String> listData=getData();
        ListViewAdapter adapter = new ListViewAdapter(listData,getContext());
        listView.setAdapter(adapter);
    }


    private List<String> getData(){
        List<String>list = new ArrayList<>();
        for(int i = 0; i <10 ; i++) {
            list.add("数据"+i);

        }
        return list;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
