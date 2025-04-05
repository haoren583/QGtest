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
        component: () => import('./components/User/UserHome.vue'),
        meta: { needToken: true},
        children: [
            {
                //个人中心
                path: 'PIM',
                name: 'PIM',
                component: () => import('./components/User/PIM.vue'),
            },
            {
                //预约挂号
                path: 'appointment',
                name: 'Appointment',
                component: () => import('./components/User/Appointment.vue'),
            },
            {
                //历史挂号
                path: 'history',
                name: 'History',
                component: () => import('./components/User/History.vue'),
            },
            {
                //修改手机号
                path: 'changephone',
                name: 'PhoneChange',
                component: () => import('./components/User/PhoneChange.vue'),
            },
            {
                //修改密码
                path: 'changepassword',
                name: 'PasswordChange',
                component: () => import('./components/User/PasswordChange.vue'),
            }

        ],
    },
    {
        path: '/admin',
        name: 'Admin',
        component: () => import('./components/Admin/AdminHome.vue'),
        meta: { needToken: true, isAuthCode: STATUS_INIT.Admin },
        children: [
            {
                //用户管理
                path: 'usermanage',
                name: 'UserManage',
                component: () => import('./components/Admin/UserManage.vue'),
            },
            {
                //认证管理
                path: 'attest',
                name: 'Attest',
                component: () => import('./components/Admin/Attest.vue'),
            },
            
            {
                //评论审核
                path: 'commentmanage',
                name: 'CommentManage',
                component: () => import('./components/Admin/CommentManage.vue'),
            },
            {
                //医生信息管理
                path: 'doctormanage',
                name: 'DoctorManage',
                component: () => import('./components/Admin/DoctorManage.vue'),
            },
            {
                //科室信息管理
                path: 'departmentmanage',
                name: 'DepartmentManage',
                component: () => import('./components/Admin/DepartmentManage.vue'),
            },
            {
                //患者信息管理
                path: 'patientmanage',
                name: 'PatientManage',
                component: () => import('./components/Admin/PatientManage.vue'),
            },
        ],
    },
    {
        path: '/doctor',
        name: 'Doctor',
        component: () => import('./components/Doctor/DoctorHome.vue'),
        meta: { needToken: true , isAuthCode: STATUS_INIT.Doctor },
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
