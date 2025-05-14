<script setup>
import {Delete, Edit, Plus, Refresh, Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post, put} from "@/net";
import {ElMessage} from "element-plus";
import {useSearchAndPagination} from "@/net/common";
import {typeRules} from "@/net/rules.js";

const tableData = ref([])

const getData = () => {
  get(`/api/type/all/payment`, (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData
  })
}

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const updateFormRef = ref()
let deleteTid = 0

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post('api/type/add', {
        type: 'payment',
        description: form.value.description
      }, (message) => {
        ElMessage.success(message)
        form.value = []
        dialogNewVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写类型信息')
    }
  })
}

const getUpdateData = (tid) => {
  get(`api/type/tid/${tid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  updateFormRef.value.validate((valid) => {
    if (valid) {
      put('api/type/update', {
        tid: updateForm.value.tid,
        type: updateForm.value.type,
        description: form.value.description
      }, (message) => {
        ElMessage.success(message)
        dialogUpdateVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写类型信息')
    }
  })
}

function openDelete(tid) {
  dialogDeleteVisible.value = true;
  deleteTid = tid
}

const deleteUser = () => {
  post('api/type/delete', deleteTid
          , (message) => {
            ElMessage.success(message)
            dialogDeleteVisible.value = false
            getData()
          })
}

const changeStatus = (tid) => {
  put('api/type/change-status', tid, (message) => {
    ElMessage.success(message)
    getData()
  })
}

const {
  search,
  highlight,
  pageSize,
  currentPage,
  pagedData,
  total,
  initData,
  handlePageChange,
} = useSearchAndPagination(tableData, 10, ['description']);

let lastRefreshTime = 0

function refresh() {
  const currentTime = Date.now()

  // 计算时间差
  const timeDiff = currentTime - lastRefreshTime

  // 5秒内禁止重复刷新
  if (timeDiff < 5000) {
    ElMessage.warning('操作过于频繁，请5秒后再试')
    return
  }

  // 更新最后一次刷新时间
  lastRefreshTime = currentTime

  // 原有刷新逻辑
  getData()
  ElMessage.info('刷新成功')
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
        新增类型
      </el-button>
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按类型名称搜索"></el-input>
      <el-button
              style="height: 35px;width: 100px;font-size: 16px"
              type="primary"
              plain
              @click="">
        <el-icon>
          <Search/>
        </el-icon>
        搜索
      </el-button>
      <el-button
              style="height: 35px;width: 100px;font-size: 16px"
              type="warning"
              plain
              @click="refresh">
        <el-icon>
          <Refresh/>
        </el-icon>
        刷新
      </el-button>
    </div>
    <div class="bottom">
      <el-table :data="pagedData" height="525" style="width: 100%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="description" label="类型名称" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.description, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="启用状态" width="120"
                         header-align="center" align="center">
          <template #default="scope">
            <el-switch
                    v-model="scope.row.status"
                    active-value="active"
                    inactive-value="inactive"
                    inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949;margin-left: 5px"
                    active-text="启用"
                    inactive-text="禁用"
                    @change="changeStatus(scope.row.tid)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button :icon="Edit" type="primary"
                         @click="getUpdateData(scope.row.tid)"></el-button>
              <el-button :icon="Delete" type="danger"
                         @click="openDelete(scope.row.tid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增类型"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="form"
                 :rules="typeRules"
                 hide-required-asterisk
                 ref="formRef"
                 label-position="top">
          <el-form-item label="类型名称" prop="description">
            <el-input v-model="form.description" autocomplete="off"
                      placeholder="请输入类型名称"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false;form = []">取消
            </el-button>
            <el-button type="primary" @click="add">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改类型信息"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="updateForm"
                 :rules="typeRules"
                 hide-required-asterisk
                 ref="updateFormRef"
                 label-position="top">
          <el-form-item label="类型名称" prop="description">
            <el-input v-model="updateForm.description" autocomplete="off"
                      placeholder="请输入类型名称"/>
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
        <span>是否删除该类型?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="deleteUser">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <div>
      <el-pagination
              style="width: 30%;margin: auto"
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

.results_pdfFile span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 350px;
  color: rgba(133, 51, 0, 0.99);
}
</style>