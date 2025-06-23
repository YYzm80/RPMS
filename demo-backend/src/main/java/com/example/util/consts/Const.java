package com.example.util.consts;

public class Const {
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    public static final String PAYMENT_SESSION = "payment:session:";
    public static final String LOCATION_API = "https://restapi.amap.com/v3/ip?key=077e7585e935dd09dcdf2098c801cad4";
    public static final String WEATHER_API = "https://restapi.amap.com/v3/weather/weatherInfo?key=077e7585e935dd09dcdf2098c801cad4";
    public static String UPLOAD_PATH = "D:/CodeDevelopment/uploaded/";
    public static final String FRONT_PATH = "http://localhost:8088/";

    // 角色
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_OWNER = "owner";

    // 类型分类
    public static final String TYPE_PAYMENT = "payment";
    public static final String TYPE_REPAIR = "repair";
    public static final String TYPE_COMPLIANT = "compliant";
    public static final String TYPE_PROPERTY = "property";

    // AI prompt
    public static final String PROMPT_ANNOUNCEMENT = "我现在需要你根据我写的公告标题来帮我生成一则在小区物业发布的公告，" +
            "要求贴合标题，字数在100到300字以内，符合一般小区公告格式，且符合富文本格式，你的回答中只包含公告内容，不包含其它的，以下是标题：";
}
