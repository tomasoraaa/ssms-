package com.lgs.interceptor;

import com.lgs.service.StudentService;
import com.lgs.service.TeacherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码变更检测拦截器
 * 当用户密码被管理员重置后，强制用户重新登录
 */
@Component
public class PasswordCheckInterceptor implements HandlerInterceptor {

    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跳过登录、注册等公开接口
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/login") || requestURI.endsWith("/register") || requestURI.equals("/")) {
            return true;
        }

        // 获取请求头中的用户信息
        String username = request.getHeader("X-User-Username");
        String role = request.getHeader("X-User-Role");
        String clientChecksum = request.getHeader("X-Password-Checksum");

        // 如果没有用户信息，允许请求继续（可能是管理员或其他情况）
        if (username == null || role == null || clientChecksum == null) {
            return true;
        }

        // 获取数据库中的密码并生成校验值
        String storedPassword = null;
        if ("STUDENT".equals(role)) {
            var student = studentService.loginByUsername(username);
            if (student != null) {
                storedPassword = student.getPassword();
            }
        } else if ("TEACHER".equals(role)) {
            var teacher = teacherService.loginByUsername(username);
            if (teacher != null) {
                storedPassword = teacher.getPassword();
            }
        }

        if (storedPassword != null) {
            String serverChecksum = generatePasswordChecksum(username, storedPassword);
            // 如果校验值不匹配，说明密码已被重置
            if (!serverChecksum.equals(clientChecksum)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":\"401\",\"msg\":\"认证失败，请重新登录\"}");
                return false;
            }
        }

        return true;
    }

    /**
     * 生成密码校验值
     */
    private String generatePasswordChecksum(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = username + ":" + password;
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return username + ":" + password;
        }
    }
}
