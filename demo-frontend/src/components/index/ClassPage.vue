<script setup>

import {Plus, Edit, Delete} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])
let deleteCid = 0

const getData = () => {
  get('api/class/all', (data) => {
    tableData.value = data
    initData()
  })
}

const add = () => {
  post('api/class/add', {
    name: form.value.name
  }, (message) => {
    ElMessage.success(message)
    dialogNewVisible.value = false
    form.value = []
    getData()
  })
}

const getUpdateForm = (cid) => {
  get(`api/class/cid?cid=${cid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/class/update', {
    cid: updateForm.value.cid,
    name: updateForm.value.name,
    state: updateForm.value.state
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

function openDelete(cid) {
  dialogDeleteVisible.value = true;
  deleteCid = cid
}

const doDelete = () => {
  post('api/class/delete', {
    cid: deleteCid
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
    <div class="top">
      <el-button type="success" plain @click="dialogNewVisible = true">
        <el-icon>
          <Plus/>
        </el-icon>
        新增班级
      </el-button>
    </div>
    <div class="bottom">
      <el-table :data="pagedData" height="550" style="width: 600px" stripe>
        <el-table-column prop="date" label="序号" width="120" header-align="center" align="center">
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="班级名称" width="180" header-align="center" align="center"/>
        <el-table-column prop="state" label="状态" width="120" header-align="center" align="center">
          <template #default="scope1">
            <el-tag v-if="scope1.row.state === '1'" type="success">启用</el-tag>
            <el-tag v-if="scope1.row.state === '0'" type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope1">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="getUpdateForm(scope1.row.cid)"></el-button>
              <el-button :icon="Delete" type="danger" @click="openDelete(scope1.row.cid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增班级"
              width="400"
      >
        <el-form :model="form" label-position="top">
          <el-form-item label="班级名称">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false">取消</el-button>
            <el-button type="primary" @click="add">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改班级信息"
              width="400"
      >
        <el-form :model="updateForm" label-position="top">
          <el-form-item label="班级名称">
            <el-input v-model="updateForm.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="班级状态">
            <el-radio-group v-model="updateForm.state">
              <el-radio value="1">启用</el-radio>
              <el-radio value="0">弃用</el-radio>
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
        <span>是否删除班级?</span>
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