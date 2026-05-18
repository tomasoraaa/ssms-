package com.lgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 配置类
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT 密钥（至少256位）
     */
    private String secret = "student-score-management-system-jwt-secret-key-must-be-at-least-32-characters";

    /**
     * Access Token 过期时间（分钟）
     * 设置为10分钟，以确保单点登录能及时生效
     */
    private long accessExpire = 10;

    /**
     * Refresh Token 过期时间（分钟）
     */
    private long refreshExpire = 1440;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessExpire() {
        return accessExpire;
    }

    public void setAccessExpire(long accessExpire) {
        this.accessExpire = accessExpire;
    }

    public long getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(long refreshExpire) {
        this.refreshExpire = refreshExpire;
    }
}
