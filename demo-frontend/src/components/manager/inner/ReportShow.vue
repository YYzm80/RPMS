<script setup>
import {ref} from 'vue';
import * as echarts from 'echarts';
import {ElCard, ElStatistic} from 'element-plus';
import {Warning} from "@element-plus/icons-vue";
import {get} from "@/net/index.js";
import {getChartData} from "@/net/options";

const props = defineProps({
  reportId: {
    type: Number,
    default: () => ({})
  }
})

let chartData = ref({
  month: '',
  basicCommunityStat: {
    livingCount: 0,
    emptyPropertyRate: 0.0,
    complaintCount: 0,
    repairCount: 0
  },
  managerWorkStat: {
    managerCount: 0,
    resolvedComplaintsCount: 0,
    complaintResolvedRate: 0.0,
    solvedRepairsCount: 0,
    repairSolvedRate: 0.0,
    publishedAnnouncementCount: 0
  },
  incomeStat: {
    total: 0.0,
    paymentTypeStatList: [
      {
        paymentType: '',
        totalAmount: 0.0
      }
    ]
  },
  generateTime: ''
})
const vacancyRateChart = ref(null)
const complaintResolutionChart = ref(null)
const repairResolutionChart = ref(null)
const incomeSourcesChart = ref(null)
const managerWorkStat = ref({})
const basicCommunityStat = ref({})
const incomeStat = ref({})
const id = ref(props.reportId)

const getData = () => {
  // console.log(id)
  get(`api/report/id/${id.value}`, (data) => {
    chartData.value = data
    managerWorkStat.value = chartData.value.managerWorkStat
    basicCommunityStat.value = chartData.value.basicCommunityStat
    incomeStat.value = chartData.value.incomeStat
    initCharts()
  })
}

const initCharts = () => {
  const myChart1 = echarts.init(vacancyRateChart.value)
  const myChart2 = echarts.init(complaintResolutionChart.value)
  const myChart3 = echarts.init(repairResolutionChart.value)
  const myChart4 = echarts.init(incomeSourcesChart.value)
  myChart1.setOption(getChartData(chartData.value).option1)
  // console.log(getChartData(chartData.value).option1)
  myChart2.setOption(getChartData(chartData.value).option2)
  myChart3.setOption(getChartData(chartData.value).option3)
  myChart4.setOption(getChartData(chartData.value).option4)
}

getData()
</script>

<template>
  <div class="content">
    <div class="left">
      <div class="top-left">
        <ElCard shadow="hover">
          <h2>月报基本信息</h2>
          <ElStatistic title="月份" :value="chartData.month"/>
          <ElStatistic title="生成时间" :value="chartData.generateTime"/>
        </ElCard>
      </div>
      <div class="bottom-left">
        <ElCard shadow="hover">
          <h2>物业工作统计</h2>
          <div style="display: flex;flex-direction: row;gap: 30px">
            <ElStatistic title="总物业人员数" :value="managerWorkStat.managerCount"/>
            <ElStatistic title="投诉解决数" :value="managerWorkStat.resolvedComplaintsCount"/>
            <ElStatistic title="报修解决数" :value="managerWorkStat.solvedRepairsCount"/>
            <ElStatistic title="发布公告数" :value="managerWorkStat.publishedAnnouncementCount"/>
          </div>
          <div style="display: flex;flex-direction: row;gap: 10px">
            <div ref="complaintResolutionChart" style="width: 100%; height: 200px;"></div>
            <div ref="repairResolutionChart" style="width: 100%; height: 200px;"></div>
          </div>
        </ElCard>
      </div>
      <div style="width: 100%;padding: 10px;height: 180px;">
        <ElCard shadow="hover" style="height: 133px">
          <el-text type="warning">
            <el-icon>
              <Warning/>
            </el-icon>
            温馨提示：每月1日由系统自动生成上个月月报，所有数据来源于系统数据且均为系统自动生成，未录入的数据将不会显示在月报中。
          </el-text>
        </ElCard>
      </div>
    </div>
    <div class="right">
      <div class="top-right">
        <ElCard shadow="hover">
          <h2>社区基础信息</h2>
          <div style="display: flex;flex-direction: row;gap: 30px">
            <ElStatistic title="总居住人数" :value="basicCommunityStat.livingCount"/>
            <ElStatistic title="投诉数量" :value="basicCommunityStat.complaintCount"/>
            <ElStatistic title="报修数量" :value="basicCommunityStat.repairCount"/>
          </div>
          <div ref="vacancyRateChart" style="width: 100%; height: 200px;"></div>
        </ElCard>
      </div>
      <div class="bottom-right">
        <ElCard shadow="hover">
          <h2>物业收入数据</h2>
          <ElStatistic title="总收入(单位：元)" :value="incomeStat.total"/>
          <div ref="incomeSourcesChart" style="width: 100%; height: 200px;"></div>
        </ElCard>
      </div>
    </div>
  </div>
</template>

<style scoped>
.content {
  width: 100%;
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.left, .right {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top-left, .top-right, .bottom-left, .bottom-right {
  width: 100%;
  padding: 10px;
}

.top-left {
  height: 200px; /* 缩小高度 */
}

.top-right {
  height: 350px; /* 保持原有高度 */
}

.bottom-left {
  height: 350px; /* 保持原有高度 */
}

.bottom-right {
  height: 200px; /* 缩小高度 */
}
</style>