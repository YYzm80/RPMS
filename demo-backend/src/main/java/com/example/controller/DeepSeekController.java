package com.example.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.entity.RestBean;
import com.example.entity.dto.common.DeepSeekSession;
import com.example.entity.vo.response.DeepSeekResult;
import com.example.util.DeepSeekUtil;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/deepseek")
public class DeepSeekController {

    // 每秒允许7个请求
    private static final RateLimiter rateLimiter = RateLimiter.create(7.0);

    @Operation(summary = "与DeepSeek对话接口")
    @GetMapping("/chat/{userId}/{content}")
    public RestBean<DeepSeekResult> chat(
            @PathVariable("userId") String userId,
            @PathVariable("content") String content) {

        // 尝试获取令牌，如果获取不到则返回失败
        if (!rateLimiter.tryAcquire()) {
            log.warn("Request rate limit exceeded for user: {}", userId);
            return null;
        }

        DeepSeekResult res = null;
        try {
            DeepSeekUtil.initSession(userId); // 初始化会话
            DeepSeekSession session = DeepSeekUtil.getSession(userId);

            Generation gen = new Generation();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(content).build();
            res = DeepSeekUtil.streamCallWithMessage(gen, userMsg, session);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error(e.getMessage());
        }
        return RestBean.success(res);
    }

    @Operation(summary = "清除会话接口")
    @GetMapping("/chat/clear/{userId}")
    public RestBean<String> clear(@PathVariable("userId") String userId) {
        DeepSeekUtil.clearSession(userId);
        return RestBean.success("本次对话清除成功");
    }
}
