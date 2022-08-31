package com.cong.picturehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cong.pojo.Picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends AppCompatActivity {

    Button turn;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        turn = findViewById(R.id.turn);
        upload = findViewById(R.id.upload);
        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity1.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

//    public void inittest() {
//        Bmob.initialize(this, "522fe1a4eb69f21a532361eaa1748c1d");// 初始化Bmob
//        Person p2 = new Person();
//        p2.setName("lucky");
//        p2.setAddress("北京海淀");
//        p2.save(new SaveListener<String>() {
//            @Override
//            public void done(String objectId,BmobException e) {
//                if(e==null){
//                    System.out.println("添加数据成功，返回objectId为："+objectId);
//                }else{
//                    System.out.println("创建数据失败：" + e.getMessage());
//                }
//            }
//        });
//    }

    public void init() {
        Bmob.initialize(this, "522fe1a4eb69f21a532361eaa1748c1d");// 初始化Bmob

        BmobQuery<Picture> query = new BmobQuery<Picture>();
        query.findObjects(new FindListener<Picture>() {
            @Override
            public void done(List<Picture> list, BmobException e) {
                if (e == null) {
                    Log.i("查询成功", "");
                    int j = list.size();
                    //System.out.print(j + "\n");

                    ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    int i = 0;
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    for (Picture picture : list) {
                        if (picture.getCurl() != null) {
                            //文件下载地址
                            String imgs = picture.getCurl().getUrl();
                            String imgs1 = imgs.substring(0,4);
                            String imgs2 = imgs.substring(5);
                            String imgs0 = imgs1 + imgs2;
                            System.out.println("url为：" + imgs0);
                            map.put("" + i, imgs0);
                            i++;
                            data.add(map);
                        }
                    }
                    Set<String> set = map.keySet();
                    Iterator<String> it = set.iterator();
                    String[][] ss = new String[map.size()][2];
                    for (i = 0; i < map.size(); i++) {
                        ss[i][0] = it.next();
                        ss[i][1] = (String) map.get(ss[i][0]);
                        System.out.print("这里：" + ss[i][1] + "\n");
                    }

                    //System.out.println(ss.length);
                    String[] urls = new String[(ss.length) * 2];
                    for (i = 0; i < ss.length; i++) {
                        for (j = 0; j < ss[i].length; j++) {
                            urls[i + j] = ss[i][j + 1];
                            // mString[i+j]=urls[i+j];
                            System.out.println("最后：" + urls[i + j]);
                            j++;
                        }
                    }
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putStringArray("name", urls);
                    Intent intent = new Intent(MainActivity.this, Slide.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });

    }
}