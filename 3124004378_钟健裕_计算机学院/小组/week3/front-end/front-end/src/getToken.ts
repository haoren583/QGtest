import axios from 'axios';
import type { AxiosInstance, AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import  {TokenStore}  from './Mytoken';
import router from './router';


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
                case 401:
                    // 处理未授权
                    TokenStore().saveToken('');
                    ElMessage.error('登录失效，请重新登录');
                    router.push('/login');
                    break;
                case 404:
                    // 处理资源未找到
                    ElMessage.error('请求的资源不存在');
                    break;
            }
        }
        throw error;
    }
);

export default require;