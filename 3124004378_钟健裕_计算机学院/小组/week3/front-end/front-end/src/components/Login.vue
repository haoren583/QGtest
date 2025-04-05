<template>
	<div class="main">
		<div class="login_box">
			<div class="head">
				<img src="../assets/联想截图_20250326160402.png" alt="logo" class="logo">
				<div class="title"><b>登录</b></div>
			</div>
			<div class="login-form">
				<el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
					<el-form-item prop="phone">
						<el-input prefix-icon="User" v-model="loginForm.phone" placeholder="请输入手机号" />
					</el-form-item>

					<el-form-item prop="password">
						<el-input type="password" prefix-icon="Lock" v-model="loginForm.password" placeholder="请输入密码" />
					</el-form-item>

					<el-form-item style="margin: 90px 0 0 0;">
						<el-button :disabled="canClick" type="danger" @click="LoginSubmit(loginFormRef)"
							:loading="isLoading"
							style="width: 100%;">登录</el-button>
					</el-form-item>

					<el-link style="margin-top: 25px;" @click="router.push({name:'Register'})" type="primary"><b style="color:firebrick;">没有账号？点击注册</b></el-link>
				</el-form>
			</div>

		</div>
	</div>
</template>

<script setup>
import axios from 'axios'
import { useDisabled, useFormDisabled } from 'element-plus'
import { ca, el, he } from 'element-plus/es/locales.mjs'
import { ref, computed, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router' // 导入 useRouter 钩子
//import { ElNotification } from 'element-plus' // 导入element-plus的通知弹窗组件
import { Loading } from 'element-plus/es/components/loading/src/service.mjs'
import { TokenStore } from '../Mytoken'

const router = useRouter() // 调用 useRouter 钩子
const route = useRoute() // 调用 useRoute 钩子
const isLoading = ref(false) // 登录按钮loading状态
const loginForm = reactive({
	phone: "",
	password: "",
})
// 未输入账号密码禁用登录
const canClick = computed(() => {
	if (loginForm.phone !== '' && loginForm.password !== '') { return false }
	else { return true }
})
//表单校验规则
const loginRules = reactive({
	//trigger: 'blur' 失去焦点触发验证
	phone: [
		{ required: true, message: '手机号不能为空', trigger: 'blur' },
		{ validator: validatePhone, trigger: 'blur' },
	],
	password: [
		{ required: true, message: '密码不能为空', trigger: 'blur' },
		{ min: 6, max: 18, message: '密码长度6-18位', trigger: 'blur' },
	]
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

const loginFormRef = ref(null) // 表单ref

function LoginSubmit(loginFormRef) {
	// 表单校验
	loginFormRef.validate(async (valid) => {
		if (valid) {
			// 发送登录请求
			isLoading.value = true // 显示loading
			console.log(loginForm)
			const res = await loginRequest(loginForm)
			// 处理登录结果
			//如果没有res
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
				console.log('登录成功')
				//弹窗提示登录成功
				ElNotification({
					title: '登录成功',
					message: '欢迎回来',
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
				 console.log('登录失败')
				// //弹窗提示登录失败
				ElNotification({
					title: '登录失败',
					message: '账号或密码错误',
					type: 'error',
					duration: 2000
				})
			}
			isLoading.value = false // 隐藏loading
		} else {
			console.log('error')
		}
	})
}
async function loginRequest(loginForm) {
	//将登录表单数据转为json格式
	const loginData = JSON.stringify(loginForm)
	//发送axios请求
	try {
		return await axios.post('http://localhost:8080/zhongjianyu.cat.com/api/user/login', loginData);
	} catch (error) {
		console.log(error);
		ElNotification({
			title: '登录失败',
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
	background: url('../assets/f5a3e71a03d1c914bcaa404a14e857c4e5691b77.jpg');
	background-size: cover;

	.login_box {
		width: 500px;
		height: 460px;
		box-shadow: 0 0 5px #9804e2;
		position: absolute;
		top: calc(50vh - 330px);
		left: calc(50vw - 250px);
		border-radius: 20px;
		background: linear-gradient(to right, rgba(93, 0, 255, 0.1), rgba(255, 0, 4, 0.1));
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

	.login-form {
		margin: 0 30px;
		::v-deep(.el-input__wrapper) {
			background : linear-gradient(to right,  rgba(202, 202, 202, 0.5),rgba(255, 255, 255, 0)) !important;
		}
		::v-deep(input::-webkit-input-placeholder) {
			color:rgb(43, 69, 93);
		}
		::v-deep(.el-button){
			background: linear-gradient(to right,  rgba(255, 0, 4, 0.5),rgba(93, 0, 255, 0.5));
		}
	}
}
</style>
