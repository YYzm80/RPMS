<script setup>

import {ref} from "vue";
import {get, post} from "@/net";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const tableData = ref([])

const getData = () => {
  get(`api/score/all-detail-uid?uid=${user.uid}`, (data) => {
    tableData.value = data
  })
}

getData()
</script>

<template>
  <div class="content">
    <div class="bottom">
      <el-table :data="tableData"
                height="600"
                style="width: 600px"
                stripe
                empty-text="还没有成绩呢，等待成绩公布吧~">
        <el-table-column prop="date" label="序号" width="120" header-align="center" align="center">
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="参赛人员姓名" width="120" header-align="center" align="center"/>
        <el-table-column prop="gname" label="参加比赛名称" width="120" header-align="center" align="center"/>
        <el-table-column prop="score" label="得分" width="120" header-align="center" align="center" sortable/>
      </el-table>
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>