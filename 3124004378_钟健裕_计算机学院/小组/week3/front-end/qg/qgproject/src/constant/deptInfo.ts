import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const deptInfoStore = defineStore('deptInfo', () => {
    //json数据
    const deptInfoJson = ref("")
    const deptInfo= computed(():any => {
        if (deptInfoJson.value||deptInfoJson.value=='') {
            deptInfoJson.value = window.localStorage.getItem("localdeptInfo") || "";
            return JSON.parse(deptInfoJson.value)
            } else {
                return JSON.parse(deptInfoJson.value)
            }
        } 
    )
    const setDeptInfo = (data: any) => {
        deptInfoJson.value = JSON.stringify(data)
        window.localStorage.setItem("localdeptInfo", deptInfoJson.value)
    }
    return { deptInfo, setDeptInfo }
})