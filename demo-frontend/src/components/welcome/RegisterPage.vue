<script setup>
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})
</script>

<template>
  <div style="text-align: center;margin: 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color: gray">
        欢迎注册我们的XX学习平台
      </div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="form.username" type="text" placeholder="用户名">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password" style="margin-top: 10px" type="password"
                placeholder="密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password_repeat" style="margin-top: 10px" type="password"
                placeholder="重复密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.email" type="text" placeholder="电子邮箱地址"
                style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Message/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row :gutter="10" style="width: 100%">
        <el-col :span="17">
          <el-input v-model="form.email" type="text" placeholder="请输入验证码">
            <template #prefix>
              <el-icon>
                <EditPen/>
              </el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success">获取验证码</el-button>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 80px">
      <el-button style="width: 250px" type="warning" plain>立即注册
      </el-button>
    </div>
    <div style="font-size: 14px;line-height: 15px;margin-top: 20px">
      <span>已有账号？</span>
      <el-link @click="router.push('/')" type="primary"
               style="translate: 0 -1px">立即登录
      </el-link>
    </div>
  </div>
</template>

<style scoped>

</style>