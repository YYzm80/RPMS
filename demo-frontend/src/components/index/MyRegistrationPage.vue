<script setup>

import {CloseBold} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const tableData = ref([])
const dialogDeleteVisible = ref(false)
let deleteRid = 0

const getData = () => {
  get(`api/registration/all-uid?uid=${user.uid}`, (data) => {
    tableData.value = data
  })
}

function openDelete(rid) {
  dialogDeleteVisible.value = true;
  deleteRid = rid
}

const doDelete = () => {
  post('api/registration/delete', {
    rid: deleteRid
  }, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

getData()
</script>

<template>
  <div class="content">
    <div class="bottom">
      <el-table
              :data="tableData"
              height="600"
              stripe
              empty-text="您还没有报名呢，快去报名试试吧~"
              style="width: 800px">
        <el-table-column label="序号" width="120" header-align="center" align="center" fixed>
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" header-align="center" align="center"/>
        <el-table-column prop="gname" label="比赛名称" width="180" header-align="center" align="center" sortable/>
        <el-table-column prop="role" label="参与角色" width="160" header-align="center" align="center" sortable>
          <template #default="scope1">
            <el-tag v-if="scope1.row.role === 'athlete'" type="primary">运动员</el-tag>
            <el-tag v-if="scope1.row.role === 'referee'" type="warning">裁判员</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="state" label="审核状态" width="120" header-align="center" align="center">
          <template #default="scope1">
            <el-tag v-if="scope1.row.state === '0'" type="info">未审核</el-tag>
            <el-tag v-if="scope1.row.state === '1'" type="success">通过</el-tag>
            <el-tag v-if="scope1.row.state === '2'" type="danger">未通过</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-tooltip content="取消报名" placement="left" effect="light" v-if="scope.row.state === '0'">
              <el-button :icon="CloseBold" type="danger" @click="openDelete(scope.row.rid)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogDeleteVisible"
              width="400"
              center
              align-center
      >
        <span>是否取消本次报名?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">否</el-button>
            <el-button type="danger" @click="doDelete">
              是
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>

</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>