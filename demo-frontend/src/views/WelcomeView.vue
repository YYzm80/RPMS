<script setup>

</script>

<template>
  <div>
    <div class="welcome">
      <div class="welcome-left" style="width: 600px;height: 100%">
        <el-image style="width: 100%;height: 100%" fit="cover"
                  src="https://bpic.588ku.com/back_pic/06/16/57/8162f7bab2cf49c.jpg"/>
        <div class="welcome-title">
          <div style="font-size: 26px;font-weight: bold">欢迎使用住宅物业管理系统</div>
          <div style="margin-top: 16px;font-size: 13px">
            在线办公，让工作更简单
          </div>
        </div>

      </div>
      <div style="width: 400px;height: 100%;background-color: rgba(255,255,255)">
        <router-view v-slot="{ Component }">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component" style="height: 100%"/>
          </transition>
        </router-view>
      </div>
    </div>
    <div class="footer">
      @2025 版权所有
      <el-link href="http://8.155.59.231/">yz820.com</el-link>
    </div>
  </div>
</template>

<style scoped>

.welcome {
  width: 1000px;
  height: 550px;
  margin: 80px auto auto;
  display: flex;
  flex-direction: row;
  box-shadow: var(--el-box-shadow-dark);
}

.welcome-title {
  position: relative;
  bottom: 100px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

.footer {
  margin-top: 40px;
  text-align: center;
  cursor: pointer;
}

.footer:hover {
  color: #776b3d;
}
</style>