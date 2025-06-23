package com.example.util;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.entity.dto.common.DeepSeekSession;
import com.example.entity.vo.response.DeepSeekResult;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DeepSeekUtil {

    private static final Map<String, DeepSeekSession> sessionMap = new ConcurrentHashMap<>();

    public static void initSession(String userId) {
        if (sessionMap.containsKey(userId)) {
            return;
        }
        DeepSeekSession session = new DeepSeekSession();
        session.setUserId(userId);
        sessionMap.put(userId, session);
    }

    public static DeepSeekSession getSession(String userId) {
        return sessionMap.get(userId);
    }

    public static void clearSession(String userId) {
        sessionMap.remove(userId);
    }

    public static DeepSeekResult handleGenerationResult(GenerationResult message, String userId) {
        String reasoning = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
        StringBuilder reasoningContent = sessionMap.get(userId).getReasoningContent();
        StringBuilder finalContent = sessionMap.get(userId).getFinalContent();
        boolean isFirstPrint = sessionMap.get(userId).isFirstPrint();

        if (!reasoning.isEmpty()) {

            reasoningContent.append(reasoning);
            if (isFirstPrint) {
                log.info("====================思考过程====================");
                isFirstPrint = false;
                sessionMap.get(userId).setFirstPrint(isFirstPrint);
            }
            log.info(reasoning);
        }

        if (!content.isEmpty()) {
            finalContent.append(content);
            if (!isFirstPrint) {
                log.info("\n====================完整回复====================");
                isFirstPrint = true;
                sessionMap.get(userId).setFirstPrint(isFirstPrint);
            }
            log.info(content);
        }
        DeepSeekResult result = new DeepSeekResult();
        result.setReasoning(reasoningContent.toString());
        result.setContent(finalContent.toString());
        return result;
    }

    public static GenerationParam buildGenerationParam(Message userMsg, String userId) {
        List<Message> messages = sessionMap.get(userId).getMessages();
        if (!messages.isEmpty()) {
            Message botMsg = Message.builder()
                    .role(Role.ASSISTANT.getValue())
                    .content(String.valueOf(sessionMap.get(userId).getFinalContent()))
                    .build();
            messages.add(botMsg);
        }
        messages.add(userMsg);
        sessionMap.get(userId).setMessages(messages);

        return GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("deepseek-r1-distill-qwen-32b")
                .messages(messages)
                // 不可以设置为"text"
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
    }

    public static DeepSeekResult streamCallWithMessage(Generation gen, Message userMsg, DeepSeekSession session, boolean isNeedContext)
            throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam param = buildGenerationParam(userMsg, session.getUserId());

        StringBuilder reasoningContent = session.getReasoningContent();
        StringBuilder finalContent = session.getFinalContent();
        reasoningContent.delete(0, reasoningContent.length());
        finalContent.delete(0, finalContent.length());
        Flowable<GenerationResult> result = gen.streamCall(param);
        result.blockingForEach(message -> handleGenerationResult(message, session.getUserId()));
        DeepSeekResult seekResult = handleGenerationResult(result.blockingFirst(), session.getUserId());
        if (isNeedContext) {
            sessionMap.get(session.getUserId()).setFinalContent(new StringBuilder(seekResult.getContent()));
        }
        return seekResult;
    }

}
