package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Account;
import com.lgs.service.AdminService;
import com.lgs.service.StudentService;
import com.lgs.service.TeacherService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


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
        return Result.success(ac);
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
