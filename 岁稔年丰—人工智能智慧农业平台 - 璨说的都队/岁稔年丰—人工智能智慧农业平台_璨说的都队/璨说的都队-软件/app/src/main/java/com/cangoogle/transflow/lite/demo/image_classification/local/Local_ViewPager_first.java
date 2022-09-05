package com.baidu.paddle.lite.demo.image_classification.local;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Characteristic_local.char_local_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Characteristic_local.char_local_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Model.LocalModel;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Utils.Ipconfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Local_ViewPager_first extends Fragment {
    protected RecyclerView recyclerView;
    protected char_local_list_Adapter char_local_list_adapter ;
    protected static List<char_local_list_Bean> list_beans;
    protected OkHttpClient client;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.char_local_first,
                container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.local_firts_recyclerview);
////        new Get_List().run();
        for (int i = 0; i < 10; i++) {
            char_local_list_Bean list_bean = new char_local_list_Bean
                    ("五寨亲子农家乐",
                            "山西省忻州市五寨县狮新村",
                            "150",
                           "地点：山西太原市娄烦县",
                            "2022-04-09 21:56:33",
                            "2022-04-09 20:56:38",
                            "农活",
                            "130",
                            "2022-04-09 20:56:38",
                            "100",
                            "篝火",
                            "150",
                            "2022-04-09 20:56:38",
                            "100",
                          "爬山",
                            "100",
                            "2022-04-09 20:56:38",
                            "100");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        char_local_list_adapter = new char_local_list_Adapter(list_beans);
        recyclerView.setAdapter(char_local_list_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public class Get_List {
        private void run() {
            client = new OkHttpClient();
            String urls = String.format(Ipconfig.Ip + Ipconfig.kid_list);
            Request request = new Request.Builder()
                    .url(urls)  // ”key“+=“你的key值”
                    .get()
                    .build();

            try {
                Call call = client.newCall(request);
                //异步请求
                //Response response = client.newCall(request).execute();
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("TAG", "onFailure: "+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<LocalModel>() {}.getType();
                        LocalModel jsonBean = gson.fromJson(response.body().string(), type);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < jsonBean.data.getTotal(); i++) {
                                    char_local_list_Bean list_bean = new char_local_list_Bean
                                            (jsonBean.data.getList().get(i).getMaintype(),
                                                    jsonBean.data.getList().get(i).getAddress(),
                                                    jsonBean.data.getList().get(i).getPrize(),
                                                    jsonBean.data.getList().get(i).getDescs(),
                                                    jsonBean.data.getList().get(i).getTimea(),
                                                    jsonBean.data.getList().get(i).getTimeb(),
                                                    jsonBean.data.getList().get(i).getType1(),
                                                    jsonBean.data.getList().get(i).getType1Prize(),
                                                    jsonBean.data.getList().get(i).getType1Timea(),
                                                    jsonBean.data.getList().get(i).getType1Timeb(),
                                                    jsonBean.data.getList().get(i).getType2(),
                                                    jsonBean.data.getList().get(i).getType2Prize(),
                                                    jsonBean.data.getList().get(i).getType2Timea(),
                                                    jsonBean.data.getList().get(i).getType2Timeb(),
                                                    jsonBean.data.getList().get(i).getType3(),
                                                    jsonBean.data.getList().get(i).Type3Timea,
                                                    jsonBean.data.getList().get(i).Type3Timeb,
                                                    jsonBean.data.getList().get(i).Type3Prize);
                                    list_beans.add(list_bean);
                                }

                                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                char_local_list_adapter = new char_local_list_Adapter(list_beans);
                                recyclerView.setAdapter(char_local_list_adapter);
                                recyclerView.setLayoutManager(gridLayoutManager);
                            }
                        });
                        Log.i("json-------", String.valueOf(jsonBean.data.getList().get(0)));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
