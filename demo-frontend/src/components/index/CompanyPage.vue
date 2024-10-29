<script setup>

import {Delete, Edit, Location, Plus, Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import {ElMessage, genFileId} from "element-plus";
import {get, multipartPost, post} from "@/net";

const tableData = ref([])

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const upload = ref()
const fileList = ref([])

const handleExceed = function (files) {
  upload.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  upload.value.handleStart(file)
}

const getData = () => {
  get('api/company/all', (data) => {
    tableData.value = data
    initData()
  })
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

const add = () => {
  let formData = new FormData()
  console.log(fileList.value[0])
  formData.append("file", fileList.value[0].raw)
  formData.append("name", form.value.name)
  formData.append("introduce", form.value.introduce)
  formData.append("address", form.value.address)
  multipartPost('api/company/add', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    form.value = []
    getData()
    dialogNewVisible.value = false
  })
}

const openUpdate = (cid) => {
  get(`api/company/${cid}`, (data) => {
    updateForm.value = data
    fileList.value = [{
      'url': getImgSrc(data.photo),
    }]
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  let formData = new FormData()
  if (fileList.value[0] && fileList.value[0].raw) {
    // 用户选择了新文件，将其添加到formData中
    formData.append("file", fileList.value[0].raw)
  }
  formData.append("cid", updateForm.value.cid)
  formData.append("name", updateForm.value.name)
  formData.append("introduce", updateForm.value.introduce)
  formData.append("address", updateForm.value.address)
  multipartPost('api/company/update', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    getData()
    dialogUpdateVisible.value = false
  })
}
let deleteCid = 0;

const openDelete = (cid) => {
  deleteCid = cid
  dialogDeleteVisible.value = true
}

const deleteCompany = () => {
  post('api/company/delete', {
    cid: deleteCid
  }, (message) => {
    ElMessage.success(message)
    getData()
    dialogDeleteVisible.value = false
  })
}

const pageSize = 12 // 每页显示的数据数量
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
      <div style="position: fixed;left: 180px">
        <el-input style="width: 750px;height: 50px;" placeholder="搜索公司"></el-input>
        <el-button style="height: 50px;width: 120px;font-size: 20px" type="primary" plain @click="">
          <el-icon>
            <Search/>
          </el-icon>
          搜索
        </el-button>
      </div>
      <div style="right: 50px;position: fixed" v-if="user.role === 'admin'">
        <el-button type="success"
                   style="width: 100px;height: 50px;font-size: 15px"
                   plain
                   @click="dialogNewVisible = true">
          <el-icon>
            <Plus/>
          </el-icon>
          注册公司
        </el-button>
      </div>
    </div>
    <div class="bottom">
      <el-space wrap :size="20">
        <el-card class="card" shadow="hover" v-for="c in pagedData">
          <div style="display: flex;flex-direction: row;margin: 20px 0 0 20px">
            <el-image
                    fit="cover"
                    :src="getImgSrc(c.photo)"
                    style="width: 60px;height: 60px;"
            />
            <div style="display: flex;flex-direction: column;margin-left: 10px;text-align: left">
              <span class="cName">{{ c.name }}</span>
              <span class="intro">{{ c.introduce }}</span>
              <div class="intro">
                <el-icon size="14">
                  <Location/>
                </el-icon>
                <span>{{ c.address }}</span>
              </div>
            </div>
          </div>
          <el-divider style="margin: 7px 0 7px 0"/>
          <el-button type="primary" plain @click="openUpdate(c.cid)" v-if="user.role === 'admin'">
            <el-icon>
              <Edit/>
            </el-icon>
          </el-button>
          <el-button type="danger" plain @click="openDelete(c.cid)" v-if="user.role === 'admin'">
            <el-icon>
              <Delete/>
            </el-icon>
          </el-button>
        </el-card>
      </el-space>
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
    <el-dialog
            v-model="dialogNewVisible"
            title="注册公司"
            width="600"
    >
      <el-form :model="form" label-position="left">
        <el-form-item label="公司名称">
          <el-input v-model="form.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input
                  v-model="form.introduce"
                  autocomplete="off"
                  type="textarea"
                  style="width: 700px;"
                  rows="5"
                  resize='none'
                  maxlength="300"
                  show-word-limit/>
        </el-form-item>
        <el-form-item label="公司地址">
          <el-input v-model="form.address" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="公司Logo">
          <el-upload
                  ref="upload"
                  list-type="picture-card"
                  v-model:file-list="fileList"
                  :show-file-list="true"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :auto-upload="false"
                  accept="image/png,image/gif,image/jpg,image/jpeg"
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
            title="修改公司信息"
            width="600"
    >
      <el-form :model="updateForm" label-position="left">
        <el-form-item label="公司名称">
          <el-input v-model="updateForm.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input
                  v-model="updateForm.introduce"
                  autocomplete="off"
                  type="textarea"
                  style="width: 700px;"
                  rows="5"
                  resize='none'
                  maxlength="300"
                  show-word-limit/>
        </el-form-item>
        <el-form-item label="公司地址">
          <el-input v-model="updateForm.address" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="公司Logo">
          <el-upload
                  ref="upload"
                  list-type="picture-card"
                  v-model:file-list="fileList"
                  :show-file-list="true"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :auto-upload="false"
                  accept="image/png,image/gif,image/jpg,image/jpeg"
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
      <span>是否删除该公司信息?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteCompany">
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
  padding-left: 180px;
  padding-top: 15px;
  height: 480px;
}

.card {
  width: 280px;
  height: 140px;
  cursor: pointer;
}

.cName:hover {
  color: #5baecb;
}

.cName {
  font-size: 18px;
  font-weight: 400;
}

.intro {
  margin-top: 3px;
  font-size: 13px;
  width: 170px;
  color: #555666;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
}

/deep/ .el-card__body {
  padding: 0;
}

</style>