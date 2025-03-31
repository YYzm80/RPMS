package com.example.util;

import java.util.concurrent.ConcurrentHashMap;

public class OnlineUserUtils {
    private static final ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();

    /**
     * 添加在线用户
     * @param username 用户名
     * @param sessionId JWT令牌
     */
    public static void addOnlineUser(String username, String sessionId) {
        onlineUsers.put(username, sessionId);
    }

    /**
     * 移除在线用户
     * @param username 用户名
     */
    public static void removeOnlineUser(String username) {
        onlineUsers.remove(username);
    }

    /**
     * 获取在线用户的sessionId
     * @param username 用户名
     * @return sessionId 如果用户在线，返回sessionId，否则返回null
     */
    public static String getOnlineUserSessionId(String username) {
        return onlineUsers.get(username);
    }

    /**
     * 判断用户是否在线
     * @param username 用户名
     * @return  如果用户在线返回true，否则返回false
     */
    public static boolean isUserOnline(String username) {
        return onlineUsers.containsKey(username);
    }
}
