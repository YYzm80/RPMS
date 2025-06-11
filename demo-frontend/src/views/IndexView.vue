<script setup>
import {get, logout} from "@/net";
import router from "@/router";
import {
  Comment,
  Drizzling,
  House,
  ChatLineRound,
  More, MostlyCloudy,
  PartlyCloudy,
  Sunny
} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {locationStore} from "@/stores/locationStore";
import {weatherStore} from "@/stores/weatherStore";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
const weather_store = weatherStore()
const location_store = locationStore()
const loading = ref(false)
const form = reactive({
  city_id: '',
})

function userLogout() {
  loading.value = true
  logout(() => {
    loading.value = false
    router.push("/")
  })
}

const getImgSrc = (picName) => {
  if (picName === null) {
    return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  } else {
    return `http://localhost:8080/uploaded/${picName}`
  }
}

const weather = () => {
  // if (!location_store.api.location) {
  //   get('/api/location/get-location',
  //           (location) => {
  //             location_store.api.location = location
  //             form.city_id = location_store.api.location.adcode
  //             getWeather()
  //           }
  //   )
  // } else {
  //   getWeather()
  // }
  getWeather()
}

const getWeather = () => {
  if (!weather_store.api.weather) {
    get(`/api/weather/cityId/${500000}`, (data) => {
      weather_store.api.weather = data
    })
  }
}

weather()
</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;flex-direction: column">
    <div class="header">
      <div class="title">
        <span style="font-size: 23px;font-weight: bold;font-family: 'Segoe UI', serif;color: white">住宅物业</span>
      </div>
      <div class="menu">
        <el-menu
                active-text-color="#ffd04b"
                background-color="#535b64"
                default-active="1"
                text-color="#fff"
                mode="horizontal"
                style="height: 100%;"
        >
          <el-menu-item index="1" @click="router.push('/index')">
            <el-icon>
              <House/>
            </el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="2" v-if="user.role !== 'student' && user.role !== 'counsellor'">
            <template #title>
              <el-icon>
                <More/>
              </el-icon>
              <span>更多</span>
            </template>
            <el-menu-item index="2-1"
                          style="padding: 0 15px 0 25px;"
                          @click="router.push('/index/chat')"
                          v-if="user.role === 'admin'">
              <el-icon>
                <ChatLineRound/>
              </el-icon>
              <span>与物业AI小助手对话</span>
            </el-menu-item>
            <el-menu-item index="2-2" style="padding: 0 15px 0 25px;" @click="router.push('/index/online-chat')">
              <el-icon>
                <Comment/>
              </el-icon>
              <span>社区在线聊天室</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="weather">
        <div class="weather-item" v-if="weather_store.api.weather">
          <span class="weather-item-title">天气：</span>
          <span class="weather-item-content">{{ weather_store.api.weather.lives[0].weather }}
            <el-icon
                    v-if="weather_store.api.weather.lives[0].weather==='小雨'||weather_store.api.weather.lives[0].weather==='雨'">
                                    <Drizzling/>
                                </el-icon>
                                <el-icon v-if="weather_store.api.weather.lives[0].weather==='晴'">
                                    <Sunny/>
                                </el-icon>
                                <el-icon v-if="weather_store.api.weather.lives[0].weather==='阴'">
                                    <MostlyCloudy/>
                                </el-icon>
                                <el-icon v-if="weather_store.api.weather.lives[0].weather==='多云'">
                                    <PartlyCloudy/>
                                </el-icon>
          </span>
          <span class="weather-item-title" style="margin-left: 20px">温度：</span>
          <span class="weather-item-content">{{ weather_store.api.weather.lives[0].temperature }}°C</span>
        </div>
      </div>
      <div style="width: 200px;">
        <el-popover
                :width="80"
                popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
        >
          <template #reference>
            <el-avatar
                    style="translate: 20px 9px"
                    :src="getImgSrc(null)"
            />
          </template>
          <template #default>
            <div
                    class="rich-content"
                    style="display: flex; gap: 12px; flex-direction: column; text-align: center"
            >
              <el-avatar
                      :size="50"
                      style="margin:auto"
                      :src="getImgSrc(null)"
              />
              <div>
                <p style="margin: auto; font-weight: 500">
                  {{ user.username }}
                </p>
              </div>
              <el-button type="success"
                         size="small"
                         v-if="user.role !== 'owner' "
                         @click="router.push('/manager')">进入后台
              </el-button>
              <el-button type="primary"
                         size="small"
                         style="margin: 0"
                         @click="router.push('/index/personal')">个人中心
              </el-button>
              <el-button type="danger"
                         size="small"
                         @click="userLogout"
                         style="margin: 0" :loading="loading">退出登录
              </el-button>
            </div>
          </template>
        </el-popover>
      </div>
    </div>
    <div class="content">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  line-height: 50px;
  background-color: #535b64;
  display: flex;
  flex-direction: row;
}

.content {
  height: 100%;
  width: 100%;
  text-align: center;
}

.title {
  margin-left: 40px;
  width: 100px;
  line-height: 50px;
}

.menu {
  margin-left: 40px;
  height: 100%;
  width: 900px;
}

.weather {
  margin-left: auto;
  margin-right: 40px;
  width: 220px;
}

.weather-item {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #ffffff;
}

.weather-item-title {
  margin-right: 10px;
}
</style>