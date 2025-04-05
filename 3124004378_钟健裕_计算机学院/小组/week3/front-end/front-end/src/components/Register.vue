<template>
    <div class="main">
        <div class="register_box">
            <div class="head">
                <img src="../assets/preview.jpg" alt="logo" class="logo">
                <div class="title"><b>注册</b></div>
            </div>
            <div class="register-form">
                <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
                    <el-form-item prop="useremail">
                        <el-input prefix-icon="User" v-model="registerForm.useremail" placeholder="请输入手机号" />
                    </el-form-item>

                    <el-form-item prop="password">
                        <el-input type="password" prefix-icon="Lock" v-model="registerForm.password"
                            placeholder="请输入密码" />
                    </el-form-item>

                    <el-form-item prop="confirmPassword">
                        <el-input type="password" prefix-icon="Lock" v-model="registerForm.confirmPassword"
                            placeholder="请确认密码" />
                    </el-form-item>

                    <el-form-item style="margin: 50px 0;">
                        <el-button :disabled="canClick" type="danger" @click="RegisterSubmit(registerFormRef)"
                            :loading="isLoading"
                            style="width: 100%;">注册</el-button>
                    </el-form-item>

                    <el-link @click="router.push({ name: 'Login' })" type="primary">已有账号？点击登录</el-link>
                </el-form>
            </div>

        </div>
    </div>
</template>

<script setup>
import { useDisabled, useFormDisabled } from 'element-plus'
import { ref, computed, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router' // 导入 useRouter 钩子
import { TokenStore } from '../Mytoken'
import axios from 'axios'
const router = useRouter() // 调用 useRouter 钩子
const route = useRoute() // 调用 useRoute 钩子


const registerForm = reactive({
    useremail: "",
    password: "",
    confirmPassword: "",
})
const registerFormRef = ref(null)
// 未输入账号密码禁用
const canClick = computed(() => {
    if (registerForm.useremail !== '' && registerForm.password !== '' && registerForm.confirmPassword !== '') { return false }
    else { return true }
})
//表单校验规则
const registerRules = reactive({
    //trigger: 'blur' 失去焦点触发验证
    useremail: [
        { required: true, message: '手机号不能为空', trigger: 'blur' },
        { validator: validatePhone, trigger: 'blur' },
    ],
    password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        { min: 6, max: 18, message: '密码长度6-18位', trigger: 'blur' },
    ],
    confirmPassword: [
        { required: true, message: '确认密码不能为空', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' },
    ],
})
/**
 * @param rule // 校验规则
 * @param value // 用户的输入值
 * @param callback // 回调函数，用于返回错误信息
 */
function validatePhone(rule, value, callback) {
    const phoneRegex = /^1[3-9]\d{9}$/; // 这是一个简化的中国手机号验证正则表达式

    if (!phoneRegex.test(value)) {
        callback(new Error('请输入正确的手机号'));
    } else {
        callback();
    }
}

function validateConfirmPassword(rule, value, callback) {
    if (value !== registerForm.password) {
        callback(new Error('两次密码输入不一致'));
    } else {
        callback();
    }
}


const isLoading = ref(false) // 控制按钮的loading状态
function RegisterSubmit(registerFormRef) {
    // 表单校验
    registerFormRef.validate(async (valid) => {
        isLoading.value = true
        if (valid) {
            console.log(registerForm)
            // 发送axios请求
            const res = await registerRequest(registerForm);
            if (!res) {
                isLoading.value = false // 隐藏loading
                return
            }
            if (res.data.code >= 200 && res.data.code < 300) {
                //获取token
                const token = res.data.data.access_token
                //存储token
                const store = TokenStore()
                store.saveToken(token)
                console.log('注册成功')
                //弹窗提示登录成功
                ElNotification({
                    title: '注册成功',
                    message: '欢迎加入',
                    type: 'success',
                    duration: 2000
                })
                // 跳转到首页或者跳转到登录前的页面
                if (route.query.redirect) {
                    router.push(route.query.redirect)
                } else {
                    router.push({ name: 'Role' })
                }
            } else {
                console.log('注册失败')
                if (res.data.code === 409) {
                    ElNotification({
                        title: '注册失败',
                        message: '手机号已注册',
                        type: 'error',
                        duration: 2000
                    })
                }
            }
        }
        else {
            console.log('表单校验失败')
        }
        isLoading.value = false
    })
}

async function registerRequest(registerForm) {
    //提取表单的账号和密码
    const registerData = {
        "phone": registerForm.useremail,
        "password": registerForm.password
    }
    const registerDataJson = JSON.stringify(registerData)
    //发送axios请求
    try {
        return await axios.post('http://localhost:8080/zhongjianyu.cat.com/api/user/register', registerDataJson);
    } catch (error) {
        console.log(error);
        ElNotification({
            title: '注册失败',
            message: '网络错误',
            type: 'error',
            duration: 2000
        })
        
        return null
    }

}



</script>

<style scoped lang='scss'>
.main {
    width: 100vw;
    height: 100vh;
    /* background: url('../assets/hz.svg'); */
    background: url('../assets/good.jpg');
    background-size: cover;

    .register_box {
        width: 500px;
        height: 460px;
        box-shadow: 0 0 5px #9804e2;
        position: absolute;
        top: calc(50vh - 230px);
        left: calc(50vw - 250px);
        border-radius: 20px;
        background-color: rgba(128, 61, 61, 0.8);
    }

    .head {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 150px;

        .logo {
            width: 60px;
            height: 60px;
            border-radius: 50px;
        }

        .title {
            font-size: 30px;
            font-weight: bold;
            color: var(--el-color-primary);
            margin-left: 20px;
        }
    }

    .register-form {
        margin: 0 30px;
    }
}
</style>
