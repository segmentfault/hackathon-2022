package com.baidu.paddle.lite.demo.image_classification.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.LiveActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Pic_Word_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.VideosActivity;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom.Classroom_Banner_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom.Classroom_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Classroom.Classroom_list_Adapter_2;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Characteristic_local.char_local_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Banner.Classroom_Banner_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Characteristic_local.char_local_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.fragment3_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.FirstFragmentGridView;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

public class Fragment_third extends Fragment {
    protected Banner banner;
    private FirstFragmentGridView firstFragmentGridView;
    protected RecyclerView recyclerView,recyclerView_2;
    protected Classroom_list_Adapter classroom_list_adapter;
    protected Classroom_list_Adapter_2 classroom_list_adapter_2;
    protected static List<fragment3_Bean> list_beans,list_beans_2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_third,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBanner();
        init_Grid();
        Onclick();
        initRecyclerView();
    }

    private void initRecyclerView() {
        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.third_firts_recyclerview);
        for (int i = 0; i < 5; i++) {
            fragment3_Bean list_bean = new fragment3_Bean
                    ("忻州糖人制艺");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        classroom_list_adapter = new Classroom_list_Adapter(list_beans);
        recyclerView.setAdapter(classroom_list_adapter);


        recyclerView.setLayoutManager(gridLayoutManager);


        list_beans_2 = new ArrayList<>();
        recyclerView_2 = (RecyclerView) getView().findViewById(R.id.third_firts_recyclerview_2);
        for (int i = 0; i < 5; i++) {
            fragment3_Bean list_bean = new fragment3_Bean
                    ("忻州糖人制艺");
            list_beans_2.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager_2 = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        classroom_list_adapter_2 = new Classroom_list_Adapter_2(list_beans_2);
        recyclerView_2.setAdapter(classroom_list_adapter_2);


        recyclerView_2.setLayoutManager(gridLayoutManager_2);
    }

    private void Onclick() {
        firstFragmentGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        firstFragmentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), VideosActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), LiveActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), Pic_Word_Activity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void init_Grid() {
        firstFragmentGridView = (FirstFragmentGridView) getView().findViewById(R.id.third_gridview);
        String []names = {"视频教程", "大咖直播", "图文教程"};
        int []imagesEdu = {R.drawable.shipin, R.drawable.zhibo, R.drawable.jiaocheng};
        FirstFragment_Adapter firstFragment_adapter = new FirstFragment_Adapter(getActivity(), names, imagesEdu);
        firstFragmentGridView.setAdapter(firstFragment_adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initBanner() {
        banner = (Banner)getView().findViewById(R.id.Classroom_banner);
        Classroom_Banner_Adapter adapter = new Classroom_Banner_Adapter(Classroom_Banner_Bean.getTestData());

        banner.setAdapter(adapter)//设置适配器
                .setCurrentItem(0,false)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setBannerRound(BannerUtils.dp2px(13))//圆角
                .setLoopTime(3500)
                .addPageTransformer(new ScaleInTransformer())//添加切换效果
                .setIndicator(new CircleIndicator(getContext()));//设置指示器
    }

}
