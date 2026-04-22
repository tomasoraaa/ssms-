package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.StudentCourse;
import com.lgs.service.StudentCourseService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studentCourse")
public class StudentCourseController {

    @Resource
    private StudentCourseService studentCourseService;
    
    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody StudentCourse studentCourse) {
        studentCourseService.add(studentCourse);
        activityLogService.recordLog("成绩", "admin", "为学生添加课程成绩", "ADMIN");
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(StudentCourse studentCourse) {
        return Result.success(studentCourseService.selectAll(studentCourse));
    }

    @PutMapping("/updateScore")
    public Result updateScore(@RequestBody StudentCourse studentCourse) {
        studentCourseService.updateScore(studentCourse);
        activityLogService.recordLog("成绩", "admin", "修改学生课程成绩", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        studentCourseService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteByStudentIdAndCourseId")
    public Result deleteByStudentIdAndCourseId(@RequestBody StudentCourse studentCourse) {
        studentCourseService.deleteByStudentIdAndCourseId(studentCourse);
        return Result.success();
    }

    @GetMapping("/selectByCourseId/{courseId}")
    public Result selectByCourseId(@PathVariable String courseId) {
        return Result.success(studentCourseService.selectByCourseId(courseId));
    }

    @GetMapping("/selectByStudentId/{studentId}")
    public Result selectByStudentId(@PathVariable String studentId) {
        return Result.success(studentCourseService.selectByStudentId(studentId));
    }

    @GetMapping("/selectByTeachingClassId/{teachingClassId}")
    public Result selectByTeachingClassId(@PathVariable Integer teachingClassId) {
        return Result.success(studentCourseService.selectByTeachingClassId(teachingClassId));
    }
}