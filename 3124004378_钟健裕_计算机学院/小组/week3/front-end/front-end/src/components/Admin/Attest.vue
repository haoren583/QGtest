<template>
    <div class="main">
        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="date" label="Date" width="180" />
            <el-table-column prop="name" label="Name" width="180" />
            <el-table-column prop="address" label="Address" />
        </el-table>
        <div class="demo-pagination-block">
            <div class="demonstration">Jump to</div>
            <el-pagination v-model:current-page="currentPage3" v-model:page-size="pageSize3" :small="small"
                :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>

    </div>
</template>

<script lang="ts" setup>
const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
]
import { ref } from 'vue'
const currentPage3 = ref(5)
const pageSize3 = ref(100)
const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
console.log(`${val} items per page`)
}
const handleCurrentChange = (val: number) => {
console.log(`current page: ${val}`)
}
</script>

<style scoped>
.demo-pagination-block+.demo-pagination-block {
    margin-top: 10px;
}

.demo-pagination-block .demonstration {
    margin-bottom: 16px;
}
.main {
    padding: 20px;

}
</style>