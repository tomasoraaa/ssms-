import { ElMessage } from 'element-plus'
import router from '../router'
import axios from "axios";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL || '/api',
    timeout: 30000  // 后台接口超时时间设置
})

// 防重复登录提示标志
let isLoggedOut = false;

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // 添加密码校验值到请求头
    const user = sessionStorage.getItem('xm-user');
    if (user) {
        try {
            const userObj = JSON.parse(user);
            if (userObj.passwordChecksum) {
                config.headers['X-Password-Checksum'] = userObj.passwordChecksum;
                config.headers['X-User-Username'] = userObj.username;
                config.headers['X-User-Role'] = userObj.role;
            }
        } catch (e) {
            console.error('解析用户信息失败', e);
        }
    }
    
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当权限验证不通过的时候给出提示
        if (res.code === '401') {
            handleUnauthorized(res.msg);
        }
        return res;
    },
    error => {
        // 处理 HTTP 状态码错误，特别是 401 密码被重置的情况
        if (error.response) {
            const status = error.response.status;
            if (status === 401) {
                let message = '密码已被重置，请重新登录';
                try {
                    if (error.response.data && error.response.data.msg) {
                        message = error.response.data.msg;
                    }
                } catch (e) {
                    // 如果解析失败，使用默认消息
                }
                handleUnauthorized(message);
                return Promise.reject(error);
            }
        }
        console.log('err' + error)
        return Promise.reject(error)
    }
)

/**
 * 处理未授权错误，确保只提示一次
 */
function handleUnauthorized(message) {
    if (isLoggedOut) {
        return;
    }
    isLoggedOut = true;
    ElMessage.error(message);
    sessionStorage.removeItem('xm-user');
    router.push("/login");
    // 重置标志，允许下次登录后再次触发
    setTimeout(() => {
        isLoggedOut = false;
    }, 3000);
}

export default request
