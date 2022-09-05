package com.baidu.paddle.lite.demo.image_classification.Acitivity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.Employment_grid_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Employment_list_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Classroom_list_Bean;
import com.baidu.paddle.lite.demo.image_classification.Model.EmployModel;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Utils.Ipconfig;
import com.baidu.paddle.lite.demo.image_classification.View.FirstFragmentGridView;
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

public class Employment_Activity extends AppCompatActivity {
    private FirstFragmentGridView firstFragmentGridView;
    protected RecyclerView recyclerView;
    protected Employment_list_Adapter employment_list_adapter;
    protected static List<Classroom_list_Bean> list_beans;
    protected OkHttpClient client;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment);
        init_Grid();
        initRecyclerView();
    }

    private void init_Grid() {
            firstFragmentGridView = (FirstFragmentGridView) this.findViewById(R.id.employment_gridview);
            String []names = { "县城招聘", "零工兼职", "急招"};
            int []imagesEdu = {R.drawable.fujin, R.drawable.lingong, R.drawable.jizhao};
            Employment_grid_Adapter firstFragment_adapter = new Employment_grid_Adapter(Employment_Activity.this, names, imagesEdu);
            firstFragmentGridView.setAdapter(firstFragment_adapter);
    }

    private void initRecyclerView() {
        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) this.findViewById(R.id.employment_recyclerview);
        for (int i = 0; i < 10 ; i++) {
            Classroom_list_Bean list_bean = new Classroom_list_Bean
                    ("线上兼职直招",
                            "2000/月",
                            "yangyudong",
                            "五寨风味小吃",
                            "公司",
                            "https://www.twoyl.cn/1.jpg");
            list_beans.add(list_bean);
        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Employment_Activity.this,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        employment_list_adapter = new Employment_list_Adapter(list_beans);
        recyclerView.setAdapter(employment_list_adapter);

        recyclerView.setLayoutManager(gridLayoutManager);
//        new  GetEmployList().run();
    }

    public class GetEmployList {
        private void run() {
            client = new OkHttpClient();
            String urls = String.format(Ipconfig.Ip + Ipconfig.employ_listall);
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
                        java.lang.reflect.Type type = new TypeToken<EmployModel>() {}.getType();
                        final EmployModel jsonBean = gson.fromJson(response.body().string(), type);
                        Employment_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //进行添加数据
                                Log.i("json-------", String.valueOf(jsonBean.data.getList().get(0).getTele()));
                                for (int i = 0; i < jsonBean.data.getTotal() ; i++) {
                                    Classroom_list_Bean list_bean = new Classroom_list_Bean
                                            (jsonBean.data.getList().get(i).getTitle(),
                                                    jsonBean.data.getList().get(i).getPrice(),
                                                    jsonBean.data.getList().get(i).getUserName(),
                                                    jsonBean.data.getList().get(i).getShopName(),
                                                    jsonBean.data.getList().get(i).getTypes(),
                                                    jsonBean.data.getList().get(i).getUrl().replace("\r","")
                                            .replace("\n","")
                                            .replace("\t",""));
                                    list_beans.add(list_bean);
                                }

                                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Employment_Activity.this,LinearLayoutManager.VERTICAL,false){
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                };
                                employment_list_adapter = new Employment_list_Adapter(list_beans);
                                recyclerView.setAdapter(employment_list_adapter);

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
