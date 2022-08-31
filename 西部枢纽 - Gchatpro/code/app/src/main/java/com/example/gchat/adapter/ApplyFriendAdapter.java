package com.example.gchat.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gchat.ApplyFriend;
import com.example.gchat.LoginActivity;
import com.example.gchat.MainActivity;
import com.example.gchat.R;
import com.example.gchat.WelcomeActivity;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.model.ApplyFriendModel;
import com.example.gchat.model.ChatMsgEntity;
import com.example.gchat.model.Person;
import com.google.android.exoplayer2.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.List;

/**
 * User: xiahao
 * DateTime: 2022/4/15 15:47
 * Description: 聊天记录ListView的Adapter
 */
//BaseAdapter就Android应用程序中经常用到的基础数据适配器，它的主要用途是将一组数据传到像ListView、Spinner、Gallery及GridView等UI显示组件，它是继承自接口类Adapter
public class ApplyFriendAdapter extends BaseAdapter {


    private List<ApplyFriendModel> coll;      // 申请好友对象数组
    private LayoutInflater mInflater;//LayoutInflater主要是用于加载布局的。LayoutInflater技术广泛应用于需要动态添加View的时候，比如在ScrollView和ListView中

    public ApplyFriendAdapter(Context context, List<ApplyFriendModel> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);//首先需要获取到LayoutInflater的实例
    }

    @Override
    public int getCount() {
        return coll.size();
    }

    @Override
    public Object getItem(int position) {
        return coll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ApplyFriendModel entity=coll.get(position);

        ViewHolder viewHolder=null;
        if(convertView==null){
                convertView = mInflater.inflate(R.layout.apply_list_item, null);

                viewHolder = new ViewHolder();  //自定义类 ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
                viewHolder.FriendName = (TextView) convertView.findViewById(R.id.applyy_name);
                viewHolder.bt_aggre = (Button) convertView.findViewById(R.id.btn_agree);
                viewHolder.bt_refuse = (Button) convertView.findViewById(R.id.btn_refuse);

            convertView.setTag(viewHolder);//使用setTag把查找的view缓存起来方便多次重用
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //  通过回调原理，实现按钮监听
        viewHolder.bt_aggre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 向服务器发送同意确认包
                WelcomeActivity.tcPclient.jsonObject = new JSONObject();
                try {
                    WelcomeActivity.tcPclient.jsonObject.put("act",6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    WelcomeActivity.tcPclient.jsonObject.put("id",entity.getid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 将好友id 写入数据库
                WelcomeActivity.tcPclient.senddata();

                PersonDAO personDAO = new PersonDAO();
                Person person = new Person(entity.getname(), entity.getid());
                personDAO.insert(person);
                // 再跳到主界面会自动更新
                Log.d("AAA","加好友成功");
            }
        });
        viewHolder.bt_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "已拒绝", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.FriendName.setText(entity.getname());
        return convertView;
    }
    static class ViewHolder {
        Button bt_aggre,bt_refuse;
        TextView FriendName;
    }
}
