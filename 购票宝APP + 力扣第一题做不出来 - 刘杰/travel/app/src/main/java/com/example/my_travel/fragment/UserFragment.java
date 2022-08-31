package com.example.my_travel.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_travel.R;
import com.example.my_travel.Utils;
import com.example.my_travel.adapter.ListGpAdapter;
import com.example.my_travel.bean.GP;
import com.example.my_travel.sql.gp.GpHelp;
import com.example.my_travel.sql.gp.GpSqlite;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private View view;
    private String strname;
    private TextView name;
    private ListView listView;
    private static GpSqlite gpSqlite;
    private ListGpAdapter adapter;
    private List<GP> list = new ArrayList<>();
    public UserFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    listView.setAdapter(adapter);
                    break;
            }
            return false;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        gpSqlite = new GpSqlite(getContext(),"Gp.db",null,2);
        SharedPreferences sp = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        strname = sp.getString("username","admin");
        listView = view.findViewById(R.id.user_gp_list);

        initView();
        return view;
    }

    private void initView() {
        name = view.findViewById(R.id.user_name);
        name.setText(Utils.NAME);
        list.clear();
        initSql();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                GpHelp.delete(gpSqlite,list.get(i).getId()+"");
                Toast.makeText(getActivity(),"取消购票成功",Toast.LENGTH_SHORT).show();
                initSql();
                return true;
            }
        });
    }

    private void initSql() {
        list.clear();
        Cursor allgp = GpHelp.getAllGpMaps(gpSqlite);
        for (allgp.moveToFirst(); !allgp.isAfterLast(); allgp.moveToNext()){
            GP gp = new GP();
            gp.setId(allgp.getInt(allgp.getColumnIndex("_id")));
            gp.setQuantity(allgp.getString(allgp.getColumnIndex("quantity")));
            gp.setMoney(allgp.getString(allgp.getColumnIndex("money")));
            list.add(gp);
            /*System.out.println("获取到的值是"+allgp);*/
            adapter = new ListGpAdapter(list,getContext());
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initSql();
    }

    //给其他类提供dbHelper
    public static GpSqlite getSqlite() {
        return gpSqlite;
    }
}
