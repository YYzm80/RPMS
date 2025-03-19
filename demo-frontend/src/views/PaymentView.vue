<script setup>

import {put} from "@/net";
import {ref} from "vue";

// 从URL参数中获取支付信息
const urlParams = new URLSearchParams(window.location.search);
// console.log(urlParams.get('sessionId'))
const sessionId = urlParams.get('sessionId');
// console.log(sessionId)
const success = ref(false)
const fail = ref(false)
const failMessage = ref('')

// 自动提交表单
setTimeout(() => {
  put('api/payment/confirm', sessionId, () => {
    success.value = true
  }, (message) => {
      fail.value = true
      failMessage.value = message
  })
}, 3000);
</script>

<template>
  <div>
    <p v-if="!success && !fail">支付处理中，请稍候...</p>
    <form>
      <input type="hidden" name="sessionId" v-model="sessionId">
    </form>
    <el-result
            icon="success"
            title="支付成功"
            sub-title="感谢您本次支付,欢迎下次使用!"
            v-if="success"
    >
    </el-result>
    <el-result
            icon="error"
            title="支付失败"
            :sub-title="failMessage + ',请稍后再试!'"
            v-if="fail"
    >
    </el-result>
  </div>
</template>

<style scoped>

</style>