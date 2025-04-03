package com.example.controller.webSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.example.util.WebSocketUtils.ONLINE_USER_SESSIONS;
import static com.example.util.WebSocketUtils.sendMessageAll;


@RestController
@ServerEndpoint("/api/online-chat/{username}")
public class ChatServerEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(ChatServerEndpoint.class);

    @OnOpen
    public void openSession(@PathParam("username") String username, Session session) {
        ONLINE_USER_SESSIONS.put(username, session);
        String userList = "USER_LIST:" + ONLINE_USER_SESSIONS.keySet().toString()
                .replace("[", "").replace("]", "");
        String message = "欢迎用户[" + username + "] 来到聊天室！\n\r";
        logger.info("用户登录："+message);
        sendMessageAll(userList);
        sendMessageAll(message);
    }

    @OnMessage
    public void onMessage(@PathParam("username") String username, String message) {
        logger.info("发送消息："+message);
        sendMessageAll("[" + username + "] : " + message);
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session) {
        //当前的Session 移除
        ONLINE_USER_SESSIONS.remove(username);
        //并且通知其他人当前用户已经离开聊天室了
        String userList = "USER_LIST:" + ONLINE_USER_SESSIONS.keySet().toString()
                .replace("[", "").replace("]", "");
        sendMessageAll(userList);
        sendMessageAll("用户[" + username + "] 已经离开聊天室了！\n\r");
        try {
            session.close();
        } catch (IOException e) {
            logger.error("onClose error",e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            logger.error("onError exception",e);
        }
        logger.info("Throwable msg "+throwable.getMessage());
    }
}
