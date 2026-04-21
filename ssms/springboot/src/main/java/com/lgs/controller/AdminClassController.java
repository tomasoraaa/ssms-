package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.AdminClass;
import com.lgs.service.AdminClassService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminClass")
public class AdminClassController {

    @Resource
    private AdminClassService adminClassService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody AdminClass adminClass) {
        adminClassService.add(adminClass);
        activityLogService.recordLog("系统", "admin", "新增行政班 " + adminClass.getClass_name(), "ADMIN");
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody AdminClass adminClass) {
        adminClassService.updateById(adminClass);
        activityLogService.recordLog("系统", "admin", "修改行政班 " + adminClass.getClass_name(), "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        AdminClass adminClass = adminClassService.selectById(id);
        adminClassService.deleteById(id);
        if (adminClass != null) {
            activityLogService.recordLog("系统", "admin", "删除行政班 " + adminClass.getClass_name(), "ADMIN");
        }
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(AdminClass adminClass,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<AdminClass> page = adminClassService.selectPage(adminClass, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectAll")
    public Result selectAll(AdminClass adminClass) {
        List<AdminClass> list = adminClassService.selectAll(adminClass);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(adminClassService.selectById(id));
    }
}