package com.baidu.paddle.lite.demo.image_classification.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.AccommodationActivity.AccommodationActivity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Agriculture_Products_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Agritainment_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Agritainment_Product_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Characteristic_local_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Craft_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.Employment_Activity;
import com.baidu.paddle.lite.demo.image_classification.Acitivity.VideoCaptureActivity;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Banner_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.FirstFragment_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.DataBean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Frist_fragment_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.View.BannerIndicator;
import com.baidu.paddle.lite.demo.image_classification.View.FirstFragmentGridView;
import com.youth.banner.Banner;
import com.youth.banner.transformer.RotateYTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

public class Fragment_First extends Fragment {
    private FirstFragmentGridView firstFragmentGridView;
    private Banner banner;
    protected RecyclerView recyclerView;
    protected FirstFragment_list_Adapter firstFragment_list_adapter ;
    protected static List<Frist_fragment_list_Bean> MyInstanceList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_first,container,false);
        return view;
    }

    private void InitViews() {
        banner =(Banner)getView().findViewById(R.id.banner);

        Banner_Adapter adapter = new Banner_Adapter(DataBean.getTestData());

        banner.setAdapter(adapter)//设置适配器
                .setCurrentItem(3,false)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setBannerRound(BannerUtils.dp2px(13))//圆角
                .setLoopTime(3500)
                .addPageTransformer(new RotateYTransformer())//添加切换效果
                .setIndicator(new BannerIndicator(getContext()));//设置指示器
        // .addOnPageChangeListener(this);//添加切换监听


        firstFragmentGridView = (FirstFragmentGridView) getView().findViewById(R.id.gv_first_fragment);
        String []names = {"特色当地", "特色民宿", "特色农产品", "附近就业","耕地智检","民间手工艺"};
        int []imagesEdu = {R.drawable.ts, R.drawable.zs, R.drawable.ncp, R.drawable.zp,R.drawable.qy,R.drawable.fy};
        FirstFragment_Adapter firstFragment_adapter = new FirstFragment_Adapter(getActivity(), names, imagesEdu);
        firstFragmentGridView.setAdapter(firstFragment_adapter);


        MyInstanceList = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);
        for (int i = 0; i < 10; i++) {
                Frist_fragment_list_Bean list_bean = new Frist_fragment_list_Bean
                        (R.drawable.hb3,R.drawable.hb2,R.drawable.hb2,R.drawable.hb);
                MyInstanceList.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,
                false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        firstFragment_list_adapter = new FirstFragment_list_Adapter(MyInstanceList);
        recyclerView.setAdapter(firstFragment_list_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void Onclick() {
        firstFragmentGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        firstFragmentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), Characteristic_local_Activity.class));
                        break;

                    case 1:
                        startActivity(new Intent(getActivity(), Agritainment_Activity.class));
                        break;

                    case 2:
                        startActivity(new Intent(getActivity(), Agriculture_Products_Activity.class));
                        break;

                    case 3:
                        startActivity(new Intent(getActivity(), Employment_Activity.class));
//                        Toast.makeText(getContext(), "功能暂未开放，请等待后续更新", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), VideoCaptureActivity.class));

                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), Craft_Activity.class));
                        break;

                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitViews();
        Onclick();
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
