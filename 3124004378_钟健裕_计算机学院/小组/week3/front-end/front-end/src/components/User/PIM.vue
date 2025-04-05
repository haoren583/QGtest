<template>
  <div class="main">
    <el-form :model="form" label-width="120px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="身份">
        <el-select v-model="form.role" placeholder="请选择你的身份">
          <el-option label="普通用户" value="Common" />
          <el-option label="医生" value="Doctor" />
        </el-select>
      </el-form-item>
      <dev v-if="form.role === 'Doctor'">
        <el-form-item label="科室">
          <el-select v-model="form.dept_name" placeholder="请选择科室">
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="脑科" value="脑科" />
            <el-option label="妇产科" value="妇产科" />
            <el-option label="肿瘤科" value="肿瘤科" />
            <el-option label="神经科" value="神经科" />
            <el-option label="耳鼻喉科" value="耳鼻喉科" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="自我介绍">
          <el-input v-model="form.job_intro" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea"
            placeholder="请简要介绍自己,500字以内" />
        </el-form-item>
      </dev>
      <el-form-item label="学号/工号">
        <el-input v-model="form.cardId" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio label="男" name="gender" />
          <el-radio label="女" name="gender" />
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="canClick" type="primary" @click="onSubmit" :loading="isLoading">申请认证</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import require from '../../require';
import { ElNotification } from 'element-plus';

const form = reactive({
  name: '',
  role: '',
  cardId: '',
  gender: '',
  dept_name: '',
  title: '',
  job_intro: ''
})

// 未输入完毕禁用登录
const canClick = computed(() => {
  if (form.name !== '' && form.role !== '' && form.cardId !== '' && form.gender !== '') { return false }
  if (form.role === 'Doctor' && form.department !== '' && form.title !== '' && form.introduction !== '') { return false }
  return true
})

const isLoading = ref(false)

let data;

if (form.role === 'Doctor') {
  data = {
    name: form.name,
    role: form.role,
    cardId: form.cardId,
    gender: form.gender,
    department: form.department,
    title: form.title,
    introduction: form.introduction
  }
} else {
  data = {
    name: form.name,
    role: form.role,
    cardId: form.cardId,
    gender: form.gender
  }
}

async function submitResult(data) {
  const formData = JSON.stringify(data);
  console.log(formData)
  //发送axios请求
  return await require.post('/user/attest', formData);
} 
const onSubmit = async () => {
  isLoading.value = true // 显示loading
  console.log('submit!')
  try {
      await submitResult(form);
      console.log('申请成功')
      //弹窗提示申请成功
      ElNotification({
        title: '申请成功',
        message: '请等待管理员审核',
        type: 'success',
        duration: 2000
      })
  }catch (error) {
    console.log(error)
  }
  isLoading.value = false // 隐藏loading
}


const cancel = () => {
  console.log('cancel!')
  form.name = ''
  form.role = ''
  form.cardId = ''
  form.gender = ''
}
</script>

<style lang="scss" scoped>
.main {
  padding: 20px;

}
</style>
