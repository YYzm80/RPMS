<script setup>

import * as echarts from 'echarts';
import {ref, reactive} from "vue";
import {get} from "@/net";
import {Search} from "@element-plus/icons-vue";

const tableData = ref([])
const scoreList = ref([])
const echartsRef = ref(null);
const chartData = reactive([])

const getData = (sid) => {
  get(`api/score/all-detail-sid-sort?sid=${sid}`, (data) => {
    tableData.value = data
    chartData.value = getChartData()
    console.log(chartData.value)
    showChart()
    // console.log(scoreList.value)
    // getScoreList()
  })
}

const cellStyle = ({ rowIndex }) => {
  if (rowIndex === 0 ) {
    return {
      backgroundColor: 'gold',
      color: 'black',
    }
  } else if (rowIndex === 1) {
    return {
      backgroundColor: 'silver',
      color: 'black',
    }
  } else if (rowIndex === 2) {
    return {
      backgroundColor: '#cd7f32',
      color: 'black',
    }
  }
}

const getScoreList = () => {
  get('api/score/all-list', (data) => {
    data.forEach(item => {
      if (item.state === '1')
        scoreList.value.push(item)
    })
  })
}

const getChartData = () => {
  const name = []
  let gName = ''
  const score = []

  for (let i = 0; i < tableData.value.length; i++) {
    name.push(tableData.value[i].name)
    gName = tableData.value[0].gname
    score.push((tableData.value[i].score))
  }

  return {
    name,
    gName,
    score: score.map(Number)  // 将字符串转换回数字
  }
}

const showChart = () => {
  const myChart = echarts.init(echartsRef.value);
  // 配置图表
  myChart.setOption({
    title: {
      text: chartData.value.gName + "比赛得分排行榜",
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      max: 100,
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      //不显示X轴刻度线和数字
      splitLine: {show: false},
      axisLabel: {show: false},
    },
    yAxis: {
      type: 'category',
      data: chartData.value.name,
      inverse: true,
      splitLine: {show: false},
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      //key和图间距
      offset: 10,
      //动画部分
      animationDuration: 300,
      animationDurationUpdate: 300,
      //key文字大小
      nameTextStyle: {
        fontSize: 5,
      },
    },
    series: [
      {
        name: '得分',
        type: 'bar',
        stack: 'total',
        label: {
          normal: {
            show: true,
            position: "right",
            valueAnimation: true,
            offset: [5, -2],
            textStyle: {
              color: "#333",
              fontSize: 13,
            },
          },
        },
        barWidth: 20,
        barGap: 18,
        smooth: true,
        valueAnimation: true,
        realtimeSort: true,
        data: chartData.value.score,
        itemStyle: {
          emphasis: {
            barBorderRadius: 7,
          },
          //颜色样式部分
          normal: {
            barBorderRadius: 7,
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              {offset: 0, color: "#3977E6"},
              {offset: 1, color: "#37BBF8"},
            ]),
          },
        },
      }
    ],
  })
}

getScoreList()
</script>

<template>
  <div class="content">
    <div class="top">
      <el-form :model="scoreList">
        <el-form-item>
          <el-select placeholder="请选择您要查询的赛事" v-model="scoreList.sid" style="width: 200px">
            <el-option
                    v-for="list in scoreList"
                    :label="list.name"
                    :value="list.sid"
                    @click="getData(list.sid)"
            />
            <template #prefix>
              <el-icon>
                <search/>
              </el-icon>
            </template>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="bottom">
      <el-table
              :data="tableData"
              height="600"
              style="width: 500px"
              :cell-style="cellStyle"
              empty-text="请先选择比赛~"
      >
        <el-table-column prop="date" label="排名" width="120" header-align="center" align="center">
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="选手姓名" width="180" header-align="center" align="center"/>
        <el-table-column prop="score" label="得分" width="120" header-align="center" align="center"/>
      </el-table>
      <div class="chart" ref="echartsRef" style="width: 600px; height: 400px;"></div>
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}

.bottom {
  display: flex;
  flex-direction: row;
}

</style>