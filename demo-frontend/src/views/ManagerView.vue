<script setup>

import router from "../router";
import {
  ArrowLeftBold,
  DataAnalysis,
  DataBoard,
  House,
  Monitor,
  Odometer,
  Setting,
  User
} from "@element-plus/icons-vue";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;flex-direction: row">
    <el-scrollbar class="menu" height="100%">
      <el-row class="tac">
        <el-col style="font-size: 16px;text-align: center;margin: 20px 0;font-weight: bolder">
          <el-icon style="transform: translateY(7px);color: #6676f6;font-size: 25px;margin-right: 3px">
            <Odometer/>
          </el-icon>
          <span>物业管理系统</span>
        </el-col>
        <el-col>
          <el-menu
                  active-text-color="#6676f6"
                  background-color="#304156"
                  default-active="1"
                  text-color="white"
          >
            <el-menu-item index="1" @click="router.push('/manager')">
              <el-icon>
                <house/>
              </el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="2" @click="router.push('/manager/user')">
              <el-icon>
                <User/>
              </el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-sub-menu index="3">
              <template #title>
                <el-icon>
                  <Monitor/>
                </el-icon>
                <span>基本信息管理</span>
              </template>
              <el-menu-item index="3-1" @click="router.push('/manager/property')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>房产信息</span>
              </el-menu-item>
              <el-menu-item index="3-2" @click="router.push('/manager/announce')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>公告管理</span>
              </el-menu-item>
              <el-menu-item index="3-3" @click="router.push('/manager/property-type')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>房产类型</span>
              </el-menu-item>
              <el-menu-item index="3-4" @click="router.push('/manager/complaint-type')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>投诉类型</span>
              </el-menu-item>
              <el-menu-item index="3-5" @click="router.push('/manager/repair-type')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>报修类型</span>
              </el-menu-item>
              <el-menu-item index="3-6" @click="router.push('/manager/pay-type')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>账单类型</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="4">
              <template #title>
                <el-icon>
                  <data-board/>
                </el-icon>
                <span>办公板</span>
              </template>
              <el-menu-item index="4-1" @click="router.push('/manager/complaint')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>投诉处理</span>
              </el-menu-item>
              <el-menu-item index="4-2" @click="router.push('/manager/repair')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>报修处理</span>
              </el-menu-item>
              <el-menu-item index="4-3" @click="router.push('/manager/payment')">
                <el-icon>
                  <setting/>
                </el-icon>
                <span>缴费管理</span>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="5" @click="router.push('/manager/report')">
              <el-icon>
                <data-analysis/>
              </el-icon>
              <span>月度工作报告</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </el-scrollbar>
    <div class="content">
      <div class="header">
        <el-row style="width: 100%;">
          <el-col :span="22">
            <span style="font-size: 18px;font-weight: bolder;position: relative;line-height: 50px">
              欢迎{{ user.username }}进入物业管理系统！</span>
          </el-col>
          <el-col :span="2" style="line-height: 50px">
            <el-button @click="router.push('/index')" type="primary">
              <el-icon>
                <arrow-left-bold/>
              </el-icon>
              返回前台
            </el-button>
          </el-col>
        </el-row>
      </div>
      <div class="innerContent">
        <el-scrollbar max-height="1600px">
          <router-view v-slot="{ Component }">
            <transition name="el-fade-in-linear" mode="out-in">
              <component :is="Component" style="height: 100%;width: 100%;"/>
            </transition>
          </router-view>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  height: 50px;
  box-shadow: 0 2px 14px -4px rgba(0, 0, 0, 0.75);
  text-align: center;
}

.content {
  height: 100%;
  width: 85%;
  display: flex;
  flex-direction: column;
}

.innerContent {
  height: 100%;
  width: 100%;
}

.menu {
  height: 100vh;
  width: 15%;
  background-color: #304156;
  color: white;
  box-shadow: 1px 1px 16px -6px rgba(0, 0, 0, 0.75);

}
</style>