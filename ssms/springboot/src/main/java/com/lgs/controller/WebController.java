package com.lgs.controller;

import com.lgs.common.JwtUtil;
import com.lgs.common.Result;
import com.lgs.config.JwtConfig;
import com.lgs.entity.Account;
import com.lgs.service.AdminService;
import com.lgs.service.StudentService;
import com.lgs.service.TeacherService;
import com.lgs.service.ActivityLogService;
import com.lgs.service.TokenService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ActivityLogService activityLogService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private TokenService tokenService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        String role = account.getRole();

        if (role == null || role.isEmpty()) {
            return Result.error("请选择登录身份");
        }

        if ("ADMIN".equals(role)) {
            ac = adminService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "管理员 " + ac.getUsername() + " 登录系统", "ADMIN");
            }
        } else if ("STUDENT".equals(role)) {
            ac = studentService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "学生 " + ac.getUsername() + " 登录系统", "STUDENT");
            }
        } else if ("TEACHER".equals(role)) {
            ac = teacherService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "教师 " + ac.getUsername() + " 登录系统", "TEACHER");
            }
        } else {
            return Result.error("无效的身份类型");
        }

        if (ac != null) {
            // 先记录登录（递增版本号），再生成包含版本号的 Token
            String refreshToken = jwtUtil.generateRefreshToken(ac.getUsername());
            tokenService.loginSuccess(ac.getUsername(), refreshToken);

            // 获取当前登录版本号
            long loginVersion = tokenService.getLoginVersion(ac.getUsername());

            // 生成包含版本号的 Access Token
            String accessToken = jwtUtil.generateAccessToken(ac.getUsername(), ac.getRole(), loginVersion);

            // 返回用户信息和 Token
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("user", ac);
            resultData.put("accessToken", accessToken);
            resultData.put("refreshToken", refreshToken);
            resultData.put("tokenType", "Bearer");
            resultData.put("expiresIn", jwtConfig.getAccessExpire() * 60);
            resultData.put("loginVersion", loginVersion);

            return Result.success(resultData);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 验证密码是否变更
     */
    @PostMapping("/validatePassword")
    public Result validatePassword(@RequestBody Account account) {
        String storedChecksum = null;
        if ("STUDENT".equals(account.getRole())) {
            Account student = studentService.loginByUsername(account.getUsername());
            if (student != null) {
                storedChecksum = generatePasswordChecksum(student.getUsername(), student.getPassword());
            }
        } else if ("TEACHER".equals(account.getRole())) {
            Account teacher = teacherService.loginByUsername(account.getUsername());
            if (teacher != null) {
                storedChecksum = generatePasswordChecksum(teacher.getUsername(), teacher.getPassword());
            }
        }

        if (storedChecksum == null) {
            return Result.error("用户不存在");
        }

        if (storedChecksum.equals(account.getPasswordChecksum())) {
            return Result.success("密码未变更");
        } else {
            Result result = Result.error("密码已被重置，请重新登录");
            result.setCode("401");
            return result;
        }
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

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if ("STUDENT".equals(account.getRole())) {
            studentService.register(account);
        } else if ("TEACHER".equals(account.getRole())) {
            teacherService.register(account);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("STUDENT".equals(account.getRole())) {
            studentService.updatePassword(account);
        } else if ("TEACHER".equals(account.getRole())) {
            teacherService.updatePassword(account);
        }

        // 修改密码后，清除该用户的 Token，强制重新登录（实现单点登录）
        if (account.getUsername() != null) {
            tokenService.logout(account.getUsername());
            activityLogService.recordLog("修改密码", account.getUsername(),
                "用户 " + account.getUsername() + " 修改密码，已清除登录状态", account.getRole());
        }

        return Result.success();
    }

    /**
     * 刷新 Token
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            // 验证 Refresh Token 格式
            if (!jwtUtil.isRefreshToken(refreshToken)) {
                return Result.error("无效的 Refresh Token");
            }

            if (jwtUtil.isTokenExpired(refreshToken)) {
                return Result.error("Refresh Token 已过期，请重新登录");
            }

            String username = jwtUtil.getUsername(refreshToken);

            // 验证 Refresh Token 是否是当前登录用户的有效 Token（单点登录验证）
            if (!tokenService.isValidRefreshToken(username, refreshToken)) {
                return Result.error("账号已在其他设备登录，请重新登录");
            }

            // 查询用户获取角色信息
            Account user = adminService.loginByUsername(username);
            if (user == null) {
                user = studentService.loginByUsername(username);
            }
            if (user == null) {
                user = teacherService.loginByUsername(username);
            }

            if (user == null) {
                return Result.error("用户不存在");
            }

            // 获取当前登录版本号
            long loginVersion = tokenService.getLoginVersion(username);

            // 生成新的 Access Token（包含当前版本号）
            String newAccessToken = jwtUtil.generateAccessToken(username, user.getRole(), loginVersion);

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtConfig.getAccessExpire() * 60);

            return Result.success(result);

        } catch (Exception e) {
            return Result.error("Token 刷新失败: " + e.getMessage());
        }
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        if (username != null && !username.isEmpty()) {
            tokenService.logout(username);
            activityLogService.recordLog("登出", username, "用户 " + username + " 登出系统", "SYSTEM");
        }

        return Result.success("登出成功");
    }

}
