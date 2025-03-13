<script setup>

// 从URL参数中获取支付信息
import {post} from "@/net";
import {ElMessage} from "element-plus";
import {ref} from "vue";

const urlParams = new URLSearchParams(window.location.search);
// console.log(urlParams.get('sessionId'))
const sessionId = urlParams.get('sessionId');
// console.log(sessionId)
const success = ref(false)

// 自动提交表单
setTimeout(() => {
  post('api/payment/confirm', sessionId, () => {
    success.value = true
  })
}, 10000);
</script>

<template>
  <div>
    <p v-if="!success">支付处理中，请稍候...</p>
    <form>
      <input type="hidden" name="sessionId" v-model="sessionId">
    </form>
    <el-result
            icon="success"
            title="支付成功"
            sub-title="感谢您本次支付!欢迎下次使用"
            v-if="success"
    >
    </el-result>
  </div>
</template>

<style scoped>

</style>