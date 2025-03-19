<script setup>

import {get} from "@/net";
import {
  Warning,
  CaretTop
} from "@element-plus/icons-vue";
import * as echarts from "echarts";
import {ref} from "vue";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
const homeData = ref([])
const echartsRef1 = ref(null)
const echartsRef2 = ref(null)

const getData = () => {
  get(`/api/static/getHomeDataBackend/${user.uid}`, (data) => {
    homeData.value = data
    // console.log(homeData.value)
    show()
})
}

const getChartData1 = () => {
  const date = []
  const complaintCount = []
  const repairCount = []

  homeData.value.dailyWorkStatList.forEach((item) => {
    date.push(item.date)
    complaintCount.push(item.complaintResolvedCount)
    repairCount.push(item.repairCompletedCount)
  })

  return {
    date,
    complaintCount,
    repairCount
  }
}

const getChartData2 = () => {
  const map = []

  homeData.value.paymentTypeStatList.forEach((item) => {
    map.push({
      name: item.paymentType,
      value: item.totalAmount
    })
  })

  return {
    map
  }
}

const show = () => {
  const myChart1 = echarts.init(echartsRef1.value)
  const myChart2 = echarts.init(echartsRef2.value)
  let chartData1 = getChartData1()
  let chartData2 = getChartData2()
  console.log(chartData2)
  myChart1.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['投诉处理量', '报修处理量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: chartData1.date
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '投诉处理量',
        type: 'line',
        stack: 'complaint',
        data: chartData1.complaintCount
      },
      {
        name: '报修处理量',
        type: 'line',
        stack: 'repair',
        data: chartData1.repairCount
      }
    ]
  })
  myChart2.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '物业收费类别占比',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: chartData2.map
      }
    ]
  })
}

getData()

</script>

<template>
  <div class="flex">
    <div class="top">
      <el-card style="width: 918px;height: 130px;background-color: #e1eaf9" shadow="hover">
        <div style="display: flex;flex-direction: row">
          <img src="@/assets/header.svg" alt="header" style="width: 140px;">
          <div style="display: flex;flex-direction: column;margin-left: 20px">
            <span style="font-size: 20px;font-weight:bold;color: #8c9bf9;">
              上午好，{{ user.username }}！
            </span>
            <span>
              开源等于互助；开源需要大家一起来支持，支持的方式有很多种，比如使用、推荐、写教程、保护生态、贡献代码、回答问题、分享经验、打赏赞助等；欢迎您加入我们！
            </span>
          </div>
        </div>
      </el-card>
      <el-card class="data-card" shadow="hover">
        <div style="display: flex;flex-direction: column;text-align: center">
          <img src="@/assets/coffee.svg" alt="coffee" style="width: 80px;display: block;margin: auto">
          <span>
            您今天已工作
          </span>
        </div>
      </el-card>
    </div>
    <div class="middle">
      <el-card class="data-card" shadow="hover">
        <el-statistic :value="homeData.newComplaintsAndRepairsToday">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              今日新增投诉/报修数
              <el-tooltip
                      effect="dark"
                      content="每天统计新增投诉/报修数"
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
      <el-card class="data-card" shadow="hover">
        <el-statistic :value="homeData.unsolvedComplaintsAndRepairs">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              未处理报修/投诉数
              <el-tooltip
                      effect="dark"
                      content="未受理的报修/投诉"
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
      <el-card class="data-card" shadow="hover">
        <el-statistic :value="homeData.solvingComplaintsAndRepairs">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              正在处理中的投诉/报修数
              <el-tooltip
                      effect="dark"
                      content="正在处理的投诉/报修"
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
      <el-card class="data-card" shadow="hover">
        <el-statistic :value="homeData.mySolvedComplaintsAndRepairs">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              我处理的报修/投诉数
              <el-tooltip
                      effect="dark"
                      content="本人处理报修/投诉的统计"
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
    </div>
    <div class="bottom">
      <el-card style="width: 766px;height: 350px;background-color: #e7ebf1" shadow="hover">
        <div style="background-color: #7ee3f6; width: 10px;height: 30px;float: left"></div>
        <span style="font-size: 18px;font-weight: bold;margin-left: 8px;line-height: 30px">近七日工作情况</span>
        <div class="chart" ref="echartsRef1" style="width: 90%;height: 300px;"></div>
      </el-card>
      <el-card style="width: 446px;height: 350px;background-color: #e7ebf1" shadow="hover">
        <div style="background-color: #7ee3f6; width: 10px;height: 30px;float: left"></div>
        <span style="font-size: 18px;font-weight: bold;margin-left: 8px;line-height: 30px">近一年物业收费类别占比</span>
        <div class="chart" ref="echartsRef2" style="width: 100%;height: 300px;"></div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>

.top {
  display: flex;
  flex-direction: row;
  gap: 20px;
  margin: 20px 0 0 20px;
}

.middle {
  display: flex;
  flex-direction: row;
  margin: 20px 20px 0 20px;
  gap: 20px;
}

.bottom {
  display: flex;
  flex-direction: row;
  margin: 20px 20px 0 20px;
  gap: 20px;
}

.data-card {
  width: 292px;
  height: 130px;
  background-color: #e7ebf1;
}

.demo-border .text {
  width: 15%;
}

.demo-border .line {
  width: 70%;
}

.demo-border .line div {
  width: 100%;
  height: 0;
  border-top: 1px solid var(--el-border-color);
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

.green {
  color: var(--el-color-success);
}

.red {
  color: var(--el-color-error);
}
</style>