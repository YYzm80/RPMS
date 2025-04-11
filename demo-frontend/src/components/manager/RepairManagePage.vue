<script setup>

import {Edit, Refresh, Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, put} from "@/net";
import {ElMessage} from "element-plus";
import {useSearchAndPagination} from "@/net/common";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));

const getData = () => {
  get('/api/repair/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData
  })
}

const dialogUpdateVisible = ref(false)
const updateForm = ref([])

const getUpdateData = (rid) => {
  get(`api/repair/rid/${rid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const accept = () => {
  put('api/repair/update', {
    repairId: updateForm.value.repairId,
    handlerId: user.uid,
    status: 'processing',
    completionTime: new Date()
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

const completed = () => {
  put('api/repair/update', {
    repairId: updateForm.value.repairId,
    handlerId: user.uid,
    status: 'completed',
    handleResult: updateForm.value.handleResult,
    completionTime: new Date()
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

const changeStep = (status) => {
  // console.log(status)
  switch (status) {
    case '待定': return 1
    case '处理中': return 2
    case '已解决': return 3
  }
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
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按关键词搜索"></el-input>
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
        <el-table-column prop="submitterName" label="申请人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="description" label="报修描述" width="300"
                         show-overflow-tooltip
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.description, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="statusDesc" label="处理状态" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.statusDesc === '已解决' ? 'success' :
            scope.row.statusDesc === '处理中' ? 'primary' : 'warning'">
              {{ scope.row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button :icon="Edit" type="primary"
                         @click="getUpdateData(scope.row.repairId)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="报修处理"
              width="600"
              style="margin-top: 50px"
      >
        <el-form :model="updateForm" label-position="top">
          <el-steps style="max-width: 500px" :active="changeStep(updateForm.statusDesc)" finish-status="success" simple>
            <el-step title="报修申请" />
            <el-step title="报修受理" />
            <el-step title="报修解决" />
          </el-steps>
          <el-row>
            <el-col :span="11">
              <el-form-item label="申请人">
                <el-input v-model="updateForm.submitterName"
                          disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="11" :offset="2">
              <el-form-item label="业主楼栋信息">
                <el-input v-model="updateForm.fullAddress"
                          disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="报修描述">
            <el-input v-model="updateForm.description"
                      show-word-limit
                      maxlength="200"
                      type="textarea"
                      rows="5"
                      resize='none'
                      disabled
                      placeholder="请输入报修描述"/>
          </el-form-item>
          <el-form-item label="提交时间">
            <el-date-picker type="date"
                            style="width: 100%;"
                            v-model="updateForm.submitTime"
                            disabled
                            format="YYYY/MM/DD HH:mm:ss"
                            value-format="YYYY-MM-DD HH:mm:ss"/>
          </el-form-item>
          <el-row v-if="updateForm.statusDesc !== '待定'">
            <el-col :span="11">
              <el-form-item label="处理人">
                <el-input v-model="updateForm.handlerName"
                          disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="处理时间">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="updateForm.completionTime"
                                disabled
                                format="YYYY/MM/DD HH:mm:ss"
                                value-format="YYYY-MM-DD HH:mm:ss"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="处理结果" v-if="updateForm.statusDesc !== '待定'">
            <el-input v-model="updateForm.handleResult"
                      show-word-limit
                      maxlength="200"
                      type="textarea"
                      rows="5"
                      resize='none'
                      :disabled="updateForm.statusDesc === '已解决'"
                      placeholder="请输入报修处理结果"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogUpdateVisible = false">取消</el-button>
            <el-button
                    type="warning"
                    v-if="updateForm.statusDesc === '待定'"
                    @click="accept">
              受理
            </el-button>
            <el-button
                    type="success"
                    v-if="updateForm.statusDesc === '处理中'"
                    :disabled="updateForm.handleResult === null"
                    @click="completed">
              确认解决
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
</style>