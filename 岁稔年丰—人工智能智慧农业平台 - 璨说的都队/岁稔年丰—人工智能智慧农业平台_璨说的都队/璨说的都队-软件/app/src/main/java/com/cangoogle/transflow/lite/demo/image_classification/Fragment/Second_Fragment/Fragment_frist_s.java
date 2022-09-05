package com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.LiveActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.live_data;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Attention_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Attention_bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Frist_fragment_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_frist_s extends Fragment {
    private RecyclerView recyclerView;
    private Attention_Adapter attention_adapter;
    protected static List<Attention_bean> list;
    protected CircleImageView circleImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second_viewpager_1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
    }

    private void initview() {
        circleImageView = getView().findViewById(R.id.attention_up_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), live_data.class));
            }
        });

        circleImageView.setBorderColor(0xFF2EE2C9);
        recyclerView = getView().findViewById(R.id.attention_recylerview);

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Attention_bean list_bean = new Attention_bean
                    ("师新村一号","生活往往充满了紫菜蛋花与青椒炒蛋~","99");
            list.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,
                false);
        attention_adapter = new Attention_Adapter(list);
        recyclerView.setAdapter(attention_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
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
