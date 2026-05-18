import { ElMessage } from 'element-plus'
import router from '../router'
import axios from "axios";

const request = axios.create({
    baseURL: '/api',
    timeout: 30000  // 后台接口超时时间设置
})

// 防重复登录提示标志
let isLoggedOut = false;
// Token 刷新状态
let isRefreshing = false;
// 刷新期间等待的请求队列
let refreshSubscribers = [];
// Token 自动刷新定时器
let refreshTimer = null;

/**
 * 启动 Token 自动刷新定时器
 * 在 Token 过期前 1 分钟自动刷新
 */
function startRefreshTimer() {
    stopRefreshTimer();

    const expiresAt = sessionStorage.getItem('expiresAt');
    if (!expiresAt) return;

    const expiresTime = parseInt(expiresAt);
    const now = Date.now();
    const timeUntilExpire = expiresTime - now;

    // 在过期前 1 分钟刷新
    const refreshDelay = Math.max(timeUntilExpire - 60 * 1000, 0);

    refreshTimer = setTimeout(async () => {
        try {
            await refreshToken();
            // 刷新成功后，重新设置定时器
            startRefreshTimer();
        } catch (error) {
            console.error('自动刷新 Token 失败:', error);
            // 刷新失败，可能是在其他设备登录了，强制登出
            if (!isLoggedOut) {
                handleUnauthorized('登录已失效');
            }
        }
    }, refreshDelay);
}

/**
 * 停止 Token 自动刷新定时器
 */
function stopRefreshTimer() {
    if (refreshTimer) {
        clearTimeout(refreshTimer);
        refreshTimer = null;
    }
}

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // 添加 JWT Token 到请求头
    const accessToken = sessionStorage.getItem('accessToken');
    if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
        // 启动自动刷新定时器（如果还没启动）
        if (!refreshTimer) {
            startRefreshTimer();
        }
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (response.config.responseType === 'blob') {
            return res
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            handleUnauthorized(res.msg);
        }
        return res;
    },
    error => {
        if (error.response) {
            const status = error.response.status;
            if (status === 401) {
                let errorMsg = '登录已失效';
                try {
                    if (error.response.data && error.response.data.msg) {
                        errorMsg = error.response.data.msg;
                    }
                } catch (e) {
                    // 解析失败，使用默认消息
                }

                // 单点登录验证失败（账号已在其他设备登录），直接登出
                if (errorMsg.includes('其他设备')) {
                    handleUnauthorized(errorMsg);
                    return Promise.reject(error);
                }

                // Token 过期，尝试刷新
                const originalRequest = error.config;

                // 如果正在刷新 Token，将请求加入队列
                if (isRefreshing) {
                    return new Promise((resolve) => {
                        refreshSubscribers.push(() => {
                            resolve(request(originalRequest));
                        });
                    });
                }

                isRefreshing = true;

                // 尝试刷新 Token
                return refreshToken().then(() => {
                    isRefreshing = false;
                    // 重新发送所有排队的请求
                    refreshSubscribers.forEach(subscriber => subscriber());
                    refreshSubscribers = [];
                    // 重新发送当前请求
                    return request(originalRequest);
                }).catch(() => {
                    isRefreshing = false;
                    handleUnauthorized('登录已失效');
                    return Promise.reject(error);
                });
            }
        }
        console.log('err' + error)
        return Promise.reject(error)
    }
)

/**
 * 刷新 Token
 */
async function refreshToken() {
    const refreshToken = sessionStorage.getItem('refreshToken');
    if (!refreshToken) {
        throw new Error('无 Refresh Token');
    }

    const response = await axios.post('/api/refreshToken', { refreshToken });
    if (response.data.code === '200' && response.data.data) {
        const { accessToken, expiresIn } = response.data.data;
        sessionStorage.setItem('accessToken', accessToken);
        sessionStorage.setItem('expiresAt', (Date.now() + expiresIn * 1000).toString());
    } else {
        throw new Error(response.data.msg || 'Token 刷新失败');
    }
}

/**
 * 处理未授权错误，确保只提示一次
 */
function handleUnauthorized(message) {
    if (isLoggedOut) {
        return;
    }
    isLoggedOut = true;

    // 停止自动刷新定时器
    stopRefreshTimer();

    ElMessage.error(message);
    // 清除所有登录相关信息
    sessionStorage.removeItem('xm-user');
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    sessionStorage.removeItem('expiresAt');
    sessionStorage.removeItem('loginVersion');
    router.push("/login");
    setTimeout(() => {
        isLoggedOut = false;
    }, 3000);
}

export default request
