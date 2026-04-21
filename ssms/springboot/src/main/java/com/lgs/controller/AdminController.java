package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Admin;
import com.lgs.service.AdminService;
import com.lgs.service.StudentService;
import com.lgs.service.TeacherService;
import com.lgs.service.CourseService;
import com.lgs.service.CourseEvaluationService;
import com.lgs.service.ActivityLogService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;
    
    @Resource
    private StudentService studentService;
    
    @Resource
    private TeacherService teacherService;
    
    @Resource
    private CourseService courseService;
    
    @Resource
    private CourseEvaluationService courseEvaluationService;
    
    @Resource
    private ActivityLogService activityLogService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        activityLogService.recordLog("用户", "admin", "新增管理员 " + admin.getUsername(), "ADMIN");
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        adminService.deleteById(id);
        if (admin != null) {
            activityLogService.recordLog("用户", "admin", "删除管理员 " + admin.getUsername(), "ADMIN");
        }
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin);
        activityLogService.recordLog("用户", "admin", "修改管理员 " + admin.getUsername() + " 的信息", "ADMIN");
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        adminService.importExcel(file);
        activityLogService.recordLog("用户", "admin", "批量导入管理员", "ADMIN");
        return Result.success();
    }

    /**
     * 获取系统统计数据
     */
    @GetMapping("/getSystemStats")
    public Result getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("studentCount", studentService.count());
        stats.put("teacherCount", teacherService.count());
        stats.put("courseCount", courseService.count());
        stats.put("evaluationCount", courseEvaluationService.count());
        return Result.success(stats);
    }

    /**
     * 获取最近活动日志
     */
    @GetMapping("/getRecentActivities")
    public Result getRecentActivities() {
        return Result.success(activityLogService.getRecentLogs(10));
    }

}