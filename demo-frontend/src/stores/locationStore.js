import {defineStore} from "pinia";
import {reactive} from "vue";

export const locationStore = defineStore('weather_store', () => {
    const api = reactive({
        location: ''
    })

    return {api}
})
