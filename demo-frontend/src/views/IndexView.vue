<script setup>
import {get, logout, post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {reactive, ref} from "vue";
import {weatherStore} from "@/stores/weatherStore";
import {locationStore} from "@/stores/locationStore";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

function userLogout() {
  logout(() => router.push("/"))
}

const form = reactive({
    city_id: '',
})

const weather_store = weatherStore()
const location_store = locationStore()
const active = ref(0);

const weather = () => {
    get('/api/location/get-location',
            (adcode) => {
                location_store.api.location = adcode
                form.city_id = location_store.api.location.adcode
                getWeather()
            }, () => {
                ElMessage.warning('获取位置信息失败，请检查网络')
            })
    }

const getWeather = () => {
    if (form.city_id) {
        post(`/api/weather/cityId?city_id=${form.city_id}`, {
        }, (data) => {
            weather_store.api.weather = data
            active.value = 1
        })
}
}

</script>

<template>
    <div style="width: 100vw;height: 100vh;overflow: hidden">
        <div style="margin: 5px 5px">欢迎{{ user.username }}进入学习平台！</div>
        <el-button @click="userLogout()" type="danger">退出登录</el-button>
        <div id="weather">
            <el-button @click="weather" type="success">{{ '查询天气' }}</el-button>
        </div>
        <div v-if="active===1">
            <table class="demo-border" style="margin-left: 50px">
                <tbody>
                    <tr>
                        <td class="text">城市：</td>
                        <td class="text" style="font-size: 20px">{{ weather_store.api.weather.lives[0].city }}</td>
                    </tr>
                    <tr>
                        <td colspan="2" class="line">
                            <div />
                        </td>
                    </tr>
                    <tr>
                        <td class="text">今日天气：</td>
                        <td class="text">
                            <el-text tag="b">
                                {{ weather_store.api.weather.lives[0].weather }}
                            </el-text>
                        </td>
                        <td class="line"></td>
                    </tr>
                    <tr>
                        <td class="text">今日温度：</td>
                        <td class="text">
                            <el-text tag="b">
                                {{ weather_store.api.weather.lives[0].temperature }}℃
                            </el-text>
                        </td>
                        <td class="line"></td>
                    </tr>
                    <tr>
                        <td class="text">当前风向：</td>
                        <td class="text">
                            <el-text tag="b">
                                {{ weather_store.api.weather.lives[0].winddirection }}
                            </el-text>
                        </td>
                        <td class="line"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

</template>

<style scoped>
.demo-border .text {
    width: 15%;
}
.demo-border .line {
    width: 70%;
}
.demo-border .line div {
    width: 100%;
    height: 0;
    border-top: 1px solid var(--el-border-color);
}
</style>