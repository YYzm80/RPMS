<script setup>

import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const form = reactive({
    city_id: '',
})

const weather_data = ref({})

const weather = () => {
    if (form.city_id) {
        post('/api/weather/cityId', {
            city_id: form.city_id
        }, (data) => {
            weather_data.value = data.data
        })
    } else {
        ElMessage.warning('请填写城市代码')
    }
}

</script>

<template>
    <div>
        <div id="weather">
            <el-form :model="form">
                <el-form-item prop="city_id">
                    <el-row :gutter="10">
                        <el-col :span="20">
                            <el-input v-model="form.city_id" type="text" placeholder="请输入查询城市代码"/>
                        </el-col>
                        <el-col :span="8">
                            <el-button @click="weather" type="success"/>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
        </div>
        <div>
            城市：<span style="font-size: 20px"/>
            今日温度：<span></span>
        </div>
    </div>
</template>

<style scoped>

</style>