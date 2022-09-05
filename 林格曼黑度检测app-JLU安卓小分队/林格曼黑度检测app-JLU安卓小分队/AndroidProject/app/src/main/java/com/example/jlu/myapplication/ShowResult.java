package com.example.jlu.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mingle.widget.ShapeLoadingDialog;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowResult extends AppCompatActivity {
    private ListView listView;
    private List<Smoke> SmokeList;
    private ImageView main_photo;
    private void init()
    {
        ImageView scale = (ImageView)findViewById(R.id.scale);
        TextView tv_hint = (TextView)findViewById(R.id.hint);
        tv_hint.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scale.bringToFront();
                        scale.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        scale.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
        Drawable drawableSearch = getResources().getDrawable(R.drawable.hint);
        drawableSearch.setBounds(0, 0, 25, 25);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        tv_hint.setCompoundDrawables(drawableSearch, null, null, null);//只放上面
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_show_result);
        init();
        listView = findViewById(R.id.listview);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        SmokeList = new ArrayList<>();

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(data).getAsJsonArray();
        for (JsonElement user : jsonArray) {
            Smoke userBean = gson.fromJson(user, Smoke.class);
            SmokeList.add(userBean);
        }
        main_photo = (ImageView) findViewById(R.id.imageView);
        main_photo.setImageURI(Uri.parse("file://" + SmokeList.get(0).url));

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        for (int i = 1; i < SmokeList.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemImage", new File(SmokeList.get(i).url));
            map.put("ItemText", SmokeList.get(i).level);
            listItem.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(ShowResult.this,
            listItem,
            R.layout.item,
            new String[] {"ItemImage", "ItemText"},
            new int[] {R.id.imageView, R.id.textView});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent  =  new Intent(ShowResult.this,ShowDetailInfor.class);
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(SmokeList.get(i+1)));
                startActivity(intent);
            }
        });
        Button creat_btn = (Button) findViewById(R.id.creatReport);
        creat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this,SharePage.class);
                intent.putExtra("path",SmokeList.get(0).url);
                double num = SmokeList.size() - 1;
                int over_num = 0;
                double sum = 0;
                for (int i = 1; i < SmokeList.size(); i++) {
                    String dt[] = SmokeList.get(i).level.split(" ");
                    String val = dt[dt.length - 1];
                    sum += Integer.parseInt(val);
                    if(Integer.parseInt(val) >= 3) over_num ++;
                }
                intent.putExtra("num",String.valueOf((new Double(num)).intValue()));
                intent.putExtra("over_num",String.valueOf(over_num));
                intent.putExtra("avg",String.valueOf(sum/num));
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}