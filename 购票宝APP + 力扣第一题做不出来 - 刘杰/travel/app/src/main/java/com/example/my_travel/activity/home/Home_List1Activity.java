package com.example.my_travel.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.my_travel.MyScrollListView;
import com.example.my_travel.R;
import com.example.my_travel.adapter.ListMentAdapter;
import com.example.my_travel.bean.Ment;
import com.example.my_travel.sql.plun.MentHelp;
import com.example.my_travel.sql.plun.MentSqlite;

import java.util.ArrayList;
import java.util.List;

public class Home_List1Activity extends AppCompatActivity {
    private ImageView back_img;
    private Button bnt1,bnt2;
    private LinearLayout ll_1;
    private String strname;
    private EditText ed_conten;
    private MyScrollListView listView;
    private static MentSqlite mentSqlite;
    private ListMentAdapter adapter;
    private List<Ment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__list1);
        //创建数据库
        mentSqlite = new MentSqlite(this,"Ment.db",null,2);
        //读取缓存文件
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        strname = sp.getString("username","");
        initView();
    }

    private void initView() {
        back_img = findViewById(R.id.Home_list1_back);
        bnt1 = findViewById(R.id.Home_list1_bnt1);
        bnt2 = findViewById(R.id.Home_list1_bnt2);
        ll_1 = findViewById(R.id.ll_home_1);
        listView = findViewById(R.id.Home_list1_listview);
        ed_conten = findViewById(R.id.Home_list1_ed);
        getMentList();
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    Toast.makeText(getBaseContext(),"您输入的内容为空!",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getBaseContext(),"添加成功!",Toast.LENGTH_SHORT).show();
                    ed_conten.setText("");
                    getMentList();
                    break;
                case 3:
                    listView.setAdapter(adapter);
                    break;
                    default:break;
            }
            return false;
        }
    });
    private void getMentList() {
        list.clear();
        //获取数据库中内容
        Cursor allMents = MentHelp.getAllMents(mentSqlite);
        for (allMents.moveToFirst(); !allMents.isAfterLast(); allMents.moveToNext()){
            Ment ment = new Ment();
            ment.setId(allMents.getInt(allMents.getColumnIndex("_id")));
            ment.setUsername(allMents.getString(allMents.getColumnIndex("username")));
            ment.setConten(allMents.getString(allMents.getColumnIndex("conten")));
            list.add(ment);
            adapter = new ListMentAdapter(list,this);
            handler.sendEmptyMessage(3);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bnt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conten = ed_conten.getText().toString();
                if (conten.equals("")){
                    handler.sendEmptyMessage(1);
                }else {
                    MentSqlite mentSqlite = getSqlite();
                    ContentValues values = new ContentValues();
                    values.put("username",strname);
                    values.put("conten",ed_conten.getText().toString());
                    MentHelp.insertMent(mentSqlite,values);
                    handler.sendEmptyMessage(2);
                }
            }
        });
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_List1Activity.this,Home_List2Activity.class));
            }
        });
        bnt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_List1Activity.this,PurchaseActivity.class));
            }
        });
    }
    //给其他类提供dbHelper
    public static MentSqlite getSqlite() {
        return mentSqlite;
    }
}
