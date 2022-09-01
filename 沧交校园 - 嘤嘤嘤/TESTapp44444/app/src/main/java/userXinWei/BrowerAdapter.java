package userXinWei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.testapp.R;

import java.util.List;

public class BrowerAdapter  extends ArrayAdapter<Brower> {
    //resourceID指定ListView的布局方式
    private int resourceID;
    private Context context;
    //重写BrowserAdapter的构造器
    public BrowerAdapter(Context context, int textViewResourceID , List<Brower> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
        this.context=context;
    }
    //自定义item资源的解析方式
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前Browser实例
        Brower brower = getItem(position);
        View view ;
        ViewHolder viewHolder;
        //convertView为空则加载布局,不为空则重用
        //使用LayoutInfater为子项加载传入的布局
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.userTouXiang = view.findViewById(R.id.imageView2);
            viewHolder.username = view.findViewById(R.id.textView13);
            viewHolder.neirong=view.findViewById(R.id.textView4);
            viewHolder.time=view.findViewById(R.id.textView12);
            viewHolder.dianzanNUMS=view.findViewById(R.id.textView7);
            viewHolder.pinlunNUMS=view.findViewById(R.id.textView5);
            view.setTag(viewHolder);//将ViewHolder存储在View中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();//如果converView不为空说明这个布局已经加载过了，说明资源id已经绑定过了，那么直接使用getTag（）方法将这个绑定传入就行，不用再次绑定
        }

        //引入Browser对象的属性值
        /**
         * String url = "";
         *         Glide.with(this)
         *                 .load(url)
         *                 .into(imageView);//加载网络图片
         *子项里只显示头像但不显示帖子图像。
         */
        Glide.with(context).load(brower.getUserimageurl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(viewHolder.userTouXiang);
        viewHolder.username.setText(brower.getUsername());//用户名显示
        viewHolder.neirong.setText(brower.getNeiRong());
        viewHolder.time.setText(brower.getTime());
        viewHolder.dianzanNUMS.setText(brower.getDianZanNUMS()+"");
        viewHolder.pinlunNUMS.setText(brower.getPinLunNUMS()+"");
        return view;
    }
    class ViewHolder{
        ImageView userTouXiang;
        TextView username;
        TextView neirong;
        TextView time;
        TextView dianzanNUMS;
        TextView pinlunNUMS;
    }
}
