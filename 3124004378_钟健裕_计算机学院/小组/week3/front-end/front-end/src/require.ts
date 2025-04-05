import axios from 'axios';
import type { AxiosInstance,AxiosRequestHeaders, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { ElMessage, ElNotification } from 'element-plus';
import  {TokenStore}  from './Mytoken';
import { useRouter } from 'vue-router' // 导入 useRouter 钩子
const router = useRouter() // 获取路由实例


// 定义接口类型
export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
}

// 创建 axios 实例，凡是通过axios.create()创建的实例，都是 AxiosInstance 类型
const require: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/zhongjianyu.cat.com/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});


// 请求拦截器
require.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        if (!config.headers) {
            // 防止请求头为空
            config.headers = {} as AxiosRequestHeaders;
        }
        const store = TokenStore();
        const nowToken = store.token?.access_token;
        if (nowToken) {
            config.headers.Authorization = nowToken;
        } else {
            ElNotification({
                title: '提示',
                message: '登录失效',
                type: 'warning',
                duration: 2000
            })
            router.push({ name: 'Login' });
            return Promise.reject(new Error('登录失效'));
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
require.interceptors.response.use(
    (response: AxiosResponse<ApiResponse>) => {
        const { code, data, message } = response.data;
        if (code !== 200) {
            throw new Error(message || '请求失败');
        }
        if (data.tokenCode === "REFRESH") {
            const store = TokenStore();
            store.saveToken(data.token);
        }
        return data;
    },
    (error) => {
        if (axios.isAxiosError(error)) {
            switch (error.response?.status) {
                case 400:
                    // 处理请求参数错误
                    ElNotification({
                        title: '请求参数错误',
                        message: error.message,
                        type: 'error',
                        duration: 2000
                    })
                case 401:
                    // 处理未授权
                    TokenStore().saveToken('');
                    // //弹窗提示登录失效
                    ElNotification({
                        title: '登录失效',
                        message: '请重新登录',
                        type: 'error',
                        duration: 2000
                    })
                    router.push({ name: 'Login' });
                    break;
                case 404:
                    // 处理资源未找到
                    ElNotification({
                        title: '资源未找到',
                        message: '请求的资源不存在',
                        type: 'error',
                        duration: 2000
                    })
                    break;
            }
        }
        throw error;
    }
);

export default require;