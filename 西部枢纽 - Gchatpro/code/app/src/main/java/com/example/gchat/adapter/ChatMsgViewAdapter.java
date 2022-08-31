package com.example.gchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gchat.R;
import com.example.gchat.model.ChatMsgEntity;

import java.util.List;

/**
 * User: xiahao
 * DateTime: 2022/4/15 15:47
 * Description: 聊天记录ListView的Adapter
 */
//BaseAdapter就Android应用程序中经常用到的基础数据适配器，它的主要用途是将一组数据传到像ListView、Spinner、Gallery及GridView等UI显示组件，它是继承自接口类Adapter
public class ChatMsgViewAdapter extends BaseAdapter {

    //消息类型
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;       // 收到对方的消息
        int IMVT_TO_MSG = 1;        // 自己发送出去的消息
    }

    private static final int ITEMCOUNT = 2;// 消息类型的总数
    private List<ChatMsgEntity> coll;      // 消息对象数组
    private LayoutInflater mInflater;//LayoutInflater主要是用于加载布局的。LayoutInflater技术广泛应用于需要动态添加View的时候，比如在ScrollView和ListView中

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
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

    /**
     * 得到item的类型，是自己发送的消息，还是接受到的消息
     * @param position
     * @return
     */
    public int getItemViewType(int position) {
        ChatMsgEntity entity=coll.get(position);
        if(entity.getMsgType()) {   //收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        }else {                     //自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    /**
     * Item类型的总数:消息类型总数
     */
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity=coll.get(position);
        boolean isComMsg=entity.getMsgType();

        ViewHolder viewHolder=null;
        if(convertView==null){
            if(isComMsg){//如果是收到的
                convertView=mInflater.inflate(R.layout.chatting_item_msg_text_left,null);
            }else {
                convertView=mInflater.inflate(R.layout.chatting_item_msg_text_right,null);
            }

            viewHolder=new ViewHolder();  //自定义类 ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
            viewHolder.tvSendTime= (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);//使用setTag把查找的view缓存起来方便多次重用
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.tvUserName.setText(entity.getName());
        viewHolder.tvContent.setText(entity.getMessage());
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public boolean isComMsg = true;
    }



}
