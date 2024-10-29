<script setup>

import {CircleCheck, CircleClose, Delete, Edit, Message, Refresh, UploadFilled, ZoomIn} from "@element-plus/icons-vue";
import {ref} from "vue";
import {ElMessage, genFileId} from "element-plus";
import pdfIcon from "../../assets/pdf.png";
import {get, multipartPost, post} from "@/net";
import PDFViewer from "@/components/index/inner/PDFViewer.vue";

const tableData1 = ref([])
const tableData2 = ref([])
const updateForm = ref()
const dialogVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)

const edit = ref(false)
const loading = ref(false)

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const upload = ref()
const fileList = ref([])
const readFileList = ref([])
const activeName = ref('one')
let pdfSrc = ref('')

const handleExceed = function (files) {
  upload.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  upload.value.handleStart(file)
}

const handleChange = function (file) {
  // console.log(file)
  let array = file.name.split('.')
  let suffix = array[1]
  // console.log(fileList.value)
  if (suffix.toLowerCase() !== 'pdf') { // 添加toLowerCase()来确保比较不区分大小写
    fileList.value = fileList.value.filter(f => f.uid !== file.uid); // 使用filter来删除特定文件
    ElMessage.warning("请上传pdf文件");
    upload.value.clearFiles();
  } else {
    // 如果文件是PDF，确保将其添加到fileList中
    if (!fileList.value.some(f => f.uid === file.uid)) {
      fileList.value.push(file);
    }
  }
}

const deleteUpload = (file) => {
  fileList.value = fileList.value.filter(f => f.uid !== file.raw.uid)
  // console.log(fileList.value)
}

function changeEdit() {
  edit.value = !edit.value
  fileList.value = []
}

const getImgSrc = (picName) => {
  if (picName === null) {
    return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  } else {
    return `http://localhost:8080/uploaded/${picName}`
  }
}

const getData = () => {
  get(`api/deliver/all-uid/${user.uid}`, (data) => {
    tableData1.value = data
    getMyResume()
    let i = 1
    tableData1.value.forEach((item) => {
      item.index = i
      i++
    })
    getInterviewData()
  })

}

