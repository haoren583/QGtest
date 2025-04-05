<template>
    <div class="main">
  <el-form :model="form" label-width="120px" >
    <el-form-item label="原手机号">
      <el-input v-model="form.phone" />
    </el-form-item>
    <el-form-item label="新手机号">
      <el-input v-model="form.newPhone" />
    </el-form-item>
    <el-form-item label="密码">
      <el-input v-model="form.password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">修改</el-button>
      <el-button>取消</el-button>
    </el-form-item>
  </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue'

// do not use same name with ref
const form = reactive({
  phone: '',
  newPhone: '',
  password: ''
})

const onSubmit = () => {
  console.log('submit!')
}
</script>

<style lang="scss" scoped>
.main {
  padding: 20px;

}
</style>
