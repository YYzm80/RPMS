<script setup>

import router from "@/router";
import {ref} from "vue";

const time = ref(5)

setInterval(() => {
  time.value--
}, 1000)

setTimeout(() => {
  router.push("/index")
}, 5000)

</script>

<template>
  <el-result
          icon="warning"
          title="403"
          sub-title="对不起，您无权限访问该页面！"
  >
    <template #extra>
      <span style="font-family: -apple-system,serif">{{ time }}秒后将自动跳转至首页</span>
      <el-link :underline="false" @click="router.push('/index')">直接返回</el-link>
    </template>
  </el-result>
</template>

<style scoped>

</style>