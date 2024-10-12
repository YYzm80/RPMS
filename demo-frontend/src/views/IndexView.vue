<script setup>
import {logout} from "@/net";
import router from "@/router";
import logo from "@/assets/logo.png"
import {Document, Monitor, School, Setting, User} from "@element-plus/icons-vue";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

function userLogout() {
  logout(() => router.push("/"))
}

// const form = reactive({
//   city_id: '',
// })

// const weather_store = weatherStore()
// const location_store = locationStore()
// const active = ref(0);

// const weather = () => {
//   get('/api/location/get-location',
//           (adcode) => {
//             location_store.api.location = adcode
//             form.city_id = location_store.api.location.adcode
//             getWeather()
//           }, () => {
//             ElMessage.warning('获取位置信息失败，请检查网络')
//           })
// }

// const getWeather = () => {
//   if (form.city_id) {
//     post(`/api/weather/cityId?city_id=${form.city_id}`, {}, (data) => {
//       weather_store.api.weather = data
//       active.value = 1
//     })
//   }
// }

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;flex-direction: row">
    <div class="menu">
      <el-row class="tac">
        <el-col style="text-align: center">
          <el-image :src="logo" :fit="'cover'" style="width: 150px;margin-top: 10px"></el-image>
        </el-col>
        <el-col>
          <el-menu
                  active-text-color="#6676f6"
                  background-color="#304156"
                  default-active="1"
                  text-color="white"
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <Document/>
                </el-icon>
                <span>赛事中心</span>
              </template>
              <el-menu-item index="1-1" @click="router.push('/index/project')" v-if="user.role === 'admin'">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>比赛项目</span>
              </el-menu-item>
              <el-menu-item index="1-2" @click="router.push('/index')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>比赛列表</span>
              </el-menu-item>
              <el-menu-item index="1-3" @click="router.push('/index/myArrange')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>我的比赛安排</span>
              </el-menu-item>
              <el-menu-item index="1-4" @click="router.push('/index/myScore')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>我的比赛成绩</span>
              </el-menu-item>
              <el-menu-item index="1-5" @click="router.push('/index/score')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>成绩查询</span>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="2" @click="router.push('/index/manager')" v-if="user.role === 'admin'">
              <template #title>
                <el-icon>
                  <User/>
                </el-icon>
                <span>用户管理</span>
              </template>
            </el-menu-item>
            <el-menu-item v-if="user.role === 'admin'" index="3" @click="router.push('/index/registration')">
              <template #title>
                <el-icon>
                  <Monitor/>
                </el-icon>
                <span>报名管理</span>
              </template>
            </el-menu-item>
            <el-menu-item v-if="user.role === 'student' || user.role === 'teacher'" index="3" @click="router.push('/index/myRegistration')">
              <template #title>
                <el-icon>
                  <Monitor/>
                </el-icon>
                <span>我的报名</span>
              </template>
            </el-menu-item>
            <el-sub-menu index="4"  v-if="user.role === 'admin'">
              <template #title>
                <el-icon>
                  <School/>
                </el-icon>
                <span>学校信息管理</span>
              </template>
              <el-menu-item index="4-1" @click="router.push('/index/academy')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>学院</span>
              </el-menu-item>
              <el-menu-item index="4-2" @click="router.push('/index/class')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>班级</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-col>
      </el-row>
    </div>
    <div class="content">
      <div class="header">
        <el-row style="width: 100%;">
          <el-col :span="16">
            <span
                    style="font-size: 18px;font-weight: bolder;position: relative;top: 3px">欢迎{{user.username}}进入运动会管理平台！</span>
          </el-col>
          <el-col :span="8">
            <el-button @click="userLogout" type="danger" style="line-height: 50px">退出登录</el-button>
          </el-col>
        </el-row>
      </div>
      <el-scrollbar max-height="1600px">
        <router-view v-slot="{ Component }">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component" style="height: 100%;width: 100%;"/>
          </transition>
        </router-view>
      </el-scrollbar>
    </div>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  height: 50px;
  box-shadow: 10px 2px 16px -8px rgba(0, 0, 0, 0.75);
  padding-top: 9px;
  text-align: center;
}

.content {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.menu {
  height: 100vh;
  width: 210px;
  background-color: #304156;
  color: white;
  box-shadow: 1px 1px 16px -6px rgba(0, 0, 0, 0.75);

}
</style>