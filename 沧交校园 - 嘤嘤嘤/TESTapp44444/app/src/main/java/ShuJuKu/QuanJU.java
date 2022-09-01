package ShuJuKu;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;

/**
 * 保存与用户信息有关的需要频繁用到的数据
 */
public class QuanJU extends Application {
    private static Context context;
    public int isDengLu;//如果是0默认，则未登录，否则是登录
    public int isYanzheng;//是否填了验证码，默认为0；
    public String YanZhengMa="123456";//默认验证码
    public int TieZiRuKou;//0代表主页进入，1代表我的帖子进入，2代表评论进入
    public String shoujihao=null;
    public String username=null;
    public String password=null;
    public String touxiang="";
    public String WODianZanDe="";
    public String tieziID="";
    private static  QuanJU quanju;
    public HashMap<String,String>quanjuMap=new HashMap<>();

    public static QuanJU getInstance(){
        return quanju;
    }
    public void onCreate() {
        isDengLu=0;
        isYanzheng=0;
        super.onCreate();
        quanju=this;//打开应用时为实例赋值
        context=getApplicationContext();
    }
    public static Context getContext() {
        return context;
    }
}
