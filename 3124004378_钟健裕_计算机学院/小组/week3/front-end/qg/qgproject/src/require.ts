import axios from 'axios';
import { useRouter } from 'vue-router' // 导入 useRouter 钩子
import type { AxiosInstance,AxiosRequestHeaders, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { ElMessage, ElNotification } from 'element-plus';
import { TokenStore } from './Mytoken';
import { el } from 'element-plus/es/locales.mjs';



// // 定义接口类型
// export interface ApiResponse<T = any> {
//     code: number;
//     message: string;
//     data: T;
// }

// 创建 axios 实例，凡是通过axios.create()创建的实例，都是 AxiosInstance 类型
const require: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/zhongjianyu.qg.com/api',
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
                title: '登录失效',
                message: '请重新登录',
                type: 'warning',
                duration: 2000
            })
            return Promise.reject(new Error('登录失效'));
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
    require.interceptors.response.use(
        (response: AxiosResponse) => {
            const code = response.status;
            const message = response.data.msg;
            const data = response.data;
            if (code !== 200) {
                switch (code) {
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
                        break;
                    default:
                        // 处理其它错误
                        ElNotification({
                            title: '请求失败' + code,
                            message: message,
                            type: 'error',
                            duration: 2000
                        })
                }
            }
            if (data.tokenCode === "REFRESH") {
                const store = TokenStore();
                store.saveToken(data.token);
            }
            return response;
        },
        (error) => {
            const res = error.response;
            if (res) {
                const code = res.status;
                const message = res.data.msg;
                switch (code) {
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
                        break;
                    default:
                        // 处理其它错误
                        ElNotification({
                            title: '请求失败' + code,
                            message: message||error.message,
                            type: 'error',
                            duration: 2000
                        })
                        break;
                }
            } else {
                // 处理未响应
                ElNotification({
                    title: '请求失败',
                    message: error.message,
                    type: 'error',
                    duration: 2000
                })
            }
        }
    );

export default require;