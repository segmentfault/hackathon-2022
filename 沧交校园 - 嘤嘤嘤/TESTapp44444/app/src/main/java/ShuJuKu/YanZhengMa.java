package ShuJuKu;

import java.util.Random;

public class YanZhengMa {
    char[] shuzu={'0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f','g','h','i','j','k','m','n','o','p','q','r','s','t','u','v','w','x','y','z',//为了便于辨认，去掉了大写的o和小写的L
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'};//从这59个数里面随机取出六个数当验证码。


    public String setYanzhengma(){
        Random rand=new Random();
        String str="";
        for(int i=0;i<6;i++){
            str+=shuzu[rand.nextInt(59)];
        }
        return str;
    }
    public void sendYanzhengma(){
        String str=setYanzhengma();//生成验证码
        QuanJU.getInstance().YanZhengMa=str;//将验证码赋给全局变量里面
        /**
         * 接下来是发送验证码的流程，将str作为验证码调用阿里云的接口发送,还没写
         */
    }
}
