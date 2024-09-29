<script setup>

import {Edit, Delete} from "@element-plus/icons-vue";
import {ref, getCurrentInstance} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const updateForm = ref([])
const {proxy} = getCurrentInstance();
let deleteAid = 0

const getData = () => {
  get(`api/arrangement/all-gid?gid=${proxy.$route.query.gid}`, (data) => {
    tableData.value = data
  })
}

const getUpdateForm = (aid) => {
  get(`api/arrangement/aid?aid=${aid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/arrangement/update', {
    aid: updateForm.value.aid,
    role: updateForm.value.role
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

function openDelete(aid) {
  dialogDeleteVisible.value = true;
  deleteAid = aid
}

const doDelete = () => {
  post('api/arrangement/delete', {
    aid: deleteAid
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
      <el-table :data="tableData" height="600" style="width: 800px">
        <el-table-column label="序号" width="120" header-align="center" align="center" fixed>
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="180" header-align="center" align="center"/>
        <el-table-column prop="gname" label="参与比赛名" width="160" header-align="center" align="center"/>
        <el-table-column prop="role" label="报名人员类别" width="160" header-align="center" align="center" sortable>
          <template #default="scope1">
            <el-tag v-if="scope1.row.role === 'athlete'" type="primary">运动员</el-tag>
            <el-tag v-if="scope1.row.role === 'referee'" type="warning">裁判员</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="getUpdateForm(scope.row.aid)"></el-button>
              <el-button :icon="Delete" type="danger" @click="openDelete(scope.row.aid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改安排信息"
              width="400"
      >
        <el-form :model="updateForm" label-position="top">
          <el-row>
            <el-col :span="10" style="margin-right: 30px">
              <el-form-item label="参赛人姓名">
                <el-input v-model="updateForm.name" autocomplete="off" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参加的比赛名称">
                <el-input v-model="updateForm.gname" autocomplete="off" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="参与角色">
            <el-radio-group v-model="updateForm.role">
              <el-radio value="athlete">运动员</el-radio>
              <el-radio value="referee">裁判员</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogUpdateVisible = false">取消</el-button>
            <el-button type="warning" @click="update">
              修改
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogDeleteVisible"
              width="400"
              center
              align-center
      >
        <span>是否删除该安排?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="doDelete">
              删除
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