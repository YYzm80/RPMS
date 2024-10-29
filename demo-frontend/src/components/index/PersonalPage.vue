<script setup>

import {Plus, Message} from "@element-plus/icons-vue";
import {ref} from "vue";
import {ElMessage, genFileId} from "element-plus";
import {get, multipartPost} from "@/net";

const updateForm = ref([])
const edit = ref(false)
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

function changeEdit() {
  edit.value = !edit.value
  getData()
}

const getImgSrc = (picName) => {
  if (picName === null) {
    return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  } else {
    return `http://localhost:8080/uploaded/${picName}`
  }
}

const getData = () => {
  get(`api/user/uid?uid=${user.uid}`, (data) => {
    updateForm.value = data
    getCompany()
    console.log(updateForm.value)
  })

}

const getCompany = () => {
  get('api/company/all', (data) => {
    updateForm.value.company = data
  })
}

const update = () => {
  let formData = new FormData()
  if (fileList.value[0] && fileList.value[0].raw) {
    // 用户选择了新文件，将其添加到formData中
    formData.append("file", fileList.value[0].raw)
  }
  formData.append("uid", updateForm.value.uid)
  if (updateForm.value.cid !== null) {
    formData.append("cid", updateForm.value.cid)
  }
  formData.append("name", updateForm.value.name)
  formData.append("username", updateForm.value.username)
  console.log(updateForm.value)
  multipartPost('api/user/update',
    formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    getData()
    edit.value = false
  })
}

getData()
</script>

<template>
  <el-scrollbar height="100vh">
    <div class="content">
      <div class="top">
        <el-avatar :size="110" style="margin: 40px 50px" :src="getImgSrc(user.avatar)"></el-avatar>
        <div class="top-info">
          <span style="font-size: 20px">{{user.username}}，你好!</span>
          <span style="font-size: 13px;color: gray;margin-top: 5px">uid:{{user.uid}}</span>
          <el-tag v-if="user.role === 'admin'" style="margin-top: 5px;width: 100px;" effect="plain" type="warning">
            管理员
          </el-tag>
          <el-tag v-if="user.role === 'student'" style="margin-top: 5px;width: 100px;" effect="plain" type="primary">
            学生账号
          </el-tag>
          <el-tag v-if="user.role === 'company'" style="margin-top: 5px;width: 100px;" effect="plain" type="danger">
            企业账号
          </el-tag>
          <el-tag v-if="user.role === 'counsellor'" style="margin-top: 5px;width: 100px;" effect="plain" type="info">
            辅导员
          </el-tag>
          <el-divider style="width: 300px;margin-top: 2px"/>
          <span style="font-size: 15px">
            <el-icon>
              <Message/>
            </el-icon>
            邮箱地址：{{user.email}}
          </span>
        </div>
        <el-button type="success" style="margin: 40px 10px 0 310px" plain v-if="edit" @click="update">保存</el-button>
        <el-button type="danger" style="margin: 40px 0 0 0" plain v-if="edit" @click="changeEdit()">取消</el-button>
        <el-button type="primary" style="margin: 40px 0 0 380px" plain v-if="!edit" @click="changeEdit()">编辑</el-button>
      </div>
      <div class="bottom">
        <div class="bottom-left">
          <div style="margin: 20px 20px">
            <p style="font-size: 20px;font-weight: bold">个人信息</p>
            <el-form :model="updateForm" label-position="left" label-width="140px">
              <el-row :gutter="50">
                <el-col span="12">
                  <el-form-item label="昵称/用户名">
                    <el-input v-model="updateForm.username"
                              autocomplete="off"
                              style="width: 150px;"
                              :disabled="!edit"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="姓名" label-width="50px">
                    <el-input
                        v-model="updateForm.name"
                        autocomplete="off"
                        style="width: 150px;"
                        :disabled="!edit"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="所属公司" v-if="user.role === 'company' || user.role === 'admin'">
                <el-select v-model="updateForm.cid" type="text" :maxlength="10" placeholder="请选择公司" style="width: 300px;"
                           :disabled="!edit">
                  <el-option v-for="c in updateForm.company" :label="c.name" :value="c.cid"/>
                </el-select>
              </el-form-item>
              <el-form-item label="上传新头像">
                <el-upload
                    ref="upload"
                    list-type="picture-card"
                    v-model:file-list="fileList"
                    :show-file-list="true"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :auto-upload="false"
                    :disabled="!edit"
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
              <el-form-item label="注册时间">
                <el-input v-model="updateForm.create_time"
                          autocomplete="off"
                          style="width: 200px;"
                          disabled/>
              </el-form-item>
              <el-form-item label="最近一次修改时间">
                <el-input v-model="updateForm.update_time"
                          autocomplete="off"
                          style="width: 200px;"
                          disabled/>
              </el-form-item>
            </el-form>
          </div>

        </div>
        <div class="bottom-right">
          <div>
            <el-calendar style="margin-top: 20px"/>
          </div>
        </div>
      </div>
    </div>
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
}

.top-info {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  text-align: left;
}

.bottom {
  width: 1014px;
  display: flex;
  flex-direction: row;
  margin: auto;
  gap: 14px;
}

.bottom-left {
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
  height: 700px;
  width: 676px;
}

.bottom-right {
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
  height: 610px;
  width: 324px;
}
</style>