<script setup>

import {Lock, User} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import router from "@/router";
import {login} from "@/net";
import {ElMessage} from "element-plus";

const loading = ref(false)
const formRef = ref()
const form = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [
    {required: true, message: '请输入用户名'}
  ],
  password: [
    {required: true, message: '请输入密码'}
  ]
}

function userLogin() {
  formRef.value.validate((isValid) => {
    if (isValid) {
      loading.value = true
      login(form.username, form.password, form.remember, () => {
        loading.value = false
        router.push("/index")
      }, (message) => {
        loading.value = false
        ElMessage.warning(message)
      })
    } else {
      ElMessage.warning("请检查输入是否正确")
    }
  })
}
</script>

<template>
  <div style="text-align: center;margin: 20px">
    <div style="margin-top: 70px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
      <div style="font-size: 14px;color: gray">
        在进入系统之前请先输入用户名和密码进行登录
      </div>
    </div>
    <div style="margin-top: 50px;margin-left: 32px">
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" type="text" placeholder="用户名" style="width: 300px"
                    v-on:keyup.enter="userLogin()">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" style="margin-top: 10px; width: 300px"
                    placeholder="密码" v-on:keyup.enter="userLogin()">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row style="margin-top: 10px">
          <el-col :span="10" style="text-align: left">
            <el-form-item prop="remember">
              <el-checkbox v-model="form.remember" label="记住我" size="small"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link style="font-size: 12px" @click="router.push('/welcome/forget')">忘记密码？</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 60px">
      <el-button @click="userLogin()" style="width: 250px" type="success"
                 plain v-loading="loading">立即登录
      </el-button>
    </div>
    <el-divider>
      <span style="color: gray">没有账号</span>
    </el-divider>
    <div style="color: orange">
      <el-button @click="router.push('/welcome/register')" style="width: 250px" type="warning" plain>注册账号</el-button>
    </div>
  </div>
</template>

<style scoped>
.el-form-item{
  margin-bottom: 5px;
}
</style>