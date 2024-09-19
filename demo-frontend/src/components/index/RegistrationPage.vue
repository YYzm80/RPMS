<script setup>

import {Select, CloseBold, Edit} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableDataTure = ref([])
const tableDataFalse = ref([])
const dialogUpdateVisible = ref(false)
const updateForm = ref([])

const getData = () => {
  get(`api/registration/all-state?state=${'0'}`, (data) => {
    tableDataFalse.value = data
  })
  get(`api/registration/all-state?state=${'1'}`, (data) => {
    tableDataTure.value = data
    get(`api/registration/all-state?state=${'2'}`, (data) => {
      tableDataTure.value.push(...data)
      initData()
    })
  })
}

const audit = (rid, state) => {
  post('api/registration/update', {
    rid: rid,
    state: state
  }, (message) => {
    ElMessage.success(message)
    getData()
  })
}

const getUpdateForm = (rid) => {
  get(`api/registration/rid?rid=${rid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  audit(updateForm.value.rid, updateForm.value.state)
  dialogUpdateVisible.value = false
}

const pageSize = 10 // 每页显示的数据数量
const currentPage = ref(1) // 当前页码
const total = ref(0) // 总数据条数
const pagedData = ref([]) // 当前页的数据
const currentPage2 = ref(1) // 当前页码
const total2 = ref(0) // 总数据条数
const pagedData2 = ref([]) // 当前页的数据

// 初始化数据
const initData = () => {
  total.value = tableDataFalse.value.length
  total2.value = tableDataTure.value.length
  updatePageData()
  updatePageData2()
}

// 更新当前页的数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  pagedData.value = tableDataFalse.value.slice(start, end)
}

const updatePageData2 = () => {
  const start = (currentPage2.value - 1) * pageSize
  const end = start + pageSize
  pagedData2.value = tableDataTure.value.slice(start, end)
}

// 处理页码变化的函数
const handlePageChange = (newPage) => {
  currentPage.value = newPage
  updatePageData()
}

const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage
  updatePageData2()
}

getData()
</script>

<template>
  <div class="content">
    <el-tabs type="border-card">
      <el-tab-pane label="未审核">
        <div class="bottom">
          <el-table
                  :data="pagedData"
                  height="540"
                  stripe
                  empty-text="没有更多需要审核的报名信息了，休息一下吧~"
                  style="width: 900px">
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
                <el-button-group>
                  <el-tooltip content="通过" placement="left" effect="light">
                    <el-button :icon="Select" type="success" @click="audit(scope.row.rid, '1')"></el-button>
                  </el-tooltip>
                  <el-tooltip content="不通过" placement="right" effect="light">
                    <el-button :icon="CloseBold" type="danger" @click="audit(scope.row.rid, '2')"></el-button>
                  </el-tooltip>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
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
      </el-tab-pane>
      <el-tab-pane label="已审核">
        <div class="bottom">
          <el-table
                  :data="pagedData2"
                  height="540"
                  stripe
                  empty-text="您还没有审核过报名信息呢，快去审核吧~"
                  style="width: 900px">
            <el-table-column label="序号" width="120" header-align="center" align="center" fixed>
              <template v-slot="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120" header-align="center" align="center"/>
            <el-table-column prop="gname" label="比赛名称" width="180" header-align="center" align="center" sortable/>
            <el-table-column prop="role" label="报名人员类别" width="160" header-align="center" align="center" sortable>
              <template #default="scope1">
                <el-tag v-if="scope1.row.role === 'athlete'" type="primary">运动员</el-tag>
                <el-tag v-if="scope1.row.role === 'referee'" type="warning">裁判员</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="state" label="审核状态" width="120" header-align="center" align="center" sortable>
              <template #default="scope1">
                <el-tag v-if="scope1.row.state === '0'" type="info">未审核</el-tag>
                <el-tag v-if="scope1.row.state === '1'" type="success">通过</el-tag>
                <el-tag v-if="scope1.row.state === '2'" type="danger">未通过</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-tooltip content="修改" placement="top" effect="light">
                  <el-button :icon="Edit" type="warning" @click="getUpdateForm(scope.row.rid)"></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <el-dialog
                  v-model="dialogUpdateVisible"
                  title="修改审核信息"
                  width="400"
          >
            <el-form :model="updateForm" label-position="top">
              <el-form-item label="审核状态">
                <el-radio-group v-model="updateForm.state">
                  <el-radio value="1">通过</el-radio>
                  <el-radio value="2">不通过</el-radio>
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
        </div>
        <div>
          <el-pagination
                  style="margin: 20px 350px"
                  layout="total, prev, pager, next"
                  background
                  :page-size="pageSize"
                  :current-page="currentPage2"
                  :total="total2"
                  @current-change="handlePageChange2"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>