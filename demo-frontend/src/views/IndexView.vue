<script setup>
import {logout} from "@/net";
import router from "@/router";
import {
  Comment,
  Document,
  House,
  More,
  OfficeBuilding,
  PriceTag,
  Suitcase
} from "@element-plus/icons-vue";
import {ref} from "vue";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
const loading = ref(false)

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
</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;flex-direction: column">
    <div class="header">
      <div class="title">
        <span style="font-size: 28px;font-weight: bold;font-family: 'Segoe UI', serif;color: white">title</span>
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
          <el-menu-item index="2" @click="router.push('/index/meeting')">
            <el-icon>
              <OfficeBuilding/>
            </el-icon>
            <span>title</span>
          </el-menu-item>
          <el-menu-item index="3" @click="router.push('/index/resume')"
                        v-if="user.role === 'student' || user.role === 'admin'">
            <el-icon>
              <document/>
            </el-icon>
            <span>text</span>
          </el-menu-item>
          <el-menu-item index="4" @click="router.push('/index/company')">
            <el-icon>
              <Suitcase/>
            </el-icon>
            <span>text</span>
          </el-menu-item>
          <el-sub-menu index="5" v-if="user.role !== 'student' && user.role !== 'counsellor'">
            <template #title>
              <el-icon>
                <More/>
              </el-icon>
              <span>更多</span>
            </template>
            <el-menu-item index="5-1"
                          style="padding: 0 15px 0 25px;"
                          @click="router.push('/index/tags')"
                          v-if="user.role === 'admin'">
              <el-icon>
                <PriceTag/>
              </el-icon>
              <span>text</span>
            </el-menu-item>
            <el-menu-item index="5-2" style="padding: 0 15px 0 25px;" @click="router.push('/index/center')">
              <el-icon>
                <Comment/>
              </el-icon>
              <span>text</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div style="width: 200px;">
        <el-popover
                :width="80"
                popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
        >
          <template #reference>
            <el-avatar
                    style="translate: 20px 9px"
                    :src="getImgSrc(user.avatar)"/>
          </template>
          <template #default>
            <div
                    class="rich-content"
                    style="display: flex; gap: 12px; flex-direction: column; text-align: center"
            >
              <el-avatar
                      :size="50"
                      :src="getImgSrc(user.avatar)"
                      style="margin:auto"
              />
              <div>
                <p v-if="user === null" style="margin: auto; font-weight: 500">
                  未登录
                </p>
                <p v-if="user !== null" style="margin: auto; font-weight: 500">
                  {{ user.username }}
                </p>
              </div>
              <el-button v-if="user === null"
                         type="success"
                         size="small"
                         @click="router.push('/')">登录/注册
              </el-button>
              <el-button v-if="user !== null"
                         type="primary"
                         size="small"
                         @click="router.push('/index/personal')">个人中心
              </el-button>
              <el-button v-if="user !== null"
                         type="danger"
                         size="small"
                         @click="userLogout"
                         style="margin: 0" v-loading="loading">退出登录
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
  width: 1200px;
}
</style>