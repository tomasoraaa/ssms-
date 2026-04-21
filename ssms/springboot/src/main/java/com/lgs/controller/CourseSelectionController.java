package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.CourseSelection;
import com.lgs.service.CourseSelectionService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseSelection")
public class CourseSelectionController {

    @Resource
    private CourseSelectionService courseSelectionService;
    
    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody CourseSelection courseSelection) {
        courseSelectionService.add(courseSelection);
        activityLogService.recordLog("选课", courseSelection.getUser_id(), "学生 " + courseSelection.getUser_id() + " 申请选课", "STUDENT");
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(CourseSelection courseSelection) {
        return Result.success(courseSelectionService.selectAll(courseSelection));
    }

    @PutMapping("/update")
    public Result update(@RequestBody CourseSelection courseSelection) {
        courseSelectionService.updateById(courseSelection);
        activityLogService.recordLog("选课", "admin", "更新选课申请", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        courseSelectionService.deleteById(id);
        activityLogService.recordLog("选课", "admin", "删除选课申请", "ADMIN");
        return Result.success();
    }

    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        courseSelectionService.approve(id);
        activityLogService.recordLog("选课", "admin", "批准选课申请", "ADMIN");
        return Result.success();
    }

    @PutMapping("/reject/{id}")
    public Result reject(@PathVariable Integer id) {
        courseSelectionService.reject(id);
        activityLogService.recordLog("选课", "admin", "拒绝选课申请", "ADMIN");
        return Result.success();
    }
}