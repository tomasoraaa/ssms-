package com.lgs.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 管理服务
 * 用于实现单点登录：一个账号只能在一处登录
 */
@Service
public class TokenService {

    /**
     * 存储用户当前有效的 Refresh Token
     * key: username, value: refreshToken
     */
    private final Map<String, String> validRefreshTokens = new ConcurrentHashMap<>();

    /**
     * 存储用户的登录版本号，用于单点登录验证
     * key: username, value: loginVersion (每次登录递增)
     */
    private final Map<String, Long> loginVersions = new ConcurrentHashMap<>();

    /**
     * 登录成功时，记录当前有效的 Refresh Token 和版本号
     * 之前的 Token 将失效
     */
    public void loginSuccess(String username, String refreshToken) {
        Long newVersion = loginVersions.getOrDefault(username, 0L) + 1;
        loginVersions.put(username, newVersion);
        validRefreshTokens.put(username, refreshToken);
    }

    /**
     * 验证 Refresh Token 是否有效（是否是当前登录的 Token）
     */
    public boolean isValidRefreshToken(String username, String refreshToken) {
        String validToken = validRefreshTokens.get(username);
        return refreshToken != null && refreshToken.equals(validToken);
    }

    /**
     * 验证登录版本号是否有效（是否是最新登录）
     * @param username 用户名
     * @param tokenVersion 请求中携带的版本号
     * @return true 表示是最新登录，false 表示已被其他设备登录顶替
     */
    public boolean isValidLoginVersion(String username, long tokenVersion) {
        Long currentVersion = loginVersions.get(username);
        if (currentVersion == null) {
            return false;
        }
        return currentVersion.equals(tokenVersion);
    }

    /**
     * 登出时，移除用户的有效 Token
     */
    public void logout(String username) {
        validRefreshTokens.remove(username);
        loginVersions.remove(username);
    }

    /**
     * 检查用户是否已登录
     */
    public boolean isLoggedIn(String username) {
        return validRefreshTokens.containsKey(username);
    }

    /**
     * 获取用户的当前登录版本号
     */
    public long getLoginVersion(String username) {
        return loginVersions.getOrDefault(username, 0L);
    }

    /**
     * 检查用户是否有有效的登录会话（用于单点登录验证）
     * 如果用户在其他设备登录，原设备的会话将失效
     */
    public boolean hasValidSession(String username) {
        return validRefreshTokens.containsKey(username);
    }

    /**
     * 强制用户下线（管理员操作）
     */
    public void forceLogout(String username) {
        validRefreshTokens.remove(username);
        loginVersions.remove(username);
    }
}
