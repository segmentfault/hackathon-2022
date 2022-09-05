package com.baidu.paddle.lite.demo.image_classification.View.Shopping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.paddle.lite.demo.image_classification.Acitivity.ShoppingCart_Activity;
import com.baidu.paddle.lite.demo.image_classification.Adapter.ListViewAdapter;
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

public class refundViewpage extends Fragment {
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_shoppingcart_list_second,
                container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView)getView().findViewById(R.id.shopping_listview_2);
        List<String> listData=getData();
        ListViewAdapter adapter = new ListViewAdapter(listData, getContext());
        listView.setAdapter(adapter);
    }
    private List<String> getData(){
        List<String>list = new ArrayList<>();
        for(int i = 0; i <1 ; i++) {
            list.add("五寨农田玉米1号");

        }
        return list;
    }
}
