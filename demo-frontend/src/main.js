import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'
import axios from "axios";
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8080'

app.use(createPinia())
app.use(router)

app.mount('#app')
app.use(ElementPlus, {
    locale: zhCn,
})
