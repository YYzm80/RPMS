<script setup>
import {computed, ref} from "vue";
import {get, post} from "@/net";
import {Delete, Edit, Plus, Search, View} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const form = ref([])
const updateForm = ref([])
const repair = ref([])
const dialogNewRepairVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogRepairVisible = ref(false)
let deleteRid = 0

const getData = () => {
  get(`/api/repair/uid/${user.uid}`, (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

const getRepair = (rid) => {
  get(`api/repair/rid/${rid}`, (data) => {
    repair.value = data
    dialogRepairVisible.value = true
  })
}

const getUpdateForm = (rid) => {
  get(`api/repair/rid/${rid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const newRepair = () => {
  post('api/repair/add', {
    description: form.value.description,
    userId: user.uid,
    submitTime: new Date()
  }, (message) => {
    ElMessage.success(message)
    dialogNewRepairVisible.value = false
    getData()
  })
}

function openDelete(rid) {
  dialogDeleteVisible.value = true;
  deleteRid = rid
}

const deleteRepair = () => {
  post('api/repair/delete', deleteRid, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const changeStep = (status) => {
  // console.log(status)
  switch (status) {
    case '待定':
      return 1
    case '处理中':
      return 2
    case '已解决':
      return 3
  }
}

const search = ref('')

const highlight = (text, keyword) => {
  if (!keyword.trim()) return text // 无关键词时返回原文

  // 转义正则特殊字符
  const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const regex = new RegExp(`(${escapedKeyword})`, 'gi') // 全局+忽略大小写

  return text.replace(regex, '<mark>$1</mark>')
}

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.description.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 7 // 每页显示的数据数量
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
  <div class="announce">
    <el-page-header content="报修中心" style="height: 40px;margin-left: 20px" @back="router.back()"/>
    <div class="top">
      <el-button type="success"
                 plain
                 :icon="Plus"
                 style="margin-left: 20px"
                 @click="dialogNewRepairVisible = true">
        申请报修
      </el-button>
      <el-input v-model="search"
                placeholder="查找报修"
                style="width: 300px;margin-left: 20px"/>
      <el-button type="primary"
                 :icon="Search"
                 style="margin-left: 20px"
                 @click=""
      >搜索
      </el-button>
    </div>
    <div class="content">
      <el-table :data="pagedData" height="380" style="width: 98%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="description" label="报修详情" width="300"
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
        <el-table-column label="操作" header-align="center" align="center">
          <template #default="scope">
            <el-button type="warning"
                       circle
                       plain
                       :icon="Edit"
                       v-if="scope.row.statusDesc === '待定'"
                       @click="getUpdateForm(scope.row.repairId)">
            </el-button>
            <el-button type="primary"
                       circle
                       plain
                       :icon="View"
                       @click="getRepair(scope.row.repairId)">
            </el-button>
            <el-button type="danger"
                       circle
                       plain
                       :icon="Delete"
                       @click="openDelete(scope.row.repairId)">
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="bottom">
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
    <el-dialog
            v-model="dialogNewRepairVisible"
            title="申请报修"
            width="600"
            style="margin-top: 50px"
    >
      <el-form :model="form" label-position="top">
        <el-steps style="max-width: 500px" :active="0" finish-status="success" simple>
          <el-step title="报修申请"/>
          <el-step title="报修受理"/>
          <el-step title="报修解决"/>
        </el-steps>
        <el-form-item label="报修详情">
          <el-input v-model="form.description"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    placeholder="请填写报修详情"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogNewRepairVisible = false">取消</el-button>
          <el-button
                  type="primary"
                  @click="newRepair">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogUpdateVisible"
            title="申请报修修改"
            width="600"
            style="margin-top: 50px"
    >
      <el-form :model="updateForm" label-position="top">
        <el-steps style="max-width: 500px" :active="1" finish-status="success" simple>
          <el-step title="报修申请"/>
          <el-step title="报修受理"/>
          <el-step title="报修解决"/>
        </el-steps>
        <el-form-item label="报修详情">
          <el-input v-model="updateForm.description"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    placeholder="请填写报修详情"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogUpdateVisible = false">取消</el-button>
          <el-button
                  type="warning"
                  @click="accept">
            修改
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogRepairVisible"
            title="报修处理进度"
            width="600"
            style="margin-top: 50px"
    >
      <el-form :model="repair" label-position="top">
        <el-steps style="max-width: 500px" :active="changeStep(repair.statusDesc)" finish-status="success" simple>
          <el-step title="报修申请"/>
          <el-step title="报修受理"/>
          <el-step title="报修解决"/>
        </el-steps>
        <el-form-item label="报修人">
          <el-input v-model="repair.submitterName"
                    disabled/>
        </el-form-item>
        <el-form-item label="报修详情">
          <el-input v-model="repair.description"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    disabled/>
        </el-form-item>
        <el-form-item label="提交时间">
          <el-date-picker type="date"
                          style="width: 100%;"
                          v-model="repair.submitTime"
                          disabled
                          format="YYYY/MM/DD HH:mm:ss"
                          value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>
        <el-row v-if="repair.statusDesc !== '待定'">
          <el-col :span="11">
            <el-form-item label="处理人">
              <el-input v-model="repair.handlerName"
                        disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="处理时间">
              <el-date-picker type="date"
                              style="width: 100%;"
                              v-model="repair.handleTime"
                              disabled
                              format="YYYY/MM/DD HH:mm:ss"
                              value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="处理结果" v-if="repair.statusDesc !== '待定'">
          <el-input v-model="repair.handleResult"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    disabled/>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
            v-model="dialogDeleteVisible"
            width="400"
            center
            align-center
    >
      <span>是否取消该报修?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteRepair">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.announce {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin: 20px auto;
  gap: 14px;
}

.top {
  margin: auto;
  display: flex;
  align-items: center;
  width: 80%;
  height: 80px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

.content {
  margin: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
  height: 390px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

.bottom {
  margin: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
  height: 80px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

mark {
  transition: background-color 0.3s ease;
  animation: highlightFade 1s;
}

@keyframes highlightFade {
  from {
    background-color: #ffff00;
  }
  to {
    background-color: #ffeb3b;
  }
}

</style>