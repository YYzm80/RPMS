<script setup>
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('用户名不能为空！'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中文、字母或数字！'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码！'))
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致！"))
  } else {
    callback()
  }
}

const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    {
      min: 2,
      max: 10,
      message: '用户名长度必须在2-10之间！',
      trigger: ['blur', 'change']
    }
  ],
  password: [
    {required:true, message:'请输入密码！', trigger: 'blur'},
    {
      min: 6,
      max: 16,
      message: '密码长度必须在6-16之间！',
      trigger: ['blur', 'change']
    }
  ],
  password_repeat: [
    {validator: validatePassword, trigger: ['blur', 'change']}
  ],
  email: [
    {
      required: true,
      message: '请输入电子邮箱地址！',
      trigger: 'blur'
    },
    {
      type: 'email',
      message: '请输入正确的邮箱地址！',
      trigger: ['blur', 'change']
    }
  ]
}

const isEmailValid = ref(false)

const onValidate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}
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
      <el-form :model="form" :rules="rules" @validate="onValidate">
        <el-form-item prop="username">
          <el-input v-model="form.username" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮箱地址">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input v-model="form.code" type="text" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button type="success" :disabled="!isEmailValid">获取验证码</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
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