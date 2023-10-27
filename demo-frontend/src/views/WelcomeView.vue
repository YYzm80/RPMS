<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">

    <div style="flex: 1">
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="https://pica.zhimg.com/v2-905513990381422285bd36ce1cbdc4f1_r.jpg?source=2c26e567"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到我们的XX学习平台
      </div>
      <div style="margin-top: 10px">
        在这里你可以学习到Java，如何搭建网站，并且与Java之父密切交流
      </div>
    </div>
    <div style="width: 400px;background-color: white;z-index: 1">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>

  </div>
</template>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>