package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Course;
import com.lgs.service.AcademicYearService;
import com.lgs.service.CourseService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private AcademicYearService academicYearService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        activityLogService.recordLog("课程", "admin", "新增课程 \"" + course.getCourse_name() + "\"", "ADMIN");
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        courseService.updateById(course);
        activityLogService.recordLog("课程", "admin", "修改课程 \"" + course.getCourse_name() + "\" 的信息", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        courseService.deleteById(id);
        activityLogService.recordLog("课程", "admin", "删除课程 \"" + course.getCourse_name() + "\"", "ADMIN");
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(Course course, Integer pageNum, Integer pageSize) {
        return Result.success(courseService.selectPage(course, pageNum, pageSize));
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(courseService.selectById(id));
    }



    /**
     * 根据学生ID查询课程
     */
    @GetMapping("/selectByStudentId")
    public Result selectByStudentId(@RequestParam String student_id) {
        return Result.success(courseService.selectByStudentId(student_id));
    }

    /**
     * 根据教师ID查询课程
     */
    @GetMapping("/selectByTeacherId")
    public Result selectByTeacherId(@RequestParam String teacher_id) {
        return Result.success(courseService.selectByTeacherId(teacher_id));
    }
}