<template>
    <div>
        <ul>
            <!-- 使用唯一 id 作为 key -->
            <header style="display: block; text-align: center;font-size: 30px;font-weight: bold;margin-bottom: 20px;">
                可选课程 </header>
            <li v-for="item in list" :key="item.courseId " >
                <div class="main" v-if="item.courseName!== ''">
                    <el-descriptions :column="2" border >
                        <el-descriptions-item label="课程" label-align="right" align="center" label-class-name="my-label"
                            class-name="my-content" >{{ item.courseName }}</el-descriptions-item>
                        <el-descriptions-item label="教师" label-align="right"  align="center">{{ item.teacherName
                        }}</el-descriptions-item>
                        <el-descriptions-item label="可容纳学生" label-align="right" align="center" >{{ item.volumeStudents
                        }}</el-descriptions-item>
                        <el-descriptions-item label="学分" label-align="right" align="center">
                            {{ item.score }}
                        </el-descriptions-item>

                    </el-descriptions>
                </div>
            </li>
        </ul>
    </div>


</template>

<style scoped>
.my-label {
    background: var(--el-color-success-light-9);
}

.my-content {
    background: var(--el-color-danger-light-9);
}

.main {
    padding: 20px;
}
</style>

<script lang="ts" setup>

// async function dit(item: any) {
//     console.log(item)
//     let data = {
//         slotId: item.appointmentSoltId,
//         operation: 0,
//     }
//     console.log(data)
//     await require.post('/user/appoint', data);
//     ElMessage.success('预约成功');
// }


const deptName = ref('');

import { onMounted, ref } from 'vue';
import require from '../../require';
import router from '../../router';
import { descriptionProps } from 'element-plus';
import { departmentName } from '../../constant/departmentName';
import { Operation } from '@element-plus/icons-vue';
// 页面加载完成后执行
const list = ref([{
    courseName: "",
    teacherName: "",
    volumeStudents: "",
    score: "",
    courseId: "",
}])

let allData;

onMounted(async () => {
    const res = await submitResult();
    allData = res.data.courseList;
    console.log(allData)
    //const orgList= data.
    let iform = {
        courseName: "",
        teacherName: "",
        volumeStudents: "",
        score: "",
        courseId: "",
    }
    for (let i = 0; i < allData.length; i++) {
        iform.courseName = allData[i].courseName;
        iform.teacherName = allData[i].teacherName;
        iform.volumeStudents = allData[i].volumeStudents;
        iform.score = allData[i].score;
        iform.courseId = allData[i].courseId;
        list.value.push(iform);
    }
    console.log(list.value)
});


const total = ref(0)
const pageSize = ref(10)
const isLoading = ref(false)

const departments = ref([]);

const currentPage4 = ref(1)




async function submitResult() {
    const store = departmentName();
    deptName.value = store.deopartmentName;
    const data = {
        startRow: (currentPage4.value - 1) * pageSize.value,
        size: pageSize.value,
        departmentName: deptName.value,
    }
    const formData = JSON.stringify(data);
    console.log(formData)
    //发送axios请求
    return await require.post('/user/query_courses', formData);
}

</script>
