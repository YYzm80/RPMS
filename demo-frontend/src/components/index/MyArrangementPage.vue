<script setup>

import {ref} from "vue";
import {get} from "@/net";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const tableData = ref([])

const getData = () => {
  get(`api/arrangement/all-uid?uid=${user.uid}`, (data) => {
    tableData.value = data
  })
}

getData()
</script>

<template>
  <div class="content">
    <div class="bottom">
      <el-table :data="tableData" height="600" style="width: 600px">
        <el-table-column label="序号" width="120" header-align="center" align="center" fixed>
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" header-align="center" align="center"/>
        <el-table-column prop="gname" label="参与比赛名" width="180" header-align="center" align="center"/>
        <el-table-column prop="role" label="参与角色" width="160" header-align="center" align="center" sortable>
          <template #default="scope1">
            <el-tag v-if="scope1.row.role === 'athlete'" type="primary">运动员</el-tag>
            <el-tag v-if="scope1.row.role === 'referee'" type="warning">裁判员</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>