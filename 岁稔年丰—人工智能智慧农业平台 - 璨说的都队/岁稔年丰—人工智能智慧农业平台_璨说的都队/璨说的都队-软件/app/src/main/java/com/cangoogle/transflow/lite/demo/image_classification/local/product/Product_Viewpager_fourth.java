package com.baidu.paddle.lite.demo.image_classification.local.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Adapter.product.product_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.productBean.productBean1;
import com.baidu.paddle.lite.demo.image_classification.Model.ProductModel;
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


public class Product_Viewpager_fourth extends Fragment {
    protected RecyclerView recyclerView;
    protected product_Adapter product_viewAdapter ;
    protected static List<productBean1> list_beans;
    protected OkHttpClient client;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.products_first,
                container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //配置recycleView以及下拉刷新
        list_beans = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.listView_1);
        new GetRecommead_List().run();
    }

    public class GetRecommead_List {
        private void run() {
            client = new OkHttpClient();
            String urls = String.format(Ipconfig.Ip + Ipconfig.vegetable_list);
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
                        java.lang.reflect.Type type = new TypeToken<ProductModel>() {}.getType();
                        ProductModel jsonBean = gson.fromJson(response.body().string(), type);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //进行添加数据

                                for (int i = 0; i < jsonBean.data.getTotal(); i++) {
                                    productBean1 list_bean = new productBean1
                                            (jsonBean.data.getList().get(i).ProdTitle,
                                                    jsonBean.data.getList().get(i).getProdPrice(),
                                                    jsonBean.data.getList().get(i).getProdUrl().replace("\r","")
                                                            .replace("\n",""));
                                    list_beans.add(list_bean);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
                                product_viewAdapter = new product_Adapter(list_beans);
                                recyclerView.setAdapter(product_viewAdapter);
                                recyclerView.setLayoutManager(gridLayoutManager);
                            }
                        });
                        Log.i("json-------", String.valueOf(jsonBean.data.getList().get(0).ProdTitle));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
