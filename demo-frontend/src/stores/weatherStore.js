import {defineStore} from "pinia";
import {reactive} from "vue";

export const weatherStore = defineStore('weather_store', () => {
    const api = reactive({
        weather: ''
    })

    return {api}
})
