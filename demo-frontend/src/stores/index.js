import {defineStore} from 'pinia'
import {reactive} from "vue";

export const useStore = defineStore('store', () => {
    const auth = reactive({
        user: null
    })

    return {auth}
})
