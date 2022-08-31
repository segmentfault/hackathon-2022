package com.example.gchat;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.gchat.Service.NotiFicationService;
import com.example.gchat.Service.NotiFicationService;
import com.example.gchat.dao.PersonDAO;
import com.example.gchat.model.Person;
import com.example.gchat.utils.MessageActivity;
import com.example.gchat.viewUI.ChatMainActivity;
import com.example.gchat.viewUI.RegistActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * User: xiahao

 * Description:与服务器的通讯协议
 */
public class TCPclient {
    String Firedate;

    String ReadBuffer ;
    byte[] FireByte;

    public JSONObject jsonObject;
    public JSONObject jsonRead;
    public ThreadSendData threadSendData;
    public ThreadReadData threadReadData;



    public Socket socket = null;//Socket

    public boolean RD = false;//用于控制读数据线程是否执行
    String getData;
    //定义数据输出流，用于发送数据

    //InputStream inputStream = null;//定义数据输入流，用于接收数据
    Connect_Thread Connect_thread;
    public void Start(){
        Connect_thread = new Connect_Thread();
        Connect_thread.start();
    }

    public void senddata() {
        threadSendData = new ThreadSendData();
        threadSendData.start();
    }


    //用线程创建Socket连接
    class Connect_Thread extends Thread{
        public void run(){

            //定义一个变量用于储存ip
            InetAddress ipAddress;

            try {
                //判断socket的状态，防止重复执行
                if (socket == null) {
                    //如果socket为空则执行
                    //获取输入的IP地址
                    ipAddress = InetAddress.getByName("110.40.159.232");
                    //获取输入的端口
                    int port = 8011;
                    //新建一个socket
                    socket = new Socket(ipAddress, port);
                    threadReadData = new ThreadReadData();
                    threadReadData.start();
                    //获取socket的输入流和输出流

                }
            } catch (Exception e) {
                //如果有错误则在这里返回
                e.printStackTrace();
            }
        }
    }
    int count(byte[] x){
        int res=0;
        int a = (int)(x[3] & 0xff);
        int[] b = new int[33];
        for(int i=0;i<32;i++)b[i]=0;
        int idx=31;
        while(a>0){
            if((a&1)==1) {
                b[idx] = 1;
            }
            idx--;
            a>>=1;
        }
        idx=23;
        a=(int)( x[2] & 0xff );

        while(a>0){
            if((a&1)==1) {
                b[idx] = 1;
            }
            idx--;
            a>>=1;
        }
        idx=15;
        a=(int)(x[1] & 0xff) ;

        while(a>0){
            if((a&1)==1) {
                b[idx] = 1;
            }
            idx--;
            a>>=1;
        }
        idx=7;
        a=(int)(x[0] & 0xff);

        while(a>0){
            if((a&1)==1) {
                b[idx] = 1;
            }
            idx--;
            a>>=1;
        }
        for(int i=31;i>=0;i--){
            if(b[i]==1){
                res+=(int)Math.pow(2,31-i);
            }
        }
        return res;
    }
    //用线程执行读取服务器发来的数据
    class ThreadReadData extends Thread{
        public void run() {
            //定义一个变量用于储存服务器发来的数据
            String textdata;
            jsonRead = new JSONObject();
            int readCount = 0; // 已经成功读取的字节的个数
            //根据RD变量的值判断是否执行读数据
             while(true){
                Log.d("AAA","接受信息");
                try {
                        readCount =0;
                        BufferedReader bufferedReader = null;
                        //定义一个字节集，存放输入的数据，缓存区大小为2048字节
                        byte[] ReadBuffer = new byte[65535];
                        Log.d("AAA", "接受包头中");
                        while((readCount!=-1)&&readCount<12) {
                            readCount += socket.getInputStream().read(ReadBuffer, readCount, 12 - readCount);
                        }// 读取包头12个字节

                        int datalength = count(ReadBuffer);

                        Log.d("AAA",String.valueOf(datalength));
                        readCount=0;
                        //从输入流获取服务器发来的数据和数据宽度
                        //ReadBuffer为参考变量，在这里会改变为数据
                        //输入流的返回值是服务器发来的数据宽度
                        Log.d("AAA", "接受数据包中");
                        while((readCount!=-1)&&readCount<datalength) {
                            readCount += socket.getInputStream().read(ReadBuffer, readCount, datalength - readCount);
                        }// 读取数据包

                        textdata =new String(ReadBuffer,0,datalength,"utf-8");//原始编码数据

                        Log.d("AAA",textdata);

                        jsonRead =new JSONObject(textdata);
                        new Thread() {
                        public void run() {
                            Message message = new Message();
                            message.what = 1;
                            try {
                                if(jsonRead.getInt("act")==4 )
                                LoginActivity.mHandler1 .sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(jsonRead.getInt("act")==3)
                                RegistActivity.mHandler2.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(jsonRead.getInt("act")==8){
                                        AddFriendActivity.myhandlerAddFriend.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(jsonRead.getInt("act")==5 ){
                                    synchronized (WelcomeActivity.ApplyFriendQueue){
                                        WelcomeActivity.ApplyFriendQueue.offer(jsonRead);
                                    }
                                    if(NotiFicationService.MessageServiceHandler !=null){
                                        message.what = 3;
                                        NotiFicationService.MessageServiceHandler.sendMessage(message);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(jsonRead.getInt("act")==6){
                                    PersonDAO personDAO = new PersonDAO();
                                    Person person = new Person(jsonRead.getString("name"), jsonRead.getInt("id"));
                                    personDAO.insert(person);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(jsonRead.getInt("act")==2){ // 聊天信息
                                    synchronized (WelcomeActivity.messageQueue) {
                                        Log.d("AAA","写入队列");
                                        WelcomeActivity.messageQueue.offer(jsonRead);
                                    }
                                    if(ChatMainActivity.myHandler3!=null) {
                                        ChatMainActivity.myHandler3.sendMessage(message);
                                    }
                                    else{

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        };
                    }.start();
                        //转为UTF-8编码后显示在编辑框中

                        Log.i("AAA",textdata);

                    } catch(Exception e){
                        e.printStackTrace();
                    }


            }
        }
    }
    int getPackageHeadbyte(int x,int y,int a[]){
        int res=0;
        for(int i=x;i>=y;i--){
            if(a[i]==1){
                res= (res+ (int)Math.pow(2,(x-i)));
            }
        }

        return res;
    }
    //用线程发送数据



    class ThreadSendData extends Thread{
        public void run(){

            try {
                final String result = jsonObject.toString();

                Log.i("AAA", result);

                byte[] JsonData = new byte[65536];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    JsonData = result.getBytes(StandardCharsets.UTF_8);
                }
                int len = JsonData.length;
                //用输出流发送数据
                FireByte = new byte[len+12];
                int bufferlength = len;

                int[] x = new int[33];
                for (int i = 0; i < 32; i++) x[i] = 0;
                int idx = 31;

                while ((bufferlength > 0)) {
                    if ((bufferlength & 1) == 1) {
                        x[idx] = 1;
                    }
                    idx--;
                    bufferlength >>= 1;
                }
                // 处理包头

                FireByte[0] = (byte) getPackageHeadbyte(7, 0, x);

                FireByte[1] = (byte) getPackageHeadbyte(15, 8, x);

                FireByte[2] = (byte) getPackageHeadbyte(23, 16, x);

                FireByte[3] = (byte) getPackageHeadbyte(31, 24, x);

                for (int i = 4; i < 12; i++) FireByte[i] = (byte) 0;


                for (int i = 0; i < JsonData.length; i++) FireByte[i + 12] = JsonData[i];
                new Thread() {
                    @Override
                    public void run() {

                        BufferedWriter outputStream = null;
                        try {
                            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("AAA", FireByte.toString());
                        try {
                            String ss=new String(FireByte,"utf-8");

                            outputStream.write(ss);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //防止服务端read方法读阻塞
                    }
                }.start();


                //发送数据之后会自动断开连接，所以，恢复为最初的状态
                //有个坑要说一下，因为发送完数据还得等待服务器返回，所以，不能把Socket也注销掉



            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("AAA","发送完毕");
        }
    }



}
