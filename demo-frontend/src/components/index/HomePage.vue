<script setup>

import {
  ArrowRightBold,
  CaretTop,
  Warning,
  CaretBottom} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get} from "@/net";
import router from "@/router";

const urls = ref([
  'src/assets/lunbo1.jpg',
  'src/assets/lunbo2.jpg',
  'src/assets/lunbo3.jpg',
  'src/assets/lunbo4.jpg',
])

const tableData = ref([])
const announcement = ref([])
const dialogAnnounceVisible = ref(false)

const getData = () => {
  get('api/static/getHomeData', (data) => {
    tableData.value = data
    // console.log(tableData.value)
  })
}

const getAnnouncement = (aid) => {
  get(`api/announce/aid/${aid}`, (data) => {
    announcement.value = data
    dialogAnnounceVisible.value = true
  })
}

getData()
</script>

<template>
  <el-scrollbar height="100%">
    <div style="display: flex;flex-direction:column;height: 100%;">
      <div class="header">
        <el-carousel :interval="4000" height="330px" trigger="click" type="card">
          <el-carousel-item v-for="url in urls" :key="url">
            <img v-lazy="url" alt="Carousel Image" style="width: 100%; height: 100%; object-fit: cover;"/>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="info">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最新公告
                <el-text type="danger">
                  new!
                </el-text>
              </span>
              <el-link :underline="false" @click="router.push('/index/announce') ">
                更多公告
                <el-icon>
                  <CaretTop/>
                </el-icon>
              </el-link>
            </div>
          </template>
          <div class="text">
            <el-table :data="tableData.announcements" style="width: 100%">
              <el-table-column label="标题" prop="title" align="center" width="500" show-overflow-tooltip>
                <template class="announce" #default="scope">
                  <span @click="getAnnouncement(scope.row.aid)">
                    {{ scope.row.title }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="发布时间" prop="publishTime" align="center"/>
            </el-table>
          </div>
        </el-card>
        <div style="display: flex;flex-direction: column;width: 200px;gap: 20px">
          <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
            <el-statistic :value="tableData.userCount">
              <template #title>
                <div style="display: inline-flex; align-items: center">
                  总入住人数
                  <el-tooltip
                          effect="light"
                          content="每月新增入住人数统计"
                          placement="top"
                  >
                    <el-icon style="margin-left: 4px" :size="12">
                      <Warning/>
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-statistic>
            <div class="statistic-footer">
              <div class="footer-item">
                <span>比上个月</span>
                <span class="green" v-if="tableData.lastMonthUserCount >= 0">{{ tableData.lastMonthUserCount }}
                  <el-icon>
                    <CaretTop/>
                  </el-icon>
                </span>
                <span class="red" v-else>{{ tableData.lastMonthUserCount }}
                  <el-icon>
                    <CaretBottom/>
                  </el-icon>
                </span>
              </div>
            </div>
          </el-card>
          <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
            <el-statistic :value="tableData.emptyPropertyCount">
              <template #title>
                <div style="display: inline-flex; align-items: center">
                  总空闲房产数
                  <el-tooltip
                          effect="light"
                          content="每月新增空闲房产数统计"
                          placement="top"
                  >
                    <el-icon style="margin-left: 4px" :size="12">
                      <Warning/>
                    </el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-statistic>
            <div class="statistic-footer">
              <div class="footer-item">
                <span>比上个月</span>
                <span class="green" v-if="tableData.lastMonthEmptyPropertyCount >= 0">
                  {{ tableData.lastMonthEmptyPropertyCount }}
                  <el-icon>
                    <CaretTop/>
                  </el-icon>
                </span>
                <span class="red" v-else>{{ tableData.lastMonthEmptyPropertyCount }}
                  <el-icon>
                    <CaretBottom/>
                  </el-icon>
                </span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
      <div class="why">
        <div class="about">
          <span class="title">物业服务快捷入口</span>
          <div class="reason">
            我们承诺：提供优质服务，保障您的权益，为您提供便捷的物业服务。
          </div>
        </div>
        <div class="intro">
          <el-space wrap :size="30">
            <el-card class="box-card">
              <div class="card-header">
                <div class="text">投诉专区，快速响应</div>
              </div>
              <div class="card-content">
                <div class="text">投诉专区，快速响应，24小时在线处理</div>
              </div>
              <div style="margin-top: 18px">
                <el-button type="primary" @click="router.push('/index/complaint')">
                  立即投诉
                  <el-icon>
                    <ArrowRightBold/>
                  </el-icon>
                </el-button>
              </div>
            </el-card>
            <el-card class="box-card">
              <div class="card-header">
                <div class="text">一键报修，快速响应</div>
              </div>
              <div class="card-content">
                <div class="text">家中设备故障？提交即受理</div>
              </div>
              <div style="margin-top: 18px">
                <el-button type="primary" @click="router.push('/index/repair')">
                  一键报修
                  <el-icon>
                    <ArrowRightBold/>
                  </el-icon>
                </el-button>
              </div>
            </el-card>
            <el-card class="box-card">
              <div class="card-header">
                <div class="text">在线缴费，省时省心</div>
              </div>
              <div class="card-content">
                <div class="text">在线缴费，省时省心，物业费、水电费，3秒完成</div>
              </div>
              <div style="margin-top: 18px">
                <el-button type="primary" @click="router.push('/index/payment')">
                  在线缴费
                  <el-icon>
                    <ArrowRightBold/>
                  </el-icon>
                </el-button>
              </div>
            </el-card>
          </el-space>
        </div>
      </div>
      <el-dialog
              v-model="dialogAnnounceVisible"
              :title="announcement.title"
              width="1000"
              style="margin-top: 80px"
              left
              align-center
      >
        <div style="margin: 20px 20px">
          <p v-html="announcement.content"></p>
        </div>
      </el-dialog>
    </div>
  </el-scrollbar>
</template>

<style scoped>
.el-image {
  display: block;
  width: 100%;
}

.header {
  margin: 10px 40px 0;
}

.info {
  margin: 20px 0 0 20px;
  display: flex;
  flex-direction: row;
}

.green {
  color: var(--el-color-success);
}

.red {
  color: var(--el-color-error);
}

.why {
  width: 100%;
  height: 410px;
  margin-top: 60px;
  background-color: #7853f1;
}

.about {
  width: 838px;
  height: 218px;
  text-align: start;
  margin-left: 48px;
  padding: 48px 16px 0;
}

.about .title {
  font-size: 36px;
  font-family: 'Microsoft YaHei', serif;
  color: white;
}

.about .reason {
  padding-top: 16px;
  font-size: 18px;
  font-family: 'Microsoft YaHei', serif;
  color: white;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  margin-top: 16px;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.text {
  font-size: 14px;
}

.announce :hover {
  color: var(--el-color-primary);
  cursor: pointer;
}

.box-card {
  width: 790px;
  margin-left: 180px;
}

.intro .box-card {
  width: 400px;
  height: 249px;
  margin-left: 0;
}

.copy .box-card {
  width: 234px;
  height: 111px;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.el-statistic {
  --el-statistic-content-font-size: 28px;
}

.statistic-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  font-size: 12px;
  color: var(--el-text-color-regular);
  margin-top: 16px;
}

.statistic-footer .footer-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistic-footer .footer-item span:last-child {
  display: inline-flex;
  align-items: center;
  margin-left: 4px;
}
</style>