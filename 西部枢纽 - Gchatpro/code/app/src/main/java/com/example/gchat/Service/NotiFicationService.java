package com.example.gchat.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.core.app.NotificationCompat;

import com.example.gchat.ApplyFriend;
import com.example.gchat.R;
import com.google.android.exoplayer2.util.Log;

/**
 * User: xiahao
 * DateTime: 2022/4/20 18:04
 * Description:好友请求通知
 */
public class NotiFicationService extends Service {
    public static Handler MessageServiceHandler;
    public NotiFicationService() {
    }

    @Override
    public void onCreate() {
        // 读取好友申请队列
        super.onCreate();
        MessageServiceHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 3:
                        Log.d("AAA","进入消息");
                        PendingIntent pendingIntent = PendingIntent.getActivity(NotiFicationService.this, 0, new Intent(NotiFicationService.this, ApplyFriend.class), 0);
                        // 点击通知跳转到ApplyFriend 界面
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        final String CHANNEL_ID = "channel_id_1";
                        final String CHANNEL_NAME = "channel_name_1";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            //只在Android O之上需要渠道
                            Log.d("AAA","消息渠道建立成功！");
                            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
                            //通知才能正常弹出
                            manager.createNotificationChannel(notificationChannel);
                        }

                        Notification notification = new NotificationCompat.Builder(NotiFicationService.this,CHANNEL_ID) // 构建的消息渠道
                                .setContentTitle("好友通知")
                                .setContentText("你收到一条好友请求!")
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launche)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launche))
                                .setContentIntent(pendingIntent)
                                .setLights(Color.GREEN, 1000, 1000)
                                .setAutoCancel(true)
                                .build();
                        manager.notify(1,notification);
                        break;
                    default:
                        break;
                }
            }


        };
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}