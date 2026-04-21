package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.TeachingClass;
import com.lgs.service.TeachingClassService;
import com.lgs.service.CourseTeacherService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachingClass")
public class TeachingClassController {

    @Resource
    private TeachingClassService teachingClassService;

    @Resource
    private CourseTeacherService courseTeacherService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody TeachingClass teachingClass) {
        teachingClassService.add(teachingClass);
        activityLogService.recordLog("系统", "admin", "新增教学班 " + teachingClass.getClass_code(), "ADMIN");
        return Result.success(teachingClass.getId());
    }

    @PutMapping("/update")
    public Result update(@RequestBody TeachingClass teachingClass) {
        teachingClassService.updateById(teachingClass);
        activityLogService.recordLog("系统", "admin", "修改教学班 " + teachingClass.getClass_code(), "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        TeachingClass teachingClass = teachingClassService.selectById(id);
        courseTeacherService.deleteByTeachingClassId(id);
        teachingClassService.deleteById(id);
        if (teachingClass != null) {
            activityLogService.recordLog("系统", "admin", "删除教学班 " + teachingClass.getClass_code(), "ADMIN");
        }
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(TeachingClass teachingClass,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TeachingClass> page = teachingClassService.selectPage(teachingClass, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectAll")
    public Result selectAll(TeachingClass teachingClass) {
        List<TeachingClass> list = teachingClassService.selectAll(teachingClass);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        TeachingClass tc = teachingClassService.selectById(id);
        return Result.success(tc);
    }

    @GetMapping("/selectByAcademicYearId/{academic_year_id}")
    public Result selectByAcademicYearId(@PathVariable Integer academic_year_id) {
        List<TeachingClass> list = teachingClassService.selectByAcademicYearId(academic_year_id);
        return Result.success(list);
    }

    @GetMapping("/selectAvailable/{academic_year_id}")
    public Result selectAvailable(@PathVariable Integer academic_year_id) {
        List<TeachingClass> list = teachingClassService.selectAvailable(academic_year_id);
        return Result.success(list);
    }

    @GetMapping("/selectByCourseId/{course_id}")
    public Result selectByCourseId(@PathVariable Integer course_id) {
        List<TeachingClass> list = teachingClassService.selectByCourseId(course_id);
        return Result.success(list);
    }
}
