import { createRouter, createWebHashHistory } from 'vue-router'
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import Home from './components/Home.vue'


//定义路由
const routers = [
    {
        path: '/',//访问该路由的URL路径
        name: 'Login',//路由名称
        component: Login//对应组件
    },
    {
        path: '/register',//访问该路由的URL路径
        name: 'Register',//路由名称
        component: Register//对应组件
    },
    {
        path: '/home',//访问该路由的URL路径
        name: 'Home',//路由名称
        component: Home//对应组件
    }
]

//创建路由实例
const router = createRouter({
    history: createWebHashHistory(),//
    routes: routers//将之前定义的路由配置数组 routers 传递给路由器。
})

export default router//导出路由实例
