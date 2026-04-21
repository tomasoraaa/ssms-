package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.CourseWithdrawal;
import com.lgs.service.CourseWithdrawalService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseWithdrawal")
public class CourseWithdrawalController {

    @Resource
    private CourseWithdrawalService courseWithdrawalService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody CourseWithdrawal courseWithdrawal) {
        courseWithdrawalService.add(courseWithdrawal);
        activityLogService.recordLog("退课", courseWithdrawal.getStudentId(), "学生 " + courseWithdrawal.getStudentId() + " 提交退课申请", "STUDENT");
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(CourseWithdrawal courseWithdrawal) {
        return Result.success(courseWithdrawalService.selectAll(courseWithdrawal));
    }

    @GetMapping("/selectByStudentId/{studentId}")
    public Result selectByStudentId(@PathVariable String studentId) {
        return Result.success(courseWithdrawalService.selectByStudentId(studentId));
    }

    @PutMapping("/update")
    public Result update(@RequestBody CourseWithdrawal courseWithdrawal) {
        courseWithdrawalService.updateById(courseWithdrawal);
        activityLogService.recordLog("退课", "admin", "更新退课申请", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        courseWithdrawalService.deleteById(id);
        activityLogService.recordLog("退课", "admin", "删除退课申请", "ADMIN");
        return Result.success();
    }

    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        courseWithdrawalService.approve(id);
        activityLogService.recordLog("退课", "admin", "批准退课申请", "ADMIN");
        return Result.success();
    }

    @PutMapping("/reject/{id}")
    public Result reject(@PathVariable Integer id) {
        courseWithdrawalService.reject(id);
        activityLogService.recordLog("退课", "admin", "拒绝退课申请", "ADMIN");
        return Result.success();
    }
}