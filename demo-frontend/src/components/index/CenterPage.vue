<script setup>

import {
  Promotion,
  ZoomIn,
  CircleCheck,
  CircleClose, Refresh
} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import PDFViewer from "@/components/index/inner/PDFViewer.vue";
import {ElMessage} from "element-plus";

const tableData1 = ref([])
const tableData2 = ref([])

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const activeName = ref('one')
const dialogVisible = ref(false)
const dialogDeleteVisible = ref(false)

let pdfSrc = ref('')

const getData = () => {
  get(`api/deliver/all-cid/${user.cid}`, (data) => {
    tableData1.value = data
    let i = 1
    tableData1.value.forEach((item) => {
      item.index = i
      i++
    })
    getInterviewData()
    // console.log(tableData1.value)
  })
}

function getInterviewData() {
  get(`api/interview/all-cid/${user.cid}`, (data) => {
    tableData2.value = data
    let i = 1
    tableData2.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

const preview = (fileName, did, state) => {
  dialogVisible.value = true
  pdfSrc = getImgSrc(fileName);
  if (state === '0') {
    post('api/deliver/update', {
      state: 1,
      did: did
    }, () => {
      getData()
    })
  }
}

const postInterview = (uid, rid) => {
  post('api/interview/add', {
    uid: uid,
    rid: rid,
    state: 0
  }, (message) => {
    ElMessage.success(message)
    getData()
  })
}

const update = (iid, state) => {
  post('api/interview/update', {
    iid: iid,
    state: state
  }, (message) => {
    ElMessage.success(message)
    getData()
  })
}

const pageSize = 10 // 每页显示的数据数量
const currentPage1 = ref(1) // 当前页码
const total1 = ref(0) // 总数据条数
const pagedData1 = ref([]) // 当前页的数据
const currentPage2 = ref(1) // 当前页码
const total2 = ref(0) // 总数据条数
const pagedData2 = ref([]) // 当前页的数据

// 初始化数据
const initData = () => {
  total1.value = tableData1.value.length
  total2.value = tableData2.value.length
  updatePageData1()
  updatePageData2()
}

// 更新当前页的数据
const updatePageData1 = () => {
  const start = (currentPage1.value - 1) * pageSize
  const end = start + pageSize
  pagedData1.value = tableData1.value.slice(start, end)
}

const updatePageData2 = () => {
  const start = (currentPage1.value - 1) * pageSize
  const end = start + pageSize
  pagedData2.value = tableData2.value.slice(start, end)
}

// 处理页码变化的函数
const handlePageChange1 = (newPage) => {
  currentPage1.value = newPage
  updatePageData1()
}

const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage
  updatePageData2()
}

getData()
</script>

<template>
  <div class="content">
    <div class="top">
      <div style="position: fixed;left: 258px">
        <span style="font-size: 30px;font-weight: lighter">招聘中心</span>
      </div>
    </div>
    <div class="bottom">
      <el-tabs
              v-model="activeName"
              class="demo-tabs"
      >
        <el-tab-pane label="投递管理" name="one">
          <el-table :data="pagedData1"
                    height="480"
                    style="width: 100%;margin: auto"
                    border
                    stripe
          >
            <el-table-column prop="index" label="序号" width="120" header-align="center" align="center"/>
            <el-table-column prop="recruitmentName"
                             label="岗位名称"
                             width="200"
                             header-align="center"
                             align="center"
                             show-overflow-tooltip
                             sortable/>
            <el-table-column prop="username" label="投递人" width="120" header-align="center" align="center"/>
            <el-table-column prop="deliveryTime" label="投递时间" width="200" header-align="center" align="center"/>
            <el-table-column prop="state" label="状态" width="120" header-align="center" align="center" sortable>
              <template #default="scope">
                <el-tag v-if="scope.row.state === '1'" type="success">已查看</el-tag>
                <el-tag v-if="scope.row.state === '0'" type="info">未查看</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-link :underline="false"
                         @click="preview(scope.row.resumeFile, scope.row.did, scope.row.state)">
                  查看简历
                  <el-icon class="el-icon--right">
                    <ZoomIn/>
                  </el-icon>
                </el-link>
                <el-divider direction="vertical" v-if="!scope.row.interview"/>
                <el-link :underline="false"
                         @click="postInterview(scope.row.uid, scope.row.rid)"
                         v-if="!scope.row.interview">
                  发出面试
                  <el-icon class="el-icon--right">
                    <Promotion/>
                  </el-icon>
                </el-link>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
                  style="margin: 10px 350px"
                  layout="total, prev, pager, next"
                  background
                  :page-size="pageSize"
                  :current-page="currentPage1"
                  :total="total1"
                  @current-change="handlePageChange1"
          />
        </el-tab-pane>
        <el-tab-pane label="面试安排" name="two">
          <el-table :data="pagedData2"
                    height="480"
                    style="width: 100%;margin: auto"
                    border
                    stripe
          >
            <el-table-column prop="index" label="序号" width="120" header-align="center" align="center"/>
            <el-table-column prop="recruitmentName"
                             label="岗位名称"
                             width="200"
                             header-align="center"
                             align="center"
                             sortable
                             show-overflow-tooltip/>
            <el-table-column prop="username" label="面试人" width="120" header-align="center" align="center"/>
            <el-table-column prop="state" label="面试状态" width="120" header-align="center" align="center" sortable>
              <template #default="scope">
                <el-tag v-if="scope.row.state === '0'" type="primary">面试中</el-tag>
                <el-tag v-if="scope.row.state === '1'" type="success">通过</el-tag>
                <el-tag v-if="scope.row.state === '2'" type="danger">未通过</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-link :underline="false" type="success"
                         v-if="scope.row.state === '0'"
                         @click="update(scope.row.iid, '1')">
                  通过
                  <el-icon class="el-icon--right">
                    <CircleCheck/>
                  </el-icon>
                </el-link>
                <el-divider direction="vertical" v-if="scope.row.state === '0'"/>
                <el-link :underline="false" type="danger"
                         v-if="scope.row.state === '0'"
                         @click="update(scope.row.iid, '2')">
                  不通过
                  <el-icon class="el-icon--right">
                    <CircleClose/>
                  </el-icon>
                </el-link>
                <el-link :underline="false" type="primary"
                         v-if="scope.row.state !== '0'"
                         @click="update(scope.row.iid, '0')">
                  重新评估
                  <el-icon class="el-icon--right">
                    <Refresh/>
                  </el-icon>
                </el-link>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
                  style="margin: 10px 350px"
                  layout="total, prev, pager, next"
                  background
                  :page-size="pageSize"
                  :current-page="currentPage2"
                  :total="total2"
                  @current-change="handlePageChange2"
          />
        </el-tab-pane>
      </el-tabs>
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
            <el-button type="danger" @click="dialogDeleteVisible = false">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog v-model="dialogVisible" title="简历预览" width="80%" top="2vh">
        <PDFViewer
                page-scale="page-fit"
                theme="dark"
                style="width: 100%;height: 630px;"
                :src="pdfSrc"/>

      </el-dialog>
    </div>
  </div>
</template>

<style scoped>
.top {
  height: 70px;
  background-color: white;
  padding-top: 20px;
}

.bottom {
  width: 70%;
  height: 570px;
  margin: auto;
  background-color: white;
}

.demo-tabs {
  margin: 0 30px 0 30px;
}
</style>