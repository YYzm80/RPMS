package com.example.common.aop;

import com.example.entity.dto.common.LogDTO;
import com.example.mapper.LogMapper;
import com.example.util.HttpContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Order(5)
@Component
public class WebLogAspect {

    ThreadLocal<LogDTO> logDTOThreadLocal = new ThreadLocal<>();
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    @Autowired
    private LogMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 第一个*表示返回任何类型,com.example.controller下任何类,任何方法,任何参数
     * 也可以加入参数限定例如com.example.controller.*.*(..)&&args(name,..)
     * 下面那中表示方法也是对的,表示com.example.controller.下面任何子包下任何方法,任何参数
     **/
    @Pointcut("execution(public * com.example.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Map<String, Object> logInfo = getLogInfo(joinPoint, request);

        // 创建LogDTO对象并存储在ThreadLocal中
        LogDTO logDTO = new LogDTO();
        logDTO.setType(request.getMethod());
        logDTO.setUserId(null); // 根据实际情况设置用户ID
        logDTO.setIp(HttpContextUtils.getIpAddress());

        try {
            logDTO.setDetail(objectMapper.writeValueAsString(logInfo));
        } catch (Exception e) {
            log.error("Failed to convert log info to JSON", e);
        }

        logDTOThreadLocal.set(logDTO);
    }

    @NotNull
    private static Map<String, Object> getLogInfo(JoinPoint joinPoint, HttpServletRequest request) {
        // 构建JSON格式的日志信息
        Map<String, Object> logInfo = new HashMap<>();
        logInfo.put("URL", request.getRequestURL().toString());
        logInfo.put("CLASS_METHOD", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logInfo.put("ARGS", Arrays.toString(joinPoint.getArgs()));
        return logInfo;
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 获取之前存储的LogDTO对象
        LogDTO logDTO = logDTOThreadLocal.get();
        if (logDTO != null) {
            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("RESPONSE", ret);
            logInfo.put("SPEND_TIME", System.currentTimeMillis() - startTime.get());

            try {
                // 合并日志信息
                Map<String, Object> existingLogInfo = objectMapper.readValue(logDTO.getDetail(), Map.class);
                existingLogInfo.putAll(logInfo);
                logDTO.setDetail(objectMapper.writeValueAsString(existingLogInfo));

                // 插入到数据库
                mapper.insert(logDTO);
            } catch (Exception e) {
                log.error("Failed to convert log info to JSON or insert log to database", e);
            } finally {
                // 清理ThreadLocal
                logDTOThreadLocal.remove();
            }
        }
    }



}
