package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Account;
import com.lgs.service.AdminService;
import com.lgs.service.StudentService;
import com.lgs.service.TeacherService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "管理员 " + ac.getUsername() + " 登录系统", "ADMIN");
            }
        } else if ("STUDENT".equals(account.getRole())) {
            ac = studentService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "学生 " + ac.getUsername() + " 登录系统", "STUDENT");
            }
        } else if ("TEACHER".equals(account.getRole())) {
            ac = teacherService.login(account);
            if (ac != null) {
                activityLogService.recordLog("登录", ac.getUsername(), "教师 " + ac.getUsername() + " 登录系统", "TEACHER");
            }
        }
        if (ac != null) {
            // 设置密码校验值
            ac.setPasswordChecksum(generatePasswordChecksum(ac.getUsername(), ac.getPassword()));
        }
        return Result.success(ac);
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
        return Result.success();
    }

}
