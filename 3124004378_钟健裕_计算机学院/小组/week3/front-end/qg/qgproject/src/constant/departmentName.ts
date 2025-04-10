import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const departmentName = defineStore('departName', () => {
    const name = ref<string>("")
    const deopartmentName = computed(() => {
        return name.value||window.localStorage.getItem("localDepartmentName")||"未知"
    })
    const setName = (data: string) => {
        name.value = data
        window.localStorage.setItem("localDepartmentName", data);
    }
    return { deopartmentName, setName }
})