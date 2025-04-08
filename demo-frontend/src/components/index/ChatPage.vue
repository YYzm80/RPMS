<script setup>
import { ref } from 'vue';
import { onBeforeRouteLeave } from 'vue-router'
import { ElInput, ElButton, ElCard, ElMessage, ElScrollbar, ElPopover, ElTag } from 'element-plus';
import smile from '../../assets/smile.svg';
import {webSocketUrl} from "@/stores/commonAPI";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
const message = ref('')
const responseText = ref('')
const onlineUsers = ref([]) // 新增：在线用户列表
let ws = null

// 加入聊天室
const join = () => {
  const url = webSocketUrl + user.username
  ws = new WebSocket(url)
  ws.onmessage = (event) => {
    // console.log(event.data)
    if (event.data.startsWith('USER_LIST:')) {
      // 更新在线用户列表
      onlineUsers.value = event.data.replace('USER_LIST:', '').split(',')
    } else {
      responseText.value += event.data
    }
  }
  ws.onopen = () => {
    responseText.value += "建立 websocket 连接... \n\r"
  }
  ws.onclose = () => {
    onlineUsers.value = onlineUsers.value.filter(user => user !== user.username)
    responseText.value += `用户[${user.username}] 已经离开聊天室! \n\r`
    responseText.value += "关闭 websocket 连接.\n\r"
  }
}

// 发送消息
const send = () => {
  if (!ws || ws.readyState !== WebSocket.OPEN) {
    ElMessage.warning("WebSocket 连接没有建立成功！")
    return
  }
  ws.send(message.value.trim() + '\n\r')
  message.value = ''
}

// 在组件卸载前关闭 WebSocket 连接
onBeforeRouteLeave(() => {
  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.close()
  }
})

// 插入表情
const insertEmoji = (emoji) => {
  message.value += emoji
}

// 表情列表
const emojis = ['😀', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥵', '🥶', '😱', '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶', '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱', '😴', '🤤', '😪', '😵', '🤐', '🥴', '🤢', '🤮', '🤧', '😷', '🤒', '🤕', '🤑', '🤠', '😈', '👿', '👹', '👺', '🤡', '💩', '👻', '💀', '☠️', '👽', '👾', '🤖', '🎃', '😺', '😸', '😹', '😻', '😼', '😽', '🙀', '😿', '😾']

join()
</script>

<template>
  <ElCard class="chat-container">
    <h3>在线聊天室</h3>
    <el-scrollbar style="height: 420px">
      <div class="response-text" v-text="responseText"></div>
    </el-scrollbar>
    <div class="chat-toolbar">
      <ElPopover
        placement="top-start"
        width="410"
        trigger="hover"
      >
        <template #reference>
          <ElButton type="text">
            <el-image :src="smile"/>
          </ElButton>
        </template>
        <div class="emoji-picker">
          <span
            v-for="emoji in emojis"
            :key="emoji"
            @click="insertEmoji(emoji)"
            class="emoji-item"
          >{{ emoji }}</span>
        </div>
      </ElPopover>
    </div>
    <div class="chat-input">
      <ElInput
        v-model="message"
        placeholder="输入消息..."
        @keyup.enter="send"
        show-word-limit
        type="textarea"
        resize="none"
        :rows="2"
        maxlength="1000"
        class="message-input"
      />
      <ElButton type="primary" @click="send">发送消息</ElButton>
    </div>
  </ElCard>
  <div class="user-list">
    <h4>在线用户</h4>
    <div class="user-tags">
      <ElTag v-for="user in onlineUsers" :key="user" type="info">{{ user }}</ElTag>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
  background-color: #f0f2f5;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.user-list {
  position: absolute;
  top: 80px;
  right: 90px;
  background-color: #6dd766;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-list h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #333333;
}

.user-tags {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.chat-toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 8px;
}

.emoji-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  width: 400px; /* 调整宽度以容纳每行10个表情 */
}

.emoji-item {
  cursor: pointer;
  font-size: 20px;
  width: 25px; /* 设置固定宽度，确保每行展示10个表情 */
  text-align: center;
}

.emoji-item:hover {
  transform: scale(1.2);
}

.response-text {
  width: 96%;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
  padding: 15px;
  height: 380px;
  white-space: pre-wrap;
  text-align: left;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-size: 14px;
  color: #333333;
}

.chat-input {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.username-input, .message-input {
  flex: 1;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.el-button--primary {
  background-color: #007bff;
  border-color: #007bff;
  color: #ffffff;
  border-radius: 8px;
}

.el-button--danger {
  background-color: #dc3545;
  border-color: #dc3545;
  color: #ffffff;
  border-radius: 8px;
}
</style>