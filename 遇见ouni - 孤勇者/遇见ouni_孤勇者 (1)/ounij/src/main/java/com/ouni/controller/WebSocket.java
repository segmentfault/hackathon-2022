package com.ouni.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ouni.domain.ChatRecord;
import com.ouni.mapper.ChatRecordMapper;
import com.ouni.utils.RedisUtils;
import com.ouni.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    private static RedisUtils redisTemplate;
    private static ChatRecordMapper chatRecordMapper;

    private static Map<Integer, WebSocket> clients = new ConcurrentHashMap<>();
    private Session session;
    private int userId;

    /**
     * 建立连接
     *OnOpen 表示有浏览器链接过来的时候被调用
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") int userId, Session session) {

        log.info("现在来连接的客户id：" + session.getId() + "用户id：" + userId);
        this.session = session;
        this.userId = userId;
        redisTemplate = SpringUtil.getBean(RedisUtils.class);
        chatRecordMapper = SpringUtil.getBean(ChatRecordMapper.class);
        try {
            clients.put(userId, this);
            while (Boolean.TRUE.equals(redisTemplate.hasKey("ws_userId:"+userId)) && redisTemplate.llen("ws_userId:"+userId)>0){
                ChatRecord record = (ChatRecord)redisTemplate.llpop("ws_userId:"+userId);
                log.info("用户{}上线了，消费离线消息{}...",userId,record);
                sendMessageTo(record, userId,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * OnError 表示有错误发生，比如网络断开了等等
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误" + error.getMessage());
    }

    /**
     * OnClose 表示浏览器发出关闭请求的时候被调用
     */
    @OnClose
    public void onClose() {
        log.info("连接已关闭............");
        clients.remove(userId);
    }
    /**
     * 收到客户端的消息  message 是前端传来的
     * OnMessage 表示浏览器发消息的时候被调用
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message);
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("message");
            int fromuserId = Integer.parseInt(jsonObject.getString("userId"));
            int touserId = Integer.parseInt(jsonObject.getString("touserId"));
            String imgUrl = jsonObject.getString("imgUrl");
            ChatRecord record = new ChatRecord(fromuserId,touserId,new Date(),textMessage,imgUrl);
            if (record != null){
                sendMessageTo(record, touserId,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发生了错误了");
        }
    }

    public void sendMessageTo(ChatRecord record, int touser_id,boolean isRedis) {
        boolean sign = false;
        for (WebSocket item : clients.values()) {
            if (item.userId == touser_id) {
                item.session.getAsyncRemote().sendText(JSON.toJSONString(record));
                sign = true;
                break;
            }
        }
        if (!sign){ //用户不在线，就加入了缓存
            log.info("用户{},将{}加入缓存了.....",touser_id,record);
            redisTemplate.lrpush("ws_userId:"+ touser_id,record);
        }
        try {
            if (!isRedis){
                log.info("插入数据库.....，{}",record);
                chatRecordMapper.insert(record);
                log.info("插入数据库wanc.....");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

