package com.baidu.paddle.lite.demo.image_classification.Fragment.Second_Fragment.Second_down_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Banner_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Fragment_Recommend_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.DataBean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Fragment_recommend_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Frist_fragment_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.BannerIndicator;
import com.baidu.paddle.lite.demo.image_classification.View.FirstFragmentGridView;
import com.youth.banner.Banner;
import com.youth.banner.transformer.RotateYTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

public class Fragment_recommend extends Fragment {
    protected RecyclerView recyclerView;
    protected Fragment_Recommend_list_Adapter fragment_recommend_list_adapter ;
    protected static List<Fragment_recommend_list_Bean> list_beans;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recommend,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitViews();
    }

    private void InitViews() {



        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView_recommend);
        for (int i = 0; i < 3; i++) {
            Fragment_recommend_list_Bean list_bean = new Fragment_recommend_list_Bean
                    (R.drawable.mike,R.drawable.mike,R.drawable.mike,R.drawable.mike);
            list_beans.add(list_bean);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        fragment_recommend_list_adapter = new Fragment_Recommend_list_Adapter(list_beans);
        recyclerView.setAdapter(fragment_recommend_list_adapter);
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
