<script setup>

import * as echarts from 'echarts';
import {getCurrentInstance, ref} from "vue";
import {getChartData} from "@/assets/option"
import router from "@/router";
import {get} from "@/net";

const echartsRef1 = ref(null)
const echartsRef2 = ref(null)
const echartsRef3 = ref(null)
let chartData = ref({})
const {proxy} = getCurrentInstance();

const getData = () => {
  get(`/api/static/static-mid/${proxy.$route.query.mid}`, (data) => {
    chartData.value = data
    // console.log(chartData)
    showChart()
  })
}

const showChart = () => {
  const myChart1 = echarts.init(echartsRef1.value)
  const myChart2 = echarts.init(echartsRef2.value)
  const myChart3 = echarts.init(echartsRef3.value)
  myChart1.setOption(getChartData(chartData.value).option1)
  myChart2.setOption(getChartData(chartData.value).option2)
  myChart3.setOption(getChartData(chartData.value).option3)
}

function goBack() {
  router.push('/index/meeting')
}

getData()
</script>

<template>
  <div class="chart">
    <el-page-header @back="goBack" style="margin: 20px 0 0 50px;">
      <template #content>
        <span class="text-large font-600 mr-3"> 数据统计 </span>
      </template>
    </el-page-header>
    <span style="color: gray;font-size: 14px;font-weight: lighter">数据统计截止于：{{chartData.createTime}}</span>
    <div ref="echartsRef1" style="width: 400px; height: 300px;margin: auto"/>
    <div style="display: flex;flex-direction: row;margin: auto">
      <div ref="echartsRef2" style="width: 400px; height: 300px;"/>
      <div ref="echartsRef3" style="width: 400px; height: 300px;"/>
    </div>
  </div>
</template>

<style scoped>
.chart {
  height: 100vh;
  width: 100%;
  background-color: white;
  display: flex;
  flex-direction: column;
}
</style>