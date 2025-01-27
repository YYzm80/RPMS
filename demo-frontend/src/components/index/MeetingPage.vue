<script setup>

import {Search, Plus} from "@element-plus/icons-vue";
import {computed, ref} from "vue";
import {ElMessage, genFileId} from "element-plus";
import router from "@/router";
import {get, multipartPost, post} from "@/net";

const tableData = ref([])
const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const loading = ref(false)
const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])

const upload = ref()
const fileList = ref([])
let deleteMid = 0;

const handleExceed = function (files) {
  upload.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  upload.value.handleStart(file)
}

const getData = () => {
  get('api/meeting/all', (data) => {
    tableData.value = data
    initData()
  })
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

const add = () => {
  let formData = new FormData()
  // console.log(form.value)
  formData.append("file", fileList.value[0].raw)
  formData.append("name", form.value.name)
  formData.append("startTime", form.value.time[0])
  formData.append("endTime", form.value.time[1])
  multipartPost('api/meeting/add', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    form.value = []
    getData()
    dialogNewVisible.value = false
  })
}

function openUpdate(mid) {
  get(`api/meeting/${mid}`, (data) => {
    fileList.value = [{
      'url': getImgSrc(data.img),
    }]
    updateForm.value = data
    updateForm.value.time = [data.startTime, data.endTime]
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  let formData = new FormData()
  // console.log(updateForm.value)
  if (fileList.value[0] && fileList.value[0].raw) {
    // 用户选择了新文件，将其添加到formData中
    formData.append("file", fileList.value[0].raw)
  }
  formData.append("mid", updateForm.value.mid)
  formData.append("name", updateForm.value.name)
  formData.append("startTime", updateForm.value.time[0])
  formData.append("endTime", updateForm.value.time[1])
  multipartPost('api/meeting/update', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    updateForm.value = []
    getData()
    dialogUpdateVisible.value = false
  })
}

function openDelete(mid) {
  deleteMid = mid
  dialogDeleteVisible.value = true
}

const deleteMeeting = () => {
  post('api/meeting/delete', {
    mid: deleteMid
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogDeleteVisible.value = false
  })
}

function intoMeeting(mid, state) {
  switch (state) {
    case "0": ElMessage.warning("该双选会还未开始，请耐心等待");break
    case "1": router.push({path:'/index/recruitment',query:{mid:mid, state:state}});break
    case "2": ElMessage.warning("该双选会已经结束，无法再次进入");break
  }
}

function doStatic(mid) {
  loading.value = true
  get(`/api/static/static-mid/${mid}`, (data) => {
    if (data == null) {
      post('api/static/do-static', {
        mid: mid
      }, (message) => {
        ElMessage.success(message)
        loading.value = false
        router.push({path:'/index/static',query:{mid:mid}})
      })
    } else {
      loading.value = false
      router.push({path:'/index/static',query:{mid:mid}})
    }
  })
}

const search = ref('')

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.name.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 6 // 每页显示的数据数量
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
      <div style="position: fixed;left: 256px">
        <el-input style="width: 750px;height: 50px;" placeholder="搜索双选会" v-model="search"></el-input>
        <el-button style="height: 50px;width: 120px;font-size: 20px" type="primary" plain @click="">
          <el-icon>
            <Search/>
          </el-icon>
          搜索
        </el-button>
      </div>
      <div style="right: 50px;position: fixed">
        <el-button type="success"
                   style="width: 100px;height: 50px;font-size: 15px"
                   plain
                   @click="dialogNewVisible = true"
                   v-if="user.role === 'admin'">
          <el-icon>
            <Plus/>
          </el-icon>
          新增双选会
        </el-button>
      </div>
    </div>
    <div class="bottom">
      <el-scrollbar height="95%">
        <div class="meeting" v-for="m in pagedData" @click="intoMeeting(m.mid, m.state)">
          <el-image
                  :src="getImgSrc(m.img)"
                  alt="宣传图"
                  fit="cover"
                  style="height: 150px;width: 300px;"/>
          <div style="margin-left: 30px;margin-top: 10px;display: flex;flex-direction: column">
            <span style="font-size: 20px;font-weight: bold">{{ m.name }}</span>
            <el-tag style="position: absolute;right: 20px" v-if="m.state === '0'" type="info">未开始</el-tag>
            <el-tag style="position: absolute;right: 20px" v-if="m.state === '1'" type="success">进行中</el-tag>
            <el-tag style="position: absolute;right: 20px" v-if="m.state === '2'" type="danger">已结束</el-tag>
            <div style="font-size: 14px;color: #464545;margin-top: 10px">
              <span v-if="m.state !== '0'">参会企业数：{{ m.companyCount }}</span>
              <span v-if="m.state !== '0'" style="margin-left: 60px">招聘岗位数：{{ m.recruitmentCount }}</span>
              <span v-if="m.state === '0'">正在火热报名中...</span>
            </div>
            <div style="font-size: 14px;color: #464545;margin-top: 10px">
              <span>开始时间：{{ m.startTime }}</span>
              <span style="margin-left: 60px">结束时间：{{ m.endTime }}</span>
            </div>
            <div style="margin-top: 15px;gap: 10px">
              <el-button plain type="warning"
                         v-if="m.state === '0' && (user.role === 'admin' || user.role === 'company')"
                         @click="router.push({path:'/index/recruitment',query:{mid:m.mid,state:'0'}})">报名参加选会
              </el-button>
              <el-button plain type="success"
                         v-if="m.state === '2' && (user.role === 'admin' || user.role === 'counsellor')"
                         @click="doStatic(m.mid)"
                         v-loading="loading">查看统计数据
              </el-button>
              <el-button plain type="primary"
                         v-if="m.state === '0' && user.role === 'admin'"
                         @click="openUpdate(m.mid)">修改
              </el-button>
              <el-button plain type="danger"
                         v-if="user.role === 'admin' && m.state !== '1'"
                         @click="openDelete(m.mid)">删除
              </el-button>
            </div>
          </div>
        </div>
        <el-pagination
                style="margin: 10px 350px"
                layout="total, prev, pager, next"
                background
                :page-size="pageSize"
                :current-page="currentPage"
                :total="total"
                @current-change="handlePageChange"
        />
      </el-scrollbar>
    </div>
    <el-dialog
            v-model="dialogNewVisible"
            title="新增双选会"
            width="600"
    >
      <el-form :model="form" label-position="left" label-width="90">
        <el-form-item label="双选会名称">
          <el-input v-model="form.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="选会时间">
          <div class="block">
            <el-date-picker
                    v-model="form.time"
                    type="datetimerange"
                    range-separator="-"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
            />
          </div>
        </el-form-item>
        <el-form-item label="宣传图">
          <el-upload
                  ref="upload"
                  list-type="picture-card"
                  v-model:file-list="fileList"
                  :show-file-list="true"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :auto-upload="false"
          >
            <template #file="{ file }">
              <div>
                <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
              </div>
            </template>
            <el-icon class="avatar-uploader-icon">
              <Plus/>
            </el-icon>
          </el-upload>
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
            title="修改双选会信息"
            width="600"
    >
      <el-form :model="updateForm" label-position="left" label-width="90">
        <el-form-item label="双选会名称">
          <el-input v-model="updateForm.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="选会时间">
          <div class="block">
            <el-date-picker
                    v-model="updateForm.time"
                    type="datetimerange"
                    range-separator="-"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
            />
          </div>
        </el-form-item>
        <el-form-item label="宣传图">
          <el-upload
                  ref="upload"
                  list-type="picture-card"
                  v-model:file-list="fileList"
                  :show-file-list="true"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :auto-upload="false"
          >
            <template #file="{ file }">
              <div>
                <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
              </div>
            </template>
            <el-icon class="avatar-uploader-icon">
              <Plus/>
            </el-icon>
          </el-upload>
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
      <span>是否删除该双选会?<span style="color: red">其下所有数据也会被一并永久删除</span></span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteMeeting">
            删除
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.top {
  height: 80px;
  background-color: white;
  padding-top: 20px;
}

.bottom {
  margin: auto;
  height: 600px;
  width: 1024px;
  background-color: rgba(255, 255, 255, 0.7);
}

.meeting {
  width: 100%;
  height: 150px;
  display: flex;
  flex-direction: row;
  position: relative;
  border-bottom: #181818 1px solid;
  background-color: rgba(236, 235, 235, 0.7);
}

.meeting:hover {
  cursor: pointer;
  background-color: rgb(190, 187, 187);
}

</style>