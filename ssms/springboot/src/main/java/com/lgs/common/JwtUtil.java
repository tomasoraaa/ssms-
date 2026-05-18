package com.lgs.common;

import com.lgs.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {

    @Resource
    private JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 Access Token（包含登录版本号用于单点登录验证）
     */
    public String generateAccessToken(String username, String role, long loginVersion) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        claims.put("type", "access");
        claims.put("loginVersion", loginVersion);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessExpire() * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成 Access Token（兼容旧版本，无版本号）
     */
    public String generateAccessToken(String username, String role) {
        return generateAccessToken(username, role, 0L);
    }

    /**
     * 生成 Refresh Token
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpire() * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 验证并解析 Token
     */
    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsername(String token) {
        try {
            return validateToken(token).getPayload().get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取角色
     */
    public String getRole(String token) {
        try {
            return validateToken(token).getPayload().get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取登录版本号
     */
    public Long getLoginVersion(String token) {
        try {
            return validateToken(token).getPayload().get("loginVersion", Long.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            return validateToken(token).getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 检查是否为 Refresh Token
     */
    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(validateToken(token).getPayload().get("type"));
        } catch (Exception e) {
            return false;
        }
    }
}
