package com.example.gchat.viewUI;

import android.os.Bundle;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.example.gchat.R;
import com.example.gchat.adapter.MoodAdapter;
import com.example.gchat.dao.MoodDAO;
import com.example.gchat.model.MoodEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cy
 * DateTime: 2022/5/3 19:09
 * Description:历史时间轴
 */
public class QzoneActivity  extends FragmentActivity {


    private ListView lv;            //QQ说说视图
    private MoodAdapter adapter;    //QQ说说适配器
    private List<MoodEntity> list;  //QQ说说列表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzone);


        lv = (ListView) findViewById(R.id.lv);//QQ说说ListView
        list =new ArrayList<>();
        //list.add(new MoodEntity("2016-12-13 19:13:50","今天心情怎么样~~~"));
        //list.add(new MoodEntity("2016-12-13 19:21:50","今天上了计算机网络课程..."));
        //list.add(new MoodEntity("2016-12-13 19:33:50","计算机组成原理实验报告还没写"));
        //list.add(new MoodEntity("2016-12-13 19:59:50","嵌入式要怎么考试啊？？？"));
        //list.add(new MoodEntity("2016-12-13 20:03:50","我的基础版本快要完成了，哈哈"));
        //list.add(new MoodEntity("2016-12-13 20:05:50","之后增加服务器版........."));
        //list.add(new MoodEntity("2016-12-13 20:08:50",".NET项目还没完成呢！"));
        //list.add(new MoodEntity("2016-12-13 20:10:52","假期去哪玩呢？"));
        //list.add(new MoodEntity("2016-12-13 20:23:15","好久没看Java啦"));
        //list.add(new MoodEntity("2016-12-13 20:44:40","哈哈哈，分享生活！！"));
        list=MoodDAO.getmoodEntityArrayList();
        adapter = new MoodAdapter(this,list);
        lv.setAdapter(adapter);


    }

}
