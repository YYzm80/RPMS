<script setup>

import {CaretTop, Warning} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get} from "@/net";

const urls = ref([])

const tableData = ref([])
const ready = ref(false)

const getData = () => {
  get('api/static/all-home', (data) => {
    tableData.value.data = data
    ready.value = true
  })
  get('api/meeting/all', (data) => {
    let length = data.length > 5 ? 5 : data.length
    for (let i = 0; i < length; i++) {
      urls.value.push(getImgSrc(data[i].img))
    }
  })
}

const getImgSrc = (picName) => {
    return `http://localhost:8080/uploaded/${picName}`
}

getData()
</script>

<template>
  <el-scrollbar height="100%">
    <div style="display: flex;flex-direction:column;height: 100%;">
      <div class="header">
        <el-carousel :interval="4000" height="300px" trigger="click" type="card">
          <el-carousel-item v-for="url in urls" :key="url">
            <img v-lazy="url" alt="Carousel Image" style="width: 100%; height: 100%; object-fit: cover;"/>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="info" v-if="ready">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最受欢迎的公司</span>
            </div>
          </template>
          <div class="text item">
            <el-table :data="tableData.data.popularCompanies" style="width: 100%">
              <el-table-column label="企业" prop="name" align="left" width="180"/>
              <el-table-column label="投递简历人数" prop="deliverNum" align="right"/>
            </el-table>
          </div>
        </el-card>
        <div style="display: flex;flex-direction: column;width: 1200px;">
          <div class="row-flex">
            <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
              <el-statistic :value="tableData.data.meetingCount">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    总举办双选会场次
                    <el-tooltip
                            effect="light"
                            content="每天新增双选会统计"
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
                  <span>比昨天</span>
                  <span class="green">24%
                                <el-icon>
                                    <CaretTop/>
                                </el-icon>
                            </span>
                </div>
              </div>
            </el-card>
            <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
              <el-statistic :value="tableData.data.userCount">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    总注册用户人数
                    <el-tooltip
                            effect="light"
                            content="每天新增用户统计"
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
                  <span>比昨天</span>
                  <span class="green">8%
                                <el-icon>
                                    <CaretTop/>
                                </el-icon>
                            </span>
                </div>
              </div>
            </el-card>
          </div>
          <div class="row-flex" style="margin-top: 30px">
            <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
              <el-statistic :value="tableData.data.meetingRunCount">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    正在进行的双选会
                    <el-tooltip
                            effect="light"
                            content="进行中的双选会统计"
                            placement="top"
                    >
                      <el-icon style="margin-left: 4px" :size="12">
                        <Warning/>
                      </el-icon>
                    </el-tooltip>
                  </div>
                </template>
              </el-statistic>
            </el-card>
            <el-card style="width: 292px;height: 130px;margin-left: 60px;background-color: #e7ebf1" shadow="hover">
              <el-statistic :value="tableData.data.companyCount">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    已入驻公司
                    <el-tooltip
                            effect="light"
                            content="每天新增公司统计"
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
                  <span>比昨天</span>
                  <span class="green">12%
                                <el-icon>
                                    <CaretTop/>
                                </el-icon>
                            </span>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
      <div class="why">
        <div class="about">
          <span class="title">为什么选择我们</span>
          <div class="reason">
            学堂在线是清华大学发起建立的面向未来的慕课在线学习平台，为学习者提供从高校课程到实战技能的在线教育服务。
          </div>
        </div>
        <div class="intro">
          <el-space wrap :size="30">
            <el-card class="box-card">
              <div>1</div>
            </el-card>
            <el-card class="box-card">
              <div>2</div>
            </el-card>
            <el-card class="box-card">
              <div>3</div>
            </el-card>
          </el-space>
        </div>
      </div>
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

.row-flex {
  display: flex;
  flex-direction: row;
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

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 420px;
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