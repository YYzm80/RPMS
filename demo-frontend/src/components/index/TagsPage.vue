<script setup>

import {Plus, Edit, Delete, Search} from "@element-plus/icons-vue";
import {computed, ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])
const search = ref('')

const getData = () => {
  get('api/tags/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

const add = () => {
  post('api/tags/add', {
    name: form.value.name
  }, (message) => {
    ElMessage.success(message)
    form.value = []
    getData()
    dialogNewVisible.value = false
  })
}

const openUpdate = (tid) => {
  get(`api/tags/${tid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/tags/update', {
    tid: updateForm.value.tid,
    name: updateForm.value.name,
    state: updateForm.value.state
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogUpdateVisible.value = false
  })
}
let deleteTid = 0;

const openDelete = (tid) => {
  deleteTid = tid
  dialogDeleteVisible.value = true
}

const deleteTag = () => {
  post('api/tags/delete', {
    tid: deleteTid
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogDeleteVisible.value = false
  })
}

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.name.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 10 // 每页显示的数据数量
const currentPage = ref(1) // 当前页码
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredData.value.slice(start, start + pageSize)
})
const total = computed(() => filteredData.value.length)

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
      <div style="position: fixed;left: 385px">
        <el-input style="width: 300px;height: 40px;"
                  v-model="search"
                  placeholder="搜索标签"></el-input>
        <el-button
                style="height: 40px;width: 100px;font-size: 18px"
                type="primary"
                plain
                @click="">
          <el-icon>
            <Search/>
          </el-icon>
          搜索
        </el-button>
      </div>
      <div style="right: 400px;position: fixed">
        <el-button type="success"
                   style="width: 90px;height: 40px;font-size: 15px"
                   plain
                   @click="dialogNewVisible = true">
          <el-icon>
            <Plus/>
          </el-icon>
          新增标签
        </el-button>
      </div>
    </div>
    <div class="bottom">
      <el-table :data="pagedData"
                height="530"
                style="width: 50%;margin: auto"
                border
                stripe
      >
        <el-table-column prop="index" label="序号" width="120" header-align="center" align="center"/>
        <el-table-column prop="name" label="标签名称" width="180" header-align="center" align="center">
          <template #default="scope">
            <el-tag>{{scope.row.name}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="state" label="标签状态" width="120" header-align="center" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.state === '1'" type="success">启用</el-tag>
            <el-tag v-if="scope.row.state === '0'" type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template v-slot="scope">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="openUpdate(scope.row.tid)"></el-button>
              <el-button :icon="Delete" type="danger" @click="openDelete(scope.row.tid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增标签"
              width="400"
      >
        <el-form :model="form" label-position="left">
          <el-form-item label=" 标签名称">
            <el-input v-model="form.name" type="text" autocomplete="off" v-on:keyup.enter="add"/>
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
              title="修改标签信息"
              width="400"
      >
        <el-form :model="updateForm" label-position="left">
          <el-form-item label="标签名称">
            <el-input v-model="updateForm.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="标签状态">
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
        <span>是否删除标签?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="deleteTag">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <div>
      <el-pagination
              style="width: 50%;margin: auto"
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
.top {
  height: 70px;
  background-color: white;
  padding-top: 20px;
}
</style>