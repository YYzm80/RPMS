<script setup>

import { Message } from "@element-plus/icons-vue";
import { ref } from "vue";
import { ElMessage } from "element-plus";
import {get, put} from "@/net";

const updateForm = ref([]);
const edit = ref(false);
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));

function changeEdit() {
  edit.value = !edit.value;
  getData();
}

const getImgSrc = (picName) => {
  if (picName === null) {
    return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  } else {
    return `http://localhost:8080/uploaded/${picName}`
  }
}

const getData = () => {
  get(`api/user/uid/${user.uid}`, (data) => {
    updateForm.value = data;
    // getPosition();
    console.log(updateForm.value)
  });

}

const update = () => {
    put("api/user/update-personal", {
        userId: user.uid,
        username: updateForm.value.username,
        realName: updateForm.value.realName,
        gender: updateForm.value.gender,
        position: updateForm.value.position,
        phone: updateForm.value.phone
    }, (message) => {
      ElMessage.success(message)
      changeEdit()
    })
}

getData();

</script>

<template>
  <el-scrollbar height="100vh">
    <div class="content">
      <div class="top">
        <el-avatar :size="110" style="margin: 40px 50px">
          <!-- 使用 v-lazy 替换 v-loading 和 img 标签的组合 -->
          <img v-lazy="getImgSrc(null)" alt="Avatar" />
        </el-avatar>
        <div class="top-info">
          <span style="font-size: 20px">{{user.username}}，你好!</span>
          <span style="font-size: 13px;color: gray;margin-top: 5px">uid:{{user.uid}}</span>
          <el-tag v-if="user.role === 'admin'" style="margin-top: 5px;width: 100px;" effect="plain" type="warning">
            管理员
          </el-tag>
          <el-tag v-if="user.role === 'manager'" style="margin-top: 5px;width: 100px;" effect="plain" type="danger">
            物业人员
          </el-tag>
          <el-tag v-if="user.role === 'owner'" style="margin-top: 5px;width: 100px;" effect="plain" type="success">
            业主
          </el-tag>
          <el-divider style="width: 300px;margin-top: 2px"/>
          <span style="font-size: 15px">
            <el-icon>
              <Message/>
            </el-icon>
            邮箱地址：{{updateForm.address}}
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
            <el-form :model="updateForm" label-position="left" label-width="90px">
              <el-row :gutter="50">
                <el-col :span="12">
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
                        v-model="updateForm.realName"
                        autocomplete="off"
                        style="width: 150px;"
                        :disabled="!edit"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="性别">
                <el-select v-model="updateForm.gender"
                           style="width: 90px;"
                           :maxlength="10"
                           placeholder="请选择性别"
                           :disabled="!edit">
                  <el-option label="男" value="male"></el-option>
                  <el-option label="女" value="female"></el-option>
                  <el-option label="隐藏" value="other"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="职位" v-if="user.role !== 'owner'">
                <el-input v-model="updateForm.position"
                          autocomplete="off"
                          style="width: 150px;"
                          :disabled="!edit"/>
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="updateForm.phone"
                          autocomplete="off"
                          style="width: 200px;"
                          :disabled="!edit"/>
              </el-form-item>
              <el-form-item label="注册时间">
                <el-input v-model="updateForm.createdAt"
                          autocomplete="off"
                          style="width: 200px;"
                          disabled/>
              </el-form-item>
              <el-form-item label="入职时间" v-if="user.role !== 'owner'">
                <el-input v-model="updateForm.hireDate"
                          autocomplete="off"
                          style="width: 200px;"
                          disabled/>
              </el-form-item>
              <el-form-item label="账号状态">
                <el-tag style="margin-top: 5px;width: 50px;height: 30px;" effect="plain"
                        :type="updateForm.status === 'active' ? 'success' : 'danger' ">
                  {{ updateForm.status }}
                </el-tag>
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