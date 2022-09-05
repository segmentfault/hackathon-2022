package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Employment_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Videos_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Video_Bean;
import com.baidu.paddle.lite.demo.image_classification.Model.EmployModel;
import com.baidu.paddle.lite.demo.image_classification.Model.VideoModel;
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

public class VideosActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected Videos_Adapter videos_adapter;
    protected static List<Video_Bean> list_beans;
    protected OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initRecyclerView();
    }

    private void initRecyclerView() {
        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.videos_recyclerview);
//        new GetList().run();
        for (int i = 0; i < 5; i++) {
            Video_Bean list_bean = new Video_Bean
                    ("15天练就简单动物剪纸技艺，拯救手残党",
                            "剪纸教程",
                            "剪纸传承人大师",
                           "http://www.twoyl.cn/xczxpt/banner/a2.jpg",
                           "20.36",
                            "150",
                            "http://www.twoyl.cn/xczxpt/banner/a4.jpg",
                            "15");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(VideosActivity.this,LinearLayoutManager.VERTICAL,false);
        videos_adapter = new Videos_Adapter(list_beans);
        recyclerView.setAdapter(videos_adapter);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    public class GetList {
        private void run() {
            client = new OkHttpClient();
            String urls = String.format(Ipconfig.Ip + Ipconfig.videolist);
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
                        Log.i("json-------","shibai");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<VideoModel>() {}.getType();
                         VideoModel jsonBean = gson.fromJson(response.body().string(), type);
                        VideosActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //进行添加数据
                                Log.i("json-------", String.valueOf(jsonBean.data.getList().get(0).getVideoPic()));
                                for (int i = 0; i < 5; i++) {
                                    Video_Bean list_bean = new Video_Bean
                                            (jsonBean.data.getList().get(i).getTitle(),
                                                    jsonBean.data.getList().get(i).getLabel(),
                                                    jsonBean.data.getList().get(i).getTeaName(),
                                                    jsonBean.data.getList().get(i).getTeaUrl(),
                                                    jsonBean.data.getList().get(i).getPrice(),
                                                    jsonBean.data.getList().get(i).getPersons(),
                                                    jsonBean.data.getList().get(i).getVideoPic(),
                                                    jsonBean.data.getList().get(i).getHours());
                                    list_beans.add(list_bean);
                                }

                                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(VideosActivity.this,LinearLayoutManager.VERTICAL,false);
                                videos_adapter = new Videos_Adapter(list_beans);
                                recyclerView.setAdapter(videos_adapter);
                                recyclerView.setLayoutManager(gridLayoutManager);
                            }
                        });

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}