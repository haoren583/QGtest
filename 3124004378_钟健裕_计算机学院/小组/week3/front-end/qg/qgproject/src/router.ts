import { createRouter, createWebHashHistory } from 'vue-router'
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import Home from './components/Home.vue'
import { TokenStore } from './Mytoken'
import Role from './components/Role.vue'
import { ElNotification } from 'element-plus'
import { STATUS_INIT, isAuth } from './status.ts'

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
        path: '/',//访问该路由的URL路径
        name: '',//路由名称
        component: Home,//对应组件
        meta:{needToken:true},//该路由需要登录才能访问
    },
    {
        path: '/role',//访问该路由的URL路径
        name: 'Role',//路由名称
        component: Role,//对应组件
        meta:{needToken:true},//该路由需要登录才能访问
    },
    {
        path: '/user',
        name: 'User',
        component: () => import('./components/User/QueryCourses.vue'),
        meta: { needToken: true},
        children: [
            
        ],
    },
    

]

//创建路由实例
const router = createRouter({
    history: createWebHashHistory(),//
    routes: routers//将之前定义的路由配置数组 routers 传递给路由器。
})

//路由守卫
router.beforeEach((to, from, next) => {
    const store = TokenStore();
     if (to.matched.some(r=>r.meta?.needToken)) {
         if (!store.token || !store.token.access_token) {
             ElNotification({
                 title: '提示',
                 message: '请先登录',
                 type: 'warning',
                 duration: 2000
             })
            return next({name: 'Login',query: {redirect: to.fullPath}});
         }
     }
     if (to.matched.some(r => (r.meta?.isAuthCode && !isAuth(r.meta.isAuthCode as number)))) {
             ElNotification({
                 title: '提示',
                 message: '权限不足',
                 type: 'warning',
                 duration: 2000
             })
             return;
     }
     next();
 })

export default router//导出路由实例
