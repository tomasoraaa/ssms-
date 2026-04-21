package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.CourseTeacher;
import com.lgs.service.CourseTeacherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courseTeacher")
public class CourseTeacherController {

    @Resource
    private CourseTeacherService courseTeacherService;

    @PostMapping("/add")
    public Result add(@RequestBody CourseTeacher courseTeacher) {
        courseTeacherService.add(courseTeacher);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody CourseTeacher courseTeacher) {
        courseTeacherService.updateById(courseTeacher);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        courseTeacherService.deleteByTeachingClassId(id);
        return Result.success();
    }

    @GetMapping("/selectByCourseId/{courseId}")
    public Result selectByCourseId(@PathVariable Integer courseId) {
        List<CourseTeacher> list = courseTeacherService.selectByCourseId(courseId);
        return Result.success(list);
    }

    @GetMapping("/selectByTeacherId/{teacherId}")
    public Result selectByTeacherId(@PathVariable String teacherId) {
        List<CourseTeacher> list = courseTeacherService.selectByTeacherId(teacherId);
        return Result.success(list);
    }

    @GetMapping("/selectByTeachingClassId/{teachingClassId}")
    public Result selectByTeachingClassId(@PathVariable Integer teachingClassId) {
        List<CourseTeacher> list = courseTeacherService.selectByTeachingClassId(teachingClassId);
        return Result.success(list);
    }
}
