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
// 是否已添加可见性监听
let visibilityListenerAdded = false;

/**
 * 验证 expiresAt 是否有效
 */
function isExpiresAtValid() {
    const expiresAt = sessionStorage.getItem('expiresAt');
    if (!expiresAt) return false;
    const expiresTime = parseInt(expiresAt);
    if (isNaN(expiresTime) || expiresTime <= 0) return false;
    return true;
}

/**
 * 启动 Token 自动刷新定时器
 * 在 Token 过期前 1 分钟自动刷新
 */
function startRefreshTimer() {
    // 清理旧的定时器
    stopRefreshTimer();

    // 检查 expiresAt 是否有效
    if (!isExpiresAtValid()) {
        console.log('定时器启动失败：expiresAt 无效');
        return;
    }

    const expiresAt = sessionStorage.getItem('expiresAt');
    const expiresTime = parseInt(expiresAt);
    const now = Date.now();
    const timeUntilExpire = expiresTime - now;

    // 如果已经过期，不启动定时器（让后续请求处理）
    if (timeUntilExpire <= 0) {
        console.log('Token 已过期，不启动定时器，等待后续请求处理');
        return;
    }

    // 在过期前 1 分钟刷新
    const refreshDelay = timeUntilExpire - 60 * 1000;

    // 如果距离过期时间不足 1 分钟，立即刷新
    if (refreshDelay <= 0) {
        console.log('Token 即将过期，立即刷新...');
        refreshToken().then(() => {
            console.log('Token 刷新成功');
            startRefreshTimer();
        }).catch((error) => {
            console.error('Token 刷新失败，不强制登出:', error.message || error);
            // 刷新失败不强制登出，等待后续请求再尝试
        });
        return;
    }

    console.log(`定时器已启动，${Math.round(refreshDelay / 1000 / 60)} 分钟后自动刷新 Token`);

    refreshTimer = setTimeout(async () => {
        try {
            await refreshToken();
            console.log('Token 刷新成功，重新设置定时器');
            startRefreshTimer();
        } catch (error) {
            console.error('自动刷新 Token 失败:', error.message || error);
            // 刷新失败不强制登出，等待后续请求再尝试
        }
    }, refreshDelay);
}

/**
 * 检查 Token 是否需要刷新
 * 当页面重新可见时调用
 */
function checkAndRefreshToken() {
    if (!isExpiresAtValid()) {
        return;
    }

    const expiresAt = sessionStorage.getItem('expiresAt');
    const expiresTime = parseInt(expiresAt);
    const now = Date.now();
    const timeUntilExpire = expiresTime - now;

    // 如果 Token 已过期或即将在 1 分钟内过期，则刷新
    if (timeUntilExpire <= 60 * 1000) {
        console.log('页面重新可见，检测到 Token 即将过期，执行刷新...');
        refreshToken().then(() => {
            console.log('Token 刷新成功');
            startRefreshTimer();
        }).catch((error) => {
            console.error('Token 刷新失败:', error.message || error);
            // 刷新失败不强制登出
        });
    } else {
        console.log(`页面重新可见，Token 还有 ${Math.round(timeUntilExpire / 1000 / 60)} 分钟有效`);
    }
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

// 添加可见性变化监听（只添加一次）
function addVisibilityListener() {
    if (visibilityListenerAdded) return;
    visibilityListenerAdded = true;

    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible') {
            console.log('页面重新可见，检查 Token...');
            checkAndRefreshToken();
        }
    });
    console.log('已添加页面可见性监听');
}

// 立即添加可见性监听
addVisibilityListener();

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
                }).catch((error) => {
                    isRefreshing = false;
                    console.error('Token 刷新失败，不强制登出:', error.message || error);
                    // 刷新失败不强制登出，让用户继续操作
                    // 清除刷新队列，不重发请求
                    refreshSubscribers = [];
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
