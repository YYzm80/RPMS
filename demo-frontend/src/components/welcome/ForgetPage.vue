<script setup>

import {reactive, ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const form = reactive({
    email: '',
    code: '',
    password: '',
    password_repeat: ''
})

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
        },
    ],
    code: [
        {required:true, message:'请输入验证码', trigger: ['blur', 'change']}
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
    ]
}

const isEmailValid = ref(false)
const coldTime = ref(0)
const active = ref(0)

const onValidate = (prop, isValid) => {
    if (prop === 'email') {
        isEmailValid.value = isValid
    }
}

const validateEmail = () => {
    post('/api/auth/valid-email', {
        email: form.email
    }, (message) => {
        ElMessage.success(message)
        coldTime.value = 60
        setInterval(() => coldTime.value--, 1000)
    })
}
</script>

<template>
    <div>
        <div  style="margin: 30px 20px">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="验证电子邮件" />
                <el-step title="重设密码" />
            </el-steps>
        </div>
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="text-align: center;margin: 0 20px;height: 100%" v-if="active === 0">
                <div style="margin-top: 80px">
                    <div style="font-size: 25px;font-weight: bold">重置密码</div>
                    <div style="font-size: 14px;color: gray">请输入需要重置密码的电子邮件地址</div>
                </div>
                <div style="margin-top: 50px">
                    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                        <el-form-item prop="email">
                            <el-input v-model="form.email" type="email" placeholder="电子邮箱地址">
                                <template #prefix>
                                    <el-icon>
                                        <Message/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="code">
                            <el-row :gutter="10" style="width: 100%">
                                <el-col :span="17">
                                    <el-input v-model="form.code" type="text" :maxlength="6" placeholder="请输入验证码">
                                        <template #prefix>
                                            <el-icon>
                                                <EditPen/>
                                            </el-icon>
                                        </template>
                                    </el-input>
                                </el-col>
                                <el-col :span="6">
                                    <el-button @click="validateEmail" type="success" :disabled="!isEmailValid || coldTime > 0">
                                        {{coldTime > 0 ? coldTime + '秒后可获取' : '获取验证码' }}</el-button>
                                </el-col>
                            </el-row>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="margin-top: 70px">
                    <el-button @click="active = 1" style="width: 270px" type="danger"
                               plain>下一步
                    </el-button>
                </div>
            </div>
        </transition>
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="text-align: center;margin: 0 20px;height: 100%" v-if="active
         === 1">
                <div style="margin-top: 80px">
                    <div style="font-size: 25px;font-weight: bold">重置密码</div>
                    <div style="font-size: 14px;color: gray">请输入新密码</div>
                </div>
                <div style="margin-top: 50px">
                    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                        <el-form-item prop="password">
                            <el-input v-model="form.password" type="password" :maxlength="16" placeholder="密码">
                                <template #prefix>
                                    <el-icon>
                                        <Lock/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password_repeat">
                            <el-input v-model="form.password_repeat" type="password" :maxlength="16" placeholder="重复密码">
                                <template #prefix>
                                    <el-icon>
                                        <Lock/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="margin-top: 70px">
                    <el-button @click="active = 2" style="width: 270px" type="danger"
                               plain>立即重置密码
                    </el-button>
                </div>
            </div>
        </transition>
    </div>
</template>

<style scoped>

</style>