package com.example.entity.dto.common;

import com.alibaba.dashscope.common.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class DeepSeekSession {
    private String userId;
    private StringBuilder reasoningContent = new StringBuilder();
    private StringBuilder finalContent = new StringBuilder();
    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private boolean isFirstPrint = true;
}
