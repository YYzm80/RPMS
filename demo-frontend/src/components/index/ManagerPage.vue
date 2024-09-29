<script setup>

import {Edit, Delete} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const updateForm = ref([])
let deleteUid = 0

const getData = () => {
  get('api/user/all', (data) => {
    tableData.value = data
    initData()
  })
}

const getUpdateForm = (uid) => {
  get(`api/user/uid?uid=${uid}`, (data) => {
    updateForm.value = data
    getAcademy()
    getClass()
    dialogUpdateVisible.value = true
  })
}

const getAcademy = () => {
  get(`api/academy/state`, (data) => {
    updateForm.value.academy = data
  })
}

const getClass = () => {
  get(`api/class/all-state`, (data) => {
    updateForm.value.class = data
  })
}

const update = () => {
  post('api/user/update', {
    uid: updateForm.value.uid,
    username: updateForm.value.username,
    name: updateForm.value.name,
    email: updateForm.value.email,
    role: updateForm.value.role,
    aid: updateForm.value.aid,
    cid: updateForm.value.cid
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

function openDelete(uid) {
  dialogDeleteVisible.value = true;
  deleteUid = uid
}

const doDelete = () => {
  post('api/user/delete', {
    uid: deleteUid
  }, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const pageSize = 10 // 每页显示的数据数量
const currentPage = ref(1) // 当前页码
const total = ref(0) // 总数据条数
const pagedData = ref([]) // 当前页的数据

// 初始化数据
const initData = () => {
  total.value = tableData.value.length
  updatePageData()
}

// 更新当前页的数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  pagedData.value = tableData.value.slice(start, end)
}

// 处理页码变化的函数
const handlePageChange = (newPage) => {
  currentPage.value = newPage
  updatePageData()
}

getData()

</script>

<template>
  <div class="content">
    <div class="bottom">
      <el-table :data="pagedData" height="550" style="width: 1300px" stripe>
        <el-table-column label="序号" width="90" header-align="center" align="center" fixed>
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" header-align="center" align="center"/>
        <el-table-column prop="username" label="用户名" width="120" header-align="center" align="center"/>
        <el-table-column prop="role" label="角色" width="120" header-align="center" align="center" sortable>
          <template #default="scope1">
            <el-tag v-if="scope1.row.role === 'admin'" type="warning">管理员</el-tag>
            <el-tag v-if="scope1.row.role === 'student'" type="success">学生</el-tag>
            <el-tag v-if="scope1.row.role === 'teacher'" type="primary">教师</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" header-align="center" align="center"/>
        <el-table-column prop="academy" label="所属学院" width="180" header-align="center" align="center" sortable/>
        <el-table-column prop="clazz" label="所属班级" width="180" header-align="center" align="center"/>
        <el-table-column prop="create_time" label="创建时间" width="180" header-align="center" align="center"/>
        <el-table-column prop="update_time" label="修改时间" width="180" header-align="center" align="center"/>
        <el-table-column label="操作" fixed="right" min-width="120" header-align="center" align="center">
          <template #default="scope">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="getUpdateForm(scope.row.uid)"></el-button>
              <el-button :icon="Delete" type="danger" @click="openDelete(scope.row.uid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改用户信息"
              width="400"
      >
        <el-form :model="updateForm" label-position="top">
          <el-row>
            <el-col :span="10" style="margin-right: 30px">
              <el-form-item label="用户名">
                <el-input v-model="updateForm.username" autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名">
                <el-input v-model="updateForm.name" autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10" style="margin-right: 30px">
              <el-form-item label="所属学院">
                <el-select v-model="updateForm.aid" placeholder="请选择学院">
                  <el-option v-for="a in updateForm.academy" :label="a.name" :value="a.aid"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属班级">
                <el-select v-model="updateForm.cid" placeholder="请选择班级">
                  <el-option v-for="c in updateForm.class" :label="c.name" :value="c.cid"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="邮箱">
            <el-input v-model="updateForm.email" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="角色">
            <el-radio-group v-model="updateForm.role">
              <el-radio value="admin">管理员</el-radio>
              <el-radio value="student">学生</el-radio>
              <el-radio value="teacher">教师</el-radio>
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
        <span>是否删除用户?</span>
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
    <div>
      <el-pagination
              style="margin: 20px 350px"
              layout="total, prev, pager, next"
              background
              :page-size="pageSize"
              :current-page="currentPage"
              :total="total"
              @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>