function getInterviewData() {
  get(`api/interview/all-uid/${user.uid}`, (data) => {
    tableData2.value = data
    let i = 1
    tableData2.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

function getMyResume() {
  get(`api/resume/all-uid/${user.uid}`, (data) => {
    if (data !== null) {
      readFileList.value = []
      tableData1.value.resume = data
      data.forEach(i => {
        readFileList.value.push(i)
      })
    }
  })
}

const add = () => {
  let formData = new FormData()
  formData.append("filePDF", fileList.value[0].raw)
  formData.append("uid", user.uid)
  formData.append("name", fileList.value[0].raw.name)
  loading.value = true
  multipartPost('api/resume/add',
          formData, (message) => {
            ElMessage.success(message)
            fileList.value = []
            getData()
            loading.value = false
            edit.value = false
          }, (message) => {
            ElMessage.warning(message)
            loading.value = false
          })
}

const preview = (fileName) => {
  dialogVisible.value = true
  pdfSrc = getImgSrc(fileName);
}

const deleteFile = (rid) => {
  post('api/resume/delete', {
    rid: rid
  }, (message) => {
    ElMessage.success(message)
    getData()
  })
}

function openUpdate(did) {
  get(`api/deliver/${did}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/deliver/update', {
    did: updateForm.value.did,
    resumeId: updateForm.value.resumeId
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogUpdateVisible.value = false
  })
}

let deleteDid = 0

function openDelete(did) {
  deleteDid = did
  dialogDeleteVisible.value = true
}

const cancel = () => {
  post('api/deliver/delete', {
    did: deleteDid
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogDeleteVisible.value = false
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
  <el-scrollbar height="100vh">
    <div class="content">
      <div class="top">
        <el-avatar :size="110" style="margin: 40px 50px" :src="getImgSrc(user.avatar)"></el-avatar>
        <div class="top-info">
          <span style="font-size: 20px">{{ user.username }}，你好!</span>
          <span style="font-size: 13px;color: gray;margin-top: 5px">uid:{{ user.uid }}</span>
          <el-divider style="width: 300px;"/>
          <span style="font-size: 15px">
            <el-icon>
              <Message/>
            </el-icon>
            邮箱地址：{{ user.email }}
          </span>
        </div>
        <el-button type="success" style="margin: 40px 10px 0 310px" plain v-if="edit" @click="add" v-loading="loading">
          上传
        </el-button>
        <el-button type="danger" style="margin: 40px 0 0 0" plain v-if="edit" @click="changeEdit()">取消</el-button>
        <el-button type="primary" style="margin: 40px 0 0 350px" plain v-if="!edit" @click="changeEdit()">上传简历
        </el-button>
      </div>
      <div class="bottom">
        <el-tabs
                v-model="activeName"
                class="demo-tabs"
        >
          <el-tab-pane label="个人简历" name="one">
            <div style="margin: 20px 20px">
              <p style="font-size: 20px;font-weight: bold">个人简历</p>
              <el-upload
                      v-if="edit"
                      accept=".pdf"
                      :file-list="fileList"
                      :show-file-list="false"
                      :on-exceed="handleExceed"
                      :on-change="handleChange"
                      :auto-upload="false"
                      limit="1"
                      drag
              >
                <div class="results_pdfFile_upload">
                  <el-icon style="font-size:33px;color: #b6b5b5">
                    <UploadFilled/>
                  </el-icon>
                  <div class="results_pdfFile_upload_text">上传附件</div>
                </div>
              </el-upload>
              <div v-for="file in fileList" class="results_pdfFile">
                <el-image :src="pdfIcon" style="width: 50px;"></el-image>
                <div style="width: 350px;display: flex;flex-direction: column">
                  <span style="width: 350px;overflow: hidden">{{ file.raw.name }}</span>
                  <el-link :underline="false" @click="deleteUpload(file)">
                    <el-icon>
                      <Delete/>
                    </el-icon>
                    删除
                  </el-link>
                </div>
              </div>
              <div class="bottom_top">
                <div style="margin: 0 10px;width: 11px;height: 30px;background-color: #7853f1"></div>
                <span style="font-size: 18px;font-weight: bold;text-align: left">
              我的简历({{ readFileList.length }}/5)</span>
              </div>
              <div v-for="file in readFileList" class="results_pdfFile">
                <el-image :src="pdfIcon" style="width: 50px;"></el-image>
                <div style="width: 350px;display: flex;flex-direction: column">
                  <span style="width: 350px;overflow: hidden">{{ file.name }}</span>
                  <div>
                    <el-link :underline="false" @click="preview(file.file)">
                      <el-icon>
                        <ZoomIn/>
                      </el-icon>
                      预览
                    </el-link>
                    <el-divider direction="vertical"/>
                    <el-link :underline="false" @click="deleteFile(file.rid)">
                      <el-icon>
                        <Delete/>
                      </el-icon>
                      删除
                    </el-link>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="已投递" name="two">
            <el-table :data="pagedData1"
                      height="570"
                      style="width: 100%;margin: auto"
                      border
                      stripe
                      empty-text="您还没有投递过简历呢~"
            >
              <el-table-column prop="index" label="序号" width="120" header-align="center" align="center"/>
              <el-table-column
                      prop="recruitmentName"
                      label="岗位名称"
                      width="180"
                      header-align="center"
                      align="center"
                      show-overflow-tooltip/>
              <el-table-column
                      prop="resumeFile"
                      label="简历名称"
                      width="120"
                      header-align="center"
                      align="center"
                      show-overflow-tooltip/>
              <el-table-column prop="deliveryTime" label="投递时间" width="180" header-align="center" align="center"/>
              <el-table-column prop="state" label="投递状态" width="120" header-align="center" align="center">
                <template #default="scope1">
                  <el-tag v-if="scope1.row.state === '1'" type="success">已查看</el-tag>
                  <el-tag v-if="scope1.row.state === '0'" type="info">未查看</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button-group v-if="!scope.row.interview">
                    <el-button :icon="Edit" type="primary" @click="openUpdate(scope.row.did)"></el-button>
                    <el-button :icon="Delete" type="danger" @click="openDelete(scope.row.did)"></el-button>
                  </el-button-group>
                  <span v-else>简历通过了~</span>
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
          <el-tab-pane label="面试结果" name="three">
            <el-table :data="pagedData2"
                      height="570"
                      style="width: 100%;margin: auto"
                      border
                      stripe
                      empty-text="您还没有面试过呢~"
            >
              <el-table-column prop="index" label="序号" width="120" header-align="center" align="center"/>
              <el-table-column prop="recruitmentName"
                               label="岗位名称"
                               width="280"
                               header-align="center"
                               align="center"
                               show-overflow-tooltip/>
              <el-table-column prop="username" label="面试人" width="120" header-align="center" align="center"/>
              <el-table-column prop="state" label="面试状态" header-align="center" align="center">
                <template #default="scope">
                  <el-tag v-if="scope.row.state === '0'" type="primary">面试中</el-tag>
                  <el-tag v-if="scope.row.state === '1'" type="success">通过</el-tag>
                  <el-tag v-if="scope.row.state === '2'" type="danger">未通过</el-tag>
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
      </div>
    </div>
    <el-dialog
            v-model="dialogUpdateVisible"
            title="更换简历"
            width="25%"
            center
            align-center
    >
      <el-form :model="updateForm" label-position="left" label-width="50">
        <el-form-item label="简历">
          <el-select
                  v-model="updateForm.resumeId"
                  aria-required="true"
                  placeholder="请选择您要更换的简历"
                  style="width: 240px"
          >
            <el-option
                    v-for="r in tableData1.resume"
                    :label="r.name"
                    :value="r.rid"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="text-align: center">
          <el-button @click="dialogUpdateVisible = false">取消</el-button>
          <el-button type="warning" @click="update">
            更换
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogDeleteVisible"
            width="300"
            center
            align-center
    >
      <span>是否取消投递?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="cancel">
            确认
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
  </el-scrollbar>
</template>

<style scoped>
.content {
  width: 1014px;
  display: flex;
  flex-direction: column;
  margin: 20px auto;
  gap: 14px;
}

.top {
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
  height: 200px;
  width: 1014px;
  display: flex;
  flex-direction: row;
  margin: auto;
}

.top-info {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  text-align: left;
}

.bottom {
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
  height: 700px;
  width: 1014px;
  background-color: rgba(255, 255, 255, 0.7);
  margin: auto;
}

.results_pdfFile {
  border: 1px dashed #d9d9d9;
  width: 400px;
  height: 50px;
  margin-top: 10px;
  display: flex;
  flex-direction: row;
}

.bottom_top {
  display: flex;
  flex-direction: row;
  line-height: 30px;
  margin: 30px 0;
}

.demo-tabs {
  margin: 0 30px 0 30px;
}
</style>