package com.lgs.interceptor;

import com.lgs.common.JwtUtil;
import com.lgs.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跳过公开接口
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/login") ||
                requestURI.endsWith("/register") ||
                requestURI.equals("/") ||
                requestURI.startsWith("/files/")) {
            return true;
        }

        // 获取 Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":\"401\",\"msg\":\"未授权，请先登录\"}");
            return false;
        }

        // 提取 Token
        String token = authHeader.substring(7);

        // 验证 Token
        try {
            if (jwtUtil.isTokenExpired(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":\"401\",\"msg\":\"登录已过期，请重新登录\"}");
                return false;
            }

            // 验证是否为 Access Token
            if (jwtUtil.isRefreshToken(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":\"401\",\"msg\":\"请使用 Access Token\"}");
                return false;
            }

            // 将用户信息存入请求属性
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            
            // 检查username是否为null
            if (username == null || username.isEmpty()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":\"401\",\"msg\":\"登录状态无效，请重新登录\"}");
                return false;
            }
            
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            // 单点登录验证：检查登录版本号是否是最新
            // 如果用户在其他设备登录，版本号会不匹配
            Long tokenLoginVersion = jwtUtil.getLoginVersion(token);
            if (tokenLoginVersion == null) {
                tokenLoginVersion = 0L;
            }

            if (!tokenService.isValidLoginVersion(username, tokenLoginVersion)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":\"401\",\"msg\":\"账号已在其他设备登录，请重新登录\"}");
                return false;
            }

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":\"401\",\"msg\":\"登录状态无效，请重新登录\"}");
            return false;
        }

        return true;
    }
}
