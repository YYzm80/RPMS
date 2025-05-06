<script setup>
import {nextTick, onBeforeUnmount, ref} from 'vue';
import {ElButton, ElCard, ElCollapse, ElCollapseItem, ElInput, ElMessage} from 'element-plus';
import {get} from "@/net";
import VueMarkdown from 'vue3-markdown-it';

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
const messages = ref([])
messages.value.push({content: '欢迎使用ai小助手！有什么能够帮您的吗？', sender: 'bot'})
const newMessage = ref('')
const loading = ref(false)
const times = ref(0)

const sendMessage = () => {
  if (newMessage.value.trim() && loading) {
    loading.value = true
    times.value = 0
    let time = setInterval(() => {times.value++;}, 1000)
    messages.value.push({text: newMessage.value, sender: 'user'})
    get(`api/deepseek/chat/${user.uid}/${newMessage.value}`,
      (res) => {
        // console.log(res)
        messages.value.push({reasoning: res.reasoning, content: res.content, sender: 'bot'}) // 使用marked解析Markdown
        loading.value = false
        clearInterval(time)
        nextTick(() => {
          const scrollbar = document.querySelector('.chat-messages .el-scrollbar__wrap')
          if (scrollbar) {
            scrollbar.scrollTop = scrollbar.scrollHeight
          }
        })
      })
    newMessage.value = ''
    nextTick(() => {
      const scrollbar = document.querySelector('.chat-messages .el-scrollbar__wrap')
      if (scrollbar) {
        scrollbar.scrollTop = scrollbar.scrollHeight
      }
    })
  }
}

// 添加清除对话的函数
const clearChat = () => {
  get(`api/deepseek/chat/clear/${user.uid}`, (message) => {
    ElMessage.success(message)
  })
}

// 在组件卸载前调用清除对话的函数
onBeforeUnmount(() => {
  clearChat()
})
</script>

<template>
  <ElCard class="chat-container">
    <div class="chat-messages">
      <el-scrollbar height="90%">
        <ElCard
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.sender]"
            shadow="hover"
            body-style="padding:0"
        >
          <div v-if="message.sender === 'bot'">
            <ElCollapse v-model="activeNames" accordion v-if="message.reasoning !== undefined">
              <ElCollapseItem title="思考过程" name="1">
                <vue-markdown :source="message.reasoning"></vue-markdown>
              </ElCollapseItem>
            </ElCollapse>
            <vue-markdown :source="message.content"></vue-markdown>
          </div>
          <vue-markdown v-else :source="message.text"></vue-markdown>
        </ElCard>
        <el-text type="info" v-if="times !== 0">响应时间：{{times}}s</el-text>
      </el-scrollbar>
    </div>
    <div class="chat-input">
      <ElInput
          v-model="newMessage"
          placeholder="输入消息..."
          @keyup.enter="sendMessage"
          type="textarea"
          resize="none"
          :rows="2"
          show-word-limit
          maxlength="1000"
          :disabled="loading"
      />
      <ElButton type="primary" @click="sendMessage" :loading="loading">发送</ElButton>
    </div>
  </ElCard>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
  max-width: 1000px;
  margin: 0 auto;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 10px;
  height: calc(100vh - 200px);
}

.message {
  margin-bottom: 8px;
  padding: 6px 12px;
  border-radius: 15px;
  max-width: 70%;
  word-wrap: break-word;
  width: fit-content;
}

.message.user {
  background-color: #007bff;
  color: white;
  margin-left: auto;
  text-align: left;
}

.message.bot {
  background-color: #f1f1f1;
  color: black;
  margin-right: auto;
  text-align: left;
}

.chat-input {
  display: flex;
  gap: 10px;
  position: sticky;
  bottom: 0;
  background-color: white;
  padding: 10px 0;
  height: 80px;
}

.chat-input .el-input {
  flex: 1;
}
</style>
